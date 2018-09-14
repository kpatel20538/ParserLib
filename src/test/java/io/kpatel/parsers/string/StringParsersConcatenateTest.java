package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import java.util.Arrays;

import static io.kpatel.parsers.string.StringParsers.concatenateString;
import static io.kpatel.parsers.string.StringParsers.string;
import static org.junit.Assert.assertEquals;

public class StringParsersConcatenateTest {
    @Test
    public void testConcatStringSuccess() {
        StringParserStream stream = new StringParserStream("Hello World Foobar");
        StringParser<String> parser = concatenateString(Arrays.asList(
                string("Hello "),
                string("World "),
                string("Foobar")));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello World Foobar", item);
    }

    @Test(expected = ParserError.class)
    public void testConcatStringFirstFailure() {
        StringParserStream stream = new StringParserStream("Hello World Foobar");
        StringParser<String> parser = concatenateString(Arrays.asList(
                string("World "),
                string("World "),
                string("Foobar")));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testConcatStringSecondFailure() {
        StringParserStream stream = new StringParserStream("Hello World Foobar");
        StringParser<String> parser = concatenateString(Arrays.asList(
                string("Hello "),
                string("Foobar "),
                string("Foobar")));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testConcatStringThirdFailure() {
        StringParserStream stream = new StringParserStream("Hello World Foobar");
        StringParser<String> parser = concatenateString(Arrays.asList(
                string("Hello "),
                string("World "),
                string("Hello")));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
