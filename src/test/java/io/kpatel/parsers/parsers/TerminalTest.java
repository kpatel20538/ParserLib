package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import java.util.Arrays;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class TerminalTest {
    @Test
    public void testCharacterPredicateSuccess() {
        var stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                Character::isLetter,
                () -> "Cannot Match Letter Character");
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterPredicateFailure() {
        var stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                Character::isDigit,
                () -> "Cannot Match Digit Character");
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSuccess() {
        var stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                'H',
                () -> "Cannot find character 'H'");
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterFailure() {
        var stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                'W',
                () -> "Cannot find character 'W'");
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSetSuccess() {
        var stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                Arrays.asList('e', 'H', 'l', 'o'),
                () -> "Cannot find Letter from Set");
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterSetFailure() {
        var stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                Arrays.asList('d', 'l', 'o', 'r', 'W'),
                () -> "Cannot find Letter from Set");
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTerminalSequenceSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = sequence(
                "Hello", () -> "Cannot find Hello");
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testTerminalSequenceFailure() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = sequence(
                "Helper", () -> "Cannot find Helper");
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testOptionalRunPredicateSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(Character::isLetter);
        var result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOptionalRunPredicateFailure() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(Character::isDigit);
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOptionalRunCharacterSuccess() {
        var stream = new StringStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = optionalRun('H');
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test
    public void testOptionalRunCharacterFailure() {
        var stream = new StringStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = optionalRun('W');
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOptionalRunCharacterSetSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(
                Arrays.asList('e', 'H', 'l', 'o'));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOptionalRunCharacterSetFailure() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(
                Arrays.asList('d', 'l', 'o', 'r', 'W'));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testRunPredicateSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = run(
                Character::isLetter, () -> "Cannot Find Run of Letters");
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testRunPredicateFailure() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = run(
                Character::isDigit, () -> "Cannot Find Run of Digits");
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRunCharacterSuccess() {
        var stream = new StringStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = run(
                'H', () -> "Cannot Find Run of Character W");
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test(expected = ParserError.class)
    public void testRunCharacterFailure() {
        var stream = new StringStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = run(
                'W', () -> "Cannot Find Run of Character W");
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRunCharacterSetSuccess() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = run(
                Arrays.asList('e', 'H', 'l', 'o'),
                () -> "Cannot Find Run of Character Set");
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testRunCharacterSetFailure() {
        var stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = run(
                Arrays.asList('d', 'l', 'o', 'r', 'W'),
                () -> "Cannot Find Run of Character Set");
        var result = parser.parse(stream);

        result.getOrThrow();
    }
}
