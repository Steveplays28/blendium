package io.github.steveplays28.blendium.mixin.sodium;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = DefaultChunkRenderer.class, remap = false)
public class DefaultChunkRendererMixin {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gl/shader/GlProgram;getInterface()Ljava/lang/Object;"))
	public void blendium$renderCreateShaderInject(ChunkRenderMatrices matrices, CommandList commandList, ChunkRenderListIterable renderLists, TerrainRenderPass renderPass, CameraTransform camera, CallbackInfo ci) {
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value,
				GlStateManager.SrcFactor.ONE.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value
		);
		RenderSystem.enableBlend();
	}
}
