package com.glisco.hodgepodge.mixin.recipe_accessors;

import com.glisco.hodgepodge.recipe_patches.manipulators.SingleInputProvider;
import com.glisco.hodgepodge.recipe_patches.manipulators.SingleOutputProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CuttingRecipe.class)
public class CuttingRecipeMixin implements SingleOutputProvider, SingleInputProvider {

    @Mutable
    @Shadow @Final protected Ingredient input;

    @Mutable
    @Shadow @Final protected ItemStack output;

    @Override
    public void setInput(Ingredient input) {
        this.input = input;
    }

    @Override
    public void setOutput(ItemStack output) {
        this.output = output;
    }
}
