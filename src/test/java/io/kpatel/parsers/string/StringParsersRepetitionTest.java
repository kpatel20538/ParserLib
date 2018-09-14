package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.*;
import static org.junit.Assert.assertEquals;

public class StringParsersRepetitionTest {
    @Test
    public void testZeroOrMoreSuccess() {
        StringParserStream stream = new StringParserStream("Hello Hello Hello ");
        StringParser<String> parser = zeroOrMoreString(string("Hello "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test()
    public void testZeroOrMoreFailure() {
        StringParserStream stream = new StringParserStream("World World World ");
        StringParser<String> parser = zeroOrMoreString(string("Hello "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOneOrMoreSuccess() {
        StringParserStream stream = new StringParserStream("Hello Hello Hello ");
        StringParser<String> parser = oneOrMoreString(string("Hello "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test(expected = ParserError.class)
    public void testOneOrMoreFailure() {
        StringParserStream stream = new StringParserStream("World World World ");
        StringParser<String> parser = oneOrMoreString(string("Hello "));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testFixedRepetitionSuccess() {
        StringParserStream stream = new StringParserStream("1 1 1 1 1 ");
        StringParser<String> parser = repetitionString(3, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test(expected = ParserError.class)
    public void testFixedRepetitionFailure() {
        StringParserStream stream = new StringParserStream("1 1 2 2 2 ");
        StringParser<String> parser = repetitionString(3, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRangedRepetitionSuccessInRange() {
        StringParserStream stream = new StringParserStream("1 1 1 2 2 ");
        StringParser<String> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessOutRange() {
        StringParserStream stream = new StringParserStream("1 1 1 1 1 ");
        StringParser<String> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessUpperEdge() {
        StringParserStream stream = new StringParserStream("1 1 1 1 2 ");
        StringParser<String> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessLowerEdge() {
        StringParserStream stream = new StringParserStream("1 1 2 2 2 ");
        StringParser<String> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 ", item);
    }

    @Test(expected = ParserError.class)
    public void testRangedRepetitionFailure() {
        StringParserStream stream = new StringParserStream("1 2 2 2 2 ");
        StringParser<String> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
