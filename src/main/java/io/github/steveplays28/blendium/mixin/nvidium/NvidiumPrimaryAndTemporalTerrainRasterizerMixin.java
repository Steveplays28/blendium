package io.github.steveplays28.blendium.mixin.nvidium;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.cortex.nvidium.renderers.PrimaryTerrainRasterizer;
import me.cortex.nvidium.renderers.TemporalTerrainRasterizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = {PrimaryTerrainRasterizer.class, TemporalTerrainRasterizer.class}, remap = false)
public class NvidiumPrimaryAndTemporalTerrainRasterizerMixin {
	@Inject(method = "raster", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/NVMeshShader;glMultiDrawMeshTasksIndirectNV(JII)V"))
	private void blendium$rasterBlendFuncInject(int regionCount, long commandAddr, CallbackInfo ci) {
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value,
				GlStateManager.SrcFactor.ONE.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value
		);
		RenderSystem.enableBlend();
	}
}
