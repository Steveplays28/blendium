package io.github.steveplays28.blendium.client.compat;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterDhInitEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import net.coderbot.iris.Iris;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;
import static io.github.steveplays28.blendium.client.BlendiumShaderPackPresetHelper.isDhBrightnessMultiplierEqualToBlendiumShaderPackPreset;
import static io.github.steveplays28.blendium.client.BlendiumShaderPackPresetHelper.isDhSaturationMultiplierEqualToBlendiumShaderPackPreset;

public class BlendiumAfterDhInitEventHandler extends DhApiAfterDhInitEvent {
	@Override
	public void afterDistantHorizonsInit(DhApiEventParam<Void> dhApiEventParam) {
		var brightnessMultiplier = DhApi.Delayed.configs.graphics().brightnessMultiplier().getValue();
		var saturationMultiplier = DhApi.Delayed.configs.graphics().saturationMultiplier().getValue();
		var currentShaderPackName = Iris.getCurrentPackName();
		var configChangesFound = false;

		if (!isDhBrightnessMultiplierEqualToBlendiumShaderPackPreset()) {
			config.shaderPackBrightnessMultipliers.put(currentShaderPackName, brightnessMultiplier);
			saveConfig();
			configChangesFound = true;
		}

		if (!isDhSaturationMultiplierEqualToBlendiumShaderPackPreset()) {
			config.shaderPackSaturationMultipliers.put(currentShaderPackName, saturationMultiplier);
			saveConfig();
			configChangesFound = true;
		}

		if (configChangesFound && config.debug) {
			LOGGER.info("Saved changed Distant Horizons settings to Blendium's shaderpack preset for {}.", currentShaderPackName);
		}
	}
}
