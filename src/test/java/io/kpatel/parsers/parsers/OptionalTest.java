package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class OptionalTest {
    @Test
    public void testStringOptionalSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optional(sequence("Hello", () -> "Cannot Find Hello"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testStringOptionalFailure() {
        var stream = new StringStream("Hello");
        Parser<String, String, Character> parser = optional(sequence("World", () -> "Cannot Find World"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testGenericOptionalSuccess() {
        var stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = optional(item('H', () -> "Cannot Find Character H"), () -> '\0');
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testGenericOptionalFailure() {
        var stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = optional(item('W', () -> "Cannot Find Character W"), () -> '\0');
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }
}
