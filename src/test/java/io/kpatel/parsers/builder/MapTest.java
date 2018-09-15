package io.kpatel.parsers.builder;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapTest {
    @Test
    public void testEmpty() {
        var sequenceBuilder = new MapSequenceBuilder<String, Token>();

        var builder = sequenceBuilder.getNewBuilder();

        var out = sequenceBuilder.toOutput(builder);
        assertEquals(Collections.emptyMap(), out);
    }

    @Test
    public void testAppendPart() {
        var sequenceBuilder = new MapSequenceBuilder<String, Token>();
        var builder1 = sequenceBuilder.getNewBuilder();

        var builder2 = sequenceBuilder.appendPart(builder1, Map.entry("W", Token.W));
        var builder3 = sequenceBuilder.appendPart(builder2, Map.entry("o", Token.O));
        var builder4 = sequenceBuilder.appendPart(builder3, Map.entry("r", Token.R));
        var builder5 = sequenceBuilder.appendPart(builder4, Map.entry("l", Token.L));
        var builder6 = sequenceBuilder.appendPart(builder5, Map.entry("d", Token.D));

        var out = sequenceBuilder.toOutput(builder6);

        assertEquals(Map.of(
                "W", Token.W,
                "o", Token.O,
                "r", Token.R,
                "l", Token.L,
                "d", Token.D), out);
    }

    private enum Token {
        D(true), E(true), H(true), L(true),
        O(true), R(true), W(true), $(false);

        boolean flag;

        Token(boolean flag) {
            this.flag = flag;
        }

        public boolean isFlag() {
            return flag;
        }
    }
}
