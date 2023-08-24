package io.github.steveplays28.blendium.client.config;

import io.github.steveplays28.blendium.client.BlendiumClient;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = BlendiumClient.MOD_ID)
public class BlendiumConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public float viewDistanceFactor = 0.4f;
	@ConfigEntry.Gui.Tooltip
	public Boolean debug = false;
}
