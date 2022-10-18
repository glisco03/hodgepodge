package com.glisco.hodgepodge.patching.manipulators;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeManipulators {

    private static final Map<Class<? extends Recipe<?>>, RecipeManipulator<? extends Recipe<?>>> REGISTRY = new HashMap<>();

    static {
        register(ShapedRecipe.class, new SimpleCraftingRecipeManipulator<>());
        register(ShapelessRecipe.class, new SimpleCraftingRecipeManipulator<>());
        register(SmeltingRecipe.class, new SimpleConversionRecipeManipulator<>());
        register(CampfireCookingRecipe.class, new SimpleConversionRecipeManipulator<>());
        register(SmokingRecipe.class, new SimpleConversionRecipeManipulator<>());
        register(BlastingRecipe.class, new SimpleConversionRecipeManipulator<>());
        register(SmithingRecipe.class, new SmithingRecipeManipulator());
        register(StonecuttingRecipe.class, new SimpleConversionRecipeManipulator<>());
    }

    public static <T extends Recipe<?>> void register(Class<T> clazz, RecipeManipulator<? super T> manipulator) {
        REGISTRY.put(clazz, manipulator);
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

    @SuppressWarnings("unchecked")
    public static <R extends Recipe<?>> RecipeManipulator<R> get(R recipe) {
        return (RecipeManipulator<R>) REGISTRY.get(recipe.getClass());
    }

    public static class WrappedRecipe<R extends Recipe<?>> {

        private final R recipe;
        private final RecipeManipulator<R> manipulator;

        public WrappedRecipe(R recipe, RecipeManipulator<R> manipulator) {
            this.recipe = recipe;
            this.manipulator = manipulator;
        }

        public R getRecipe() {
            return this.recipe;
        }

        public List<Ingredient> getIngredients() {
            return this.manipulator.getIngredients(recipe);
        }

        public void setOutput(ItemStack stack) {
            this.manipulator.setOutput(recipe, stack);
        }

        public void setOutput(int index, ItemStack stack) {
            this.manipulator.setOutput(recipe, index, stack);
        }

        public void setInput(int index, Ingredient ingredient) {
            this.manipulator.setInput(recipe, index, ingredient);
        }

        public void setInput(Ingredient ingredient) {
            this.manipulator.setInput(recipe, ingredient);
        }

        public boolean supports(RecipeManipulator.Operation operation) {
            return this.manipulator.supports(operation);
        }

    }

}
