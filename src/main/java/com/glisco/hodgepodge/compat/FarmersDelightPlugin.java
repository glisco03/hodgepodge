package com.glisco.hodgepodge.compat;

import com.glisco.hodgepodge.HodgepodgePlugin;
import com.glisco.hodgepodge.mixin.compat.CookingPotRecipeAccessor;
import com.glisco.hodgepodge.mixin.compat.CuttingBoardRecipeAccessor;
import com.glisco.hodgepodge.patching.PatchPreconditions;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import com.nhoryzon.mc.farmersdelight.recipe.CuttingBoardRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.Collections;
import java.util.List;

public class FarmersDelightPlugin implements HodgepodgePlugin {

    @Override
    public void registerManipulators() {
        RecipeManipulators.register(CuttingBoardRecipe.class, new CuttingBoardRecipeManipulator());
        RecipeManipulators.register(CookingPotRecipe.class, new CookingPotRecipeManipulator());
    }

    @Override
    public List<String> getRequiredMods() {
        return Collections.singletonList("farmersdelight");
    }

    public static class CuttingBoardRecipeManipulator implements RecipeManipulator<CuttingBoardRecipe> {

        @Override
        public void setOutput(CuttingBoardRecipe recipe, int index, ItemStack stack) {
            var outputList = recipe.getResultList();
            PatchPreconditions.checkInputIndex(recipe, index, outputList.size() - 1);

            outputList.set(index, stack);
        }

        @Override
        public void setInput(CuttingBoardRecipe recipe, int index, Ingredient ingredient) {
            PatchPreconditions.checkInputIndex(recipe, index, 1);

            if (index == 0) {
                ((CuttingBoardRecipeAccessor) recipe).hodgepodge$setInput(ingredient);
            } else {
                ((CuttingBoardRecipeAccessor) recipe).hodgepodge$setTool(ingredient);
            }
        }

        @Override
        public List<Ingredient> getIngredients(CuttingBoardRecipe recipe) {
            var accessor = (CuttingBoardRecipeAccessor) recipe;
            return List.of(accessor.hodgepodge$getInput(), recipe.getTool());
        }

        @Override
        public boolean supports(Operation operation) {
            return true;
        }
    }

    public static class CookingPotRecipeManipulator implements RecipeManipulator<CookingPotRecipe> {

        @Override
        public void setOutput(CookingPotRecipe recipe, int index, ItemStack stack) {
            PatchPreconditions.checkOutputIndex(recipe, index, 1);

            if (index == 0) {
                ((CookingPotRecipeAccessor) recipe).hodgepodge$setOutput(stack);
            } else {
                ((CookingPotRecipeAccessor) recipe).hodgepodge$setContainer(stack);
            }
        }

        @Override
        public void setInput(CookingPotRecipe recipe, int index, Ingredient ingredient) {
            var inputList = recipe.getIngredients();
            PatchPreconditions.checkInputIndex(recipe, index, inputList.size() - 1);

            inputList.set(index, ingredient);
        }

        @Override
        public List<Ingredient> getIngredients(CookingPotRecipe recipe) {
            return recipe.getIngredients();
        }

        @Override
        public boolean supports(Operation operation) {
            return true;
        }
    }
}
