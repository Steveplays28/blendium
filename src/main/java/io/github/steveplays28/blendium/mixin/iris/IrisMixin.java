package io.github.steveplays28.blendium.mixin.iris;

import com.seibel.distanthorizons.api.DhApi;
import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.blendium.client.BlendiumClient.DISTANT_HORIZONS_MOD_ID;
import static io.github.steveplays28.blendium.client.BlendiumClient.config;

@Pseudo
@Mixin(Iris.class)
public class IrisMixin {
	@Inject(method = "loadShaderpack", at = @At(value = "TAIL"))
	private static void loadShaderpackInject(CallbackInfo ci) {
		if (FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID)) {
			DhApi.Delayed.configs.graphics().brightnessMultiplier().setValue(
					config.shaderPackBrightnessMultipliers.get(Iris.getCurrentPackName()));
			DhApi.Delayed.configs.graphics().saturationMultiplier().setValue(
					config.shaderPackSaturationMultipliers.get(Iris.getCurrentPackName()));
		}
	}
}
