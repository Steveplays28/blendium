package io.github.steveplays28.blendium.mixin.distanthorizons;

import com.seibel.distanthorizons.core.render.glObject.shader.Shader;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

@Mixin(Shader.class)
public class ShaderMixin {
	@Inject(method = "loadFile", at = @At(value = "RETURN"), remap = false, cancellable = true)
	private static void loadFileInject(@NotNull String path, boolean absoluteFilePath, StringBuilder stringBuilder, CallbackInfoReturnable<StringBuilder> cir) {
		if (path.contains(DISTANT_HORIZONS_VERTEX_SHADER_NAME) || path.contains(DISTANT_HORIZONS_CURVE_SHADER_NAME)) {
			var originalShaderCode = cir.getReturnValue();
			cir.setReturnValue(new StringBuilder(injectDistantHorizonsVertexShaderCode(originalShaderCode.toString())));
		}
	}
}
