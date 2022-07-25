package com.glisco.hodgepodge.patching.manipulators;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;

import java.util.List;

public interface RecipeManipulator<R extends Recipe<? extends Inventory>> {

    default void setOutput(R recipe, ItemStack stack) {
        setOutput(recipe, 0, stack);
    }

    default void setOutput(R recipe, int index, ItemStack stack) {
        throw new UnsupportedOperationException();
    }

    default void setInput(R recipe, Ingredient ingredient) {
        setInput(recipe, 0, ingredient);
    }

    default void setInput(R recipe, int index, Ingredient ingredient) {
        throw new UnsupportedOperationException();
    }

    default List<Ingredient> getIngredients(R recipe) {
        throw new UnsupportedOperationException();
    }

    boolean supports(Operation operation);

    enum Operation {
        /**
         * This manipulator can change a single
         * output item stack of the recipe
         */
        SET_OUTPUT,
        /**
         * This manipulator can change a single
         * input ingredient of the recipe
         */
        SET_INPUT,
        /**
         * This manipulator can supply a list of the recipe's ingredients
         */
        GET_INGREDIENTS
    }

}
