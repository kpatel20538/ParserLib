package io.kpatel.parsers.builder;

import io.kpatel.parsers.SequenceBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stateless Builder for Maps
 */
public class MapSequenceBuilder<K, V> implements SequenceBuilder<HashMap<K, V>, Map.Entry<K, V>, Map<K, V>> {
    @Override
    public HashMap<K, V> getNewBuilder() {
        return new HashMap<>();
    }

    @Override
    public HashMap<K, V> appendPart(HashMap<K, V> builder, Map.Entry<K, V> part) {
        builder.put(part.getKey(), part.getValue());
        return builder;
    }

    @Override
    public Map<K, V> toOutput(HashMap<K, V> builder) {
        return Collections.unmodifiableMap(builder);
    }
}
