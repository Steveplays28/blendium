package io.github.steveplays28.blendium.client.config;

import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;
import static io.github.steveplays28.blendium.client.compat.iris.BlendiumDHShaderpackPresets.applyDHShaderpackPreset;

public class BlendiumConfigOnLoadEventHandler {
	public static void onReload() {
		if (!FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID) || !FabricLoader.getInstance().isModLoaded(
				IRIS_SHADERS_MOD_ID)) {
			return;
		}

		applyDHShaderpackPreset(Iris.getCurrentPackName());
	}
}
