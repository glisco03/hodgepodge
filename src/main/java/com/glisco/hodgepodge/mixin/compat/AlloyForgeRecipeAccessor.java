package com.glisco.hodgepodge.mixin.compat;

import com.glisco.hodgepodge.CompatMixin;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import wraith.alloyforgery.recipe.AlloyForgeRecipe;

@CompatMixin(modid = "alloy_forgery")
@Mixin(AlloyForgeRecipe.class)
public interface AlloyForgeRecipeAccessor {
    @Mutable
    @Accessor("output")
    void hodgepodge$setOutput(ItemStack output);
}
