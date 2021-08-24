package com.glisco.hodgepodge.recipe_patches.manipulators;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeManipulatorProvider {

    private static final Map<Class<? extends Recipe<?>>, RecipeManipulator<? extends Recipe<?>>> REGISTRY = new HashMap<>();

    static {
        register(ShapedRecipe.class, new RecipeManipulators.SimpleCraftingRecipeManipulator<>());
        register(ShapelessRecipe.class, new RecipeManipulators.SimpleCraftingRecipeManipulator<>());
        register(SmeltingRecipe.class, new RecipeManipulators.SimpleConversionRecipeManipulator<>());
        register(CampfireCookingRecipe.class, new RecipeManipulators.SimpleConversionRecipeManipulator<>());
        register(SmokingRecipe.class, new RecipeManipulators.SimpleConversionRecipeManipulator<>());
        register(BlastingRecipe.class, new RecipeManipulators.SimpleConversionRecipeManipulator<>());
        register(SmithingRecipe.class, new RecipeManipulators.SmithingRecipeManipulator());
        register(StonecuttingRecipe.class, new RecipeManipulators.SimpleConversionRecipeManipulator<>());
    }

    public static <T extends Recipe<?>> void register(Class<T> clazz, RecipeManipulator<T> manipulator) {
        REGISTRY.put(clazz, manipulator);
    }

    @SuppressWarnings("unchecked")
    public static <R extends Recipe<?>> RecipeManipulator<R> get(R recipe) {
        return (RecipeManipulator<R>) REGISTRY.get(recipe.getClass());
    }

    public static WrappedRecipe<? extends Recipe<?>> wrap(Recipe<?> recipe) {
        final RecipeManipulator<Recipe<? extends Inventory>> manipulator = get(recipe);
        if (manipulator == null)
            throw new UnsupportedOperationException("No manipulator registered for recipe class '" + recipe.getClass() + "' with output '" + recipe.getOutput() + "'");

        return new WrappedRecipe<>(recipe, manipulator);
    }

    public static boolean eligible(Recipe<?> recipe) {
        return REGISTRY.containsKey(recipe.getClass());
    }

    public static <R extends Recipe<?>> List<Ingredient> getIngredients(R recipe) {
        if (!REGISTRY.containsKey(recipe.getClass()))
            throw new UnsupportedOperationException("No manipulator registered for recipe class '" + recipe.getClass() + "' with output '" + recipe.getOutput() + "'");
        return get(recipe).getIngredients(recipe);
    }

    public static <R extends Recipe<?>> void setOutput(R recipe, ItemStack stack) {
        if (!REGISTRY.containsKey(recipe.getClass()))
            throw new UnsupportedOperationException("No manipulator registered for recipe class '" + recipe.getClass() + "' with output '" + recipe.getOutput() + "'");
        get(recipe).setOutput(recipe, stack);
    }

    public static <R extends Recipe<?>> void setOutput(R recipe, int index, ItemStack stack) {
        if (!REGISTRY.containsKey(recipe.getClass()))
            throw new UnsupportedOperationException("No manipulator registered for recipe class '" + recipe.getClass() + "' with output '" + recipe.getOutput() + "'");
        get(recipe).setOutput(recipe, index, stack);
    }

    public static class WrappedRecipe<R extends Recipe<?>> {

        private final R recipe;
        private final RecipeManipulator<R> manipulator;

        public WrappedRecipe(R recipe, RecipeManipulator<R> manipulator) {
            this.recipe = recipe;
            this.manipulator = manipulator;
        }

        public R getRecipe() {
            return recipe;
        }

        public List<Ingredient> getIngredients() {
            return manipulator.getIngredients(recipe);
        }

        public void setOutput(ItemStack stack) {
            manipulator.setOutput(recipe, stack);
        }

        public void setOutput(int index, ItemStack stack) {
            manipulator.setOutput(recipe, index, stack);
        }

        public void setInput(int index, Ingredient ingredient) {
            manipulator.setInput(recipe, index, ingredient);
        }

        public void setInput(Ingredient ingredient) {
            manipulator.setInput(recipe, ingredient);
        }

        public boolean supports(RecipeManipulator.Operation operation) {
            return manipulator.supports(operation);
        }

    }

}
