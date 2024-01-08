package io.github.steveplays28.blendium.mixin.sodium;

import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.steveplays28.blendium.client.BlendiumClient.injectSodiumFragmentShaderCode;

@Environment(EnvType.CLIENT)
@Mixin(ShaderLoader.class)
public class SodiumShaderLoaderMixin {
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
		var modifiedShaderSourceCode = injectSodiumFragmentShaderCode(originalShaderSourceCode);

		cir.setReturnValue(modifiedShaderSourceCode);
	}
}
