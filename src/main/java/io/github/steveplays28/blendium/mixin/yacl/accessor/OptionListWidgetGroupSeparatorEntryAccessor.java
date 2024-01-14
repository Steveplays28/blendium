package io.github.steveplays28.blendium.mixin.yacl.accessor;

import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.gui.OptionListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = OptionListWidget.GroupSeparatorEntry.class, remap = false)
public interface OptionListWidgetGroupSeparatorEntryAccessor {
	@Accessor
	OptionGroup getGroup();

	@Accessor
	List<OptionListWidget.Entry> getChildEntries();
}
