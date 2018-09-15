package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserStream;
import io.kpatel.parsers.SequenceHolder;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class StringStreamTest {
    @Test
    public void testLeadingItemPresent() {
        StringStream stream = new StringStream("Hello");

        Optional<Character> character = stream.getLeadingItem();

        assertTrue(character.isPresent());
        assertEquals(Character.valueOf('H'), character.get());
    }

    @Test
    public void testLeadingItemEmpty() {
        StringStream stream = new StringStream("");

        Optional<Character> character = stream.getLeadingItem();

        assertFalse(character.isPresent());
    }

    @Test
    public void testNonEmptyStream() {
        StringStream stream = new StringStream("Hello");

        assertFalse(stream.atEndOfStream());
    }

    @Test
    public void testEmptyStream() {
        StringStream stream = new StringStream("");

        assertTrue(stream.atEndOfStream());
    }

    @Test
    public void testLeadingSequence() {
        StringStream stream = new StringStream("Hello World");

        SequenceHolder<String> sequence = stream.getLeadingSequence(5);

        assertEquals("Hello", sequence.getSequence());
        assertEquals(5, sequence.getLength());
    }

    @Test
    public void testLargeLeadingSequence() {
        StringStream stream = new StringStream("Hello World");

        SequenceHolder<String> sequence = stream.getLeadingSequence(100);

        assertEquals("Hello World", sequence.getSequence());
        assertEquals(11, sequence.getLength());
    }

    @Test
    public void testNegativeLeadingSequence() {
        StringStream stream = new StringStream("Hello World");

        SequenceHolder<String> sequence = stream.getLeadingSequence(-100);

        assertEquals("", sequence.getSequence());
        assertEquals(0, sequence.getLength());
    }

    @Test
    public void testLeadingRun() {
        StringStream stream = new StringStream("Hello World");

        SequenceHolder<String> run = stream.getLeadingRun(Character::isAlphabetic);

        assertEquals("Hello", run.getSequence());
        assertEquals(5, run.getLength());
    }

    @Test
    public void testEmptyLeadingRun() {
        StringStream stream = new StringStream("Hello World");

        SequenceHolder<String> run = stream.getLeadingRun(Character::isDigit);

        assertEquals("", run.getSequence());
        assertEquals(0, run.getLength());
    }

    @Test
    public void testLeadingRunOnEmptyStream() {
        StringStream stream = new StringStream("");

        SequenceHolder<String> run = stream.getLeadingRun(Character::isAlphabetic);

        assertEquals("", run.getSequence());
        assertEquals(0, run.getLength());
    }

    @Test
    public void testJump() {
        StringStream stream1 = new StringStream("Hello World");

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
        StringStream stream1 = new StringStream("Hello World");

        ParserStream<String, Character> stream2 = stream1.jump(100);

        Optional<Character> leading1 = stream1.getLeadingItem();
        assertTrue(leading1.isPresent());
        assertEquals(Character.valueOf('H'), leading1.get());
        assertTrue(stream2.atEndOfStream());
    }

    @Test
    public void testNegativeJump() {
        StringStream stream1 = new StringStream("Hello World");

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
        StringStream stream1 = new StringStream("Hello World");

        StringStream stream2 = (StringStream) stream1.jump(6);

        assertEquals(0, stream1.getColumnNumber());
        assertEquals(6, stream2.getColumnNumber());
    }

    @Test
    public void testLineColumnJump() {
        StringStream stream1 = new StringStream("Hello\nWorld\nFoobar");

        StringStream stream2 = (StringStream) stream1.jump(100);

        assertEquals(1, stream1.getLineNumber());
        assertEquals(0, stream1.getColumnNumber());
        assertEquals(3, stream2.getLineNumber());
        assertEquals(6, stream2.getColumnNumber());
    }
}
