package io.github.steveplays28.blendium.mixin.yacl;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.gui.controllers.string.StringController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = StringController.class, remap = false)
public abstract class YACLStringControllerMixin {
	@Shadow
	public abstract Option<String> option();

//	@Inject(method = "getString", at = @At(value = "HEAD"), cancellable = true)
//	private void blendium$getStringInject(@NotNull CallbackInfoReturnable<String> cir) {
//		var optionValue = option().pendingValue();
//
////		if ((Object) optionValue instanceof Map.Entry<?, ?>) {
//		var e = option().pendingValue();
//			cir.setReturnValue(((Map.Entry<?, ?>) (Object) optionValue).getKey().toString());
////		}
//	}

	@Overwrite
	public String getString() {
		return ((Map.Entry<?, ?>) (Object) option().pendingValue()).getKey().toString();
	}
}
