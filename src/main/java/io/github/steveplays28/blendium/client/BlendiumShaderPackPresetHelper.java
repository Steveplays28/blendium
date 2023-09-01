package io.github.steveplays28.blendium.client;

import com.seibel.distanthorizons.api.DhApi;
import net.coderbot.iris.Iris;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.blendium.client.BlendiumClient.config;

public class BlendiumShaderPackPresetHelper {
	public static @NotNull Boolean isDhBrightnessMultiplierEqualToBlendiumShaderPackPreset() {
		return MathHelper.approximatelyEquals(
				DhApi.Delayed.configs.graphics().brightnessMultiplier().getValue(), config.shaderPackSaturationMultipliers.get(Iris.getCurrentPackName()));
	}

	public static @NotNull Boolean isDhSaturationMultiplierEqualToBlendiumShaderPackPreset() {
		return MathHelper.approximatelyEquals(
				DhApi.Delayed.configs.graphics().saturationMultiplier().getValue(), config.shaderPackSaturationMultipliers.get(Iris.getCurrentPackName()));
	}
}
