package io.github.steveplays28.blendium.client.config;

import com.seibel.distanthorizons.api.DhApi;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.ActionResult;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;
import static io.github.steveplays28.blendium.client.BlendiumShaderPackPresetHelper.isDhBrightnessMultiplierEqualToBlendiumShaderPackPreset;
import static io.github.steveplays28.blendium.client.BlendiumShaderPackPresetHelper.isDhSaturationMultiplierEqualToBlendiumShaderPackPreset;

@SuppressWarnings("unused")
public class BlendiumConfigOnLoadEventHandler implements ConfigSerializeEvent.Load<BlendiumConfig> {
	@Override
	public ActionResult onLoad(ConfigHolder<BlendiumConfig> configHolder, BlendiumConfig blendiumConfig) {
		if (FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID) && FabricLoader.getInstance().isModLoaded(
				IRIS_SHADERS_MOD_ID)) {
			var brightnessMultiplier = DhApi.Delayed.configs.graphics().brightnessMultiplier();
			var saturationMultiplier = DhApi.Delayed.configs.graphics().saturationMultiplier();
			var currentShaderPackName = Iris.getCurrentPackName();
			var shouldClearDHRenderDataCache = false;

			if (blendiumConfig.shaderPackBrightnessMultipliers.containsKey(currentShaderPackName)) {
				if (!isDhBrightnessMultiplierEqualToBlendiumShaderPackPreset()) {
					brightnessMultiplier.setValue(blendiumConfig.shaderPackBrightnessMultipliers.get(currentShaderPackName));
					shouldClearDHRenderDataCache = true;
				}
			} else {
				config.shaderPackBrightnessMultipliers.put(
						currentShaderPackName, DhApi.Delayed.configs.graphics().brightnessMultiplier().getDefaultValue());
			}

			if (blendiumConfig.shaderPackSaturationMultipliers.containsKey(currentShaderPackName)) {
				if (!isDhSaturationMultiplierEqualToBlendiumShaderPackPreset()) {
					saturationMultiplier.setValue(blendiumConfig.shaderPackSaturationMultipliers.get(currentShaderPackName));
					shouldClearDHRenderDataCache = true;
				}
			} else {
				config.shaderPackSaturationMultipliers.put(
						currentShaderPackName, DhApi.Delayed.configs.graphics().saturationMultiplier().getDefaultValue());
			}

			if (shouldClearDHRenderDataCache) {
				DhApi.Delayed.renderProxy.clearRenderDataCache();

				if (config.debug) {
					// TODO: Log info about current preset using a helper method
					LOGGER.info("Applied Blendium shaderpack preset to Distant Horizons' config.");
				}
			} else if (config.debug) {
				LOGGER.info("No changes to the Blendium shaderpack presets found to apply to Distant Horizons' config.");
			}
		}

		return ActionResult.SUCCESS;
	}
}
