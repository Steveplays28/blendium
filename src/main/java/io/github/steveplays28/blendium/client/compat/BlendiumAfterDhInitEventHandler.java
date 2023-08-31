package io.github.steveplays28.blendium.client.compat;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterDhInitEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import net.coderbot.iris.Iris;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

public class BlendiumAfterDhInitEventHandler extends DhApiAfterDhInitEvent {
	@Override
	public void afterDistantHorizonsInit(DhApiEventParam<Void> dhApiEventParam) {
		var brightnessMultiplier = DhApi.Delayed.configs.graphics().brightnessMultiplier().getValue();
		var saturationMultiplier = DhApi.Delayed.configs.graphics().saturationMultiplier().getValue();
		var currentShaderPackName = Iris.getCurrentPackName();

		if (!brightnessMultiplier.equals(config.shaderPackBrightnessMultipliers.get(currentShaderPackName))) {
			config.shaderPackBrightnessMultipliers.put(currentShaderPackName, brightnessMultiplier);
			saveConfig();

			if (config.debug) {
				LOGGER.info("Saved changed Distant Horizons LOD brightness multiplier ({}) to the config.", brightnessMultiplier);
			}
		}

		if (!saturationMultiplier.equals(config.shaderPackSaturationMultipliers.get(currentShaderPackName))) {
			config.shaderPackSaturationMultipliers.put(currentShaderPackName, saturationMultiplier);
			saveConfig();

			if (config.debug) {
				LOGGER.info("Saved changed Distant Horizons LOD saturation multiplier ({}) to the config.", saturationMultiplier);
			}
		}
	}
}
