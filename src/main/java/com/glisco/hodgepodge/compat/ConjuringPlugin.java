package com.glisco.hodgepodge.compat;

import com.glisco.conjuring.blocks.soul_weaver.SoulWeaverRecipe;
import com.glisco.conjuring.blocks.soulfire_forge.SoulfireForgeRecipe;
import com.glisco.hodgepodge.HodgepodgePlugin;
import com.glisco.hodgepodge.mixin.compat.ConjuringRecipeAccessor;
import com.glisco.hodgepodge.patching.PatchPreconditions;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.Collections;
import java.util.List;

public class ConjuringPlugin implements HodgepodgePlugin {

    @Override
    public void registerManipulators() {
        RecipeManipulators.register(SoulWeaverRecipe.class, new SoulWeaverRecipeManipulator());
        RecipeManipulators.register(SoulfireForgeRecipe.class, new SoulfireForgeRecipeManipulator());
    }

    @Override
    public List<String> getRequiredMods() {
        return Collections.singletonList("conjuring");
    }

    public static class SoulWeaverRecipeManipulator implements RecipeManipulator<SoulWeaverRecipe> {

        @Override
        public void setOutput(SoulWeaverRecipe recipe, int index, ItemStack stack) {
            PatchPreconditions.checkOutputIndex(recipe, index, 0);
            ((ConjuringRecipeAccessor) recipe).hodgepodge$setResult(stack);
        }

        @Override
        public void setInput(SoulWeaverRecipe recipe, int index, Ingredient ingredient) {
            var inputList = recipe.getInputs();
            PatchPreconditions.checkInputIndex(recipe, index, inputList.size() - 1);

            inputList.set(index, ingredient);
        }

        @Override
        public List<Ingredient> getIngredients(SoulWeaverRecipe recipe) {
            return recipe.getInputs();
        }

        @Override
        public boolean supports(Operation operation) {
            return true;
        }
    }

    public static class SoulfireForgeRecipeManipulator implements RecipeManipulator<SoulfireForgeRecipe> {

        @Override
        public void setOutput(SoulfireForgeRecipe recipe, int index, ItemStack stack) {
            PatchPreconditions.checkOutputIndex(recipe, index, 0);
            ((ConjuringRecipeAccessor) recipe).hodgepodge$setResult(stack);
        }

        @Override
        public void setInput(SoulfireForgeRecipe recipe, int index, Ingredient ingredient) {
            var inputList = recipe.getInputs();
            PatchPreconditions.checkInputIndex(recipe, index, inputList.size() - 1);

            inputList.set(index, ingredient);
        }

        @Override
        public List<Ingredient> getIngredients(SoulfireForgeRecipe recipe) {
            return recipe.getInputs();
        }

        @Override
        public boolean supports(Operation operation) {
            return true;
        }
    }

}
