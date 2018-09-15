package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import java.util.Arrays;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class TerminalTest {
    @Test
    public void testCharacterPredicateSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                Character::isLetter,
                () -> "Cannot Match Letter Character");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterPredicateFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                Character::isDigit,
                () -> "Cannot Match Digit Character");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                'H',
                () -> "Cannot find character 'H'");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                'W',
                () -> "Cannot find character 'W'");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSetSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                Arrays.asList('e', 'H', 'l', 'o'),
                () -> "Cannot find Letter from Set");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterSetFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<Character, String, Character> parser = item(
                Arrays.asList('d', 'l', 'o', 'r', 'W'),
                () -> "Cannot find Letter from Set");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTerminalSequenceSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = sequence(
                "Hello", () -> "Cannot find Hello");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testTerminalSequenceFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = sequence(
                "Helper", () -> "Cannot find Helper");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testOptionalRunPredicateSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(Character::isLetter);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOptionalRunPredicateFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(Character::isDigit);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOptionalRunCharacterSuccess() {
        StringStream stream = new StringStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = optionalRun('H');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test
    public void testOptionalRunCharacterFailure() {
        StringStream stream = new StringStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = optionalRun('W');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOptionalRunCharacterSetSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(
                Arrays.asList('e', 'H', 'l', 'o'));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOptionalRunCharacterSetFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(
                Arrays.asList('d', 'l', 'o', 'r', 'W'));
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testRunPredicateSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = run(
                Character::isLetter, () -> "Cannot Find Run of Letters");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testRunPredicateFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = run(
                Character::isDigit, () -> "Cannot Find Run of Digits");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRunCharacterSuccess() {
        StringStream stream = new StringStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = run(
                'H', () -> "Cannot Find Run of Character W");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test(expected = ParserError.class)
    public void testRunCharacterFailure() {
        StringStream stream = new StringStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = run(
                'W', () -> "Cannot Find Run of Character W");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRunCharacterSetSuccess() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = run(
                Arrays.asList('e', 'H', 'l', 'o'),
                () -> "Cannot Find Run of Character Set");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testRunCharacterSetFailure() {
        StringStream stream = new StringStream("Hello World");
        Parser<String, String, Character> parser = run(
                Arrays.asList('d', 'l', 'o', 'r', 'W'),
                () -> "Cannot Find Run of Character Set");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
