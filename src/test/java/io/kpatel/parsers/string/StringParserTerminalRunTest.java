package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.Result;
import org.junit.Test;

import static io.kpatel.parsers.string.StringParsers.nonEmptyRun;
import static io.kpatel.parsers.string.StringParsers.run;
import static org.junit.Assert.assertEquals;

public class StringParserTerminalRunTest {
    @Test
    public void testRunPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = run(Character::isLetter);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testRunPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = run(Character::isDigit);
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testRunCharacterSuccess() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        StringParser<String> parser = run('H');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test
    public void testRunCharacterFailure() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        StringParser<String> parser = run('W');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testRunCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = run("eHlo");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testRunCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = run("dlorW");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testNonEmptyRunPredicateSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = nonEmptyRun(Character::isLetter, () -> "Cannot Find a Letter");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testNonEmptyRunPredicateFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = nonEmptyRun(Character::isDigit, () -> "Cannot Find a Digit");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testNonEmptyRunCharacterSuccess() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        StringParser<String> parser = nonEmptyRun('H');
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test(expected = ParserError.class)
    public void testNonEmptyRunCharacterFailure() {
        StringParserStream stream = new StringParserStream("HHHHH WWWWW");
        StringParser<String> parser = nonEmptyRun('W');
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testNonEmptyRunCharacterSetSuccess() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = nonEmptyRun("eHlo", () -> "Cannot find letter for set");
        Result<String, ?> result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test(expected = ParserError.class)
    public void testNonEmptyRunCharacterSetFailure() {
        StringParserStream stream = new StringParserStream("Hello World");
        StringParser<String> parser = nonEmptyRun("dlorW", () -> "Cannot find letter for set");
        Result<String, ?> result = parser.parse(stream);

        result.getOrThrow();
    }
}
