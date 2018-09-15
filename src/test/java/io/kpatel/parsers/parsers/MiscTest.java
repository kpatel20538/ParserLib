package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import java.util.Map;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MiscTest {
    @Test
    public void testEndOfStreamSuccess() {
        var stream = new StringStream("");
        Parser<Void, String, Character> parser = endOfStream();
        var result = parser.parse(stream);

        Void item = result.getOrThrow();

        assertNull(item);
    }

    @Test(expected = ParserError.class)
    public void testEndOfStreamFailure() {
        var stream = new StringStream("Hello World");
        Parser<Void, String, Character> parser = endOfStream();
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testPeekSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = prefix(
                peek(sequence("Hello", () -> "Cannot Find Hello")),
                sequence("Hello World", () -> "Cannot Find Hello World"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testPeekFailure() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = prefix(
                peek(sequence("World", () -> "Cannot Find World")),
                sequence("Hello World", () -> "Cannot Find Hello World"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testExceptionSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                sequence("Hello", () -> "Cannot Find Hello"),
                str -> str.charAt(0) == 'W');
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testExceptionFailureBase() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                sequence("World", () -> "Cannot Find World"),
                str -> str.charAt(0) == 'W');
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testExceptionFailureExcept() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                sequence("Hello", () -> "Cannot Find Hello"),
                str -> str.charAt(0) == 'H');
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testPipeSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = pipe(
                sequence("Hello ", () -> "Cannot Find Hello"),
                sequence("World", () -> "Cannot Find World"),
                String::concat);
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testPipeFailureLeft() {
        var stream = new StringStream("Alpha World");
        Parser<String, String, Character> parser = pipe(
                sequence("Hello ", () -> "Cannot Find Hello"),
                sequence("World", () -> "Cannot Find World"),
                String::concat);
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPipeFailureRight() {
        var stream = new StringStream("Hello Alpha");
        Parser<String, String, Character> parser = pipe(
                sequence("Hello ", () -> "Cannot Find Hello"),
                sequence("World", () -> "Cannot Find World"),
                String::concat);
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testEntrySuccess() {
        var stream = new StringStream("Hello World");
        Parser<Map.Entry<String, String>, String, Character> parser = entry(
                sequence("Hello ", () -> "Cannot Find Hello"),
                sequence("World", () -> "Cannot Find World"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Map.entry("Hello ", "World"), item);
    }

    @Test(expected = ParserError.class)
    public void testEntryFailureKey() {
        var stream = new StringStream("Alpha World");
        Parser<Map.Entry<String, String>, String, Character> parser = entry(
                sequence("Hello ", () -> "Cannot Find Hello"),
                sequence("World", () -> "Cannot Find World"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void tesEntryFailureValue() {
        var stream = new StringStream("Hello Alpha");
        Parser<Map.Entry<String, String>, String, Character> parser = entry(
                sequence("Hello ", () -> "Cannot Find Hello"),
                sequence("World", () -> "Cannot Find World"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }
}
