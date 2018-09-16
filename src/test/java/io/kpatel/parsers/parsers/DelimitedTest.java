package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.prebuilt.RepetitionParsers;
import io.kpatel.parsers.prebuilt.TerminalParsers;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DelimitedTest {
    @Test
    public void testDelimitedZero() {
        var stream = new StringStream("");
        Parser<String, String, Character> parser = RepetitionParsers.delimitedString(
                TerminalParsers.sequence("Hello ", () -> "Cannot find Hello"),
                TerminalParsers.item(',', () -> "Cannot find Delimiter"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test()
    public void testDelimitedOne() {
        var stream = new StringStream("Hello");
        Parser<String, String, Character> parser = RepetitionParsers.delimitedString(
                TerminalParsers.sequence("Hello", () -> "Cannot find Hello"),
                TerminalParsers.item(',', () -> "Cannot find Delimiter"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test()
    public void testDelimitedMultiple() {
        var stream = new StringStream("Hello,Hello,Hello");
        Parser<String, String, Character> parser = RepetitionParsers.delimitedString(
                TerminalParsers.sequence("Hello", () -> "Cannot find Hello"),
                TerminalParsers.item(',', () -> "Cannot find Delimiter"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("HelloHelloHello", item);
    }
}
