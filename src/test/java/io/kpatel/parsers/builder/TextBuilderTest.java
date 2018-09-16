package io.kpatel.parsers.builder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextBuilderTest {
    @Test
    public void testEmpty() {
        var builder = new TextBuilder();

        var out = builder.toOutput();

        assertEquals("", out);
    }

    @Test
    public void testAppendPart() {
        var builder = new TextBuilder();
        builder.append("H")
                .append("e")
                .append("l")
                .append("l")
                .append("o");

        var out = builder.toOutput();

        assertEquals("Hello", out);
    }
}
