package com.glisco.hodgepodge.mixin.compat;

import com.glisco.hodgepodge.CompatMixin;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@CompatMixin(modid = "adorn")
@Mixin({ItemBrewingRecipe.class, FluidBrewingRecipe.class})
public interface BrewingRecipeAccessor {
    @Accessor("firstIngredient")
    Ingredient hodgepodge$getFirstIngredient();

    @Mutable
    @Accessor("firstIngredient")
    void hodgepodge$setFirstIngredient(Ingredient firstIngredient);

    @Accessor("secondIngredient")
    Ingredient hodgepodge$getSecondIngredient();

    @Mutable
    @Accessor("secondIngredient")
    void hodgepodge$setSecondIngredient(Ingredient secondIngredient);

    @Mutable
    @Accessor("result")
    void hodgepodge$setResult(ItemStack result);
}
