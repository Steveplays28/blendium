package io.github.steveplays28.blendium.client.compat.yacl.option;

import com.google.common.collect.ImmutableList;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import io.github.steveplays28.blendium.client.compat.yacl.option.impl.MapOptionImpl;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.function.*;

public interface MapOption<S, T> extends OptionGroup, Option<Map<S, T>> {
	@Override
	@NotNull ImmutableList<MapOptionEntry<S, T>> options();

	@ApiStatus.Internal
	int numberOfEntries();

	@ApiStatus.Internal
	int maximumNumberOfEntries();

	@ApiStatus.Internal
	int minimumNumberOfEntries();

	@ApiStatus.Internal
	MapOptionEntry<S, T> insertNewEntry();

	@ApiStatus.Internal
	void insertEntry(int index, MapOptionEntry<?, ?> entry);

	@ApiStatus.Internal
	int indexOf(MapOptionEntry<?, ?> entry);

	@ApiStatus.Internal
	void removeEntry(MapOptionEntry<?, ?> entry);

	@ApiStatus.Internal
	void addRefreshListener(Runnable changedListener);

	static <S, T> MapOption.Builder<S, T> createBuilder() {
		return new MapOptionImpl.BuilderImpl<>();
	}

	interface Builder<S, T> {
		/**
		 * Sets name of the list, for UX purposes, a name should always be given,
		 * but isn't enforced.
		 *
		 * @see ListOption#name()
		 */
		MapOption.Builder<S, T> name(@NotNull Text name);

		MapOption.Builder<S, T> description(@NotNull OptionDescription description);

		/**
		 * Sets the value that is used when creating new entries
		 */
		MapOption.Builder<S, T> initial(@NotNull Supplier<Map.Entry<S, T>> initialValue);

		/**
		 * Sets the value that is used when creating new entries
		 */
		MapOption.Builder<S, T> initial(@NotNull Map.Entry<S, T> initialValue);

		MapOption.Builder<S, T> keyController(@NotNull Function<MapOptionEntry<S, T>, ControllerBuilder<S>> controller);

		MapOption.Builder<S, T> valueController(@NotNull Function<MapOptionEntry<S, T>, ControllerBuilder<T>> controller);

		/**
		 * Sets the controller for the option.
		 * This is how you interact and change the options.
		 *
		 * @see dev.isxander.yacl3.gui.controllers
		 */
		MapOption.Builder<S, T> customController(@NotNull Function<MapOptionEntry<S, T>, Controller<S>> keyControl, @NotNull Function<MapOptionEntry<S, T>, Controller<T>> valueControl);

		/**
		 * Sets the binding for the option.
		 * Used for default, getter and setter.
		 *
		 * @see Binding
		 */
		MapOption.Builder<S, T> binding(@NotNull Binding<Map<S, T>> binding);

		/**
		 * Sets the binding for the option.
		 * Shorthand of {@link Binding#generic(Object, Supplier, Consumer)}
		 *
		 * @param def    default value of the option, used to reset
		 * @param getter should return the current value of the option
		 * @param setter should set the option to the supplied value
		 * @see Binding
		 */
		MapOption.Builder<S, T> binding(@NotNull Map<S, T> def, @NotNull Supplier<@NotNull Map<S, T>> getter, @NotNull Consumer<@NotNull Map<S, T>> setter);

		/**
		 * Sets if the option can be configured
		 *
		 * @see Option#available()
		 */
		MapOption.Builder<S, T> available(boolean available);

		/**
		 * Sets a minimum size for the list. Once this size is reached,
		 * no further entries may be removed.
		 */
		MapOption.Builder<S, T> minimumNumberOfEntries(int number);

		/**
		 * Sets a maximum size for the list. Once this size is reached,
		 * no further entries may be added.
		 */
		MapOption.Builder<S, T> maximumNumberOfEntries(int number);

		/**
		 * Dictates if new entries should be added to the end of the list
		 * rather than the top.
		 */
		MapOption.Builder<S, T> insertEntriesAtEnd(boolean insertAtEnd);

		/**
		 * Adds a flag to the option.
		 * Upon applying changes, all flags are executed.
		 * {@link Option#flags()}
		 */
		MapOption.Builder<S, T> flag(@NotNull OptionFlag... flag);

		/**
		 * Adds a flag to the option.
		 * Upon applying changes, all flags are executed.
		 * {@link Option#flags()}
		 */
		MapOption.Builder<S, T> flags(@NotNull Collection<OptionFlag> flags);

		/**
		 * Dictates if the group should be collapsed by default.
		 * If not set, it will not be collapsed by default.
		 *
		 * @see OptionGroup#collapsed()
		 */
		MapOption.Builder<S, T> collapsed(boolean collapsible);

		/**
		 * Adds a listener to the option. Invoked upon changing any of the list's entries.
		 *
		 * @see Option#addListener(BiConsumer)
		 */
		MapOption.Builder<S, T> listener(@NotNull BiConsumer<Option<Map<S, T>>, Map<S, T>> listener);

		/**
		 * Adds multiple listeners to the option. Invoked upon changing of any of the list's entries.
		 *
		 * @see Option#addListener(BiConsumer)
		 */
		MapOption.Builder<S, T> listeners(@NotNull Collection<BiConsumer<Option<Map<S, T>>, Map<S, T>>> listeners);

		MapOption<S, T> build();
	}
}