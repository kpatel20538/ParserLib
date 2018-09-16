package io.kpatel.parsers;

import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResultComposeTest {

    @Test
    public void testMapSuccess() {
        var parserStream = new StringStream("");
        var result1 = Result.success("Hello", parserStream);
        var result2 = result1.map(t -> t + " World");

        var item = result2.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testMapFailure() {
        var parserStream = new StringStream("");
        var result1 = Result.failure("Error", parserStream);
        var result2 = result1.map(t -> "World");

        result2.getOrThrow();
    }

    @Test
    public void testChainSuccessToSuccess() {
        var parserStream = new StringStream("");
        var result1 = Result.success("Hello", parserStream);
        var result2 = result1.chain((t, stream) -> Result.success(t + " World", stream));

        var item = result2.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testChainFailureToSuccess() {
        var parserStream = new StringStream("");
        var result1 = Result.failure("Error", parserStream);
        var result2 = result1.chain((t, stream) -> Result.success(t + " World", stream));

        result2.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testChainSuccessToFailure() {
        var parserStream = new StringStream("");
        var result1 = Result.success("Hello", parserStream);
        var result2 = result1.chain((t, stream) -> Result.failure("Error 2", stream));

        result2.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testChainFailureToFailure() {
        var parserStream = new StringStream("");
        var result1 = Result.failure("Error", parserStream);
        var result2 = result1.chain((t, stream) -> Result.failure("Error 2", stream));

        result2.getOrThrow();
    }

    @Test
    public void testOrElseSuccess() {
        var parserStream = new StringStream("");
        var result1 = Result.success("Hello", parserStream);
        var result2 = result1.orElse(() -> Result.success("World", parserStream));

        var item = result2.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOrElseFailure() {
        var parserStream = new StringStream("");
        var result1 = Result.failure("Error", parserStream);
        var result2 = result1.orElse(() -> Result.success("World", parserStream));

        var item = result2.getOrThrow();

        assertEquals("World", item);
    }
}
