package com.glisco.hodgepodge.patching.manipulators;

import com.glisco.hodgepodge.patching.PatchPreconditions;
import com.glisco.hodgepodge.patching.manipulators.marker.SingleOutputRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;

import java.util.List;

public class SimpleCraftingRecipeManipulator<R extends Recipe<?>> implements RecipeManipulator<R> {

    @Override
    public void setOutput(R recipe, int index, ItemStack stack) {
        PatchPreconditions.checkOutputIndex(recipe, index, 0);
        ((SingleOutputRecipe) recipe).setOutput(stack);
    }

    @Override
    public void setInput(R recipe, int index, Ingredient ingredient) {
        final var ingredients = this.getIngredients(recipe);
        PatchPreconditions.checkInputIndex(recipe, index, ingredients.size() - 1);

        ingredients.set(index, ingredient);
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
