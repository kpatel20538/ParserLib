package io.kpatel.parsers;

import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResultComposeTest {

    @Test
    public void testMapSuccess() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result1 = Result.success("Hello", parserStream);
        Result<String, StringParserStream> result2 = result1
                .map((t, stream) -> t + " World");

        String item = result2.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testMapFailure() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result1 = Result.failure("Error", parserStream);
        Result<String, StringParserStream> result2 = result1
                .map((t, stream) -> "World");

        result2.getOrThrow();
    }

    @Test
    public void testChainSuccessToSuccess() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result1 = Result.success("Hello", parserStream);
        Result<String, StringParserStream> result2 = result1
                .chain((t, stream) -> Result.success(t + " World", stream));

        String item = result2.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testChainFailureToSuccess() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result1 = Result.failure("Error", parserStream);
        Result<String, StringParserStream> result2 = result1
                .chain((t, stream) -> Result.success(t + " World", stream));

        result2.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testChainSuccessToFailure() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result1 = Result.success("Hello", parserStream);
        Result<String, StringParserStream> result2 = result1
                .chain((t, stream) -> Result.failure("Error 2", stream));

        result2.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testChainFailureToFailure() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result1 = Result.failure("Error", parserStream);
        Result<String, StringParserStream> result2 = result1
                .chain((t, stream) -> Result.failure("Error 2", stream));

        result2.getOrThrow();
    }

    @Test
    public void testOrElseSuccess() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result1 = Result.success("Hello", parserStream);
        Result<String, StringParserStream> result2 = result1
                .orElse(() -> Result.success("World", parserStream));

        String item = result2.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOrElseFailure() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result1 = Result.failure("Error", parserStream);
        Result<String, StringParserStream> result2 = result1
                .orElse(() -> Result.success("World", parserStream));

        String item = result2.getOrThrow();

        assertEquals("World", item);
    }


}
