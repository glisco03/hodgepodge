package com.glisco.hodgepodge.mixin.recipe_accessors;

import com.glisco.hodgepodge.patching.manipulators.marker.SimpleConversionRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractCookingRecipe.class)
public class AbstractCookingRecipeMixin implements SimpleConversionRecipe {

    @Mutable
    @Shadow
    @Final
    protected Ingredient input;

    @Mutable
    @Shadow
    @Final
    protected ItemStack output;

    @Override
    public void setInput(Ingredient input) {
        this.input = input;
    }

    @Override
    public void setOutput(ItemStack output) {
        this.output = output;
    }
}
