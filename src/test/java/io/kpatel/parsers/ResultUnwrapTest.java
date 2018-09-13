package io.kpatel.parsers;

import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResultUnwrapTest {
    @Test
    public void testGetOrThrowSuccess() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result = Result.success("Hello", parserStream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testGetOrThrowFailure() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result = Result.failure("Error", parserStream);

        result.getOrThrow();
    }

    @Test
    public void testGetOrElseSuccess() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result = Result.success("Hello", parserStream);

        String item = result.getOrElse(() -> "World");

        assertEquals("Hello", item);
    }

    @Test
    public void testGetOrElseFailure() {
        StringParserStream parserStream = new StringParserStream("");
        Result<String, StringParserStream> result = Result.failure("Error", parserStream);

        String item = result.getOrElse(() -> "World");

        assertEquals("World", item);
    }
}
