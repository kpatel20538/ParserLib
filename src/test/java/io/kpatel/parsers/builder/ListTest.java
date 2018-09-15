package io.kpatel.parsers.builder;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ListTest {
    @Test
    public void testEmpty() {
        var sequenceBuilder = new ListSequenceBuilder<Token>();

        var builder = sequenceBuilder.getNewBuilder();

        var out = sequenceBuilder.toOutput(builder);
        assertEquals(Collections.emptyList(), out);
    }

    @Test
    public void testAppendPart() {
        var sequenceBuilder = new ListSequenceBuilder<Token>();
        var builder1 = sequenceBuilder.getNewBuilder();

        var builder2 = sequenceBuilder.appendPart(builder1, Token.H);
        var builder3 = sequenceBuilder.appendPart(builder2, Token.E);
        var builder4 = sequenceBuilder.appendPart(builder3, Token.L);
        var builder5 = sequenceBuilder.appendPart(builder4, Token.L);
        var builder6 = sequenceBuilder.appendPart(builder5, Token.O);

        var out = sequenceBuilder.toOutput(builder6);

        assertEquals(Arrays.asList(Token.H, Token.E, Token.L, Token.L, Token.O), out);
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
