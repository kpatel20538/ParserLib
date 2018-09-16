package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.Arrays;

import static io.kpatel.parsers.prebuilt.Parsers.alternate;
import static io.kpatel.parsers.prebuilt.TerminalParsers.sequence;
import static org.junit.Assert.assertEquals;

public class AlternateTest {
    @Test
    public void testAlternateFirstSuccess() {
        var stream = new StringStream("Hello World Foobar");
        Parser<String, String, Character> parser = alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testAlternateSecondSuccess() {
        var stream = new StringStream("World Foobar Hello");
        Parser<String, String, Character> parser = alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("World", item);
    }

    @Test
    public void testAlternateThirdSuccess() {
        var stream = new StringStream("Foobar Hello World");
        Parser<String, String, Character> parser = alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Foobar", item);
    }

    @Test(expected = ParserError.class)
    public void testAlternateFailed() {
        var stream = new StringStream("Alpha Hello World Foobar");
        Parser<String, String, Character> parser = alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
        var result = parser.parse(stream);

        result.getOrThrow();
    }
}
