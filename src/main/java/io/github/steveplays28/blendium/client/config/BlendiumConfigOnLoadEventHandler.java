package io.github.steveplays28.blendium.client.config;

import io.github.steveplays28.blendium.client.compat.iris.BlendiumDhShaderpackPresets;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;
import static io.github.steveplays28.blendium.client.compat.iris.BlendiumDhShaderpackPresets.applyDhShaderpackPreset;

public class BlendiumConfigOnLoadEventHandler {
	public static void onReload() {
		if (!FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID) || !FabricLoader.getInstance().isModLoaded(
				IRIS_SHADERS_MOD_ID)) {
			return;
		}

		applyDhShaderpackPreset(BlendiumDhShaderpackPresets.getShaderpackName());
	}
}
