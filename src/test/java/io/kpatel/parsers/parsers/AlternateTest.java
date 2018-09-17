package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.MiscParsers.alternate;
import static io.kpatel.parsers.prebuilt.TerminalParsers.sequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AlternateTest {
    public Supplier<Parser<String, String, Character>> alternateParser() {
        return alternate(Arrays.asList(
                sequence("Hello", () -> "Cannot find Hello"),
                sequence("World", () -> "Cannot find World"),
                sequence("Foobar", () -> "Cannot find Foobar")));
    }

    @Test
    public void testAlternateFirstSuccess() {
        var stream = new StringStream("Hello World Foobar");
        var parser = alternateParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testAlternateSecondSuccess() {
        var stream = new StringStream("World Foobar Hello");
        var parser = alternateParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("World", item);
    }

    @Test
    public void testAlternateThirdSuccess() {
        var stream = new StringStream("Foobar Hello World");
        var parser = alternateParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Foobar", item);
    }

    @Test
    public void testAlternateFailed() {
        var stream = new StringStream("Alpha Hello World Foobar");
        var parser = alternateParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }
}
