package io.github.steveplays28.blendium.mixin.yacl.accessor;

import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(OptionListWidget.class)
public interface YACLOptionListWidgetAccessor {
	@Accessor
	YACLScreen getYaclScreen();
}
