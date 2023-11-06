package io.github.steveplays28.blendium.client.compat;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterDhInitEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;
import static io.github.steveplays28.blendium.client.compat.iris.BlendiumDHShaderpackPresets.applyDHShaderpackPreset;

// TODO: Maybe reset the LOD shaderpack presets when unloading the game, in case Iris gets disabled/removed
public class BlendiumAfterDhInitEventHandler extends DhApiAfterDhInitEvent {
	@Override
	public void afterDistantHorizonsInit(DhApiEventParam<Void> input) {
		if (FabricLoader.getInstance().isModLoaded(IRIS_SHADERS_MOD_ID)) {
			applyDHShaderpackPreset(Iris.getCurrentPackName());
		}

		DhApi.Delayed.configs.graphics().brightnessMultiplier().addChangeListener(this::onBrightnessMultiplierChanged);
		DhApi.Delayed.configs.graphics().saturationMultiplier().addChangeListener(this::onSaturationMultiplierChanged);
	}

	private void onBrightnessMultiplierChanged(Double brightnessMultiplier) {
		config.shaderpackBrightnessMultipliers.put(Iris.getCurrentPackName(), brightnessMultiplier);
		saveConfig();

		if (config.debug) {
			LOGGER.info("Updated shaderpack preset for {}.", Iris.getCurrentPackName());
		}
	}

	private void onSaturationMultiplierChanged(Double saturationMultiplier) {
		config.shaderpackSaturationMultipliers.put(Iris.getCurrentPackName(), saturationMultiplier);
		saveConfig();

		if (config.debug) {
			LOGGER.info("Updated shaderpack preset for {}.", Iris.getCurrentPackName());
		}
	}
}
