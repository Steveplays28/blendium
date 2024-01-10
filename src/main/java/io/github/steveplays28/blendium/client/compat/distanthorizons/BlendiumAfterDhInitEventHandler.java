package io.github.steveplays28.blendium.client.compat.distanthorizons;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterDhInitEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import io.github.steveplays28.blendium.client.compat.iris.BlendiumDhShaderpackPresets;
import org.apache.commons.math3.util.Precision;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

// TODO: Maybe reset the LOD shaderpack presets when unloading the game, in case Iris gets disabled/removed
public class BlendiumAfterDhInitEventHandler extends DhApiAfterDhInitEvent {
	@Override
	public void afterDistantHorizonsInit(DhApiEventParam<Void> input) {
		DhApi.Delayed.configs.graphics().brightnessMultiplier().addChangeListener(this::onBrightnessMultiplierChanged);
		DhApi.Delayed.configs.graphics().saturationMultiplier().addChangeListener(this::onSaturationMultiplierChanged);
	}

	private void onBrightnessMultiplierChanged(Double brightnessMultiplier) {
		DhApi.Delayed.configs.graphics().brightnessMultiplier().setValue(null);

		// Check if the changed value is the same as the currently stored value, and discard the save attempt if true
		// This avoids resaving the config file multiple times (e.x. during game loading)
		if (Precision.equals(
				brightnessMultiplier,
				config.shaderpackBrightnessMultipliers.getOrDefault(BlendiumDhShaderpackPresets.getShaderpackName(), brightnessMultiplier)
		)) {
			return;
		}

		config.shaderpackBrightnessMultipliers.put(BlendiumDhShaderpackPresets.getShaderpackName(), brightnessMultiplier);
		saveConfig();

		if (config.debug) {
			LOGGER.info("Updated shaderpack preset for {}.", BlendiumDhShaderpackPresets.getShaderpackName());
		}
	}

	private void onSaturationMultiplierChanged(Double saturationMultiplier) {
		DhApi.Delayed.configs.graphics().saturationMultiplier().setValue(null);

		// Check if the changed value is the same as the currently stored value, and discard the save attempt if true
		// This avoids resaving the config file multiple times (e.x. during game loading)
		if (Precision.equals(
				saturationMultiplier,
				config.shaderpackSaturationMultipliers.getOrDefault(BlendiumDhShaderpackPresets.getShaderpackName(), saturationMultiplier)
		)) {
			return;
		}

		config.shaderpackSaturationMultipliers.put(BlendiumDhShaderpackPresets.getShaderpackName(), saturationMultiplier);
		saveConfig();

		if (config.debug) {
			LOGGER.info("Updated shaderpack preset for {}.", BlendiumDhShaderpackPresets.getShaderpackName());
		}
	}
}
