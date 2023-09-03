package io.github.steveplays28.blendium.client.config;

import com.seibel.distanthorizons.api.DhApi;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;
import org.joml.Vector3f;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;
import static io.github.steveplays28.blendium.client.BlendiumShaderPackPresetHelper.isDhBrightnessMultiplierEqualToBlendiumShaderPackPreset;
import static io.github.steveplays28.blendium.client.BlendiumShaderPackPresetHelper.isDhSaturationMultiplierEqualToBlendiumShaderPackPreset;

public class BlendiumConfigOnLoadEventHandler {
	public static void onReload() {
		if (FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID) && FabricLoader.getInstance().isModLoaded(
				IRIS_SHADERS_MOD_ID)) {
			var brightnessMultiplier = DhApi.Delayed.configs.graphics().brightnessMultiplier();
			var saturationMultiplier = DhApi.Delayed.configs.graphics().saturationMultiplier();
			var currentShaderPackName = Iris.getCurrentPackName();
			var shouldClearDHRenderDataCache = false;
			var shouldSaveConfig = false;

			if (config.shaderPackBrightnessMultipliers.containsKey(currentShaderPackName)) {
				if (!isDhBrightnessMultiplierEqualToBlendiumShaderPackPreset()) {
					brightnessMultiplier.setValue(config.shaderPackBrightnessMultipliers.get(currentShaderPackName));
					shouldClearDHRenderDataCache = true;
				}
			} else {
				config.shaderPackBrightnessMultipliers.put(
						currentShaderPackName, DhApi.Delayed.configs.graphics().brightnessMultiplier().getDefaultValue());
				shouldSaveConfig = true;
			}

			if (config.shaderPackSaturationMultipliers.containsKey(currentShaderPackName)) {
				if (!isDhSaturationMultiplierEqualToBlendiumShaderPackPreset()) {
					saturationMultiplier.setValue(config.shaderPackSaturationMultipliers.get(currentShaderPackName));
					shouldClearDHRenderDataCache = true;
				}
			} else {
				config.shaderPackSaturationMultipliers.put(
						currentShaderPackName, DhApi.Delayed.configs.graphics().saturationMultiplier().getDefaultValue());
				shouldSaveConfig = true;
			}

			if (!config.shaderPackWaterReflectionColors.containsKey(Iris.getCurrentPackName())) {
				config.shaderPackWaterReflectionColors.put(Iris.getCurrentPackName(), new Vector3f(-1f, -1f, -1f));
				shouldSaveConfig = true;
			}

			if (shouldSaveConfig) {
				saveConfig();
			}

			if (shouldClearDHRenderDataCache) {
				DhApi.Delayed.renderProxy.clearRenderDataCache();

				if (config.debug) {
					// TODO: Log info about current preset using a helper method
					LOGGER.info("Applied Blendium shaderpack preset to Distant Horizons' config for {}.", currentShaderPackName);
				}
			} else if (config.debug) {
				LOGGER.info("No changes to the Blendium shaderpack presets found to apply to Distant Horizons' config.");
			}
		}
	}
}
