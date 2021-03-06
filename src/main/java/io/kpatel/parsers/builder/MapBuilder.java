package io.kpatel.parsers.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * WHAT: Specialized Builder for Maps
 * NOTE:
 * - This Implementation has Mutable internal states.
 * - This Implementation has Immutable Output.
 *
 * @see Builder
 */
public final class MapBuilder<K, V> implements Builder<Map<K, V>, Map.Entry<K, V>> {
    private final HashMap<K, V> builder;

    public MapBuilder() {
        builder = new HashMap<>();
    }

    @Override
    public Builder<Map<K, V>, Map.Entry<K, V>> append(Map.Entry<K, V> part) {
        builder.put(part.getKey(), part.getValue());
        return this;
    }

    @Override
    public Map<K, V> toOutput() {
        return Collections.unmodifiableMap(builder);
    }
}
