package io.kpatel.parsers;

import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResultUnwrapTest {
    @Test
    public void testGetOrThrowSuccess() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result = Result.success("Hello", parserStream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testGetOrThrowFailure() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result = Result.failure("Error", parserStream);

        result.getOrThrow();
    }

    @Test
    public void testGetOrElseSuccess() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result = Result.success("Hello", parserStream);

        String item = result.getOrElse(() -> "World");

        assertEquals("Hello", item);
    }

    @Test
    public void testGetOrElseFailure() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result = Result.failure("Error", parserStream);

        String item = result.getOrElse(() -> "World");

        assertEquals("World", item);
    }

    @Test
    public void testIsSuccess() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result = Result.success("Hello", parserStream);

        assertTrue(result.isSuccess());
    }

    @Test
    public void testIsFailure() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result = Result.failure("Error", parserStream);

        assertFalse(result.isSuccess());
    }
}
