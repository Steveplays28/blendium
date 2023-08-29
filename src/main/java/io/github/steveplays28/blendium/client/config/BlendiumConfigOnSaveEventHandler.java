package io.github.steveplays28.blendium.client.config;

import com.seibel.distanthorizons.api.DhApi;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.ActionResult;

import static io.github.steveplays28.blendium.client.BlendiumClient.DISTANT_HORIZONS_MOD_ID;
import static io.github.steveplays28.blendium.client.BlendiumClient.IRIS_SHADERS_MOD_ID;

@SuppressWarnings("unused")
public class BlendiumConfigOnSaveEventHandler implements ConfigSerializeEvent.Save<BlendiumConfig> {
	@Override
	public ActionResult onSave(ConfigHolder<BlendiumConfig> configHolder, BlendiumConfig blendiumConfig) {
		if (FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID) && FabricLoader.getInstance().isModLoaded(IRIS_SHADERS_MOD_ID)) {
			DhApi.Delayed.configs.graphics().brightnessMultiplier().setValue(
					blendiumConfig.shaderPackBrightnessMultipliers.get(Iris.getCurrentPackName()));
			DhApi.Delayed.configs.graphics().saturationMultiplier().setValue(
					blendiumConfig.shaderPackSaturationMultipliers.get(Iris.getCurrentPackName()));
		}

		return ActionResult.SUCCESS;
	}
}
