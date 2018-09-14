package io.kpatel.parsers.string;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.*;
import static org.junit.Assert.assertEquals;

public class TerminalTest {
    @Test
    public void testCharacterPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = character(
                Character::isAlphabetic,
                () -> "Cannot Match Alphabetic Character");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = character(
                Character::isDigit,
                () -> "Cannot Match Digit Character");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = character('H');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = character('W');
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = character("eHlo");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<Character, String, Character> parser = character("dlorW");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTerminalSequenceSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = string("Hello");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testTerminalSequenceFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = string("Helper");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testRunPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(Character::isLetter);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testRunPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = optionalRun(Character::isDigit);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testRunCharacterSuccess() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = optionalRun('H');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test
    public void testRunCharacterFailure() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = optionalRun('W');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testRunCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = optionalRun("eHlo");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testRunCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = optionalRun("dlorW");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testNonEmptyRunPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = run(Character::isLetter, () -> "Cannot Find a Letter");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testNonEmptyRunPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = run(Character::isDigit, () -> "Cannot Find a Digit");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testNonEmptyRunCharacterSuccess() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = run('H');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test(expected = ParserError.class)
    public void testNonEmptyRunCharacterFailure() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        Parser<String, String, Character> parser = run('W');
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testNonEmptyRunCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = run("eHlo");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testNonEmptyRunCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        Parser<String, String, Character> parser = run("dlorW");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
