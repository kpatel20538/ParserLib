package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import java.util.Arrays;

import static io.kpatel.parsers.string.StringParsers.alternate;
import static io.kpatel.parsers.string.StringParsers.string;
import static org.junit.Assert.assertEquals;

public class StringParsersAlternateTest {
    @Test
    public void testAlternateFirstSuccess() {
        StringParserStream stream = new StringParserStream("Hello World Foobar");
        StringParser<String> parser = alternate(Arrays.asList(
                string("Hello"),
                string("World"),
                string("Foobar")));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testAlternateSecondSuccess() {
        StringParserStream stream = new StringParserStream("World Foobar Hello");
        StringParser<String> parser = alternate(Arrays.asList(
                string("Hello"),
                string("World"),
                string("Foobar")));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("World", item);
    }

    @Test
    public void testAlternateThirdSuccess() {
        StringParserStream stream = new StringParserStream("Foobar Hello World");
        StringParser<String> parser = alternate(Arrays.asList(
                string("Hello"),
                string("World"),
                string("Foobar")));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Foobar", item);
    }

    @Test(expected = ParserError.class)
    public void testAlternateFailed() {
        StringParserStream stream = new StringParserStream("Alpha Hello World Foobar");
        StringParser<String> parser = alternate(Arrays.asList(
                string("Hello"),
                string("World"),
                string("Foobar")));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
