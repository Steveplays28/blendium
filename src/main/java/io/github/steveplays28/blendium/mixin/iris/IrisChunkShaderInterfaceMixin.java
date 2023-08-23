package io.github.steveplays28.blendium.mixin.iris;

import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformInt;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.coderbot.iris.compat.sodium.impl.shader_overrides.IrisChunkShaderInterface;
import net.coderbot.iris.compat.sodium.impl.shader_overrides.ShaderBindingContextExt;
import net.coderbot.iris.gl.blending.BlendModeOverride;
import net.coderbot.iris.gl.blending.BufferBlendOverride;
import net.coderbot.iris.pipeline.SodiumTerrainPipeline;
import net.coderbot.iris.uniforms.custom.CustomUniforms;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.steveplays28.blendium.client.BlendiumClient.U_FAR_NAME;

@Pseudo
@Mixin(value = IrisChunkShaderInterface.class, remap = false)
public class IrisChunkShaderInterfaceMixin {
	@Unique
	private GlUniformInt uFar;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void constructorInject(int par1, ShaderBindingContextExt context, SodiumTerrainPipeline par3, ChunkShaderOptions par4, boolean par5, BlendModeOverride par6, List<BufferBlendOverride> par7, float par8, CustomUniforms par9, CallbackInfo ci) {
		this.uFar = context.bindUniformIfPresent(U_FAR_NAME, GlUniformInt::new);
	}

	@Inject(method = "setupState", at = @At(value = "TAIL"), remap = false)
	public void setupStateInject(CallbackInfo ci) {
		this.uFar.set(MinecraftClient.getInstance().options.getClampedViewDistance());
	}
}
