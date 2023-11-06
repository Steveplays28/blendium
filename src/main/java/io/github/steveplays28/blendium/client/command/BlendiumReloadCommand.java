package io.github.steveplays28.blendium.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.blendium.client.BlendiumClient.MOD_ID;
import static io.github.steveplays28.blendium.client.BlendiumClient.reloadConfig;

/**
 * A clientside reload command for Blendium's config.
 */
public class BlendiumReloadCommand {
	public static final String NAME = "reload";
	public static final String SUCCESS_TRANSLATION_KEY = "blendium.config_reload_success";

	public static LiteralArgumentBuilder<FabricClientCommandSource> register() {
		return ClientCommandManager.literal(MOD_ID).then(ClientCommandManager.literal(NAME).executes(ctx -> execute(ctx.getSource())));
	}

	public static int execute(@NotNull FabricClientCommandSource source) {
		reloadConfig();
		source.sendFeedback(Text.translatable(SUCCESS_TRANSLATION_KEY));

		return 0;
	}
}
