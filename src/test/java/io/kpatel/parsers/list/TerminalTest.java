package io.kpatel.parsers.list;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.ParserError;
import io.kpatel.parsers.ParserStream;
import io.kpatel.parsers.Result;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static io.kpatel.parsers.list.ListParsers.*;
import static org.junit.Assert.assertEquals;

public class TerminalTest {
    @Test
    public void testTokenPredicateSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<Token, List<Token>, Token> parser = token(
                Token::isFlag, () -> "Cannot Match Flagged Token");
        Result<Token, ?> result = parser.parse(stream);

        Token item = result.getOrThrow();

        assertEquals(Token.H, item);
    }

    @Test(expected = ParserError.class)
    public void testTokenPredicateFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Predicate<Token> predicate = Token::isFlag;
        Parser<Token, List<Token>, Token> parser = token(
                predicate.negate(), () -> "Cannot Match Un-flagged Token");
        Result<Token, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTokenSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<Token, List<Token>, Token> parser = token(
                Token.H, () -> "Cannot Match H Token");
        Result<Token, ?> result = parser.parse(stream);

        Token item = result.getOrThrow();

        assertEquals(Token.H, item);
    }

    @Test(expected = ParserError.class)
    public void testTokenFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<Token, List<Token>, Token> parser = token(
                Token.W, () -> "Cannot Match W Token");
        Result<Token, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTokenSetSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<Token, List<Token>, Token> parser = token(
                Arrays.asList(Token.E, Token.H, Token.L, Token.O),
                () -> "Cannot Match Token in Set");
        Result<Token, ?> result = parser.parse(stream);

        Token item = result.getOrThrow();

        assertEquals(Token.H, item);
    }

    @Test(expected = ParserError.class)
    public void testTokenSetFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<Token, List<Token>, Token> parser = token(
                Arrays.asList(Token.D, Token.L, Token.O, Token.R, Token.W),
                () -> "Cannot Match Token in Set");
        Result<Token, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTokenSequenceSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<List<Token>, List<Token>, Token> parser = sequence(
                Arrays.asList(Token.H, Token.E, Token.L, Token.L, Token.O),
                () -> "Cannot Match Token Sequence");
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Arrays.asList(Token.H, Token.E, Token.L, Token.L, Token.O), item);
    }

    @Test(expected = ParserError.class)
    public void testTokenSequenceFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<List<Token>, List<Token>, Token> parser = sequence(
                Arrays.asList(Token.H, Token.E, Token.L, Token.P, Token.E, Token.R),
                () -> "Cannot Match Token Sequence");
        Result<List<Token>, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTokenOptionalRunPredicateSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<List<Token>, List<Token>, Token> parser = optionalRun(Token::isFlag);
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Arrays.asList(Token.H, Token.E, Token.L, Token.L, Token.O), item);
    }

    @Test
    public void testTokenOptionalRunPredicateFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Predicate<Token> predicate = Token::isFlag;
        Parser<List<Token>, List<Token>, Token> parser = optionalRun(predicate.negate());
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Collections.emptyList(), item);
    }

    @Test
    public void testTokenOptionalRunCharacterSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.H, Token.H, Token.H, Token.H, Token.$,
                Token.W, Token.W, Token.W, Token.W, Token.W));
        Parser<List<Token>, List<Token>, Token> parser = optionalRun(Token.H);
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Arrays.asList(Token.H, Token.H, Token.H, Token.H, Token.H), item);
    }

    @Test
    public void testTokenOptionalRunCharacterFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.H, Token.H, Token.H, Token.H, Token.$,
                Token.W, Token.W, Token.W, Token.W, Token.W));
        Parser<List<Token>, List<Token>, Token> parser = optionalRun(Token.W);
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Collections.emptyList(), item);
    }

    @Test
    public void testTokenOptionalRunCharacterSetSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<List<Token>, List<Token>, Token> parser = optionalRun(
                Arrays.asList(Token.E, Token.H, Token.L, Token.O));
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Arrays.asList(Token.H, Token.E, Token.L, Token.L, Token.O), item);
    }

    @Test
    public void testTokenOptionalRunCharacterSetFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<List<Token>, List<Token>, Token> parser = optionalRun(
                Arrays.asList(Token.D, Token.L, Token.O, Token.R, Token.D));
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Collections.emptyList(), item);
    }

    @Test
    public void testTokenRunPredicateSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<List<Token>, List<Token>, Token> parser = run(
                Token::isFlag, () -> "Cannot Match Flagged Token");
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Arrays.asList(Token.H, Token.E, Token.L, Token.L, Token.O), item);
    }

    @Test(expected = ParserError.class)
    public void testTokenRunPredicateFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Predicate<Token> predicate = Token::isFlag;
        Parser<List<Token>, List<Token>, Token> parser = run(
                predicate.negate(), () -> "Cannot Match Un-Flagged Token");
        Result<List<Token>, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTokenRunCharacterSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.H, Token.H, Token.H, Token.H, Token.$,
                Token.W, Token.W, Token.W, Token.W, Token.W));
        Parser<List<Token>, List<Token>, Token> parser = run(
                Token.H, () -> "Cannot Find H Token");
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Arrays.asList(Token.H, Token.H, Token.H, Token.H, Token.H), item);
    }

    @Test(expected = ParserError.class)
    public void testTokenRunCharacterFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.H, Token.H, Token.H, Token.H, Token.$,
                Token.W, Token.W, Token.W, Token.W, Token.W));
        Parser<List<Token>, List<Token>, Token> parser = run(
                Token.W, () -> "Cannot Find W Token");
        Result<List<Token>, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    @Test
    public void testTokenRunCharacterSetSuccess() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<List<Token>, List<Token>, Token> parser = run(
                Arrays.asList(Token.E, Token.H, Token.L, Token.O),
                () -> "Cannot march Token From Set");
        Result<List<Token>, ?> result = parser.parse(stream);

        List<Token> item = result.getOrThrow();

        assertEquals(Arrays.asList(Token.H, Token.E, Token.L, Token.L, Token.O), item);
    }

    @Test(expected = ParserError.class)
    public void testTokenRunCharacterSetFailure() {
        ParserStream<List<Token>, Token> stream = new ListParserStream<>(Arrays.asList(
                Token.H, Token.E, Token.L, Token.L, Token.O, Token.$,
                Token.W, Token.O, Token.R, Token.L, Token.D));
        Parser<List<Token>, List<Token>, Token> parser = run(
                Arrays.asList(Token.D, Token.L, Token.O, Token.R, Token.W),
                () -> "Cannot march Token From Set");
        Result<List<Token>, ?> result = parser.parse(stream);

        result.getOrThrow();
    }

    private enum Token {
        D(true), E(true), H(true), L(true),
        O(true), P(true), R(true), W(true),
        $(false);

        boolean flag;

        Token(boolean flag) {
            this.flag = flag;
        }

        public boolean isFlag() {
            return flag;
        }
    }
}
