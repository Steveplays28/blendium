package io.github.steveplays28.blendium.mixin;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static io.github.steveplays28.blendium.client.BlendiumClient.*;

public class BlendiumMixinPlugin implements IMixinConfigPlugin {
	private static final Supplier<Boolean> TRUE = () -> true;

	private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
			"io.github.steveplays28.blendium.mixin.distanthorizons.LodRenderProgramMixin",
			() -> FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID),
			"io.github.steveplays28.blendium.mixin.distanthorizons.ShaderMixin",
			() -> FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID),
			"io.github.steveplays28.blendium.mixin.sodium.ChunkShaderFogComponentSmoothMixin",
			() -> FabricLoader.getInstance().isModLoaded(SODIUM_MOD_ID),
			"io.github.steveplays28.blendium.mixin.sodium.DefaultChunkRendererMixin",
			() -> FabricLoader.getInstance().isModLoaded(SODIUM_MOD_ID),
			"io.github.steveplays28.blendium.mixin.sodium.ShaderLoaderMixin",
			() -> FabricLoader.getInstance().isModLoaded(SODIUM_MOD_ID),
			"io.github.steveplays28.blendium.mixin.iris.IrisMixin",
			() -> FabricLoader.getInstance().isModLoaded(IRIS_SHADERS_MOD_ID)
	);

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
	}

	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
