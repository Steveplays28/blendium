package io.github.steveplays28.blendium.client.compat.distanthorizons;

import com.seibel.distanthorizons.api.methods.events.DhApiEventRegister;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterDhInitEvent;
import io.github.steveplays28.blendium.client.BlendiumClient;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.steveplays28.blendium.client.BlendiumClient.DISTANT_HORIZONS_MOD_ID;
import static io.github.steveplays28.blendium.client.BlendiumClient.IRIS_SHADERS_MOD_ID;

public class BlendiumDhRegistry {
	public static void register() {
		// Check if Distant Horizons and Iris are loaded (sanity check)
		if (!FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID) || !FabricLoader.getInstance().isModLoaded(
				IRIS_SHADERS_MOD_ID)) {
			BlendiumClient.LOGGER.error(
					"BlendiumDhRegistry#register called when Distant Horizons and/or Iris isn't loaded. The game will probably crash after this point.");
			return;
		}

		DhApiEventRegister.on(DhApiAfterDhInitEvent.class, new BlendiumAfterDhInitEventHandler());
	}
}
