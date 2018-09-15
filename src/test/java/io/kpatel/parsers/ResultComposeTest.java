package io.kpatel.parsers;

import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResultComposeTest {

    @Test
    public void testMapSuccess() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result1 = Result.success("Hello", parserStream);
        Result<String, StringStream> result2 = result1
                .map((t, stream) -> t + " World");

        String item = result2.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testMapFailure() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result1 = Result.failure("Error", parserStream);
        Result<String, StringStream> result2 = result1
                .map((t, stream) -> "World");

        result2.getOrThrow();
    }

    @Test
    public void testChainSuccessToSuccess() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result1 = Result.success("Hello", parserStream);
        Result<String, StringStream> result2 = result1
                .chain((t, stream) -> Result.success(t + " World", stream));

        String item = result2.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testChainFailureToSuccess() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result1 = Result.failure("Error", parserStream);
        Result<String, StringStream> result2 = result1
                .chain((t, stream) -> Result.success(t + " World", stream));

        result2.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testChainSuccessToFailure() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result1 = Result.success("Hello", parserStream);
        Result<String, StringStream> result2 = result1
                .chain((t, stream) -> Result.failure("Error 2", stream));

        result2.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testChainFailureToFailure() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result1 = Result.failure("Error", parserStream);
        Result<String, StringStream> result2 = result1
                .chain((t, stream) -> Result.failure("Error 2", stream));

        result2.getOrThrow();
    }

    @Test
    public void testOrElseSuccess() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result1 = Result.success("Hello", parserStream);
        Result<String, StringStream> result2 = result1
                .orElse(() -> Result.success("World", parserStream));

        String item = result2.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOrElseFailure() {
        StringStream parserStream = new StringStream("");
        Result<String, StringStream> result1 = Result.failure("Error", parserStream);
        Result<String, StringStream> result2 = result1
                .orElse(() -> Result.success("World", parserStream));

        String item = result2.getOrThrow();

        assertEquals("World", item);
    }


}
