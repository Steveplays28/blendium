package io.github.steveplays28.blendium.mixin.distanthorizons;

import com.seibel.distanthorizons.core.render.fog.LodFogConfig;
import com.seibel.distanthorizons.core.render.glObject.shader.ShaderProgram;
import com.seibel.distanthorizons.core.render.renderer.LodRenderProgram;
import com.seibel.distanthorizons.coreapi.util.math.Mat4f;
import com.seibel.distanthorizons.coreapi.util.math.Vec3f;
import net.coderbot.iris.Iris;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.blendium.client.BlendiumClient.LOGGER;
import static io.github.steveplays28.blendium.client.BlendiumClient.config;

@Pseudo
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
	public void fillUniformDataInject(Mat4f combinedMatrix, int lightmapBindPoint, int worldYOffset, int vanillaDrawDistance, CallbackInfo ci) {
		var player = MinecraftClient.getInstance().player;
		if (player == null) return;
		var playerBlockPos = MinecraftClient.getInstance().player.getBlockPos();
		var shaderPackWaterReflectionColor = config.shaderPackWaterReflectionColors.get(Iris.getCurrentPackName());

		setUniform(cameraPosUniform, new Vec3f(playerBlockPos.getX(), playerBlockPos.getY(), playerBlockPos.getZ()));
		setUniform(waterReflectionColorUniform,
				new Vec3f((float) shaderPackWaterReflectionColor.getX(), (float) shaderPackWaterReflectionColor.getY(),
						(float) shaderPackWaterReflectionColor.getZ()
				)
		);
	}
}
