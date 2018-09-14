package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static io.kpatel.parsers.string.StringParsers.string;
import static org.junit.Assert.assertEquals;

public class RepetitionTest {
    @Test
    public void testZeroOrMoreSuccess() {
        StringParserStream stream = new StringParserStream("Hello Hello Hello ");
        Parser<String, String, Character> parser = zeroOrMoreString(string("Hello "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test()
    public void testZeroOrMoreFailure() {
        StringParserStream stream = new StringParserStream("World World World ");
        Parser<String, String, Character> parser = zeroOrMoreString(string("Hello "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOneOrMoreSuccess() {
        StringParserStream stream = new StringParserStream("Hello Hello Hello ");
        Parser<String, String, Character> parser = oneOrMoreString(string("Hello "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello Hello Hello ", item);
    }

    @Test(expected = ParserError.class)
    public void testOneOrMoreFailure() {
        StringParserStream stream = new StringParserStream("World World World ");
        Parser<String, String, Character> parser = oneOrMoreString(string("Hello "));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testFixedRepetitionSuccess() {
        StringParserStream stream = new StringParserStream("1 1 1 1 1 ");
        Parser<String, String, Character> parser = repetitionString(3, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test(expected = ParserError.class)
    public void testFixedRepetitionFailure() {
        StringParserStream stream = new StringParserStream("1 1 2 2 2 ");
        Parser<String, String, Character> parser = repetitionString(3, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRangedRepetitionSuccessInRange() {
        StringParserStream stream = new StringParserStream("1 1 1 2 2 ");
        Parser<String, String, Character> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessOutRange() {
        StringParserStream stream = new StringParserStream("1 1 1 1 1 ");
        Parser<String, String, Character> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessUpperEdge() {
        StringParserStream stream = new StringParserStream("1 1 1 1 2 ");
        Parser<String, String, Character> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 1 1 ", item);
    }

    @Test
    public void testRangedRepetitionSuccessLowerEdge() {
        StringParserStream stream = new StringParserStream("1 1 2 2 2 ");
        Parser<String, String, Character> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("1 1 ", item);
    }

    @Test(expected = ParserError.class)
    public void testRangedRepetitionFailure() {
        StringParserStream stream = new StringParserStream("1 2 2 2 2 ");
        Parser<String, String, Character> parser = repetitionString(2, 4, string("1 "));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
