package io.github.steveplays28.blendium.mixin.iris;

import net.coderbot.iris.compat.sodium.impl.shader_overrides.IrisChunkProgramOverrides;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static io.github.steveplays28.blendium.client.BlendiumClient.injectFragmentShaderCode;

@Pseudo
@Mixin(IrisChunkProgramOverrides.class)
public class IrisChunkProgramOverridesMixin {
	@ModifyVariable(method = "createFragmentShader", at = @At(value = "STORE", ordinal = 0), remap = false)
	@SuppressWarnings("InvalidInjectorMethodSignature")
	private String modifyCreateFragmentShader(String shaderSourceCode) {
		if (shaderSourceCode == null) {
			return null;
		}

		return injectFragmentShaderCode(shaderSourceCode, true);
	}
}
