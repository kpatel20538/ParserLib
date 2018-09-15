package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MiscTest {
    @Test
    public void testEndOfStreamSuccess() {
        StringStream stream = new StringStream("");
        Parser<Void, String, Character> parser = endOfStream();
        Result<Void, ?> result = parser.parse(stream);

        Void item = result.getOrThrow();

        assertNull(item);
    }

    @Test(expected = ParserError.class)
    public void testEndOfStreamFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<Void, String, Character> parser = endOfStream();
        Result<Void, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testPeekSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = prefix(
                peek(sequence("Hello", () -> "Cannot Find Hello")),
                sequence("Hello World", () -> "Cannot Find Hello World"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testPeekFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = prefix(
                peek(sequence("World", () -> "Cannot Find World")),
                sequence("Hello World", () -> "Cannot Find Hello World"));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testExceptionSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                sequence("Hello", () -> "Cannot Find Hello"),
                str -> str.charAt(0) == 'W');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testExceptionFailureBase() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                sequence("World", () -> "Cannot Find World"),
                str -> str.charAt(0) == 'W');
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testExceptionFailureExcept() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = exception(
                sequence("Hello", () -> "Cannot Find Hello"),
                str -> str.charAt(0) == 'H');
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
