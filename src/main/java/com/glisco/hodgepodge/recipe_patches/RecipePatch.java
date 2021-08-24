package com.glisco.hodgepodge.recipe_patches;

import com.glisco.hodgepodge.Hodgepodge;
import com.glisco.hodgepodge.recipe_patches.functions.PatchFunctionRegistry;
import com.glisco.hodgepodge.recipe_patches.manipulators.RecipeManipulatorProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecipePatch {

    private final List<Function> functions;
    private final UUID uuid;

    private RecipePatch(List<Function> functions) {
        this.functions = functions;
        this.uuid = UUID.randomUUID();
    }

    public void apply(RecipeManipulatorProvider.WrappedRecipe<?> recipe) {

        for (Function function : functions) {
            if (!function.eligible(recipe)) continue;
            function.apply(recipe);
        }
    }

    public static RecipePatch fromJson(JsonObject patch) {
        var functions = new ArrayList<Function>();

        for (Map.Entry<String, JsonElement> entry : patch.entrySet()) {
            var id = parseIdentifier(entry.getKey());
            functions.add(PatchFunctionRegistry.create(id, entry.getValue()));
        }

        return new RecipePatch(functions);
    }

    private static Identifier parseIdentifier(String string) {
        return string.contains(":") ? Identifier.tryParse(string) : Hodgepodge.id(string);
    }

    public interface Function {

        void apply(RecipeManipulatorProvider.WrappedRecipe<?> recipe);

        boolean eligible(RecipeManipulatorProvider.WrappedRecipe<?> recipe);

    }

}
