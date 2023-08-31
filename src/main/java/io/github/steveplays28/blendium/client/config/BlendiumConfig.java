package io.github.steveplays28.blendium.client.config;

import io.github.steveplays28.blendium.client.BlendiumClient;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

@Config(name = BlendiumClient.MOD_ID)
public class BlendiumConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public float viewDistanceFactor = 0.4f;
	@ConfigEntry.Gui.Tooltip
	public HashMap<String, Double> shaderPackBrightnessMultipliers = new HashMap<>();
	@ConfigEntry.Gui.Tooltip
	public HashMap<String, Double> shaderPackSaturationMultipliers = new HashMap<>();
	@ConfigEntry.Gui.Tooltip
	public HashMap<String, Vec3d> shaderPackWaterReflectionColors = new HashMap<>() {{
		put("(off)", new Vec3d(0f, 0f, 0f));
		put("Complementary Reimagined", new Vec3d(1f, 1f, 1f));
	}};
	@ConfigEntry.Gui.Tooltip
	public Boolean debug = false;
}
