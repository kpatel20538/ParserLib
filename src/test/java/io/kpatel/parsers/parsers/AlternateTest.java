package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import java.util.Arrays;

import static io.kpatel.parsers.Parsers.alternate;
import static io.kpatel.parsers.Parsers.sequence;
import static org.junit.Assert.assertEquals;

public class AlternateTest {
    @Test
    public void testAlternateFirstSuccess() {
        StringStream stream = new StringStream("Hello World Foobar");
        Parser<String, String, Character> parser = alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testAlternateSecondSuccess() {
        StringStream stream = new StringStream("World Foobar Hello");
        Parser<String, String, Character> parser = alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("World", item);
    }

    @Test
    public void testAlternateThirdSuccess() {
        StringStream stream = new StringStream("Foobar Hello World");
        Parser<String, String, Character> parser = alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Foobar", item);
    }

    @Test(expected = ParserError.class)
    public void testAlternateFailed() {
        StringStream stream = new StringStream("Alpha Hello World Foobar");
        Parser<String, String, Character> parser = alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
