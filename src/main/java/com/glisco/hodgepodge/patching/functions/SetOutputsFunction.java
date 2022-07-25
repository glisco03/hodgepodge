package com.glisco.hodgepodge.patching.functions;

import com.glisco.hodgepodge.patching.RecipePatch;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulator;
import com.glisco.hodgepodge.patching.manipulators.RecipeManipulators;
import com.google.gson.JsonElement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SetOutputsFunction implements RecipePatch.Function {

    private final ItemStack replacementStack;

    public SetOutputsFunction(ItemStack replacementStack) {
        this.replacementStack = replacementStack;
    }

    @Override
    public void apply(RecipeManipulators.WrappedRecipe<?> recipe) {
        recipe.setOutput(replacementStack);
    }

    @Override
    public boolean eligible(RecipeManipulators.WrappedRecipe<?> recipe) {
        return recipe.supports(RecipeManipulator.Operation.SET_OUTPUT);
    }

    public static RecipePatch.Function fromJson(JsonElement output) {
        String[] descriptor = output.getAsString().split("\\$");
        Item outputItem = Registry.ITEM.get(new Identifier(descriptor[0]));
        return new SetOutputsFunction(new ItemStack(outputItem, descriptor.length < 2 ? 1 : Integer.parseInt(descriptor[1])));
    }
}
