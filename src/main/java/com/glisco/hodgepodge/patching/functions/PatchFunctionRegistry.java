package com.glisco.hodgepodge.patching.functions;

import com.glisco.hodgepodge.Hodgepodge;
import com.glisco.hodgepodge.patching.RecipePatch;
import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PatchFunctionRegistry {

    private static final Map<Identifier, Function<JsonElement, RecipePatch.Function>> REGISTRY = new HashMap<>();

    static {
        register(Hodgepodge.id("remap"), RemapIngredientsFunction::fromJson);
        register(Hodgepodge.id("set_output"), SetOutputsFunction::fromJson);
        register(Hodgepodge.id("set_output_count"), SetOutputCountFunction::fromJson);
        register(Hodgepodge.id("set"), SetInputsFunction::fromJson);
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
