package io.github.steveplays28.blendium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class BlendiumClient implements ClientModInitializer {
	public static final String MOD_ID = "blendium";
	public static final String MOD_NAME = "Blendium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("{} is loading!", MOD_NAME);
	}
}
