package io.github.steveplays28.blendium.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.blendium.client.BlendiumClient.MOD_ID;
import static io.github.steveplays28.blendium.client.BlendiumClient.reloadConfig;

public class ReloadCommand {
	public static final String NAME = "reload";
	public static final int PERMISSION_LEVEL = 4;
	public static final String SUCCESS_TRANSLATION_KEY = "blendium.config_reload_success";

	public static LiteralArgumentBuilder<FabricClientCommandSource> register() {
		return ClientCommandManager.literal(MOD_ID).then(
				ClientCommandManager.literal(NAME).executes(ctx -> execute(ctx.getSource())).requires(
						(ctx) -> Permissions.check(ctx, String.format("%s.commands.%s", MOD_ID, NAME), PERMISSION_LEVEL)));
	}

	public static int execute(@NotNull FabricClientCommandSource source) {
		reloadConfig();
		source.sendFeedback(Text.translatable(SUCCESS_TRANSLATION_KEY));

		return 0;
	}
}
