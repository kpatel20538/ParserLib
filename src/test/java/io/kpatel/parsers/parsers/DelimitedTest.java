package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.RepetitionParsers.delimitedString;
import static io.kpatel.parsers.prebuilt.TerminalParsers.item;
import static io.kpatel.parsers.prebuilt.TerminalParsers.sequence;
import static org.junit.Assert.assertEquals;

public class DelimitedTest {

    public Supplier<Parser<String, String, Character>> delimitedParser() {
        return delimitedString(
                sequence("Hello", () -> "Cannot find Hello"),
                item(',', () -> "Cannot find Delimiter"));
    }

    @Test
    public void testDelimitedZero() {
        var stream = new StringStream("");
        var parser = delimitedParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testDelimitedOne() {
        var stream = new StringStream("Hello");
        var parser = delimitedParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testDelimitedMultiple() {
        var stream = new StringStream("Hello,Hello,Hello");
        var parser = delimitedParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("HelloHelloHello", item);
    }
}
