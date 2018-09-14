package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.character;
import static io.kpatel.parsers.string.StringParsers.string;
import static org.junit.Assert.assertEquals;

public class StringParsersTerminalTest {
    @Test
    public void testCharacterPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = character(
                Character::isAlphabetic,
                () -> "Cannot Match Alphabetic Character");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = character(
                Character::isDigit,
                () -> "Cannot Match Digit Character");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = character('H');
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = character('W');
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = character(
                "eHlo",
                () -> "Cannot find Letter from Set");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<Character> parser = character(
                "dlorW",
                () -> "Cannot find Letter from Set");
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTerminalSequenceSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = string("Hello");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testTerminalSequenceFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = string("Helper");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
