package io.github.steveplays28.blendium.client.compat;

import net.fabricmc.loader.api.FabricLoader;

public class BlendiumCompat {
	public static boolean isYACLLoaded = FabricLoader.getInstance().isModLoaded("yet-another-config-lib") || FabricLoader.getInstance().isModLoaded(
			"yet_another_config_lib_v3");
}
