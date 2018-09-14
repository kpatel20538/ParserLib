package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import java.util.Arrays;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class TerminalTest {
    @Test
    public void testCharacterPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = terminalItem(
                Character::isLetter,
                () -> "Cannot Match Letter Character");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = terminalItem(
                Character::isDigit,
                () -> "Cannot Match Digit Character");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = terminalItem(
                'H',
                () -> "Cannot find character 'H'");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = terminalItem(
                'W',
                () -> "Cannot find character 'W'");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = terminalItem(
                Arrays.asList('e', 'H', 'l', 'o'),
                () -> "Cannot find Letter from Set");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = terminalItem(
                Arrays.asList('d', 'l', 'o', 'r', 'W'),
                () -> "Cannot find Letter from Set");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTerminalSequenceSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalSequence(
                "Hello", String::length, () -> "Cannot find Hello");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testTerminalSequenceFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalSequence(
                "Helper", String::length, () -> "Cannot find Helper");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testOptionalRunPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalOptionalRun(
                Character::isLetter, String::length);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOptionalRunPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalOptionalRun(
                Character::isDigit, String::length);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOptionalRunCharacterSuccess() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = terminalOptionalRun(
                'H', String::length);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test
    public void testOptionalRunCharacterFailure() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = terminalOptionalRun(
                'W', String::length);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOptionalRunCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalOptionalRun(
                Arrays.asList('e', 'H', 'l', 'o'), String::length);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOptionalRunCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalOptionalRun(
                Arrays.asList('d', 'l', 'o', 'r', 'W'), String::length);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testRunPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalRun(
                Character::isLetter, String::length,
                () -> "Cannot Find Run of Letters");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testRunPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalRun(
                Character::isDigit, String::length,
                () -> "Cannot Find Run of Digits");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRunCharacterSuccess() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = terminalRun(
                'H', String::length,
                () -> "Cannot Find Run of Character W");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test(expected = ParserError.class)
    public void testRunCharacterFailure() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = terminalRun(
                'W', String::length,
                () -> "Cannot Find Run of Character W");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRunCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalRun(
                Arrays.asList('e', 'H', 'l', 'o'), String::length,
                () -> "Cannot Find Run of Character Set");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testRunCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = terminalRun(
                Arrays.asList('d', 'l', 'o', 'r', 'W'), String::length,
                () -> "Cannot Find Run of Character Set");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
