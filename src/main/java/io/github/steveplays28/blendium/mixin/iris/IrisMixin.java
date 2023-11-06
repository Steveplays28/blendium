package io.github.steveplays28.blendium.mixin.iris;

import net.coderbot.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.blendium.client.compat.iris.BlendiumDHShaderpackPresets.applyDHShaderpackPreset;

@Mixin(Iris.class)
public class IrisMixin {
	@Inject(method = "loadShaderpack", at = @At(value = "TAIL"), remap = false)
	private static void loadShaderpackInject(CallbackInfo ci) {
		applyDHShaderpackPreset(Iris.getCurrentPackName());
	}

	@Inject(method = "setShadersDisabled", at = @At(value = "TAIL"), remap = false)
	private static void setShadersDisabled(CallbackInfo ci) {
		applyDHShaderpackPreset(Iris.getCurrentPackName());
	}
}
