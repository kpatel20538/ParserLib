package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class OptionalTest {
    @Test
    public void testStringOptionalSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optional(sequence("Hello", () -> "Cannot Find Hello"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testStringOptionalFailure() {
        StringStream stream = new StringStream("Hello");
        Parser<String, String, Character> parser = optional(sequence("World", () -> "Cannot Find World"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testGenericOptionalSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = optional(item('H', () -> "Cannot Find Character H"), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testGenericOptionalFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = optional(item('W', () -> "Cannot Find Character W"), () -> '\0');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }
}
