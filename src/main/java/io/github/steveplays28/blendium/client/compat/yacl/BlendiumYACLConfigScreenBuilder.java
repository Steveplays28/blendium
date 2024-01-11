package io.github.steveplays28.blendium.client.compat.yacl;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.DoubleFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import io.github.steveplays28.blendium.client.BlendiumClient;
import io.github.steveplays28.blendium.client.compat.yacl.option.MapOption;
import io.github.steveplays28.blendium.client.config.BlendiumConfigLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.AbstractMap;
import java.util.Collections;

/**
 * Blendium config screen builder, uses YACL to generate the config screen.
 * <p>
 * Note: Always check if YACL is loaded using <code>ForgeroCompatInitializer.yacl.get()</code> before invoking any methods inside this class.
 * </p>
 */
public class BlendiumYACLConfigScreenBuilder {
	public static Screen createScreen(Screen parentScreen) {
		return createBuilder().generateScreen(parentScreen);
	}

	private static YetAnotherConfigLib createBuilder() {
		return YetAnotherConfigLib.createBuilder().title(Text.translatable("blendium.config.title")).save(
				BlendiumConfigLoader::save).category(MainCategory.buildMainCategory()).build();
	}

	private static class MainCategory {
		private static ConfigCategory buildMainCategory() {
			return ConfigCategory.createBuilder().name(Text.translatable("blendium.config.category.main.title")).tooltip(
					Text.translatable("blendium.config.category.main.title.tooltip")).group(MapOption.<String, Double>createBuilder().name(
					Text.translatable("blendium.config.shaderpackBrightnessMultipliers")).description(
					OptionDescription.of(Text.translatable("blendium.config.shaderpackBrightnessMultipliers.tooltip"))).binding(
					BlendiumClient.config.shaderpackBrightnessMultipliers, () -> BlendiumClient.config.shaderpackBrightnessMultipliers,
					newValue -> BlendiumClient.config.shaderpackBrightnessMultipliers = newValue
			).initial(() -> new AbstractMap.SimpleEntry<>("(off)", 1.0)).keyController(stringDoubleMapOptionEntry -> StringControllerBuilder.create(
					(Option<String>) (Object) stringDoubleMapOptionEntry)).valueController(DoubleFieldControllerBuilder::create).build()).build();
		}

//		private static OptionGroup buildRepairingGroup() {
//			return OptionGroup.createBuilder().name(Text.translatable("blendium.config.group.repairing.title")).description(
//					OptionDescription.of(Text.translatable("blendium.config.group.repairing.title.tooltip"))).option(
//					Option.<Boolean>createBuilder().name(Text.translatable("blendium.config.enableUnbreakableTools")).description(
//							OptionDescription.of(Text.translatable("blendium.config.enableUnbreakableTools.tooltip"))).binding(
//							false,
//							() -> ForgeroConfigurationLoader.configuration.enableUnbreakableTools,
//							newValue -> ForgeroConfigurationLoader.configuration.enableUnbreakableTools = newValue
//					).controller(BooleanControllerBuilder::create).build()).option(
//					Option.<Boolean>createBuilder().name(Text.translatable("blendium.config.enableRepairKits")).description(
//							OptionDescription.of(Text.translatable("blendium.config.enableRepairKits.tooltip"))).binding(
//							true,
//							() -> ForgeroConfigurationLoader.configuration.enableRepairKits,
//							newValue -> ForgeroConfigurationLoader.configuration.enableRepairKits = newValue
//					).controller(BooleanControllerBuilder::create).build()).build();
//		}
	}
}
