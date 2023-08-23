package io.github.steveplays28.blendium.mixin.sodium;

import io.github.steveplays28.blendium.client.BlendiumClient;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShaderLoader.class)
public class ShaderLoaderMixin {
	@Unique
	private static final String FRAGMENT_SHADER_NAME = "block_layer_opaque.fsh";

	@Inject(method = "getShaderSource", at = @At("RETURN"), cancellable = true)
	private static void getShaderSourceInject(@NotNull Identifier name, @NotNull CallbackInfoReturnable<String> cir) {
		String path = name.getPath();
		String[] splitPath = path.split("/");
		String shaderFileName = splitPath[splitPath.length - 1];

		if (!shaderFileName.equals(FRAGMENT_SHADER_NAME)) {
			return;
		}

		var originalShaderSourceCode = cir.getReturnValue();

		var newShaderSourceCode = insertCodeAfterCode(
				originalShaderSourceCode, "out_FragColor", "uniform int u_Far; // Blendium: the view distance");
		newShaderSourceCode = insertCodeInMain(originalShaderSourceCode, """
				// Blendium: blend the alpha of the blocks
				float far = u_Far * 16.0;
				out_FragColor.a *= 1.0 - smoothstep(0.4 * far, far, v_FragDistance);""");

		BlendiumClient.LOGGER.info("Original shader source code:\n{}", originalShaderSourceCode);
		BlendiumClient.LOGGER.info("Modified shader source code:\n{}", newShaderSourceCode);
		cir.setReturnValue(newShaderSourceCode);
	}

	@Unique
	@SuppressWarnings("SameParameterValue")
	private static @NotNull String insertCodeAfterCode(@NotNull String originalShaderSourceCode, String codeToFindAndPlaceUnder, String codeToInsert) {
		if (!originalShaderSourceCode.contains(codeToFindAndPlaceUnder)) {
			BlendiumClient.LOGGER.error(
					"Code {} could not be inserted into the block fragment shader, as {} does not exist in the original shader source code:\n{}",
					codeToInsert, codeToFindAndPlaceUnder, originalShaderSourceCode
			);
			return originalShaderSourceCode;
		}

		var codeToFindAndPlaceUnderIndex = originalShaderSourceCode.indexOf(codeToFindAndPlaceUnder);
		var newLineIndex = originalShaderSourceCode.indexOf("\n", codeToFindAndPlaceUnderIndex);
		// debug
		BlendiumClient.LOGGER.warn("{}, {}", codeToFindAndPlaceUnderIndex, newLineIndex);
		var originalShaderSourceCodeStringBuilder = new StringBuilder(originalShaderSourceCode);

		// Insert the code
		originalShaderSourceCodeStringBuilder.insert(newLineIndex, String.format("\n%s\n", codeToInsert));

		return originalShaderSourceCodeStringBuilder.toString();
	}

	@Unique
	@SuppressWarnings("SameParameterValue")
	private static @NotNull String insertCodeInMain(@NotNull String originalShaderSourceCode, String codeToInsert) {
		if (!originalShaderSourceCode.contains("}")) {
			BlendiumClient.LOGGER.error(
					"Code {} couldn't be inserted into the block fragment shader, as the original shader source code does not appear to contain a main function:\n{}",
					codeToInsert, originalShaderSourceCode
			);
			return originalShaderSourceCode;
		}

		var mainFunctionClosingCurlyBracketIndex = originalShaderSourceCode.lastIndexOf("}");
		var originalShaderSourceCodeStringBuilder = new StringBuilder(originalShaderSourceCode.trim());

		// Insert the code before the last }
		originalShaderSourceCodeStringBuilder.insert(mainFunctionClosingCurlyBracketIndex, String.format("\n%s\n", codeToInsert));

		return originalShaderSourceCodeStringBuilder.toString();
	}
}
