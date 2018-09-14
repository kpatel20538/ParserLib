package io.kpatel.parsers.string;

import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.*;
import static org.junit.Assert.assertEquals;

public class StringParsersOptionalTest {
    @Test
    public void testStringOptionalSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = optional(string("Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testStringOptionalFailure() {
        StringParserStream stream = new StringParserStream("Hello");
        StringParser<String> parser = optional(string("World"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testGenericOptionalSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = optional(character('H'), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testGenericOptionalFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = optional(character('W'), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }
}
