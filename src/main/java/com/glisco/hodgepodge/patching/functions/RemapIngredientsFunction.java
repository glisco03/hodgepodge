package com.glisco.hodgepodge.patching.functions;

import com.glisco.hodgepodge.IngredientHelper;
import com.glisco.hodgepodge.mixin.IngredientAccessor;
import com.glisco.hodgepodge.patching.RecipePatch;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import com.google.gson.JsonElement;
import net.minecraft.recipe.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class RemapIngredientsFunction implements RecipePatch.Function {

    private final Map<Ingredient.Entry, Ingredient.Entry> mappings;

    public RemapIngredientsFunction(Map<Ingredient.Entry, Ingredient.Entry> mappings) {
        this.mappings = mappings;
    }

    @Override
    public void apply(RecipeManipulators.WrappedRecipe<?> recipe) {
        recipe.getIngredients().forEach(ingredient -> {
            Ingredient.Entry[] entries = ((IngredientAccessor) (Object) ingredient).getEntries();

            for (int i = 0; i < entries.length; i++) {
                final int index = i;
                mappings.forEach((target, replacement) -> {
                    if (IngredientHelper.compareEntries(entries[index], target)) {
                        entries[index] = replacement;
                    }
                });
            }
        });
    }

    @Override
    public boolean eligible(RecipeManipulators.WrappedRecipe<?> recipe) {
        return recipe.supports(RecipeManipulator.Operation.GET_INGREDIENTS);
    }

    public static RecipePatch.Function fromJson(JsonElement json) {
        final var mappings = json.getAsJsonObject();
        final var parsedMappings = new HashMap<Ingredient.Entry, Ingredient.Entry>();

        mappings.entrySet().forEach(entry -> {
            Ingredient.Entry parsedTarget = IngredientHelper.parseEntry(entry.getKey());
            Ingredient.Entry parsedReplacement = IngredientHelper.parseEntry(entry.getValue().getAsString());
            parsedMappings.put(parsedTarget, parsedReplacement);
        });

        return new RemapIngredientsFunction(parsedMappings);
    }
}
