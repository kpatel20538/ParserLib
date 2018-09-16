package io.kpatel.parsers;

import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResultComposeTest {

    @Test
    public void testMapSuccess() {
        var parserStream = new StringStream("");
        var result1 = Result.success("Hello", parserStream);
        var result2 = result1.map(t -> t + " World");

        var item = result2.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test
    public void testMapFailure() {
        var parserStream = new StringStream("");
        var result1 = Result.failure(parserStream.getErrorContext(), () -> "Error");
        var result2 = result1.map(t -> "World");

        assertFalse(result1.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    public void testChainSuccessToSuccess() {
        var parserStream = new StringStream("");
        var result1 = Result.success("Hello", parserStream);
        var result2 = result1.chain((t, stream) ->
                Result.success(t + " World", stream));

        var item = result2.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test
    public void testChainFailureToSuccess() {
        var parserStream = new StringStream("");
        var result1 = Result.failure(parserStream.getErrorContext(), () -> "Error");
        var result2 = result1.chain((t, stream) ->
                Result.success(t + " World", stream));

        assertFalse(result1.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    public void testChainSuccessToFailure() {
        var parserStream = new StringStream("");
        var result1 = Result.success("Hello", parserStream);
        var result2 = result1.chain((t, stream) ->
                Result.failure(stream.getErrorContext(), () -> "Error 2"));

        assertTrue(result1.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    public void testChainFailureToFailure() {
        var parserStream = new StringStream("");
        var result1 = Result.failure(parserStream.getErrorContext(), () -> "Error");
        var result2 = result1.chain((t, stream) ->
                Result.failure(stream.getErrorContext(), () -> "Error 2"));

        assertFalse(result1.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    public void testOrElseSuccess() {
        var parserStream = new StringStream("");
        var result1 = Result.success("Hello", parserStream);
        var result2 = result1.orElse(
                () -> Result.success("World", parserStream));

        var item = result2.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOrElseFailure() {
        var parserStream = new StringStream("");
        var result1 = Result.<String, String, Character>failure(
                parserStream.getErrorContext(), () -> "Error");
        var result2 = result1.orElse(
                () -> Result.success("World", parserStream));

        var item = result2.getOrThrow();

        assertEquals("World", item);
    }
}
