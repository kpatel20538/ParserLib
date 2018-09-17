package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.MiscParsers.optional;
import static io.kpatel.parsers.prebuilt.TerminalParsers.item;
import static io.kpatel.parsers.prebuilt.TerminalParsers.sequence;
import static org.junit.Assert.assertEquals;

public class OptionalTest {
    public Supplier<Parser<String, String, Character>> optionalStringParser() {
        return optional(sequence("Hello", () -> "Cannot Find Hello"));
    }

    public Supplier<Parser<Character, String, Character>> optionalGenericParser() {
        return optional(item('H', () -> "Cannot Find Character H"), () -> '\0');
    }


    @Test
    public void testStringOptionalSuccess() {
        var stream = new StringStream("Hello World");
        var parser = optionalStringParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testStringOptionalFailure() {
        var stream = new StringStream("World World");
        var parser = optionalStringParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testGenericOptionalSuccess() {
        var stream = new StringStream("Hello World");
        var parser = optionalGenericParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testGenericOptionalFailure() {
        var stream = new StringStream("World World");
        var parser = optionalGenericParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }
}
