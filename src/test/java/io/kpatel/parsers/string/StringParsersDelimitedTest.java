package io.kpatel.parsers.string;

import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.*;
import static org.junit.Assert.assertEquals;

public class StringParsersDelimitedTest {
    @Test
    public void testDelimitedZero() {
        StringParserStream stream = new StringParserStream("");
        StringParser<String> parser = delimitedString(
                string("Hello"), character(','));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test()
    public void testDelimitedOne() {
        StringParserStream stream = new StringParserStream("Hello");
        StringParser<String> parser = delimitedString(
                string("Hello"), character(','));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test()
    public void testDelimitedMultiple() {
        StringParserStream stream = new StringParserStream("Hello,Hello,Hello");
        StringParser<String> parser = delimitedString(
                string("Hello"), character(','));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HelloHelloHello", item);
    }
}
