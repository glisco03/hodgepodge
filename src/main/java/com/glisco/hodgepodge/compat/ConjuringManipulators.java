package com.glisco.hodgepodge.compat;

import com.glisco.conjuring.blocks.soul_weaver.SoulWeaverRecipe;
import com.glisco.hodgepodge.recipe_patches.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.recipe_patches.manipulators.RecipeManipulatorProvider;
import net.minecraft.recipe.Ingredient;

import java.util.Collections;
import java.util.List;

public class ConjuringManipulators implements HodgepodgePlugin {

    @Override
    public void registerManipulators() {
        RecipeManipulatorProvider.register(SoulWeaverRecipe.class, new SoulWeaverManipulator());
    }

    @Override
    public List<String> getRequiredMods() {
        return Collections.singletonList("conjuring");
    }

    public static class SoulWeaverManipulator implements RecipeManipulator<SoulWeaverRecipe> {
        @Override
        public void setInput(SoulWeaverRecipe recipe, int index, Ingredient ingredient) {
            recipe.getInputs().set(index, ingredient);
        }

        @Override
        public List<Ingredient> getIngredients(SoulWeaverRecipe recipe) {
            return recipe.getInputs();
        }

        @Override
        public boolean supports(Operation operation) {
            return operation == Operation.GET_INGREDIENTS || operation == Operation.SET_INGREDIENT;
        }
    }

}
