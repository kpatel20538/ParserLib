package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserStream;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class StringParserStreamTest {
    @Test
    public void testLeadingItemPresent() {
        StringParserStream stream = new StringParserStream("Hello");

        Optional<Character> character = stream.getLeadingItem();

        assertTrue(character.isPresent());
        assertEquals(Character.valueOf('H'), character.get());
    }

    @Test
    public void testLeadingItemEmpty() {
        StringParserStream stream = new StringParserStream("");

        Optional<Character> character = stream.getLeadingItem();

        assertFalse(character.isPresent());
    }

    @Test
    public void testNonEmptyStream() {
        StringParserStream stream = new StringParserStream("Hello");

        assertFalse(stream.atEndOfStream());
    }

    @Test
    public void testEmptyStream() {
        StringParserStream stream = new StringParserStream("");

        assertTrue(stream.atEndOfStream());
    }

    @Test
    public void testLeadingSequence() {
        StringParserStream stream = new StringParserStream("Hello World");

        String sequence = stream.getLeadingSequence(5);

        assertEquals("Hello", sequence);
    }

    @Test
    public void testLargeLeadingSequence() {
        StringParserStream stream = new StringParserStream("Hello World");

        String sequence = stream.getLeadingSequence(100);

        assertEquals("Hello World", sequence);
    }

    @Test
    public void testNegativeLeadingSequence() {
        StringParserStream stream = new StringParserStream("Hello World");

        String sequence = stream.getLeadingSequence(-100);

        assertEquals("", sequence);
    }

    @Test
    public void testLeadingRun() {
        StringParserStream stream = new StringParserStream("Hello World");

        String run = stream.getLeadingRun(Character::isAlphabetic);

        assertEquals("Hello", run);
    }

    @Test
    public void testEmptyLeadingRun() {
        StringParserStream stream = new StringParserStream("Hello World");

        String run = stream.getLeadingRun(Character::isDigit);

        assertEquals("", run);
    }

    @Test
    public void testLeadingRunOnEmptyStream() {
        StringParserStream stream = new StringParserStream("");

        String run = stream.getLeadingRun(Character::isAlphabetic);

        assertEquals("", run);
    }

    @Test
    public void testJump() {
        StringParserStream stream1 = new StringParserStream("Hello World");

        ParserStream<String, Character> stream2 = stream1.jump(6);

        Optional<Character> leading1 = stream1.getLeadingItem();
        Optional<Character> leading2 = stream2.getLeadingItem();
        assertTrue(leading1.isPresent());
        assertTrue(leading2.isPresent());
        assertEquals(Character.valueOf('H'), leading1.get());
        assertEquals(Character.valueOf('W'), leading2.get());
    }

    @Test
    public void testLargeJump() {
        StringParserStream stream1 = new StringParserStream("Hello World");

        ParserStream<String, Character> stream2 = stream1.jump(100);

        Optional<Character> leading1 = stream1.getLeadingItem();
        assertTrue(leading1.isPresent());
        assertEquals(Character.valueOf('H'), leading1.get());
        assertTrue(stream2.atEndOfStream());
    }

    @Test
    public void testNegativeJump() {
        StringParserStream stream1 = new StringParserStream("Hello World");

        ParserStream<String, Character> stream2 = stream1.jump(-100);

        Optional<Character> leading1 = stream1.getLeadingItem();
        Optional<Character> leading2 = stream2.getLeadingItem();
        assertTrue(leading1.isPresent());
        assertTrue(leading2.isPresent());
        assertEquals(Character.valueOf('H'), leading1.get());
        assertEquals(Character.valueOf('H'), leading2.get());
    }

    @Test
    public void testColumnJump() {
        StringParserStream stream1 = new StringParserStream("Hello World");

        StringParserStream stream2 = (StringParserStream) stream1.jump(6);

        assertEquals(0, stream1.getColumnNumber());
        assertEquals(6, stream2.getColumnNumber());
    }

    @Test
    public void testLineColumnJump() {
        StringParserStream stream1 = new StringParserStream("Hello\nWorld\nFoobar");

        StringParserStream stream2 = (StringParserStream) stream1.jump(100);

        assertEquals(1, stream1.getLineNumber());
        assertEquals(0, stream1.getColumnNumber());
        assertEquals(3, stream2.getLineNumber());
        assertEquals(6, stream2.getColumnNumber());
    }
}
