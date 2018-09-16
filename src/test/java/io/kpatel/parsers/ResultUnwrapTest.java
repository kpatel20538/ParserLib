package io.kpatel.parsers;

import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResultUnwrapTest {
    @Test
    public void testGetOrThrowSuccess() {
        var parserStream = new StringStream("");
        var result = Result.success("Hello", parserStream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testGetOrThrowFailure() {
        var parserStream = new StringStream("");
        var result = Result.failure("Error", parserStream);

        result.getOrThrow();
    }

    @Test
    public void testGetOrElseSuccess() {
        var parserStream = new StringStream("");
        var result = Result.success("Hello", parserStream);

        var item = result.getOrElse(() -> "World");

        assertEquals("Hello", item);
    }

    @Test
    public void testGetOrElseFailure() {
        var parserStream = new StringStream("");
        var result = Result.failure("Error", parserStream);

        var item = result.getOrElse(() -> "World");

        assertEquals("World", item);
    }

    @Test
    public void testIsSuccess() {
        var parserStream = new StringStream("");
        var result = Result.success("Hello", parserStream);

        assertTrue(result.isSuccess());
    }

    @Test
    public void testIsFailure() {
        var parserStream = new StringStream("");
        var result = Result.failure("Error", parserStream);

        assertFalse(result.isSuccess());
    }
}
