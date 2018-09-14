package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.*;
import static org.junit.Assert.assertEquals;

public class StringParsersOmitTest {
    @Test
    public void testStringOmitSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = omit(string("Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test(expected = ParserError.class)
    public void testStringOmitFailure() {
        StringParserStream stream = new StringParserStream("Hello");
        StringParser<String> parser = omit(string("World"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();
    }

    @Test
    public void testGenericOmitSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = omit(character('H'), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }

    @Test(expected = ParserError.class)
    public void testGenericOmitFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = omit(character('W'), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();
    }
}
