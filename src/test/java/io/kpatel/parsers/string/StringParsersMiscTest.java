package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringParsersMiscTest {
    @Test
    public void testEndOfStreamSuccess() {
        StringParserStream stream = new StringParserStream("");
        StringParser<Void> parser = endOfStream();
        Result<Void, ?> result = parser.parse(stream);

        Void item = result.getOrThrow();

        assertNull(item);
    }

    @Test(expected = ParserError.class)
    public void testEndOfStreamFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Void> parser = endOfStream();
        Result<Void, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testPeekSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = prefix(peek(string("Hello")), string("Hello World"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test(expected = ParserError.class)
    public void testPeekFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = prefix(peek(string("World")), string("Hello World"));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
