package io.kpatel.parsers.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class ListStreamTest {
    @Test
    public void testLeadingItemPresent() {
        var stream = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O));

        var token = stream.getLeadingItem();

        assertTrue(token.isPresent());
        assertEquals(Token.H, token.get());
    }

    @Test
    public void testLeadingItemEmpty() {
        var stream = new ListStream<>(Collections.emptyList());

        var token = stream.getLeadingItem();

        assertFalse(token.isPresent());
    }

    @Test
    public void testNonEmptyStream() {
        var stream = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O));

        assertFalse(stream.atEndOfStream());
    }

    @Test
    public void testEmptyStream() {
        var stream = new ListStream<>(
                Collections.emptyList());

        assertTrue(stream.atEndOfStream());
    }

    @Test
    public void testLeadingSequence() {
        var stream = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        var sequence = stream.getLeadingSequence(5);

        assertEquals(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O),
                sequence.getSequence());
        assertEquals(5, sequence.getLength());
    }

    @Test
    public void testLargeLeadingSequence() {
        var stream = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        var sequence = stream.getLeadingSequence(100);

        assertEquals(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D),
                sequence.getSequence());
        assertEquals(11, sequence.getLength());
    }

    @Test
    public void testNegativeLeadingSequence() {
        var stream = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        var sequence = stream.getLeadingSequence(-100);

        assertEquals(Collections.emptyList(), sequence.getSequence());
        assertEquals(0, sequence.getLength());
    }

    @Test
    public void testLeadingRun() {
        var stream = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        var run = stream.getLeadingRun(Token::isFlag);

        assertEquals(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O),
                run.getSequence());
        assertEquals(5, run.getLength());
    }

    @Test
    public void testEmptyLeadingRun() {
        var stream = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        var flagPredicate = (Predicate<Token>) Token::isFlag;

        var run = stream.getLeadingRun(flagPredicate.negate());

        assertEquals(Collections.emptyList(), run.getSequence());
        assertEquals(0, run.getLength());
    }

    @Test
    public void testLeadingRunOnEmptyStream() {
        var stream = new ListStream<Token>(Collections.emptyList());

        var run = stream.getLeadingRun(Token::isFlag);

        assertEquals(Collections.emptyList(), run.getSequence());
        assertEquals(0, run.getLength());

    }

    @Test
    public void testJump() {
        var stream1 = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        var stream2 = stream1.jump(6);

        var leading1 = stream1.getLeadingItem();
        var leading2 = stream2.getLeadingItem();
        assertTrue(leading1.isPresent());
        assertTrue(leading2.isPresent());
        assertEquals(Token.H, leading1.get());
        assertEquals(Token.W, leading2.get());
    }

    @Test
    public void testLargeJump() {
        var stream1 = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        var stream2 = stream1.jump(100);

        var leading = stream1.getLeadingItem();
        assertTrue(leading.isPresent());
        assertEquals(Token.H, leading.get());
        assertTrue(stream2.atEndOfStream());
    }

    @Test
    public void testNegativeJump() {
        var stream1 = new ListStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        var stream2 = stream1.jump(-100);

        var leading1 = stream1.getLeadingItem();
        var leading2 = stream2.getLeadingItem();
        assertTrue(leading1.isPresent());
        assertTrue(leading2.isPresent());
        assertEquals(Token.H, leading1.get());
        assertEquals(Token.H, leading2.get());
    }

    private enum Token {
        D(true), E(true), H(true), L(true),
        O(true), R(true), W(true), $(false);

        boolean flag;

        Token(boolean flag) {
            this.flag = flag;
        }

        public boolean isFlag() {
            return flag;
        }
    }
}
