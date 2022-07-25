package com.glisco.hodgepodge.mixin.compat;

import com.glisco.conjuring.blocks.soul_weaver.SoulWeaverRecipe;
import com.glisco.conjuring.blocks.soulfire_forge.SoulfireForgeRecipe;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = {SoulWeaverRecipe.class, SoulfireForgeRecipe.class}, remap = false)
public interface ConjuringRecipeAccessor {

    @Mutable
    @Accessor("result")
    void hodgepodge$setResult(ItemStack result);

}
