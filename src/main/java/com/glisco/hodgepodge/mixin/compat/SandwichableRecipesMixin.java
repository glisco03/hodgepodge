package com.glisco.hodgepodge.mixin.compat;

import com.glisco.hodgepodge.patching.manipulators.marker.SimpleConversionRecipe;
import io.github.foundationgames.sandwichable.recipe.CuttingRecipe;
import io.github.foundationgames.sandwichable.recipe.ToastingRecipe;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.*;

@Pseudo
@Mixin({ToastingRecipe.class, CuttingRecipe.class})
public abstract class SandwichableRecipesMixin implements SimpleConversionRecipe, Recipe<SimpleInventory> {
    @Shadow(remap = false)
    @Final
    @Mutable
    private Ingredient input;

    @Shadow(remap = false)
    @Final
    @Mutable
    private ItemStack output;

    @Override
    public void setInput(Ingredient input) {
        this.input = input;
    }

    @Override
    public void setOutput(ItemStack output) {
        this.output = output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        var list = DefaultedList.<Ingredient>of();
        list.add(this.input);
        return list;
    }
}
