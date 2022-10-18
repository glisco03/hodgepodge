package com.glisco.hodgepodge.compat;

import com.glisco.hodgepodge.HodgepodgePlugin;
import com.glisco.hodgepodge.mixin.compat.BrewingRecipeAccessor;
import com.glisco.hodgepodge.patching.PatchPreconditions;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import juuxel.adorn.recipe.BrewingRecipe;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.Collections;
import java.util.List;

public class AdornPlugin implements HodgepodgePlugin {
    @Override
    public void registerManipulators() {
        RecipeManipulators.register(ItemBrewingRecipe.class, new BrewingRecipeManipulator());
        RecipeManipulators.register(FluidBrewingRecipe.class, new BrewingRecipeManipulator());
    }

    @Override
    public List<String> getRequiredMods() {
        return Collections.singletonList("adorn");
    }

    private static class BrewingRecipeManipulator implements RecipeManipulator<BrewingRecipe> {
        @Override
        public void setOutput(BrewingRecipe recipe, int index, ItemStack stack) {
            PatchPreconditions.checkOutputIndex(recipe, index, 0);

            ((BrewingRecipeAccessor) recipe).hodgepodge$setResult(stack);
        }

        @Override
        public void setInput(BrewingRecipe recipe, int index, Ingredient ingredient) {
            PatchPreconditions.checkInputIndex(recipe, index, 1);

            if (index == 0) {
                ((BrewingRecipeAccessor) recipe).hodgepodge$setFirstIngredient(ingredient);
            } else {
                ((BrewingRecipeAccessor) recipe).hodgepodge$setSecondIngredient(ingredient);
            }
        }

        @Override
        public List<Ingredient> getIngredients(BrewingRecipe recipe) {
            var acc = (BrewingRecipeAccessor) recipe;

            return List.of(acc.hodgepodge$getFirstIngredient(), acc.hodgepodge$getSecondIngredient());
        }

        @Override
        public boolean supports(Operation operation) {
            return true;
        }
    }
}
