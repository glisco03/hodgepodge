package com.glisco.hodgepodge.recipe_patches.functions;

import com.glisco.hodgepodge.Hodgepodge;
import com.glisco.hodgepodge.recipe_patches.RecipePatch;
import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PatchFunctionRegistry {

    private static final Map<Identifier, Function<JsonElement, RecipePatch.Function>> REGISTRY = new HashMap<>();

    static {
        register(Hodgepodge.id("remap"), DefaultPatchFunctions.ReplaceIngredientsFunction::fromJson);
        register(Hodgepodge.id("set_output"), DefaultPatchFunctions.SetOutputsFunction::fromJson);
        register(Hodgepodge.id("set_output_count"), DefaultPatchFunctions.SetOutputCountFunction::fromJson);
        register(Hodgepodge.id("set"), DefaultPatchFunctions.SetInputsFunction::fromJson);
    }

    public static void register(Identifier id, Function<JsonElement, RecipePatch.Function> functionCreator) {
        if (REGISTRY.containsKey(id)) throw new IllegalArgumentException("Attempted to register same identifier twice!");
        REGISTRY.put(id, functionCreator);
    }

    public static RecipePatch.Function create(Identifier id, JsonElement json) {
        if (!REGISTRY.containsKey(id)) return null;
        return REGISTRY.get(id).apply(json);
    }

}
