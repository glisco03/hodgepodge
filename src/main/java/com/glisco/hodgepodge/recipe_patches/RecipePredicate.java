package com.glisco.hodgepodge.recipe_patches;

import com.glisco.hodgepodge.IngredientHelper;
import com.glisco.hodgepodge.recipe_patches.manipulators.RecipeManipulatorProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public interface RecipePredicate {

    boolean test(Recipe<?> recipe, Identifier id);

    static RecipePredicate parse(JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            var predicateDescriptor = element.getAsString();
            if (predicateDescriptor.startsWith("!")) return new MatchIDRegex(predicateDescriptor.substring(1));
            return new MatchID(new Identifier(predicateDescriptor));
        }

        if (element instanceof JsonArray array) {
            var ids = new ArrayList<Identifier>();
            array.forEach(idElement -> ids.add(new Identifier(idElement.getAsString())));
            return new MatchIDList(ids);
        }

        var object = element.getAsJsonObject();

        String type = JsonHelper.getString(object, "type");

        if (type.equals("match_output")) {
            var ingredient = IngredientHelper.parse(JsonHelper.getString(object, "item"));
            return new MatchOutputIngredient(ingredient);
        } else if (type.equals("match_input")) {
            var ingredient = IngredientHelper.parse(JsonHelper.getString(object, "item"));
            return new MatchInputIngredient(ingredient);
        } else if (type.equals("multi_match")) {
            return MultiMatch.fromJson(JsonHelper.getArray(object, "predicates"));
        } else if (type.equals("recipe_type")) {
            return new MatchRecipeType(new Identifier(JsonHelper.getString(object, "id")));
        } else {
            return null;
        }

    }

    class MultiMatch implements RecipePredicate {

        private final List<RecipePredicate> elements;

        private MultiMatch(List<RecipePredicate> elements) {
            this.elements = elements;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return elements.stream().allMatch(recipePredicate -> recipePredicate.test(recipe, id));
        }

        public static MultiMatch fromJson(JsonArray json) {
            var elements = new ArrayList<RecipePredicate>();
            json.forEach(element -> elements.add(parse(element)));
            return new MultiMatch(elements);
        }
    }

    class MatchID implements RecipePredicate {

        private final Identifier target;

        public MatchID(Identifier identifier) {
            this.target = identifier;
        }

        public Identifier getTarget() {
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
            return RecipeManipulatorProvider.eligible(recipe) && RecipeManipulatorProvider.getIngredients(recipe).stream().anyMatch(ingredient -> IngredientHelper.compareIngredients(ingredient, predicate));
        }
    }

    class MatchRecipeType implements RecipePredicate {

        private final Identifier targetType;

        public MatchRecipeType(Identifier targetType) {
            this.targetType = targetType;
        }

        @Override
        public boolean test(Recipe<?> recipe, Identifier id) {
            return Registry.RECIPE_TYPE.getId(recipe.getType()).equals(targetType);
        }
    }

}
