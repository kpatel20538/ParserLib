package io.kpatel.parsers.parsers;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.prebuilt.AffixParsers;
import io.kpatel.parsers.prebuilt.TerminalParsers;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AffixTest {
    @Test
    public void testPrefixSuccess() {
        var stream = new StringStream("int x");
        Parser<Character, String, Character> parser = AffixParsers.prefix(
                TerminalParsers.sequence("int ", () -> "Cannot Find Identifier"),
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testPrefixFailurePrefix() {
        var stream = new StringStream("float x");
        Parser<Character, String, Character> parser = AffixParsers.prefix(
                TerminalParsers.sequence("int ", () -> "Cannot Find Identifier"),
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPrefixFailureRoot() {
        var stream = new StringStream("int 1");
        Parser<Character, String, Character> parser = AffixParsers.prefix(
                TerminalParsers.sequence("int ", () -> "Cannot Find Identifier"),
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testPostfixSuccess() {
        var stream = new StringStream("x;");
        Parser<Character, String, Character> parser = AffixParsers.postfix(
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"),
                TerminalParsers.sequence(";", () -> "Cannot Find Terminator"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testPostfixFailurePostfix() {
        var stream = new StringStream("x,");
        Parser<Character, String, Character> parser = AffixParsers.postfix(
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"),
                TerminalParsers.sequence(";", () -> "Cannot Find Terminator"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testPostfixFailureRoot() {
        var stream = new StringStream("1;");
        Parser<Character, String, Character> parser = AffixParsers.postfix(
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"),
                TerminalParsers.sequence(";", () -> "Cannot Find Terminator"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testBetweenSuccess() {
        var stream = new StringStream("int x;");
        Parser<Character, String, Character> parser = AffixParsers.between(
                TerminalParsers.sequence("int ", () -> "Cannot Find Identifier"),
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"),
                TerminalParsers.sequence(";", () -> "Cannot Find Terminator"));
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('x'), item);
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailurePrefix() {
        var stream = new StringStream("float x;");
        Parser<Character, String, Character> parser = AffixParsers.between(
                TerminalParsers.sequence("int ", () -> "Cannot Find Identifier"),
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"),
                TerminalParsers.sequence(";", () -> "Cannot Find Terminator"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailurePostfix() {
        var stream = new StringStream("int x,");
        Parser<Character, String, Character> parser = AffixParsers.between(
                TerminalParsers.sequence("int ", () -> "Cannot Find Identifier"),
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"),
                TerminalParsers.sequence(";", () -> "Cannot Find Terminator"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test(expected = ParserError.class)
    public void testBetweenFailureRoot() {
        var stream = new StringStream("int 1;");
        Parser<Character, String, Character> parser = AffixParsers.between(
                TerminalParsers.sequence("int ", () -> "Cannot Find Identifier"),
                TerminalParsers.item(Character::isLetter, () -> "Cannot Find Letter"),
                TerminalParsers.sequence(";", () -> "Cannot Find Terminator"));
        var result = parser.parse(stream);

        result.getOrThrow();
    }
}
