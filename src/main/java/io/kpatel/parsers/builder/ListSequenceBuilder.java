package io.kpatel.parsers.builder;

import io.kpatel.parsers.SequenceBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stateless Builder for Lists
 */
public class ListSequenceBuilder<T> implements SequenceBuilder<ArrayList<T>, T, List<T>> {
    @Override
    public ArrayList<T> getNewBuilder() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<T> appendPart(ArrayList<T> builder, T part) {
        builder.add(part);
        return builder;
    }

    @Override
    public List<T> toOutput(ArrayList<T> builder) {
        return Collections.unmodifiableList(builder);
    }
}
