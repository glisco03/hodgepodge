package com.glisco.hodgepodge.recipe_patches.manipulators;

import com.glisco.hodgepodge.mixin.recipe_accessors.SmithingRecipeAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.SmithingRecipe;

import java.util.Arrays;
import java.util.List;

public class RecipeManipulators {

    public static class SimpleCraftingRecipeManipulator<R extends Recipe<?>> implements RecipeManipulator<R> {

        @Override
        public void setOutput(R recipe, int index, ItemStack stack) {
            if (index != 0) throw new IllegalArgumentException("Recipe class '" + recipe.getClass() + "'only provides one output");
            ((SingleOutputProvider) recipe).setOutput(stack);
        }

        @Override
        public List<Ingredient> getIngredients(R recipe) {
            return recipe.getIngredients();
        }

        @Override
        public void setInput(R recipe, int index, Ingredient ingredient) {
            getIngredients(recipe).set(index, ingredient);
        }

        @Override
        public boolean supports(Operation operation) {
            return true;
        }
    }

    public static class SimpleConversionRecipeManipulator<R extends Recipe<?>> implements RecipeManipulator<R> {

        @Override
        public void setOutput(R recipe, int index, ItemStack stack) {
            if (index != 0) throw new IllegalArgumentException("Recipe class '" + recipe.getClass() + "'only provides one output");
            ((SingleOutputProvider) recipe).setOutput(stack);
        }

        @Override
        public void setInput(R recipe, int index, Ingredient ingredient) {
            if (index != 0) throw new IllegalArgumentException("Recipe class '" + recipe.getClass() + "'only provides one input");
            ((SingleInputProvider) recipe).setInput(ingredient);
        }

        @Override
        public List<Ingredient> getIngredients(R recipe) {
            return recipe.getIngredients();
        }

        @Override
        public boolean supports(Operation operation) {
            return true;
        }
    }

    public static class SmithingRecipeManipulator implements RecipeManipulator<SmithingRecipe> {

        @Override
        public void setOutput(SmithingRecipe recipe, ItemStack stack) {
            ((SmithingRecipeAccessor) recipe).setResult(stack);
        }

        @Override
        public void setInput(SmithingRecipe recipe, int index, Ingredient ingredient) {
            final var accessor = (SmithingRecipeAccessor) recipe;
            if (index == 0) accessor.setBase(ingredient);
            else if (index == 1) accessor.setAddition(ingredient);
            else throw new IllegalArgumentException("Recipe class '" + recipe.getClass() + "'only provides two inputs");
        }

        @Override
        public void setOutput(SmithingRecipe recipe, int index, ItemStack stack) {
            if (index != 0) throw new IllegalArgumentException("Recipe class '" + recipe.getClass() + "'only provides one output");
            ((SmithingRecipeAccessor) recipe).setResult(stack);
        }

        @Override
        public List<Ingredient> getIngredients(SmithingRecipe recipe) {
            final var accessor = (SmithingRecipeAccessor) recipe;
            return Arrays.asList(accessor.getBase(), accessor.getAddition());
        }

        @Override
        public boolean supports(Operation operation) {
            return true;
        }
    }

}
