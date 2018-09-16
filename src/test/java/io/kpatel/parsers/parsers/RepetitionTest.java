package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.prebuilt.RepetitionParsers;
import io.kpatel.parsers.prebuilt.TerminalParsers;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RepetitionTest {
    @Test
    public void testZeroOrMoreSuccess() {
        var stream = new StringStream("Hello Hello Hello ");
        Parser<String, String, Character> parser = RepetitionParsers.zeroOrMoreString(
                TerminalParsers.sequence("Hello ", () -> "Can not find Hello"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test()
    public void testZeroOrMoreFailure() {
        var stream = new StringStream("World World World ");
        Parser<String, String, Character> parser = RepetitionParsers.zeroOrMoreString(
                TerminalParsers.sequence("Hello ", () -> "Can not find Hello"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOneOrMoreSuccess() {
        var stream = new StringStream("Hello Hello Hello ");
        Parser<String, String, Character> parser = RepetitionParsers.oneOrMoreString(
                TerminalParsers.sequence("Hello ", () -> "Can not find Hello"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test(expected = ParserError.class)
    public void testOneOrMoreFailure() {
        var stream = new StringStream("World World World ");
        Parser<String, String, Character> parser = RepetitionParsers.oneOrMoreString(
                TerminalParsers.sequence("Hello ", () -> "Can not find Hello"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testFixedRepetitionSuccess() {
        var stream = new StringStream("1 1 1 1 1 ");
        Parser<String, String, Character> parser = RepetitionParsers.repeatString(
                TerminalParsers.sequence("1 ", () -> "Can not find 1"), 3);
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test(expected = ParserError.class)
    public void testFixedRepetitionFailure() {
        var stream = new StringStream("1 1 2 2 2 ");
        Parser<String, String, Character> parser = RepetitionParsers.repeatString(
                TerminalParsers.sequence("1 ", () -> "Can not find 1"), 3);
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRangedRepetitionSuccessInRange() {
        var stream = new StringStream("1 1 1 2 2 ");
        Parser<String, String, Character> parser = RepetitionParsers.rangedRepeatString(
                TerminalParsers.sequence("1 ", () -> "Can not find 1"), 2, 4);
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessOutRange() {
        var stream = new StringStream("1 1 1 1 1 ");
        Parser<String, String, Character> parser = RepetitionParsers.rangedRepeatString(
                TerminalParsers.sequence("1 ", () -> "Can not find 1"), 2, 4);
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessUpperEdge() {
        var stream = new StringStream("1 1 1 1 2 ");
        Parser<String, String, Character> parser = RepetitionParsers.rangedRepeatString(
                TerminalParsers.sequence("1 ", () -> "Can not find 1"), 2, 4);
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessLowerEdge() {
        var stream = new StringStream("1 1 2 2 2 ");
        Parser<String, String, Character> parser = RepetitionParsers.rangedRepeatString(
                TerminalParsers.sequence("1 ", () -> "Can not find 1"), 2, 4);
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("1 1 ", item);
    }

    @Test(expected = ParserError.class)
    public void testRangedRepetitionFailure() {
        var stream = new StringStream("1 2 2 2 2 ");
        Parser<String, String, Character> parser = RepetitionParsers.rangedRepeatString(
                TerminalParsers.sequence("1 ", () -> "Can not find 1"), 2, 4);
        var result = parser.parse(stream);

        result.getOrThrow();
    }
}
