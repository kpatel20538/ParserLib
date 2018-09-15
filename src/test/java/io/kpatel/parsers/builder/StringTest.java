package io.kpatel.parsers.builder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringTest {
    @Test
    public void testEmpty() {
        var sequenceBuilder = new StringSequenceBuilder();

        var builder = sequenceBuilder.getNewBuilder();

        var out = sequenceBuilder.toOutput(builder);
        assertEquals("", out);
    }

    @Test
    public void testAppendPart() {
        var sequenceBuilder = new StringSequenceBuilder();
        var builder1 = sequenceBuilder.getNewBuilder();

        var builder2 = sequenceBuilder.appendPart(builder1, "H");
        var builder3 = sequenceBuilder.appendPart(builder2, "e");
        var builder4 = sequenceBuilder.appendPart(builder3, "l");
        var builder5 = sequenceBuilder.appendPart(builder4, "l");
        var builder6 = sequenceBuilder.appendPart(builder5, "o");

        var out = sequenceBuilder.toOutput(builder6);

        assertEquals("Hello", out);
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
