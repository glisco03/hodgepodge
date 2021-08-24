package com.glisco.hodgepodge.mixin.recipe_accessors;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingRecipe.class)
public interface SmithingRecipeAccessor {

    @Accessor
    void setResult(ItemStack result);

    @Accessor
    Ingredient getBase();

    @Accessor
    Ingredient getAddition();

    @Accessor
    void setBase(Ingredient base);

    @Accessor
    void setAddition(Ingredient addition);

}
