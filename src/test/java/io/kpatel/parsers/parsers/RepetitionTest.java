package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class RepetitionTest {
    @Test
    public void testZeroOrMoreSuccess() {
        StringStream stream = new StringStream("Hello Hello Hello ");
        Parser<String, String, Character> parser = zeroOrMoreString(
                sequence("Hello ", () -> "Can not find Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test()
    public void testZeroOrMoreFailure() {
        StringStream stream = new StringStream("World World World ");
        Parser<String, String, Character> parser = zeroOrMoreString(
                sequence("Hello ", () -> "Can not find Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOneOrMoreSuccess() {
        StringStream stream = new StringStream("Hello Hello Hello ");
        Parser<String, String, Character> parser = oneOrMoreString(
                sequence("Hello ", () -> "Can not find Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test(expected = ParserError.class)
    public void testOneOrMoreFailure() {
        StringStream stream = new StringStream("World World World ");
        Parser<String, String, Character> parser = oneOrMoreString(
                sequence("Hello ", () -> "Can not find Hello"));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testFixedRepetitionSuccess() {
        StringStream stream = new StringStream("1 1 1 1 1 ");
        Parser<String, String, Character> parser = repetitionString(
                3, sequence("1 ", () -> "Can not find 1"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test(expected = ParserError.class)
    public void testFixedRepetitionFailure() {
        StringStream stream = new StringStream("1 1 2 2 2 ");
        Parser<String, String, Character> parser = repetitionString(
                3, sequence("1 ", () -> "Can not find 1"));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRangedRepetitionSuccessInRange() {
        StringStream stream = new StringStream("1 1 1 2 2 ");
        Parser<String, String, Character> parser = repetitionString(
                2, 4, sequence("1 ", () -> "Can not find 1"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessOutRange() {
        StringStream stream = new StringStream("1 1 1 1 1 ");
        Parser<String, String, Character> parser = repetitionString(
                2, 4, sequence("1 ", () -> "Can not find 1"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessUpperEdge() {
        StringStream stream = new StringStream("1 1 1 1 2 ");
        Parser<String, String, Character> parser = repetitionString(
                2, 4, sequence("1 ", () -> "Can not find 1"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessLowerEdge() {
        StringStream stream = new StringStream("1 1 2 2 2 ");
        Parser<String, String, Character> parser = repetitionString(
                2, 4, sequence("1 ", () -> "Can not find 1"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 ", item);
    }

    @Test(expected = ParserError.class)
    public void testRangedRepetitionFailure() {
        StringStream stream = new StringStream("1 2 2 2 2 ");
        Parser<String, String, Character> parser = repetitionString(
                2, 4, sequence("1 ", () -> "Can not find 1"));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
