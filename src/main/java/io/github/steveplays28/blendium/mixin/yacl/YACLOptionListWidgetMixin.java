package io.github.steveplays28.blendium.mixin.yacl;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.*;
import dev.isxander.yacl3.impl.utils.YACLConstants;
import io.github.steveplays28.blendium.client.compat.yacl.extension.YACLOptionListWidgetExtension;
import io.github.steveplays28.blendium.client.compat.yacl.option.MapOption;
import io.github.steveplays28.blendium.client.compat.yacl.option.MapOptionEntry;
import io.github.steveplays28.blendium.mixin.yacl.accessor.OptionListWidgetGroupSeparatorEntryAccessor;
import io.github.steveplays28.blendium.mixin.yacl.accessor.YACLOptionListWidgetAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(value = OptionListWidget.class, remap = false)
public abstract class YACLOptionListWidgetMixin extends ElementListWidgetExt<OptionListWidget.Entry> {
	public YACLOptionListWidgetMixin(MinecraftClient client, int x, int y, int width, int height, boolean smoothScrolling) {
		super(client, x, y, width, height, smoothScrolling);
	}

	@Shadow
	public abstract void addEntryBelow(OptionListWidget.Entry below, OptionListWidget.Entry entry);

	@Shadow
	@Final
	private YACLScreen yaclScreen;

	@Shadow
	public abstract Dimension<Integer> getDefaultEntryDimension();

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void blendium$constructorInject(YACLScreen screen, ConfigCategory category, MinecraftClient client, int x, int y, int width, int height, Consumer<DescriptionWithName> hoverEvent, CallbackInfo ci) {
		for (OptionGroup group : category.groups()) {
			if (group instanceof MapOption<?, ?> mapOption) {
				mapOption.addRefreshListener(() -> refreshMapEntries(mapOption, category));
			}
		}
	}

	@WrapWithCondition(method = "refreshOptions", at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/gui/OptionListWidget;addEntry(Lnet/minecraft/client/gui/widget/EntryListWidget$Entry;)I"))
	private boolean blendium$refreshOptionsAddMapSupport(OptionListWidget instance, EntryListWidget.Entry<?> entry, @Local OptionGroup group, @Local LocalRef<OptionListWidget.GroupSeparatorEntry> groupSeparatorEntry) {
		if (group instanceof MapOption<?, ?> mapOption) {
			try {
				var listGroupSeparatorEntryConstructor = OptionListWidget.ListGroupSeparatorEntry.class.getDeclaredConstructors()[0];
				listGroupSeparatorEntryConstructor.setAccessible(true);
				var listGroupSeparatorEntry = (OptionListWidget.Entry) listGroupSeparatorEntryConstructor.newInstance(null, yaclScreen);
				((YACLOptionListWidgetExtension) listGroupSeparatorEntry).blendium$setMapOption(mapOption);

				addEntry(listGroupSeparatorEntry);
			} catch (Exception ex) {
				// TODO: Add exception logging
				return false;
			}
		}

		return true;
	}

	@Unique
	private void refreshMapEntries(MapOption<?, ?> mapOption, ConfigCategory category) {
		// Find group separator for group
		OptionListWidget.ListGroupSeparatorEntry groupSeparator = super.children().stream().filter(
				entry -> entry instanceof OptionListWidget.ListGroupSeparatorEntry listGroupSeparatorEntry && ((OptionListWidgetGroupSeparatorEntryAccessor) listGroupSeparatorEntry).getGroup() == mapOption).map(
				OptionListWidget.ListGroupSeparatorEntry.class::cast).findAny().orElse(null);

		if (groupSeparator == null) {
			YACLConstants.LOGGER.warn("Can't find group separator to refresh map option entries for map option " + mapOption.name());
			return;
		}

		var groupSeparatorChildEntries = ((OptionListWidgetGroupSeparatorEntryAccessor) groupSeparator).getChildEntries();
		for (OptionListWidget.Entry entry : groupSeparatorChildEntries)
			super.removeEntry(entry);
		groupSeparatorChildEntries.clear();

		var parentInstance = (OptionListWidget) (Object) this;

		// If no entries, below loop won't run where addEntryBelow() reaches viewable children
		if (mapOption.options().isEmpty()) {
			OptionListWidget.EmptyListLabel emptyListLabel = parentInstance.new EmptyListLabel(groupSeparator, category);

			addEntryBelow(groupSeparator, emptyListLabel);
			groupSeparatorChildEntries.add(emptyListLabel);

			return;
		}

		OptionListWidget.Entry lastEntry = groupSeparator;
		for (MapOptionEntry<?, ?> mapOptionEntry : mapOption.options()) {
			OptionListWidget.OptionEntry optionEntry = parentInstance.new OptionEntry(mapOptionEntry, category, mapOption, groupSeparator,
					mapOptionEntry.controller().provideWidget(yaclScreen, getDefaultEntryDimension())
			);

			addEntryBelow(lastEntry, optionEntry);
			groupSeparatorChildEntries.add(optionEntry);
			lastEntry = optionEntry;
		}
	}

	@Mixin(value = OptionListWidget.ListGroupSeparatorEntry.class, remap = false)
	public abstract static class YACLOptionListWidgetListGroupSeparatorEntryMixin extends OptionListWidget.GroupSeparatorEntry implements YACLOptionListWidgetExtension {
		@Mutable
		@Shadow
		@Final
		private TextScaledButtonWidget resetListButton;

		@Shadow
		public abstract void setExpanded(boolean expanded);

		@Shadow
		protected abstract void updateExpandMinimizeText();

		@Shadow
		protected abstract void minimizeIfUnavailable();

		@Mutable
		@Shadow
		@Final
		private TooltipButtonWidget addListButton;

		@Unique
		private MapOption<?, ?> blendium$mapOption;

		private YACLOptionListWidgetListGroupSeparatorEntryMixin(ListOption<?> group, Screen screen, @NotNull OptionListWidget outer) {
			outer.super(group, screen);
		}

		@SuppressWarnings("UnnecessaryUnicodeEscape")
		@Inject(method = "<init>", at = @At(value = "RETURN"))
		private void blendium$constructorInject(OptionListWidget this$0, ListOption<?> group, Screen screen, CallbackInfo ci) {
			this.resetListButton = new TextScaledButtonWidget(screen, this$0.getRowRight(), -20, -50, 20, 2f, Text.literal("\u21BB"),
					button -> blendium$mapOption.requestSetDefault()
			);
			blendium$mapOption.addListener((opt, val) -> this.resetListButton.active = !opt.isPendingValueDefault() && opt.available());
			this.resetListButton.active = !blendium$mapOption.isPendingValueDefault() && blendium$mapOption.available();

			this.addListButton = new TooltipButtonWidget(((YACLOptionListWidgetAccessor) this$0).getYaclScreen(), resetListButton.getX() - 20,
					-50, 20, 20, Text.literal("+"), Text.translatable("yacl.list.add_top"), btn -> {
				blendium$mapOption.insertNewEntry();
				setExpanded(true);
			}
			);

			updateExpandMinimizeText();
			minimizeIfUnavailable();
		}

		@Inject(method = "updateExpandMinimizeText", at = @At(value = "TAIL"))
		private void blendium$updateExpandMinimizeTextInject(CallbackInfo ci) {
			expandMinimizeButton.active = blendium$mapOption == null || blendium$mapOption.available();
			if (addListButton != null)
				addListButton.active = expandMinimizeButton.active && blendium$mapOption.numberOfEntries() < blendium$mapOption.maximumNumberOfEntries();
		}

		@Override
		public void blendium$setMapOption(MapOption<?, ?> mapOption) {
			blendium$mapOption = mapOption;
		}
	}
}
