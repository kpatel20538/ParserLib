package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.optional;
import static io.kpatel.parsers.string.StringParsers.character;
import static io.kpatel.parsers.string.StringParsers.string;
import static org.junit.Assert.assertEquals;

public class OptionalTest {
    @Test
    public void testStringOptionalSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = optional(string("Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testStringOptionalFailure() {
        StringParserStream stream = new StringParserStream("Hello");
        Parser<String, String, Character> parser = optional(string("World"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testGenericOptionalSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = optional(character('H'), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testGenericOptionalFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = optional(character('W'), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }
}
