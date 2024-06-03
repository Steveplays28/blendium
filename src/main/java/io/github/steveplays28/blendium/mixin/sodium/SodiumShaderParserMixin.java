package io.github.steveplays28.blendium.mixin.sodium;

import me.jellysquid.mods.sodium.client.gl.shader.ShaderParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.regex.Matcher;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

@Environment(EnvType.CLIENT)
@Mixin(value = ShaderParser.class, remap = false)
public class SodiumShaderParserMixin {
	@Inject(method = "resolveImport", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gl/shader/ShaderParser;parseShader(Ljava/lang/String;)Ljava/util/List;", shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void blendium$resolveImportInject(String line, CallbackInfoReturnable<List<String>> cir, Matcher matcher, String namespace, String path, Identifier identifier, String originalShaderSourceCode) {
		// Check if the shader file is the import for Nvidium's scene data shader
		if (!line.equals(String.format("#import <%s:%s>", NVIDIUM_MOD_ID, "occlusion/scene.glsl"))) {
			return;
		}

		var modifiedShaderSourceCode = injectNvidiumSceneDataShaderCode(originalShaderSourceCode);
		cir.setReturnValue(ShaderParser.parseShader(modifiedShaderSourceCode));
	}
}
