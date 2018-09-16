package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.prebuilt.AffixParsers;
import io.kpatel.parsers.prebuilt.TerminalParsers;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.Map;

import static io.kpatel.parsers.prebuilt.MiscParsers.*;
import static org.junit.Assert.*;

public class MiscTest {
    @Test
    public void testEndOfStreamSuccess() {
        var stream = new StringStream("");
        Parser<Object, String, Character> parser = TerminalParsers.endOfStream();
        var result = parser.parse(stream);

        Object item = result.getOrThrow();

        assertNotNull(item);
    }

    @Test
    public void testEndOfStreamFailure() {
        var stream = new StringStream("Hello World");
        Parser<Object, String, Character> parser = TerminalParsers.endOfStream();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testPeekSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = AffixParsers.prefix(
                peek(TerminalParsers.sequence("Hello",
                        () -> "Cannot Find Hello")),
                TerminalParsers.sequence("Hello World",
                        () -> "Cannot Find Hello World"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test
    public void testPeekFailure() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = AffixParsers.prefix(
                peek(TerminalParsers.sequence("World",
                        () -> "Cannot Find World")),
                TerminalParsers.sequence("Hello World",
                        () -> "Cannot Find Hello World"));
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testExceptionSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                TerminalParsers.sequence("Hello",
                        () -> "Cannot Find Hello"),
                str -> str.charAt(0) == 'W',
                () -> "Found a W Character");
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testExceptionFailureBase() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"),
                str -> str.charAt(0) == 'W',
                () -> "Found a W Character");
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testExceptionFailureExcept() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                TerminalParsers.sequence("Hello",
                        () -> "Cannot Find Hello"),
                str -> str.charAt(0) == 'H',
                () -> "Found a H Character");
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testPipeSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = pipe(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"),
                String::concat);
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test
    public void testPipeFailureLeft() {
        var stream = new StringStream("Alpha World");
        Parser<String, String, Character> parser = pipe(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"),
                String::concat);
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testPipeFailureRight() {
        var stream = new StringStream("Hello Alpha");
        Parser<String, String, Character> parser = pipe(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"),
                String::concat);
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testEntrySuccess() {
        var stream = new StringStream("Hello World");
        Parser<Map.Entry<String, String>, String, Character> parser = entry(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Map.entry("Hello ", "World"), item);
    }

    @Test
    public void testEntryFailureKey() {
        var stream = new StringStream("Alpha World");
        Parser<Map.Entry<String, String>, String, Character> parser = entry(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"));
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void tesEntryFailureValue() {
        var stream = new StringStream("Hello Alpha");
        Parser<Map.Entry<String, String>, String, Character> parser = entry(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"));
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }
}
