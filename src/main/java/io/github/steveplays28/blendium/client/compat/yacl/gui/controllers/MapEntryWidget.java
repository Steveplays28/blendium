package io.github.steveplays28.blendium.client.compat.yacl.gui.controllers;

import com.google.common.collect.ImmutableList;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.TooltipButtonWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import io.github.steveplays28.blendium.client.compat.yacl.option.MapOption;
import io.github.steveplays28.blendium.client.compat.yacl.option.MapOptionEntry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MapEntryWidget extends AbstractWidget implements ParentElement {
	private final TooltipButtonWidget removeButton, moveUpButton, moveDownButton;
	private final AbstractWidget entryWidget;

	private final MapOption<?, ?> mapOption;
	private final MapOptionEntry<?, ?> mapOptionEntry;

	private final String optionNameString;

	private Element focused;
	private boolean dragging;

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	public MapEntryWidget(YACLScreen screen, MapOptionEntry<?, ?> mapOptionEntry, AbstractWidget entryWidget) {
		super(entryWidget.getDimension().withHeight(
				Math.max(entryWidget.getDimension().height(), 20) - ((mapOptionEntry.parentGroup().indexOf(
						mapOptionEntry) == mapOptionEntry.parentGroup().options().size() - 1) ? 0 : 2))); // -2 to remove the padding
		this.mapOptionEntry = mapOptionEntry;
		this.mapOption = mapOptionEntry.parentGroup();
		this.optionNameString = mapOptionEntry.name().getString().toLowerCase();
		this.entryWidget = entryWidget;

		Dimension<Integer> dim = entryWidget.getDimension();
		entryWidget.setDimension(dim.clone().move(20 * 2, 0).expand(-20 * 3, 0));

		removeButton = new TooltipButtonWidget(
				screen, dim.xLimit() - 20, dim.y(), 20, 20, Text.literal("\u274c"), Text.translatable("yacl.list.remove"), btn -> {
			mapOption.removeEntry(mapOptionEntry);
			updateButtonStates();
		});

		moveUpButton = new TooltipButtonWidget(
				screen, dim.x(), dim.y(), 20, 20, Text.literal("\u2191"), Text.translatable("yacl.list.move_up"), btn -> {
			int index = mapOption.indexOf(mapOptionEntry) - 1;
			if (index >= 0) {
				mapOption.removeEntry(mapOptionEntry);
				mapOption.insertEntry(index, mapOptionEntry);
				updateButtonStates();
			}
		});

		moveDownButton = new TooltipButtonWidget(
				screen, dim.x() + 20, dim.y(), 20, 20, Text.literal("\u2193"), Text.translatable("yacl.list.move_down"), btn -> {
			int index = mapOption.indexOf(mapOptionEntry) + 1;
			if (index < mapOption.options().size()) {
				mapOption.removeEntry(mapOptionEntry);
				mapOption.insertEntry(index, mapOptionEntry);
				updateButtonStates();
			}
		});

		updateButtonStates();
	}

	@Override
	public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
		updateButtonStates(); // update every render in case option becomes available/unavailable

		removeButton.setY(getDimension().y());
		moveUpButton.setY(getDimension().y());
		moveDownButton.setY(getDimension().y());
		entryWidget.setDimension(entryWidget.getDimension().withY(getDimension().y()));

		removeButton.render(graphics, mouseX, mouseY, delta);
		moveUpButton.render(graphics, mouseX, mouseY, delta);
		moveDownButton.render(graphics, mouseX, mouseY, delta);
//		entryWidget.render(graphics, mouseX, mouseY, delta);
	}

	protected void updateButtonStates() {
		removeButton.active = mapOption.available() && mapOption.numberOfEntries() > mapOption.minimumNumberOfEntries();
		moveUpButton.active = mapOption.indexOf(mapOptionEntry) > 0 && mapOption.available();
		moveDownButton.active = mapOption.indexOf(mapOptionEntry) < mapOption.options().size() - 1 && mapOption.available();
	}

	@Override
	public void unfocus() {
		entryWidget.unfocus();
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		entryWidget.appendNarrations(builder);
	}

	@Override
	public boolean matchesSearch(String query) {
		return optionNameString.contains(query.toLowerCase());
	}

	@Override
	public List<? extends Element> children() {
		return ImmutableList.of(moveUpButton, moveDownButton, entryWidget, removeButton);
	}

	@Override
	public boolean isDragging() {
		return dragging;
	}

	@Override
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	@Nullable
	@Override
	public Element getFocused() {
		return focused;
	}

	@Override
	public void setFocused(@Nullable Element focused) {
		this.focused = focused;
	}
}
