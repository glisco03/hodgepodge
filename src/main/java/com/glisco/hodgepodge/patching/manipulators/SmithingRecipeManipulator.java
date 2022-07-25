package com.glisco.hodgepodge.patching.manipulators;

import com.glisco.hodgepodge.mixin.recipe_accessors.SmithingRecipeAccessor;
import com.glisco.hodgepodge.patching.PatchPreconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;

import java.util.List;

public class SmithingRecipeManipulator implements RecipeManipulator<SmithingRecipe> {

    @Override
    public void setOutput(SmithingRecipe recipe, ItemStack stack) {
        ((SmithingRecipeAccessor) recipe).setResult(stack);
    }

    @Override
    public void setInput(SmithingRecipe recipe, int index, Ingredient ingredient) {
        PatchPreconditions.checkInputIndex(recipe, index, 1);

        final var accessor = (SmithingRecipeAccessor) recipe;
        if (index == 0) {
            accessor.setBase(ingredient);
        } else {
            accessor.setAddition(ingredient);
        }
    }

    @Override
    public void setOutput(SmithingRecipe recipe, int index, ItemStack stack) {
        PatchPreconditions.checkOutputIndex(recipe, index, 0);
        ((SmithingRecipeAccessor) recipe).setResult(stack);
    }

    @Override
    public List<Ingredient> getIngredients(SmithingRecipe recipe) {
        final var accessor = (SmithingRecipeAccessor) recipe;
        return List.of(accessor.getBase(), accessor.getAddition());
    }

    @Override
    public boolean supports(Operation operation) {
        return true;
    }
}
