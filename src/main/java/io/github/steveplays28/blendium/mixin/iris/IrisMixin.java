package io.github.steveplays28.blendium.mixin.iris;

import net.coderbot.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.blendium.client.BlendiumClient.reloadConfig;

@Pseudo
@Mixin(Iris.class)
public class IrisMixin {
	@Inject(method = "loadShaderpack", at = @At(value = "TAIL"), remap = false)
	private static void loadShaderpackInject(CallbackInfo ci) {
		reloadConfig();
	}
}
