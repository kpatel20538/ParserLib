package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.RepetitionParsers.concatenateString;
import static io.kpatel.parsers.prebuilt.TerminalParsers.sequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ConcatenateTest {
    public Supplier<Parser<String, String, Character>> concatenateParser() {
        return concatenateString(Arrays.asList(
                sequence("Hello ", () -> "Cannot find Hello"),
                sequence("World ", () -> "Cannot find World"),
                sequence("Foobar ", () -> "Cannot find Foobar")));
    }

    @Test
    public void testConcatStringSuccess() {
        var stream = new StringStream("Hello World Foobar ");
        var parser = concatenateParser().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello World Foobar ", item);
    }

    @Test
    public void testConcatStringFirstFailure() {
        var stream = new StringStream("Alpha World Foobar ");
        var parser = concatenateParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testConcatStringSecondFailure() {
        var stream = new StringStream("Hello Alpha Foobar ");
        var parser = concatenateParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testConcatStringThirdFailure() {
        var stream = new StringStream("Hello World Alpha ");
        var parser = concatenateParser().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }
}
