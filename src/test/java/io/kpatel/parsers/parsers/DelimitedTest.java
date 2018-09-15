package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class DelimitedTest {
    @Test
    public void testDelimitedZero() {
        StringStream stream = new StringStream("");
        Parser<String, String, Character> parser = delimitedString(
                sequence("Hello ", () -> "Cannot find Hello"),
                item(',', () -> "Cannot find Delimiter"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test()
    public void testDelimitedOne() {
        StringStream stream = new StringStream("Hello");
        Parser<String, String, Character> parser = delimitedString(
                sequence("Hello", () -> "Cannot find Hello"),
                item(',', () -> "Cannot find Delimiter"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test()
    public void testDelimitedMultiple() {
        StringStream stream = new StringStream("Hello,Hello,Hello");
        Parser<String, String, Character> parser = delimitedString(
                sequence("Hello", () -> "Cannot find Hello"),
                item(',', () -> "Cannot find Delimiter"));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HelloHelloHello", item);
    }
}
