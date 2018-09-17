package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.AffixParsers.*;
import static io.kpatel.parsers.prebuilt.StringParsers.letter;
import static io.kpatel.parsers.prebuilt.TerminalParsers.sequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AffixTest {
    public Supplier<Parser<Character, String, Character>> prefixParser() {
        return prefix(
                sequence("int ", () -> "Cannot Find Identifier"),
                letter());
    }

    public Supplier<Parser<Character, String, Character>> suffixParser() {
        return suffix(
                letter(),
                sequence(";", () -> "Cannot Find Terminator"));
    }

    public Supplier<Parser<Character, String, Character>> betweenParser() {
        return between(
                sequence("int ", () -> "Cannot Find Identifier"),
                letter(),
                sequence(";", () -> "Cannot Find Terminator"));
    }

    @Test
    public void testPrefixSuccess() {
        var stream = new StringStream("int x");
        var result = prefixParser().get().parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test
    public void testPrefixFailurePrefix() {
        var stream = new StringStream("float x");
        var result = prefixParser().get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testPrefixFailureRoot() {
        var stream = new StringStream("int 1");
        var result = prefixParser().get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testSuffixSuccess() {
        var stream = new StringStream("x;");
        var result = suffixParser().get().parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test
    public void testSuffixFailurePostfix() {
        var stream = new StringStream("x,");
        var result = suffixParser().get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testSuffixFailureRoot() {
        var stream = new StringStream("1;");
        var result = suffixParser().get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testBetweenSuccess() {
        var stream = new StringStream("int x;");
        var result = betweenParser().get().parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test
    public void testBetweenFailurePrefix() {
        var stream = new StringStream("float x;");
        var result = betweenParser().get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testBetweenFailurePostfix() {
        var stream = new StringStream("int x,");
        var result = betweenParser().get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testBetweenFailureRoot() {
        var stream = new StringStream("int 1;");
        var result = betweenParser().get().parse(stream);

        assertFalse(result.isSuccess());
    }
}
