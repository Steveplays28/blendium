package io.github.steveplays28.blendium.client.config;

import com.seibel.distanthorizons.api.DhApi;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

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

			if (!MathHelper.approximatelyEquals(
					brightnessMultiplier.getValue(), blendiumConfig.shaderPackBrightnessMultipliers.get(currentShaderPackName))) {
				brightnessMultiplier.setValue(blendiumConfig.shaderPackBrightnessMultipliers.get(currentShaderPackName));
				shouldClearDHRenderDataCache = true;
			}

			if (!MathHelper.approximatelyEquals(
					saturationMultiplier.getValue(), blendiumConfig.shaderPackSaturationMultipliers.get(currentShaderPackName))) {
				saturationMultiplier.setValue(blendiumConfig.shaderPackSaturationMultipliers.get(currentShaderPackName));
				shouldClearDHRenderDataCache = true;
			}

			if (shouldClearDHRenderDataCache) {
				DhApi.Delayed.renderProxy.clearRenderDataCache();
			}

			if (config.debug) {
				LOGGER.info("Applied shaderpack preset to Distant Horizons config.");
			}
		}

		return ActionResult.SUCCESS;
	}
}
