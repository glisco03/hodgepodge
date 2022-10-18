package com.glisco.hodgepodge.patching;

import com.glisco.hodgepodge.IngredientHelper;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public interface RecipePredicate {

    boolean test(Recipe<?> recipe, Identifier id);

    static RecipePredicate parse(JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            var predicateDescriptor = element.getAsString();
            if (predicateDescriptor.startsWith("!")) return new MatchIDRegex(predicateDescriptor.substring(1));
            return new MatchId(new Identifier(predicateDescriptor));
        }

        if (element instanceof JsonArray array) {
            var ids = new ArrayList<Identifier>();
            array.forEach(idElement -> ids.add(new Identifier(idElement.getAsString())));
            return new MatchIDList(ids);
        }

        var object = element.getAsJsonObject();

        String type = JsonHelper.getString(object, "type");

        return switch (type) {
            case "match_output" -> {
                var ingredient = IngredientHelper.parse(JsonHelper.getString(object, "item"));
                yield new MatchOutputIngredient(ingredient);
            }
            case "match_input" -> {
                var ingredient = IngredientHelper.parse(JsonHelper.getString(object, "item"));
                yield new MatchInputIngredient(ingredient);
            }
            case "all" -> MultiMatch.fromJson(JsonHelper.getArray(object, "predicates"), true);
            case "any" -> MultiMatch.fromJson(JsonHelper.getArray(object, "predicates"), false);
            case "recipe_type" -> new MatchRecipeType(new Identifier(JsonHelper.getString(object, "id")));
            default -> null;
        };
    }

    class MultiMatch implements RecipePredicate {

        private final List<RecipePredicate> elements;
        private final boolean requireAll;

        private MultiMatch(List<RecipePredicate> elements, boolean requireAll) {
            this.elements = elements;
            this.requireAll = requireAll;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return requireAll
                    ? elements.stream().allMatch(recipePredicate -> recipePredicate.test(recipe, id))
                    : elements.stream().anyMatch(recipePredicate -> recipePredicate.test(recipe, id));
        }

        public static MultiMatch fromJson(JsonArray json, boolean requireAll) {
            var elements = new ArrayList<RecipePredicate>();
            json.forEach(element -> elements.add(parse(element)));
            return new MultiMatch(elements, requireAll);
        }
    }

    class MatchId implements RecipePredicate {

        private final Identifier target;

        public MatchId(Identifier identifier) {
            this.target = identifier;
        }

        public Identifier target() {
            return target;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return id.equals(this.target);
        }
    }

    class MatchIDList implements RecipePredicate {

        private final List<Identifier> target;

        public MatchIDList(List<Identifier> identifier) {
            this.target = identifier;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return target.stream().anyMatch(id::equals);
        }
    }

    class MatchIDRegex implements RecipePredicate {

        private final String pattern;

        public MatchIDRegex(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return id.toString().matches(pattern);
        }
    }

    class MatchOutputIngredient implements RecipePredicate {

        private final Ingredient predicate;

        public MatchOutputIngredient(Ingredient predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return predicate.test(recipe.getOutput());
        }
    }

    class MatchInputIngredient implements RecipePredicate {

        private final Ingredient predicate;

        public MatchInputIngredient(Ingredient predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return RecipeManipulators.eligible(recipe) && RecipeManipulators.getIngredients(recipe).stream().anyMatch(ingredient -> IngredientHelper.compareIngredients(ingredient, predicate));
        }
    }

    class MatchRecipeType implements RecipePredicate {

        private final Identifier targetType;

        public MatchRecipeType(Identifier targetType) {
            this.targetType = targetType;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return Registry.RECIPE_SERIALIZER.getId(recipe.getSerializer()).equals(targetType);
        }
    }

}
