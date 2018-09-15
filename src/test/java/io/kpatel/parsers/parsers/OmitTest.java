package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class OmitTest {
    @Test
    public void testStringOmitSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = omit(sequence("Hello", () -> "Cannot Find Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test(expected = ParserError.class)
    public void testStringOmitFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = omit(sequence("World", () -> "Cannot Find World"));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testGenericOmitSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = omit(item('H', () -> "Cannot Find Character H"), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }

    @Test(expected = ParserError.class)
    public void testGenericOmitFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = omit(item('W', () -> "Cannot Find Character W"), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
