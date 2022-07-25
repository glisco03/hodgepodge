package com.glisco.hodgepodge.patching.functions;

import com.glisco.hodgepodge.IngredientHelper;
import com.glisco.hodgepodge.patching.RecipePatch;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import com.google.gson.JsonElement;
import net.minecraft.recipe.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class SetInputsFunction implements RecipePatch.Function {

    private final Map<Integer, Ingredient> inputs;

    public SetInputsFunction(Map<Integer, Ingredient> inputs) {
        this.inputs = inputs;
    }

    @Override
    public void apply(RecipeManipulators.WrappedRecipe<?> recipe) {
        inputs.forEach(recipe::setInput);
    }

    @Override
    public boolean eligible(RecipeManipulators.WrappedRecipe<?> recipe) {
        return recipe.supports(RecipeManipulator.Operation.GET_INGREDIENTS);
    }

    public static RecipePatch.Function fromJson(JsonElement element) {
        final var inputs = element.getAsJsonObject();
        final var parsedInputs = new HashMap<Integer, Ingredient>();

        inputs.entrySet().forEach(entry -> {
            parsedInputs.put(Integer.valueOf(entry.getKey()), IngredientHelper.parse(entry.getValue().getAsString()));
        });

        return new SetInputsFunction(parsedInputs);
    }
}
