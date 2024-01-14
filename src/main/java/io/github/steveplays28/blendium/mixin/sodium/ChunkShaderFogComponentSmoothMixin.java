package io.github.steveplays28.blendium.mixin.sodium;

import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformFloat;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformInt;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderFogComponent;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

@Environment(EnvType.CLIENT)
@Mixin(value = ChunkShaderFogComponent.Smooth.class, remap = false)
public class ChunkShaderFogComponentSmoothMixin {
	@Unique
	private GlUniformInt uFar;
	@Unique
	private GlUniformFloat uViewDistanceFactor;

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	public void constructorInject(@NotNull ShaderBindingContext context, CallbackInfo ci) {
		this.uFar = context.bindUniform(U_FAR_NAME, GlUniformInt::new);
		this.uViewDistanceFactor = context.bindUniform(U_VIEW_DISTANCE_FACTOR_NAME, GlUniformFloat::new);
	}

	@Inject(method = "setup", at = @At(value = "TAIL"))
	public void setupInject(CallbackInfo ci) {
		this.uFar.set(MinecraftClient.getInstance().options.getClampedViewDistance());
		this.uViewDistanceFactor.set(config.viewDistanceFactor);
	}
}
