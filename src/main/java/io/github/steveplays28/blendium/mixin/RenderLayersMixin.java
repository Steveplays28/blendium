package io.github.steveplays28.blendium.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.render.RenderLayer.getTranslucent;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {
	@Inject(method = "getBlockLayer", at = @At(value = "HEAD"), cancellable = true)
	private static void getBlockLayerInject(BlockState state, CallbackInfoReturnable<RenderLayer> cir) {
		var block = state.getBlock();

		if (block instanceof LeavesBlock || block instanceof FernBlock) {
			cir.setReturnValue(getTranslucent());
		}
	}
}
