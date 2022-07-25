package com.glisco.hodgepodge.mixin;

import com.glisco.hodgepodge.Hodgepodge;
import com.glisco.hodgepodge.MutableImmutableMapBuilder;
import com.glisco.hodgepodge.patching.RecipePatcher;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    /**
     * Redirect map builder creation to allow modification at a later stage
     *
     * @author glisco
     * @reason dude it's literally right above, shut up
     */
    @Overwrite(remap = false)
    private static <K, V> ImmutableMap.Builder<K, V> method_20707(RecipeType<?> type) {
        return new MutableImmutableMapBuilder<>();
    }

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/recipe/RecipeManager;recipes:Ljava/util/Map;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void injectPostProcessor(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci, Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> recipeTypeBuilderMap) {
        Hodgepodge.LOGGER.info("Recipe map saved");
        RecipePatcher.storeMap(recipeTypeBuilderMap, (RecipeManager) (Object) this);
    }

}
