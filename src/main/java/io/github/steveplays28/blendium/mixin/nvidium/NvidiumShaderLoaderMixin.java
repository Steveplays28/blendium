package io.github.steveplays28.blendium.mixin.nvidium;

import me.cortex.nvidium.sodiumCompat.ShaderLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

@Environment(EnvType.CLIENT)
@Mixin(ShaderLoader.class)
public class NvidiumShaderLoaderMixin {
	@Unique
	private static final String FRAGMENT_SHADER_NAME = "frag.frag";
	@Unique
	private static final String MESH_VERTEX_SHADER_NAME = "mesh.glsl";

	@Inject(method = "parse", at = @At(value = "RETURN"), cancellable = true)
	private static void parseInject(@NotNull Identifier path, @NotNull CallbackInfoReturnable<String> cir) {
		String[] splitPath = path.toString().split("/");
		String shaderFileName = splitPath[splitPath.length - 1];

		switch (shaderFileName) {
			case MESH_VERTEX_SHADER_NAME -> {
				var originalShaderSourceCode = cir.getReturnValue();
				var modifiedShaderSourceCode = injectNvidiumMeshVertexShaderCode(originalShaderSourceCode);

				cir.setReturnValue(modifiedShaderSourceCode);
			}
			case FRAGMENT_SHADER_NAME -> {
				var originalShaderSourceCode = cir.getReturnValue();
				var modifiedShaderSourceCode = injectNvidiumFragmentShaderCode(originalShaderSourceCode);

				cir.setReturnValue(modifiedShaderSourceCode);
			}
		}
	}
}
