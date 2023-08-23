package io.github.steveplays28.blendium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class BlendiumClient implements ClientModInitializer {
	public static final String MOD_ID = "blendium";
	public static final String MOD_NAME = "Blendium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String U_FAR_NAME = "u_Far";

	@Override
	public void onInitializeClient() {
		LOGGER.info("{} is loading!", MOD_NAME);
	}

	public static String injectFragmentShaderCode(String shaderSourceCode, boolean irisShaders) {
		var modifiedShaderSourceCode = insertCodeAfterCode(
				shaderSourceCode, "uniform", "uniform int u_Far; // Blendium: the view distance");

		modifiedShaderSourceCode = insertCodeInMain(modifiedShaderSourceCode, """
				// Blendium: blend the alpha of the blocks
				float far = u_Far * 16.0;""");

		if (irisShaders) {
			modifiedShaderSourceCode = insertCodeInMain(modifiedShaderSourceCode, """
					float fragDistance = length(gl_FragCoord);
					iris_FragData0.a *= smoothstep(0.4 * far, far, fragDistance);""");
		} else {
			modifiedShaderSourceCode = insertCodeInMain(modifiedShaderSourceCode, """
					out_FragColor.a *= 1.0 - smoothstep(0.4 * far, far, v_FragDistance);""");
		}

		BlendiumClient.LOGGER.info("Original shader source code:\n{}", shaderSourceCode);
		BlendiumClient.LOGGER.info("Modified shader source code:\n{}", modifiedShaderSourceCode);
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
}
