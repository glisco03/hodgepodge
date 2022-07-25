package com.glisco.hodgepodge.mixin;

import com.glisco.hodgepodge.patching.RecipePatcher;
import net.minecraft.server.DataPackContents;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataPackContents.class)
public class DataPackContentsMixin {

    @Inject(method = "refresh", at = @At("TAIL"))
    private void onRefresh(DynamicRegistryManager dynamicRegistryManager, CallbackInfo ci) {
        RecipePatcher.apply();
    }

}
