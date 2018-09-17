package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.prebuilt.AffixParsers;
import io.kpatel.parsers.prebuilt.MiscParsers;
import io.kpatel.parsers.prebuilt.TerminalParsers;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.Map;

import static io.kpatel.parsers.prebuilt.MiscParsers.*;
import static io.kpatel.parsers.prebuilt.StringParsers.letter;
import static org.junit.Assert.*;

public class MiscTest {
    public Parser<Recursive, String, Character> recursiveParser() {
        return pipe(
                letter(),
                optional(this::recursiveParser, Recursive::new),
                Recursive::new).get();
    }

    @Test
    public void testRecursiveSuccess() {
        var stream = new StringStream("abc");
        var parser = recursiveParser();
        var result = parser.parse(stream);

        Recursive item = result.getOrThrow();

        assertEquals(Character.valueOf('a'), item.character);
        assertEquals(Character.valueOf('b'), item.recursive.character);
        assertEquals(Character.valueOf('c'), item.recursive.recursive.character);
        assertNull(item.recursive.recursive.recursive.character);
    }

    @Test
    public void testRecursiveFailure() {
        var stream = new StringStream("123");
        var parser = recursiveParser();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testEndOfStreamSuccess() {
        var stream = new StringStream("");
        var parser = TerminalParsers.<String, Character>endOfStream().get();
        var result = parser.parse(stream);

        Object item = result.getOrThrow();

        assertNotNull(item);
    }

    @Test
    public void testEndOfStreamFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>endOfStream().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testPeekSuccess() {
        var stream = new StringStream("Hello World");
        var parser = AffixParsers.<String, String, String, Character>prefix(
                peek(TerminalParsers.sequence("Hello",
                        () -> "Cannot Find Hello")),
                TerminalParsers.sequence("Hello World",
                        () -> "Cannot Find Hello World")).get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test
    public void testPeekFailure() {
        var stream = new StringStream("Hello World");
        var parser = AffixParsers.<String, String, String, Character>prefix(
                peek(TerminalParsers.sequence("World",
                        () -> "Cannot Find Hello")),
                TerminalParsers.sequence("Hello World",
                        () -> "Cannot Find Hello World")).get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testExceptionSuccess() {
        var stream = new StringStream("Hello World");
        var parser = filter(
                TerminalParsers.<String, Character>sequence("Hello",
                        () -> "Cannot Find Hello"),
                str -> str.charAt(0) != 'W',
                () -> "Found a W Character").get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testExceptionFailureBase() {
        var stream = new StringStream("World Hello");
        var parser = filter(
                TerminalParsers.<String, Character>sequence("Hello",
                        () -> "Cannot Find Hello"),
                str -> str.charAt(0) != 'W',
                () -> "Found a W Character").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testExceptionFailureExcept() {
        var stream = new StringStream("World Hello");
        var parser = filter(
                TerminalParsers.<String, Character>sequence("World",
                        () -> "Cannot Find Hello"),
                str -> str.charAt(0) != 'W',
                () -> "Found a W Character").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testPipeSuccess() {
        var stream = new StringStream("Hello World");
        var parser = MiscParsers.<String, String, String, String, Character>pipe(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"),
                String::concat).get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello World", item);
    }

    @Test
    public void testPipeFailureLeft() {
        var stream = new StringStream("Alpha World");
        var parser = MiscParsers.<String, String, String, String, Character>pipe(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"),
                String::concat).get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testPipeFailureRight() {
        var stream = new StringStream("Hello Alpha");
        var parser = MiscParsers.<String, String, String, String, Character>pipe(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World"),
                String::concat).get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testEntrySuccess() {
        var stream = new StringStream("Hello World");
        var parser = MiscParsers.<String, String, String, Character>entry(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World")).get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Map.entry("Hello ", "World"), item);
    }

    @Test
    public void testEntryFailureKey() {
        var stream = new StringStream("Alpha World");
        var parser = MiscParsers.<String, String, String, Character>entry(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World")).get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void tesEntryFailureValue() {
        var stream = new StringStream("Hello Alpha");
        var parser = MiscParsers.<String, String, String, Character>entry(
                TerminalParsers.sequence("Hello ",
                        () -> "Cannot Find Hello"),
                TerminalParsers.sequence("World",
                        () -> "Cannot Find World")).get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    class Recursive {
        Character character;
        Recursive recursive;

        Recursive() {
            recursive = null;
        }

        Recursive(Character character, Recursive recursive) {
            this.character = character;
            this.recursive = recursive;
        }
    }
}
