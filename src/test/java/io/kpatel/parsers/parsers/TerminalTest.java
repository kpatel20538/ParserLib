package io.kpatel.parsers.parsers;

import io.kpatel.parsers.prebuilt.TerminalParsers;
import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TerminalTest {
    @Test
    public void testAnyItemSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>item().get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testAnyItemFailure() {
        var stream = new StringStream("");
        var parser = TerminalParsers.<String, Character>item().get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testItemPredicateSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                item(Character::isLetter, () -> "Cannot Match Letter Character").get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testItemPredicateFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                item(Character::isDigit, () -> "Cannot Match Digit Character").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testItemSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                item('H', () -> "Cannot find character 'H'").get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testItemFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                item('W', () -> "Cannot find character 'W'").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testItemSetSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                item(List.of('e', 'H', 'l', 'o'),
                () -> "Cannot find Letter from Set").get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testItemSetFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                item(List.of('d', 'l', 'o', 'r', 'W'),
                () -> "Cannot find Letter from Set").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testSequenceSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                sequence("Hello", () -> "Cannot find Hello").get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testSequenceFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                sequence("Helper", () -> "Cannot find Helper").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testOptionalRunPredicateSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                optionalRun(Character::isLetter).get();
        var result = parser.parse(stream);

        String item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOptionalRunPredicateFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                optionalRun(Character::isDigit).get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOptionalRunItemSuccess() {
        var stream = new StringStream("HHHHH WWWWW");
        var parser = TerminalParsers.<String, Character>
                optionalRun('H').get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test
    public void testOptionalRunItemFailure() {
        var stream = new StringStream("HHHHH WWWWW");
        var parser = TerminalParsers.<String, Character>
                optionalRun('W').get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testOptionalRunItemSetSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                optionalRun(List.of('e', 'H', 'l', 'o')).get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testOptionalRunItemSetFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                optionalRun(List.of('d', 'l', 'o', 'r', 'W')).get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("", item);
    }

    @Test
    public void testRunPredicateSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                run(Character::isLetter, () -> "Cannot Find Run of Letters").get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testRunPredicateFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                run(Character::isDigit, () -> "Cannot Find Run of Digits").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testRunCharacterSuccess() {
        var stream = new StringStream("HHHHH WWWWW");
        var parser = TerminalParsers.<String, Character>
                run('H', () -> "Cannot Find Run of Character W").get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("HHHHH", item);
    }

    @Test
    public void testRunItemFailure() {
        var stream = new StringStream("HHHHH WWWWW");
        var parser = TerminalParsers.<String, Character>
                run('W', () -> "Cannot Find Run of Character W").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testRunItemSetSuccess() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                run(List.of('e', 'H', 'l', 'o'),
                () -> "Cannot Find Run of Character Set").get();
        var result = parser.parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testRunItemSetFailure() {
        var stream = new StringStream("Hello World");
        var parser = TerminalParsers.<String, Character>
                run(List.of('d', 'l', 'o', 'r', 'W'),
                () -> "Cannot Find Run of Character Set").get();
        var result = parser.parse(stream);

        assertFalse(result.isSuccess());
    }
}
