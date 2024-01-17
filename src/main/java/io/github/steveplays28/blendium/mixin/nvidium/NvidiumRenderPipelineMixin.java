package io.github.steveplays28.blendium.mixin.nvidium;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;
import me.cortex.nvidium.RenderPipeline;
import me.cortex.nvidium.gl.RenderDevice;
import me.cortex.nvidium.managers.SectionManager;
import me.cortex.nvidium.util.DownloadTaskStream;
import me.cortex.nvidium.util.UploadingBufferStream;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.blendium.client.BlendiumClient.config;

@Environment(EnvType.CLIENT)
@Mixin(value = RenderPipeline.class, remap = false)
public class NvidiumRenderPipelineMixin {
	@Mutable
	@Shadow
	@Final
	private static int SCENE_SIZE;

	@Unique
	private static final int INT_UNIFORM_SIZE_BYTES = 4;
	@Unique
	private static final int FLOAT_UNIFORM_SIZE_BYTES = 4;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void blendium$constructorInject(RenderDevice device, UploadingBufferStream uploadStream, DownloadTaskStream downloadStream, SectionManager sectionManager, CallbackInfo ci) {
		SCENE_SIZE += INT_UNIFORM_SIZE_BYTES * FLOAT_UNIFORM_SIZE_BYTES;
	}

	@Inject(method = "renderFrame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/MemoryUtil;memPutByte(JB)V", ordinal = 0, shift = At.Shift.BEFORE))
	private void blendium$renderFrameSetUniformsInject(Viewport frustum, ChunkRenderMatrices crm, double px, double py, double pz, CallbackInfo ci, @Local(ordinal = 1) LocalLongRef addr) {
		// u_Far
		MemoryUtil.memPutInt(addr.get(), MinecraftClient.getInstance().options.getClampedViewDistance());
		addr.set(addr.get() + INT_UNIFORM_SIZE_BYTES);

		// u_ViewDistanceFactor
		MemoryUtil.memPutFloat(addr.get(), config.viewDistanceFactor);
		addr.set(addr.get() + FLOAT_UNIFORM_SIZE_BYTES);
	}
}
