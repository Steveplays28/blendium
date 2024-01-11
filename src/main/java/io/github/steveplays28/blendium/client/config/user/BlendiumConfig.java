package io.github.steveplays28.blendium.client.config.user;

import io.github.steveplays28.blendium.client.config.BlendiumBaseConfig;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class BlendiumConfig implements BlendiumBaseConfig {
	public float viewDistanceFactor = 0.4f;

	public Map<String, Double> shaderpackBrightnessMultipliers = new HashMap<>() {{
		put("(off)", 1.0d);
		put("ComplementaryReimagined_r2.3.zip", 1.3d);
	}};
	public Map<String, Double> shaderpackSaturationMultipliers = new HashMap<>() {{
		put("(off)", 1.0d);
		put("ComplementaryReimagined_r2.3.zip", 1.0d);
	}};
	public Map<String, Vector3f> shaderpackWaterReflectionColors = new HashMap<>() {{
		put("(off)", new Vector3f(-1f, -1f, -1f));
		put("ComplementaryReimagined_r2.3.zip", new Vector3f(0.82f, 0.9f, 0.96f));
	}};

	public Boolean debug = false;
}
