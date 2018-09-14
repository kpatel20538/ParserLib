package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.ParserStream;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static io.kpatel.parsers.string.StringParsers.character;
import static io.kpatel.parsers.string.StringParsers.string;
import static org.junit.Assert.assertEquals;

public class AffixTest {
    @Test
    public void testPrefixSuccess() {
        ParserStream<String, Character> stream = new StringParserStream("int x");
        Parser<Character, String, Character> parser = prefix(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testPrefixFailurePrefix() {
        ParserStream<String, Character> stream = new StringParserStream("float x");
        Parser<Character, String, Character> parser = prefix(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPrefixFailureRoot() {
        ParserStream<String, Character> stream = new StringParserStream("int 1");
        Parser<Character, String, Character> parser = prefix(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testPostfixSuccess() {
        StringParserStream stream = new StringParserStream("x;");
        Parser<Character, String, Character> parser = postfix(
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testPostfixFailurePostfix() {
        StringParserStream stream = new StringParserStream("x,");
        Parser<Character, String, Character> parser = postfix(
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPostfixFailureRoot() {
        StringParserStream stream = new StringParserStream("1;");
        Parser<Character, String, Character> parser = postfix(
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testBetweenSuccess() {
        StringParserStream stream = new StringParserStream("int x;");
        Parser<Character, String, Character> parser = between(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailurePrefix() {
        StringParserStream stream = new StringParserStream("float x;");
        Parser<Character, String, Character> parser = between(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailurePostfix() {
        StringParserStream stream = new StringParserStream("int x,");
        Parser<Character, String, Character> parser = between(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailureRoot() {
        StringParserStream stream = new StringParserStream("int 1;");
        Parser<Character, String, Character> parser = between(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
