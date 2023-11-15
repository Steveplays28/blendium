package io.github.steveplays28.blendium.client.compat.distanthorizons;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterDhInitEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import io.github.steveplays28.blendium.client.compat.iris.BlendiumDhShaderpackPresets;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;
import static io.github.steveplays28.blendium.client.compat.iris.BlendiumDhShaderpackPresets.applyDhShaderpackPreset;

// TODO: Maybe reset the LOD shaderpack presets when unloading the game, in case Iris gets disabled/removed
public class BlendiumAfterDhInitEventHandler extends DhApiAfterDhInitEvent {
	@Override
	public void afterDistantHorizonsInit(DhApiEventParam<Void> input) {
		if (FabricLoader.getInstance().isModLoaded(IRIS_SHADERS_MOD_ID)) {
			applyDhShaderpackPreset(BlendiumDhShaderpackPresets.getShaderpackName());
		}

		DhApi.Delayed.configs.graphics().brightnessMultiplier().addChangeListener(this::onBrightnessMultiplierChanged);
		DhApi.Delayed.configs.graphics().saturationMultiplier().addChangeListener(this::onSaturationMultiplierChanged);
	}

	private void onBrightnessMultiplierChanged(Double brightnessMultiplier) {
		config.shaderpackBrightnessMultipliers.put(BlendiumDhShaderpackPresets.getShaderpackName(), brightnessMultiplier);
		saveConfig();

		if (config.debug) {
			LOGGER.info("Updated shaderpack preset for {}.", BlendiumDhShaderpackPresets.getShaderpackName());
		}
	}

	private void onSaturationMultiplierChanged(Double saturationMultiplier) {
		config.shaderpackSaturationMultipliers.put(BlendiumDhShaderpackPresets.getShaderpackName(), saturationMultiplier);
		saveConfig();

		if (config.debug) {
			LOGGER.info("Updated shaderpack preset for {}.", BlendiumDhShaderpackPresets.getShaderpackName());
		}
	}
}
