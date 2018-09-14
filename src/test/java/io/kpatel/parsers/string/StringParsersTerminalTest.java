package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringParsersTerminalTest {
    @Test
    public void testCharacterSuccess() {
        StringParserStream stream = new StringParserStream("Hello");
        StringParser<Character> parser = StringParsers.character(
                Character::isAlphabetic,
                () -> "Cannot Match Alphabetic Character");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test(expected = ParserError.class)
    public void testCharacterFailure() {
        StringParserStream stream = new StringParserStream("Hello");
        StringParser<Character> parser = StringParsers.character(
                Character::isDigit,
                () -> "Cannot Match Digit Character");
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();
    }

    @Test
    public void testTerminalSequenceSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = StringParsers.string("Hello");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testTerminalSequenceFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = StringParsers.string("Helper");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();
    }
}
