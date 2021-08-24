package com.glisco.hodgepodge.mixin.recipe_accessors;

import com.glisco.hodgepodge.recipe_patches.manipulators.SingleOutputProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin implements SingleOutputProvider {
    @Mutable
    @Shadow
    @Final
    ItemStack output;

    @Override
    public void setOutput(ItemStack output) {
        this.output = output;
    }
}
