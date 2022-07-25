package com.glisco.hodgepodge.patching;

import com.glisco.hodgepodge.Hodgepodge;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import static com.glisco.hodgepodge.Hodgepodge.LOGGER;

public class RecipePatchLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Gson GSON = new Gson();

    public RecipePatchLoader() {
        super(new Gson(), "recipe_patches");
    }

    @Override
    public Identifier getFabricId() {
        return Hodgepodge.id("recipe_patch_loader");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {

        LOGGER.info("### Begin patch loading stage ###");

        final var patchesFile = FabricLoader.getInstance().getConfigDir().resolve("recipe_patches.json").toFile();
        if (patchesFile.exists()) {
            try {
                LOGGER.info("# Loading patches from config directory");
                prepared.put(Hodgepodge.id("builtin"), GSON.fromJson(new FileReader(patchesFile), JsonElement.class));
            } catch (FileNotFoundException e) {
                LOGGER.warn("Unable to load config patch file. Exception: {}", e.getMessage());
            }
        } else {
            LOGGER.info("# No patches found in config directory");
        }

        LOGGER.info("# Loading {} patch files", prepared.size());

        prepared.forEach((identifier, jsonElement) -> {
            var rootJson = jsonElement.getAsJsonObject();
            var patches = JsonHelper.getArray(rootJson, "patch", new JsonArray());

            patches.forEach(patchElement -> {
                if (!patchElement.isJsonObject()) {
                    LOGGER.warn("Unable to load patch entry {} because it's not an object. Skipping", patchElement);
                    return;
                }

                var patchObject = patchElement.getAsJsonObject();

                try {
                    RecipePredicate predicate = RecipePredicate.parse(patchObject.get("predicate"));
                    patchObject.remove("predicate");

                    var patch = RecipePatch.fromJson(patchObject);
                    RecipePatcher.addPatchRule(predicate, patch);
                } catch (Exception e) {
                    LOGGER.warn("Caught exception while parsing patch definition. Message: {}", e.getMessage());
                }

            });

            var removeIds = JsonHelper.getArray(rootJson, "remove", new JsonArray());
            removeIds.forEach(idElement -> {
                try {
                    RecipePatcher.addRemoveRule(RecipePredicate.parse(idElement));
                } catch (Exception e) {
                    LOGGER.warn("Caught exception while parsing removal predicate. Message: {}", e.getMessage());
                }
            });

        });

        LOGGER.info("### End patch loading stage ###");

//        RecipePatcher.apply();
    }
}
