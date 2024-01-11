package io.github.steveplays28.blendium.client.compat.yacl.option;

import com.google.common.collect.ImmutableSet;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionFlag;
import org.jetbrains.annotations.NotNull;

public interface MapOptionEntry<S, T> extends Option<T> {
	MapOption<S, T> parentGroup();

	@Override
	default @NotNull ImmutableSet<OptionFlag> flags() {
		return parentGroup().flags();
	}

	@Override
	default boolean available() {
		return parentGroup().available();
	}
}
