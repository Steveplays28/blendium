package io.github.steveplays28.blendium.client.compat.iris;

import com.seibel.distanthorizons.api.DhApi;
import io.github.steveplays28.blendium.client.BlendiumClient;
import io.github.steveplays28.blendium.client.config.BlendiumConfigLoader;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;

import static io.github.steveplays28.blendium.client.BlendiumClient.DISTANT_HORIZONS_MOD_ID;
import static io.github.steveplays28.blendium.client.BlendiumClient.IRIS_SHADERS_MOD_ID;

public class BlendiumDhShaderpackPresets {
	public static final String OFF_SHADERPACK_NAME = "(off)";

	public static void applyDhShaderpackPreset(String shaderpackName) {
		// Check if Iris and Distant Horizons are loaded (sanity check)
		if (!FabricLoader.getInstance().isModLoaded(IRIS_SHADERS_MOD_ID) || !FabricLoader.getInstance().isModLoaded(
				DISTANT_HORIZONS_MOD_ID)) {
			return;
		}

		// Check if the shaderpack exists
		// If the shaderpack doesn't exist, log an error and return
		try {
			if (!shaderpackName.equals("(off)") && !Iris.getShaderpacksDirectoryManager().enumerate().contains(shaderpackName)) {
				BlendiumClient.LOGGER.error("Couldn't apply shaderpack preset for {}: shaderpack not found.", shaderpackName);
				return;
			}
		} catch (IOException e) {
			BlendiumClient.LOGGER.error(
					"Couldn't apply shaderpack preset for {}: unable to iterate over shaderpacks. See stack trace below:", shaderpackName);
			e.printStackTrace();
			return;
		}

		// Apply DH LOD brightness from the shaderpack preset
		var shaderpackBrightnessMultiplier = BlendiumConfigLoader.BlendiumConfigurations.CONFIG.shaderpackBrightnessMultipliers.getOrDefault(
				shaderpackName, 1d);
		DhApi.Delayed.configs.graphics().brightnessMultiplier().setValue(shaderpackBrightnessMultiplier);

		// Apply DH LOD saturation from the shaderpack preset
		var shaderpackSaturationMultiplier = BlendiumConfigLoader.BlendiumConfigurations.CONFIG.shaderpackSaturationMultipliers.getOrDefault(
				shaderpackName, 1d);
		DhApi.Delayed.configs.graphics().saturationMultiplier().setValue(shaderpackSaturationMultiplier);

		BlendiumClient.LOGGER.info("Applied shaderpack preset for {}.", shaderpackName);
	}

	public static String getShaderpackName() {
		// Check if Iris is loaded (sanity check)
		if (!FabricLoader.getInstance().isModLoaded(IRIS_SHADERS_MOD_ID)) {
			return OFF_SHADERPACK_NAME;
		}

		return Iris.getCurrentPackName();
	}
}
