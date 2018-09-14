package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.*;
import static org.junit.Assert.assertEquals;

public class StringParsersAffixTest {
    @Test
    public void testPrefixSuccess() {
        StringParserStream stream = new StringParserStream("int x");
        StringParser<Character> parser = prefix(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testPrefixFailurePrefix() {
        StringParserStream stream = new StringParserStream("float x");
        StringParser<Character> parser = prefix(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPrefixFailureRoot() {
        StringParserStream stream = new StringParserStream("int 1");
        StringParser<Character> parser = prefix(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testPostfixSuccess() {
        StringParserStream stream = new StringParserStream("x;");
        StringParser<Character> parser = postfix(
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testPostfixFailurePostfix() {
        StringParserStream stream = new StringParserStream("x,");
        StringParser<Character> parser = postfix(
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPostfixFailureRoot() {
        StringParserStream stream = new StringParserStream("1;");
        StringParser<Character> parser = postfix(
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testBetweenSuccess() {
        StringParserStream stream = new StringParserStream("int x;");
        StringParser<Character> parser = between(
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
        StringParser<Character> parser = between(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailurePostfix() {
        StringParserStream stream = new StringParserStream("int x,");
        StringParser<Character> parser = between(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailureRoot() {
        StringParserStream stream = new StringParserStream("int 1;");
        StringParser<Character> parser = between(
                string("int "),
                character(Character::isLetter, () -> "Cannot Find Letter"),
                string(";"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
