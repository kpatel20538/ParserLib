package io.kpatel.parsers.builder;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapBuilderTest {
    @Test
    public void testEmpty() {
        var builder = new MapBuilder<>();

        var out = builder.toOutput();

        assertEquals(Collections.emptyMap(), out);
    }

    @Test
    public void testAppendPart() {
        var builder = new MapBuilder<String, Token>();
        builder.append(Map.entry("W", Token.W))
                .append(Map.entry("o", Token.O))
                .append(Map.entry("r", Token.R))
                .append(Map.entry("l", Token.L))
                .append(Map.entry("d", Token.D));

        var out = builder.toOutput();

        assertEquals(Map.of(
                "W", Token.W,
                "o", Token.O,
                "r", Token.R,
                "l", Token.L,
                "d", Token.D), out);
    }

    private enum Token {D, L, O, R, W}
}
