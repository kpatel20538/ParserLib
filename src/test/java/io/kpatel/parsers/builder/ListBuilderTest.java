package io.kpatel.parsers.builder;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListBuilderTest {
    @Test
    public void testEmpty() {
        var builder = new ListBuilder<Token>();

        var out = builder.toOutput();

        assertEquals(Collections.emptyList(), out);
    }

    @Test
    public void testAppendPart() {
        var builder = new ListBuilder<Token>();
        builder.append(Token.H)
                .append(Token.E)
                .append(Token.L)
                .append(Token.L)
                .append(Token.O);

        var out = builder.toOutput();

        assertEquals(List.of(Token.H, Token.E, Token.L, Token.L, Token.O), out);
    }

    private enum Token {E, H, L, O}
}
