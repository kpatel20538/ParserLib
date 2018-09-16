package io.kpatel.parsers.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ListBuilder<T> implements Builder<List<T>, T> {
    private final ArrayList<T> builder;

    public ListBuilder() {
        builder = new ArrayList<>();
    }

    @Override
    public Builder<List<T>, T> append(T item) {
        builder.add(item);
        return this;
    }

    @Override
    public List<T> toOutput() {
        return Collections.unmodifiableList(builder);
    }
}
