package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.ParserStream;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.string.StringStream;
import org.junit.Test;

import static io.kpatel.parsers.Parsers.*;
import static org.junit.Assert.assertEquals;

public class AffixTest {
    @Test
    public void testPrefixSuccess() {
        ParserStream<String, Character> stream = new StringStream("int x");
        Parser<Character, String, Character> parser = prefix(
                sequence("int ", () -> "Cannot Find Identifier"),
                item(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testPrefixFailurePrefix() {
        ParserStream<String, Character> stream = new StringStream("float x");
        Parser<Character, String, Character> parser = prefix(
                sequence("int ", () -> "Cannot Find Identifier"),
                item(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPrefixFailureRoot() {
        ParserStream<String, Character> stream = new StringStream("int 1");
        Parser<Character, String, Character> parser = prefix(
                sequence("int ", () -> "Cannot Find Identifier"),
                item(Character::isLetter, () -> "Cannot Find Letter"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testPostfixSuccess() {
        StringStream stream = new StringStream("x;");
        Parser<Character, String, Character> parser = postfix(
                item(Character::isLetter, () -> "Cannot Find Letter"),
                sequence(";", () -> "Cannot Find Terminator"));
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testPostfixFailurePostfix() {
        StringStream stream = new StringStream("x,");
        Parser<Character, String, Character> parser = postfix(
                item(Character::isLetter, () -> "Cannot Find Letter"),
                sequence(";", () -> "Cannot Find Terminator"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPostfixFailureRoot() {
        StringStream stream = new StringStream("1;");
        Parser<Character, String, Character> parser = postfix(
                item(Character::isLetter, () -> "Cannot Find Letter"),
                sequence(";", () -> "Cannot Find Terminator"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testBetweenSuccess() {
        StringStream stream = new StringStream("int x;");
        Parser<Character, String, Character> parser = between(
                sequence("int ", () -> "Cannot Find Identifier"),
                item(Character::isLetter, () -> "Cannot Find Letter"),
                sequence(";", () -> "Cannot Find Terminator"));
        Result<Character, ?> result = parser.parse(stream);

        Character item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailurePrefix() {
        StringStream stream = new StringStream("float x;");
        Parser<Character, String, Character> parser = between(
                sequence("int ", () -> "Cannot Find Identifier"),
                item(Character::isLetter, () -> "Cannot Find Letter"),
                sequence(";", () -> "Cannot Find Terminator"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailurePostfix() {
        StringStream stream = new StringStream("int x,");
        Parser<Character, String, Character> parser = between(
                sequence("int ", () -> "Cannot Find Identifier"),
                item(Character::isLetter, () -> "Cannot Find Letter"),
                sequence(";", () -> "Cannot Find Terminator"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailureRoot() {
        StringStream stream = new StringStream("int 1;");
        Parser<Character, String, Character> parser = between(
                sequence("int ", () -> "Cannot Find Identifier"),
                item(Character::isLetter, () -> "Cannot Find Letter"),
                sequence(";", () -> "Cannot Find Terminator"));
        Result<Character, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
