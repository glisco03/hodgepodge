package com.glisco.hodgepodge.recipe_patches;

import com.glisco.hodgepodge.MutableImmutableMapBuilder;
import com.glisco.hodgepodge.mixin.RecipeManagerAccessor;
import com.glisco.hodgepodge.recipe_patches.manipulators.RecipeManipulatorProvider;
import com.google.common.collect.ImmutableMap;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.glisco.hodgepodge.Hodgepodge.LOGGER;

public class RecipePatcher {

    private static Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> recipeMapCache;
    private static RecipeManager manager;

    private static final Map<RecipePredicate, RecipePatch> patchRules = new HashMap<>();
    private static final List<RecipePredicate> removeRules = new ArrayList<>();

    public static void apply() {

        LOGGER.info("### Begin patch apply stage ###");
        LOGGER.info("# {} patches to apply", patchRules.size());
        LOGGER.info("# {} removal predicates", removeRules.size());

        try {
            patchRules.forEach((identifier, recipePatch) -> findRecipes(identifier, (builder, matchedId) -> {
                Recipe<?> recipe = builder.get(matchedId);
                try {
                    recipePatch.apply(RecipeManipulatorProvider.wrap(recipe));
                } catch (Exception e) {
                    LOGGER.warn("Applying recipe patch failed with exception. Message: {}", e.getMessage());
                }
            }));

            removeRules.forEach(identifier -> findRecipes(identifier, MutableImmutableMapBuilder::remove));
        } catch (Exception e) {
            LOGGER.error("Recipe patcher failed with exception. Message: {}", e.getMessage());
            e.printStackTrace();
        }

        LOGGER.info("# Applying recipe map");

        ((RecipeManagerAccessor) manager).setRecipes(recipeMapCache.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (entryx) -> {
            return ((ImmutableMap.Builder) entryx.getValue()).build();
        })));

        LOGGER.info("### End patch apply stage ###");

        patchRules.clear();
        removeRules.clear();
        recipeMapCache = null;
        manager = null;
    }

    private static void findRecipes(RecipePredicate predicate, BiConsumer<MutableImmutableMapBuilder<Identifier, Recipe<?>>, Identifier> ifFound) {
        if (predicate instanceof RecipePredicate.MatchID matchID) {
            findRecipeByIdentifier(matchID.getTarget(), ifFound);
        } else {
            findRecipesByPredicate(predicate, ifFound);
        }
    }

    private static void findRecipeByIdentifier(Identifier id, BiConsumer<MutableImmutableMapBuilder<Identifier, Recipe<?>>, Identifier> ifFound) {
        for (Map.Entry<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> entry : recipeMapCache.entrySet()) {
            MutableImmutableMapBuilder<Identifier, Recipe<?>> builder = (MutableImmutableMapBuilder<Identifier, Recipe<?>>) entry.getValue();
            if (!builder.containsKey(id)) continue;

            ifFound.accept(builder, id);
            return;
        }
    }

    private static void findRecipesByPredicate(RecipePredicate predicate, BiConsumer<MutableImmutableMapBuilder<Identifier, Recipe<?>>, Identifier> ifFound) {
        for (Map.Entry<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> entry : recipeMapCache.entrySet()) {
            MutableImmutableMapBuilder<Identifier, Recipe<?>> builder = (MutableImmutableMapBuilder<Identifier, Recipe<?>>) entry.getValue();
            builder.forEach((identifier, recipe) -> {
                try {
                    if (!predicate.test(recipe, identifier)) return;
                    ifFound.accept(builder, identifier);
                } catch (Exception e) {
                    LOGGER.warn("Caught exception while testing predicate. Message: {}", e.getMessage());
                }
            });
        }
    }

    public static void registerPatchRule(RecipePredicate recipeId, RecipePatch patch) {
        patchRules.put(recipeId, patch);
    }

    public static void registerRemoveRule(RecipePredicate id) {
        removeRules.add(id);
    }

    public static void storeMap(Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> map, RecipeManager holder) {
        recipeMapCache = map;
        manager = holder;
    }

}
