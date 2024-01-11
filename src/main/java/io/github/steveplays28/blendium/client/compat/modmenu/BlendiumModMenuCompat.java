package io.github.steveplays28.blendium.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.steveplays28.blendium.client.compat.yacl.BlendiumYACLConfigScreenBuilder;

public class BlendiumModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return BlendiumYACLConfigScreenBuilder::createScreen;
	}
}
