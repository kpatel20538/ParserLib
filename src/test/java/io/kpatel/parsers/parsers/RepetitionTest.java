package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.RepetitionParsers.*;
import static io.kpatel.parsers.prebuilt.TerminalParsers.sequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RepetitionTest {
    public Supplier<Parser<String, String, Character>> zeroOrMoreParser() {
        return zeroOrMoreString(sequence("Hello ",
                () -> "Can not find Hello"));
    }

    public Supplier<Parser<String, String, Character>> oneOrMoreParser() {
        return oneOrMoreString(sequence("Hello ",
                () -> "Can not find Hello"));
    }

    public Supplier<Parser<String, String, Character>> repeatParser() {
        return repeatString(sequence("1 ",
                () -> "Can not find 1"), 3);
    }

    public Supplier<Parser<String, String, Character>> rangedRepeatParser() {
        return rangedRepeatString(sequence("1 ",
                () -> "Can not find 1"), 2, 4);
    }

    @Test
    public void testZeroOrMoreSuccess() {
        var stream = new StringStream("Hello Hello Hello ");
        var parser = zeroOrMoreParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test
    public void testZeroOrMoreFailure() {
        var stream = new StringStream("World World World ");
        var parser = zeroOrMoreParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOneOrMoreSuccess() {
        var stream = new StringStream("Hello Hello Hello ");
        var parser = oneOrMoreParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test
    public void testOneOrMoreFailure() {
        var stream = new StringStream("World World World ");
        var parser = oneOrMoreParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testFixedRepetitionSuccess() {
        var stream = new StringStream("1 1 1 1 1 ");
        var parser = repeatParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test
    public void testFixedRepetitionFailure() {
        var stream = new StringStream("1 1 2 2 2 ");
        var parser = repeatParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testRangedRepetitionSuccessInRange() {
        var stream = new StringStream("1 1 1 2 2 ");
        var parser = rangedRepeatParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessOutRange() {
        var stream = new StringStream("1 1 1 1 1 ");
        var parser = rangedRepeatParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessUpperEdge() {
        var stream = new StringStream("1 1 1 1 2 ");
        var parser = rangedRepeatParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessLowerEdge() {
        var stream = new StringStream("1 1 2 2 2 ");
        var parser = rangedRepeatParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 ", item);
    }

    @Test
    public void testRangedRepetitionFailure() {
        var stream = new StringStream("1 2 2 2 2 ");
        var parser = rangedRepeatParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }
}
