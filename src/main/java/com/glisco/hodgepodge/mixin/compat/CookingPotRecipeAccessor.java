package com.glisco.hodgepodge.mixin.compat;

import com.nhoryzon.mc.farmersdelight.recipe.CookingPotRecipe;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = CookingPotRecipe.class, remap = false)
public interface CookingPotRecipeAccessor {

    @Mutable
    @Accessor("output")
    void hodgepodge$setOutput(ItemStack output);

    @Mutable
    @Accessor("container")
    void hodgepodge$setContainer(ItemStack container);

}
