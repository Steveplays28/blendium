package io.github.steveplays28.blendium.client.compat;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterDhInitEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

// TODO: Maybe replace with a Fabric API event for world load/server join
public class BlendiumAfterDhInitEventHandler extends DhApiAfterDhInitEvent {
	@Override
	public void afterDistantHorizonsInit(DhApiEventParam<Void> input) {
		if (FabricLoader.getInstance().isModLoaded(IRIS_SHADERS_MOD_ID)) {
			DhApi.Delayed.configs.graphics().brightnessMultiplier().addChangeListener(this::onBrightnessMultiplierChanged);
			DhApi.Delayed.configs.graphics().saturationMultiplier().addChangeListener(this::onSaturationMultiplierChanged);
		}

		// TODO: Find a way to read DH's config and update Blendium's config when the current shaderpack name isn't null
	}

	private void onBrightnessMultiplierChanged(Double brightnessMultiplier) {
		config.shaderPackBrightnessMultipliers.put(Iris.getCurrentPackName(), brightnessMultiplier);
		DhApi.Delayed.renderProxy.clearRenderDataCache();

		if (config.debug) {
			LOGGER.info("Saved changed Distant Horizons config to Blendium's shaderpack preset for {}.", Iris.getCurrentPackName());
		}
	}

	private void onSaturationMultiplierChanged(Double saturationMultiplier) {
		config.shaderPackSaturationMultipliers.put(Iris.getCurrentPackName(), saturationMultiplier);
		DhApi.Delayed.renderProxy.clearRenderDataCache();

		if (config.debug) {
			LOGGER.info("Saved changed Distant Horizons config to Blendium's shaderpack preset for {}.", Iris.getCurrentPackName());
		}
	}
}
