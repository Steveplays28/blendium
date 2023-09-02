package io.github.steveplays28.blendium.client.config;

import io.github.steveplays28.blendium.client.BlendiumClient;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.joml.Vector3f;

import java.util.HashMap;

@Config(name = BlendiumClient.MOD_ID)
public class BlendiumConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public float viewDistanceFactor = 0.4f;
	@ConfigEntry.Gui.Tooltip
	public HashMap<String, Double> shaderPackBrightnessMultipliers = new HashMap<>() {{
		put("(off)", 1.0d);
		put("ComplementaryReimagined_r2.3.zip", 1.3d);
	}};
	@ConfigEntry.Gui.Tooltip
	public HashMap<String, Double> shaderPackSaturationMultipliers = new HashMap<>() {{
		put("(off)", 1.0d);
		put("ComplementaryReimagined_r2.3.zip", 1.0d);
	}};
	@ConfigEntry.Gui.Tooltip
	public HashMap<String, Vector3f> shaderPackWaterReflectionColors = new HashMap<>() {{
		put("(off)", new Vector3f(-1f, -1f, -1f));
		put("ComplementaryReimagined_r2.3.zip", new Vector3f(0.82f, 0.9f, 0.96f));
	}};
	@ConfigEntry.Gui.Tooltip
	public Boolean debug = false;
}
