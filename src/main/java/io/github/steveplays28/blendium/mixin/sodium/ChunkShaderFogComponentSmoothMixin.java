package io.github.steveplays28.blendium.mixin.sodium;

import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformInt;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderFogComponent;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkShaderFogComponent.Smooth.class)
public class ChunkShaderFogComponentSmoothMixin {
	@Unique
	public final String uFarName = "u_Far";
	@Unique
	private GlUniformInt uFar;

	@Inject(method = "<init>", at = @At(value = "TAIL"), remap = false)
	public void constructorInject(@NotNull ShaderBindingContext context, CallbackInfo ci) {
		this.uFar = context.bindUniform(uFarName, GlUniformInt::new);
	}

	@Inject(method = "setup", at = @At(value = "TAIL"), remap = false)
	public void setupInject(CallbackInfo ci) {
		this.uFar.set(MinecraftClient.getInstance().options.getClampedViewDistance());
	}
}
