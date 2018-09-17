package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.MiscParsers.omit;
import static io.kpatel.parsers.prebuilt.TerminalParsers.item;
import static io.kpatel.parsers.prebuilt.TerminalParsers.sequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OmitTest {

    public Supplier<Parser<String, String, Character>> omitStringParser() {
        return omit(sequence("Hello", () -> "Cannot Find Hello"));
    }

    public Supplier<Parser<Character, String, Character>> omitGenericParser() {
        return omit(item('H', () -> "Cannot Find Character H"), () -> '\0');
    }

    @Test
    public void testStringOmitSuccess() {
        var stream = new StringStream("Hello World");
        var parser = omitStringParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testStringOmitFailure() {
        var stream = new StringStream("World World");
        var parser = omitStringParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testGenericOmitSuccess() {
        var stream = new StringStream("Hello World");
        var parser = omitGenericParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }

    @Test
    public void testGenericOmitFailure() {
        var stream = new StringStream("World World");
        var parser = omitGenericParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }
}
