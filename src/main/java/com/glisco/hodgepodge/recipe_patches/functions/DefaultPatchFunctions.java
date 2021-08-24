package com.glisco.hodgepodge.recipe_patches.functions;

import com.glisco.hodgepodge.IngredientHelper;
import com.glisco.hodgepodge.mixin.IngredientAccessor;
import com.glisco.hodgepodge.recipe_patches.RecipePatch;
import com.glisco.hodgepodge.recipe_patches.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.recipe_patches.manipulators.RecipeManipulatorProvider;
import com.google.gson.JsonElement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class DefaultPatchFunctions {

    public static class ReplaceIngredientsFunction implements RecipePatch.Function {

        private final Map<Ingredient.Entry, Ingredient.Entry> mappings;

        public ReplaceIngredientsFunction(Map<Ingredient.Entry, Ingredient.Entry> mappings) {
            this.mappings = mappings;
        }

        @Override
        public void apply(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {
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
        public boolean eligible(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {
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

            return new ReplaceIngredientsFunction(parsedMappings);
        }
    }

    public static class SetOutputsFunction implements RecipePatch.Function {

        private final ItemStack replacementStack;

        public SetOutputsFunction(ItemStack replacementStack) {
            this.replacementStack = replacementStack;
        }

        @Override
        public void apply(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {
            recipe.setOutput(replacementStack);
        }

        @Override
        public boolean eligible(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {
            return recipe.supports(RecipeManipulator.Operation.SET_OUTPUT);
        }

        public static RecipePatch.Function fromJson(JsonElement output) {
            String[] descriptor = output.getAsString().split("\\$");
            Item outputItem = Registry.ITEM.get(new Identifier(descriptor[0]));
            return new SetOutputsFunction(new ItemStack(outputItem, descriptor.length < 2 ? 1 : Integer.parseInt(descriptor[1])));
        }
    }

    public static class SetOutputCountFunction implements RecipePatch.Function {

        private final int newCount;

        public SetOutputCountFunction(int newCount) {
            this.newCount = newCount;
        }

        @Override
        public void apply(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {
            final var output = recipe.getRecipe().getOutput();
            output.setCount(newCount);
            recipe.setOutput(output);
        }

        @Override
        public boolean eligible(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {
            return recipe.supports(RecipeManipulator.Operation.SET_OUTPUT);
        }

        public static RecipePatch.Function fromJson(JsonElement output) {
            return new SetOutputCountFunction(output.getAsInt());
        }
    }

    public static class SetInputsFunction implements RecipePatch.Function {

        private final Map<Integer, Ingredient> inputs;

        public SetInputsFunction(Map<Integer, Ingredient> inputs) {
            this.inputs = inputs;
        }

        @Override
        public void apply(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {
            inputs.forEach(recipe::setInput);
        }

        @Override
        public boolean eligible(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {
            return recipe.supports(RecipeManipulator.Operation.GET_INGREDIENTS);
        }

        public static RecipePatch.Function fromJson(JsonElement element) {
            final var inputs = element.getAsJsonObject();
            final var parsedInputs = new HashMap<Integer, Ingredient>();

            inputs.entrySet().forEach(entry -> {
                parsedInputs.put(Integer.valueOf(entry.getKey()), IngredientHelper.parse(entry.getValue().getAsString()));
            });

            return new SetInputsFunction(parsedInputs);
        }
    }

}
