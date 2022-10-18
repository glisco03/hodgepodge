package com.glisco.hodgepodge.compat;

import com.glisco.hodgepodge.HodgepodgePlugin;
import com.glisco.hodgepodge.mixin.compat.AlloyForgeRecipeAccessor;
import com.glisco.hodgepodge.patching.PatchPreconditions;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import wraith.alloyforgery.recipe.AlloyForgeRecipe;

import java.util.Collections;
import java.util.List;

public class AlloyForgeryPlugin implements HodgepodgePlugin {
    @Override
    public void registerManipulators() {
        RecipeManipulators.register(AlloyForgeRecipe.class, new AlloyForgeRecipeManipulator());
    }

    @Override
    public List<String> getRequiredMods() {
        return Collections.singletonList("alloy_forgery");
    }

    private static class AlloyForgeRecipeManipulator implements RecipeManipulator<AlloyForgeRecipe> {
        @Override
        public void setOutput(AlloyForgeRecipe recipe, int index, ItemStack stack) {
            PatchPreconditions.checkOutputIndex(recipe, index, 0);

            ((AlloyForgeRecipeAccessor) recipe).hodgepodge$setOutput(stack);
        }

        @Override
        public List<Ingredient> getIngredients(AlloyForgeRecipe recipe) {
            return recipe.getIngredients();
        }

        @Override
        public boolean supports(Operation operation) {
            return switch (operation) {
                case SET_OUTPUT, GET_INGREDIENTS -> true;
                case SET_INPUT -> false;
            };
        }
    }
}
