package com.glisco.hodgepodge;

import com.glisco.hodgepodge.compat.HodgepodgePlugin;
import com.glisco.hodgepodge.recipe_patches.RecipePatchLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hodgepodge implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("Hodgepodge");
    public static final String MOD_ID = "hodgepodge";

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new RecipePatchLoader());

        var plugins = FabricLoader.getInstance().getEntrypointContainers("hodgepodge_plugin", HodgepodgePlugin.class);
        plugins.forEach(hodgepodgePluginEntrypointContainer -> {
            final var entrypoint = hodgepodgePluginEntrypointContainer.getEntrypoint();
            if (!entrypoint.getRequiredMods().stream().allMatch(s -> FabricLoader.getInstance().isModLoaded(s))) return;
            entrypoint.registerManipulators();
        });
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
