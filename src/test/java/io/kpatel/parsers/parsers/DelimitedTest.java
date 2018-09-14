package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.delimitedString;
import static io.kpatel.parsers.string.StringParsers.character;
import static io.kpatel.parsers.string.StringParsers.string;
import static org.junit.Assert.assertEquals;

public class DelimitedTest {
    @Test
    public void testDelimitedZero() {
        StringParserStream stream = new StringParserStream("");
        Parser<String, String, Character> parser = delimitedString(
                string("Hello"), character(','));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test()
    public void testDelimitedOne() {
        StringParserStream stream = new StringParserStream("Hello");
        Parser<String, String, Character> parser = delimitedString(
                string("Hello"), character(','));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test()
    public void testDelimitedMultiple() {
        StringParserStream stream = new StringParserStream("Hello,Hello,Hello");
        Parser<String, String, Character> parser = delimitedString(
                string("Hello"), character(','));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HelloHelloHello", item);
    }
}
