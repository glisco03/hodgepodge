package com.glisco.hodgepodge.patching.functions;

import com.glisco.hodgepodge.patching.RecipePatch;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import com.google.gson.JsonElement;

public class SetOutputCountFunction implements RecipePatch.Function {

    private final int newCount;

    public SetOutputCountFunction(int newCount) {
        this.newCount = newCount;
    }

    @Override
    public void apply(RecipeManipulators.WrappedRecipe<?> recipe) {
        final var output = recipe.getRecipe().getOutput();
        output.setCount(newCount);
        recipe.setOutput(output);
    }

    @Override
    public boolean eligible(RecipeManipulators.WrappedRecipe<?> recipe) {
        return recipe.supports(RecipeManipulator.Operation.SET_OUTPUT);
    }

    public static RecipePatch.Function fromJson(JsonElement output) {
        return new SetOutputCountFunction(output.getAsInt());
    }
}
