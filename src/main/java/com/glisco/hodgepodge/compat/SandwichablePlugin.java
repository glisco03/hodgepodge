package com.glisco.hodgepodge.compat;

import com.glisco.hodgepodge.HodgepodgePlugin;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import com.glisco.hodgepodge.patching.manipulators.SimpleConversionRecipeManipulator;
import io.github.foundationgames.sandwichable.recipe.CuttingRecipe;
import io.github.foundationgames.sandwichable.recipe.ToastingRecipe;

import java.util.Collections;
import java.util.List;

public class SandwichablePlugin implements HodgepodgePlugin {

    @Override
    public void registerManipulators() {
        RecipeManipulators.register(CuttingRecipe.class, new SimpleConversionRecipeManipulator<>());
        RecipeManipulators.register(ToastingRecipe.class, new SimpleConversionRecipeManipulator<>());
    }

    @Override
    public List<String> getRequiredMods() {
        return Collections.singletonList("sandwichable");
    }

}
