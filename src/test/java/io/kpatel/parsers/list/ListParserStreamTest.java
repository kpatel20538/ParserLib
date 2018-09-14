package io.kpatel.parsers.list;

import io.kpatel.parsers.ParserStream;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class ListParserStreamTest {
    @Test
    public void testLeadingItemPresent() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O));

        Optional<Token> token = stream.getLeadingItem();

        assertTrue(token.isPresent());
        assertEquals(Token.H, token.get());
    }

    @Test
    public void testLeadingItemEmpty() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(
                Collections.emptyList());

        Optional<Token> token = stream.getLeadingItem();

        assertFalse(token.isPresent());
    }

    @Test
    public void testNonEmptyStream() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O));

        assertFalse(stream.atEndOfStream());
    }

    @Test
    public void testEmptyStream() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(
                Collections.emptyList());

        assertTrue(stream.atEndOfStream());
    }

    @Test
    public void testLeadingSequence() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        List<Token> sequence = stream.getLeadingSequence(5);

        assertEquals(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O),
                sequence);
    }

    @Test
    public void testLargeLeadingSequence() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(
                Arrays.asList(
                        Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                        Token.W, Token.O, Token.R, Token.L, Token.D));

        List<Token> sequence = stream.getLeadingSequence(100);

        assertEquals(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D),
                sequence);
    }

    @Test
    public void testNegativeLeadingSequence() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        List<Token> sequence = stream.getLeadingSequence(-100);

        assertEquals(Collections.emptyList(), sequence);
    }

    @Test
    public void testLeadingRun() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        List<Token> run = stream.getLeadingRun(Token::isFlag);

        assertEquals(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O),
                run);
    }

    @Test
    public void testEmptyLeadingRun() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Predicate<Token> flagPredicate = Token::isFlag;

        List<Token> run = stream.getLeadingRun(flagPredicate.negate());

        assertEquals(Collections.emptyList(), run);
    }

    @Test
    public void testLeadingRunOnEmptyStream() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(
                Collections.emptyList());

        List<Token> run = stream.getLeadingRun(Token::isFlag);

        assertEquals(Collections.emptyList(), run);
    }

    @Test
    public void testJump() {
        ParserStream<List<Token>, Token> stream1 = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        ParserStream<List<Token>, Token> stream2 = stream1.jump(6);

        Optional<Token> leading1 = stream1.getLeadingItem();
        Optional<Token> leading2 = stream2.getLeadingItem();
        assertTrue(leading1.isPresent());
        assertTrue(leading2.isPresent());
        assertEquals(Token.H, leading1.get());
        assertEquals(Token.W, leading2.get());
    }

    @Test
    public void testLargeJump() {
        ParserStream<List<Token>, Token> stream1 = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        ParserStream<List<Token>, Token> stream2 = stream1.jump(100);

        Optional<Token> leading1 = stream1.getLeadingItem();
        assertTrue(leading1.isPresent());
        assertEquals(Token.H, leading1.get());
        assertTrue(stream2.atEndOfStream());
    }

    @Test
    public void testNegativeJump() {
        ParserStream<List<Token>, Token> stream1 = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));

        ParserStream<List<Token>, Token> stream2 = stream1.jump(-100);

        Optional<Token> leading1 = stream1.getLeadingItem();
        Optional<Token> leading2 = stream2.getLeadingItem();
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
