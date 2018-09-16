package io.kpatel.parsers.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WHAT: Specialized Builder for Lists
 * NOTE:
 * - This Implementation has Mutable internal states.
 * - This Implementation has Immutable Output.
 *
 * @see Builder
 */
public final class ListBuilder<T> implements Builder<List<T>, T> {
    private final ArrayList<T> builder;

    public ListBuilder() {
        builder = new ArrayList<>();
    }

    @Override
    public Builder<List<T>, T> append(T part) {
        builder.add(part);
        return this;
    }

    @Override
    public List<T> toOutput() {
        return Collections.unmodifiableList(builder);
    }
}
