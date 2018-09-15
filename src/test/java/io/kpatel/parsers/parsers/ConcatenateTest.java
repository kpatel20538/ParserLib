package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import java.util.Arrays;

import static io.kpatel.parsers.Parsers.concatenateString;
import static io.kpatel.parsers.Parsers.sequence;
import static org.junit.Assert.assertEquals;

public class ConcatenateTest {
    @Test
    public void testConcatStringSuccess() {
        StringStream stream = new StringStream("Hello World Foobar ");
        Parser<String, String, Character> parser = concatenateString(Arrays.asList(
                sequence("Hello ", () -> "Cannot find Hello"),
                sequence("World ", () -> "Cannot find World"),
                sequence("Foobar ", () -> "Cannot find Foobar")));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello World Foobar ", item);
    }

    @Test(expected = ParserError.class)
    public void testConcatStringFirstFailure() {
        StringStream stream = new StringStream("Alpha World Foobar ");
        Parser<String, String, Character> parser = concatenateString(Arrays.asList(
                sequence("Hello ", () -> "Cannot find Hello"),
                sequence("World ", () -> "Cannot find World"),
                sequence("Foobar ", () -> "Cannot find Foobar")));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testConcatStringSecondFailure() {
        StringStream stream = new StringStream("Hello Alpha Foobar ");
        Parser<String, String, Character> parser = concatenateString(Arrays.asList(
                sequence("Hello ", () -> "Cannot find Hello"),
                sequence("World ", () -> "Cannot find World"),
                sequence("Foobar ", () -> "Cannot find Foobar")));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testConcatStringThirdFailure() {
        StringStream stream = new StringStream("Hello World Alpha ");
        Parser<String, String, Character> parser = concatenateString(Arrays.asList(
                sequence("Hello ", () -> "Cannot find Hello"),
                sequence("World ", () -> "Cannot find World"),
                sequence("Foobar ", () -> "Cannot find Foobar")));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
