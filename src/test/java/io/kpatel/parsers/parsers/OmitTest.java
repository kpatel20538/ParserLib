package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.omit;
import static io.kpatel.parsers.string.StringParsers.character;
import static io.kpatel.parsers.string.StringParsers.string;
import static org.junit.Assert.assertEquals;

public class OmitTest {
    @Test
    public void testStringOmitSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = omit(string("Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test(expected = ParserError.class)
    public void testStringOmitFailure() {
        StringParserStream stream = new StringParserStream("Hello");
        Parser<String, String, Character> parser = omit(string("World"));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testGenericOmitSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = omit(character('H'), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }

    @Test(expected = ParserError.class)
    public void testGenericOmitFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = omit(character('W'), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
