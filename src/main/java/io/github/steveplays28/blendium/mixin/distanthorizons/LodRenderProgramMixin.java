package io.github.steveplays28.blendium.mixin.distanthorizons;

import com.seibel.distanthorizons.core.render.fog.LodFogConfig;
import com.seibel.distanthorizons.core.render.glObject.shader.ShaderProgram;
import com.seibel.distanthorizons.core.render.renderer.LodRenderProgram;
import com.seibel.distanthorizons.coreapi.util.math.Mat4f;
import com.seibel.distanthorizons.coreapi.util.math.Vec3f;
import io.github.steveplays28.blendium.client.compat.iris.BlendiumDhShaderpackPresets;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.blendium.client.BlendiumClient.IRIS_SHADERS_MOD_ID;
import static io.github.steveplays28.blendium.client.BlendiumClient.config;

@Mixin(LodRenderProgram.class)
public class LodRenderProgramMixin extends ShaderProgram {
	@Unique
	public int cameraPosUniform;
	@Unique
	public int waterReflectionColorUniform;

	public LodRenderProgramMixin(String vert, String frag, String fragDataOutputName, String[] attributes) {
		super(vert, frag, fragDataOutputName, attributes);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"), remap = false)
	public void constructorInject(LodFogConfig fogConfig, CallbackInfo ci) {
		cameraPosUniform = getUniformLocation("cameraPos");
		waterReflectionColorUniform = getUniformLocation("waterReflectionColor");
	}

	@Inject(method = "fillUniformData", at = @At(value = "TAIL"), remap = false)
	public void fillUniformDataInject(Mat4f combinedMatrix, int lightmapBindPoint, int worldYOffset, float partialTicks, CallbackInfo ci) {
		var player = MinecraftClient.getInstance().player;
		if (player == null) {
			var invalidVector = new Vec3f(-1f, -1f, -1f);

			setUniform(cameraPosUniform, invalidVector);
			setUniform(waterReflectionColorUniform, invalidVector);
			return;
		}

		var playerBlockPos = MinecraftClient.getInstance().player.getBlockPos();
		var shaderPackWaterReflectionColor = new Vector3f(-1f, -1f, -1f);

		if (FabricLoader.getInstance().isModLoaded(IRIS_SHADERS_MOD_ID) && config.shaderpackWaterReflectionColors.containsKey(
				BlendiumDhShaderpackPresets.getShaderpackName())) {
			shaderPackWaterReflectionColor = config.shaderpackWaterReflectionColors.get(BlendiumDhShaderpackPresets.getShaderpackName());
		}

		// DEBUG
//		LOGGER.info("blendium brightnessMultiplier: {}", config.shaderPackBrightnessMultipliers.get(Iris.getCurrentPackName()));
//		LOGGER.info("DH brightnessMultiplier: {}", DhApi.Delayed.configs.graphics().brightnessMultiplier().getValue());

		setUniform(cameraPosUniform, new Vec3f(playerBlockPos.getX(), playerBlockPos.getY(), playerBlockPos.getZ()));
		setUniform(
				waterReflectionColorUniform,
				new Vec3f(shaderPackWaterReflectionColor.x(), shaderPackWaterReflectionColor.y(), shaderPackWaterReflectionColor.z())
		);
	}
}
