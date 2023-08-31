package io.github.steveplays28.blendium.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.seibel.distanthorizons.api.methods.events.DhApiEventRegister;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterDhInitEvent;
import io.github.steveplays28.blendium.client.command.ReloadCommand;
import io.github.steveplays28.blendium.client.compat.BlendiumAfterDhInitEventHandler;
import io.github.steveplays28.blendium.client.config.BlendiumConfig;
import io.github.steveplays28.blendium.client.config.BlendiumConfigOnLoadEventHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BlendiumClient implements ClientModInitializer {
	public static final String MOD_ID = "blendium";
	public static final String MOD_NAME = "Blendium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final List<LiteralArgumentBuilder<FabricClientCommandSource>> COMMANDS = List.of(ReloadCommand.register());
	public static final String U_FAR_NAME = "u_Far";
	public static final String U_VIEW_DISTANCE_FACTOR_NAME = "u_ViewDistanceFactor";
	public static final String DISTANT_HORIZONS_MOD_ID = "distanthorizons";
	public static final String IRIS_SHADERS_MOD_ID = "iris-shaders";
	public static final String DISTANT_HORIZONS_VERTEX_SHADER_NAME = "standard.vert";
	public static final String DISTANT_HORIZONS_CURVE_SHADER_NAME = "curve.vert";

	public static BlendiumConfig config;

	@Override
	public void onInitializeClient() {
		LOGGER.info("{} is loading!", MOD_NAME);

		registerConfig();
		registerCommands();
		registerDhApiUsage();
	}

	public static void reloadConfig() {
		config = AutoConfig.getConfigHolder(BlendiumConfig.class).getConfig();
	}

	public static void saveConfig() {
		AutoConfig.getConfigHolder(BlendiumConfig.class).save();
	}

	public static @NotNull String injectSodiumFragmentShaderCode(String shaderSourceCode) {
		var modifiedShaderSourceCode = insertCodeAfterCode(shaderSourceCode, "uniform", """
				uniform int u_Far; // Blendium: the view distance
				uniform float u_ViewDistanceFactor; // Blendium: the view distance blend factor
				""");
		modifiedShaderSourceCode = insertCodeInMain(modifiedShaderSourceCode, """
				// Blendium: blend the alpha of the blocks
				float far = u_Far * 16.0;
				out_FragColor.a *= 1.0 - smoothstep(u_ViewDistanceFactor * far, far, v_FragDistance);""");

		if (config.debug) {
			BlendiumClient.LOGGER.info("Original shader source code:\n{}", shaderSourceCode);
			BlendiumClient.LOGGER.info("Modified shader source code:\n{}", modifiedShaderSourceCode);
		}

		return modifiedShaderSourceCode;
	}

	public static @NotNull String injectDistantHorizonsVertexShaderCode(String shaderSourceCode) {
		var modifiedShaderSourceCode = insertCodeAfterCode(shaderSourceCode, "uniform", """
				uniform vec3 cameraPos; // Blendium: the camera's position
				uniform vec3 waterReflectionColor; // Blendium: the color of the water's reflection
				""");
		modifiedShaderSourceCode = insertCodeAfterCode(
				modifiedShaderSourceCode, "vertexColor = vec4(texture(lightMap, vec2(light, light2)).xyz, 1.0);", """
						// Blendium: modify the color of the water to mimic reflections
						vec4 modifiedColor = color;
												
						if (modifiedColor.a < 1.0 && waterReflectionColor != vec3(-1.0, -1.0, -1.0)) {
							modifiedColor = mix(color, vec4(waterReflectionColor, 1.0), clamp(vertexYPos / cameraPos.y + 10.0, 0.0, 1.0));
						}""");
		modifiedShaderSourceCode = insertCodeAfterCode(modifiedShaderSourceCode, "vertexColor *= color;", """
				// Blendium: apply the modified color value to water (and other transparent objects like glass)
				vertexColor *= modifiedColor;
				if (modifiedColor.a < 1.0) {
					vertexColor = modifiedColor;
				}""");
		modifiedShaderSourceCode = removeCode(modifiedShaderSourceCode, "vertexColor *= color;");

		if (config.debug) {
			BlendiumClient.LOGGER.info("Original shader source code:\n{}", shaderSourceCode);
			BlendiumClient.LOGGER.info("Modified shader source code:\n{}", modifiedShaderSourceCode);
		}

		return modifiedShaderSourceCode;
	}

	@SuppressWarnings("SameParameterValue")
	public static @NotNull String insertCodeAfterCode(@NotNull String shaderSourceCode, String codeToFindAndPlaceUnder, String codeToInsert) {
		if (!shaderSourceCode.contains(codeToFindAndPlaceUnder)) {
			BlendiumClient.LOGGER.error(
					"Code {} could not be inserted into the block layer fragment shader, as {} does not exist in the original shader source code:\n{}",
					codeToInsert, codeToFindAndPlaceUnder, shaderSourceCode
			);
			return shaderSourceCode;
		}

		var codeToFindAndPlaceUnderIndex = shaderSourceCode.indexOf(codeToFindAndPlaceUnder);
		var newLineIndex = shaderSourceCode.indexOf("\n", codeToFindAndPlaceUnderIndex);
		var shaderSourceCodeStringBuilder = new StringBuilder(shaderSourceCode.trim());

		// Insert the code
		shaderSourceCodeStringBuilder.insert(newLineIndex, String.format("\n%s\n", codeToInsert));

		return shaderSourceCodeStringBuilder.toString();
	}

	@SuppressWarnings("SameParameterValue")
	public static @NotNull String insertCodeInMain(@NotNull String shaderSourceCode, String codeToInsert) {
		if (!shaderSourceCode.contains("}")) {
			BlendiumClient.LOGGER.error(
					"Code {} couldn't be inserted into the block layer fragment shader, as the original shader source code does not appear to contain a main function:\n{}",
					codeToInsert, shaderSourceCode
			);
			return shaderSourceCode;
		}

		var mainFunctionClosingCurlyBracketIndex = shaderSourceCode.lastIndexOf("}");
		var shaderSourceCodeStringBuilder = new StringBuilder(shaderSourceCode.trim());

		// Insert the code before the last }
		shaderSourceCodeStringBuilder.insert(mainFunctionClosingCurlyBracketIndex, String.format("\n%s\n", codeToInsert));

		return shaderSourceCodeStringBuilder.toString();
	}

	@SuppressWarnings("SameParameterValue")
	public static @NotNull String removeCode(@NotNull String shaderSourceCode, String codeToRemove) {
		if (!shaderSourceCode.contains(codeToRemove)) {
			BlendiumClient.LOGGER.error("Code {} couldn't be removed as the shader does not seem to contain this code:\n{}", codeToRemove,
					shaderSourceCode
			);
			return shaderSourceCode;
		}

		return shaderSourceCode.replaceFirst(codeToRemove, "");
	}

	private void registerConfig() {
		AutoConfig.register(BlendiumConfig.class, JanksonConfigSerializer::new);
		AutoConfig.getConfigHolder(BlendiumConfig.class).registerLoadListener(new BlendiumConfigOnLoadEventHandler());
		reloadConfig();
	}

	private void registerCommands() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			for (var command : COMMANDS) {
				dispatcher.register(command);
			}
		});
	}

	private void registerDhApiUsage() {
		if (FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID) && FabricLoader.getInstance().isModLoaded(
				IRIS_SHADERS_MOD_ID)) {
			DhApiEventRegister.on(DhApiAfterDhInitEvent.class, new BlendiumAfterDhInitEventHandler());
		}
	}
}
