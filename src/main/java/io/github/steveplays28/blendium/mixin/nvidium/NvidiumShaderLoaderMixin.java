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
	private static final String SCENE_DATA_SHADER_PATH = "occlusion/scene.glsl";
	@Unique
	private static final String MESH_VERTEX_SHADER_PATH = "terrain/mesh.glsl";
	@Unique
	private static final String TRANSLUCENT_MESH_VERTEX_SHADER_PATH = "terrain/translucent/mesh.glsl";
	@Unique
	private static final String FRAGMENT_SHADER_PATH = "terrain/frag.frag";
	@Unique
	private static final String TRANSLUCENT_FRAGMENT_SHADER_PATH = "terrain/translucent/frag.frag";

	@Inject(method = "parse", at = @At(value = "RETURN"), cancellable = true)
	private static void parseInject(@NotNull Identifier path, @NotNull CallbackInfoReturnable<String> cir) {
		switch (path.getPath()) {
			case MESH_VERTEX_SHADER_PATH, TRANSLUCENT_MESH_VERTEX_SHADER_PATH -> {
				var originalShaderSourceCode = cir.getReturnValue();
				var modifiedShaderSourceCode = injectNvidiumMeshVertexShaderCode(originalShaderSourceCode);

				cir.setReturnValue(modifiedShaderSourceCode);
			}
			case FRAGMENT_SHADER_PATH, TRANSLUCENT_FRAGMENT_SHADER_PATH -> {
				var originalShaderSourceCode = cir.getReturnValue();
				var modifiedShaderSourceCode = injectNvidiumFragmentShaderCode(originalShaderSourceCode);

				cir.setReturnValue(modifiedShaderSourceCode);
			}
		}
	}
}
