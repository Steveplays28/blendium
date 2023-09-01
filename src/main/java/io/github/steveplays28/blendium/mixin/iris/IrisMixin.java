package io.github.steveplays28.blendium.mixin.iris;

import com.seibel.distanthorizons.api.DhApi;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

@Pseudo
@Mixin(Iris.class)
public class IrisMixin {
	@Inject(method = "loadShaderpack", at = @At(value = "TAIL"), remap = false)
	private static void loadShaderpackInject(CallbackInfo ci) {
		var shouldClearDHRenderDataCache = false;

		if (FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID)) {
			if (config.shaderPackBrightnessMultipliers.containsKey(Iris.getCurrentPackName())) {
				DhApi.Delayed.configs.graphics().brightnessMultiplier().setValue(
						config.shaderPackBrightnessMultipliers.get(Iris.getCurrentPackName()));
				shouldClearDHRenderDataCache = true;

			} else {
				config.shaderPackBrightnessMultipliers.put(
						Iris.getCurrentPackName(), DhApi.Delayed.configs.graphics().brightnessMultiplier().getValue());
				saveConfig();
			}

			if (config.shaderPackSaturationMultipliers.containsKey(Iris.getCurrentPackName())) {
				DhApi.Delayed.configs.graphics().saturationMultiplier().setValue(
						config.shaderPackSaturationMultipliers.get(Iris.getCurrentPackName()));
				shouldClearDHRenderDataCache = true;
			} else {
				config.shaderPackSaturationMultipliers.put(
						Iris.getCurrentPackName(), DhApi.Delayed.configs.graphics().saturationMultiplier().getValue());
				saveConfig();
			}

			if (shouldClearDHRenderDataCache) {
				DhApi.Delayed.renderProxy.clearRenderDataCache();
			}
		}

		if (!config.shaderPackWaterReflectionColors.containsKey(Iris.getCurrentPackName())) {
			config.shaderPackWaterReflectionColors.put(Iris.getCurrentPackName(), new Vec3d(-1f, -1f, -1f));
			saveConfig();
		}

		if (config.debug) {
			// TODO: Log info about current preset using a helper method
			LOGGER.info("Applied Blendium shaderpack preset to Distant Horizons' config.");
		}
	}
}
