package io.kpatel.parsers.list;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Parsers;
import io.kpatel.parsers.string.StringParsers;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.kpatel.parsers.Parsers.endOfStream;
import static io.kpatel.parsers.Parsers.postfix;

/**
 * WHAT: Specialized Helper Function for Parsing List of Tokens
 * NEEDS:
 * - Specialize the Generic Helper Functions
 * - Provide List Specific Parser Composing Functions
 */
public final class ListParsers {
    /**
     * WHY: Prevent un-necessary instances of helper class
     */
    private ListParsers() {
    }

    /**
     * WHAT: Helper Function to parse a full string
     */
    public static <T, Tkn> T runParser(
            Parser<T, List<Tkn>, Tkn> parser,
            List<Tkn> sequence) {
        return postfix(parser, endOfStream())
                .parse(new ListParserStream<>(sequence))
                .getOrThrow();
    }

    /**
     * WHAT: Parse a Token that satisfy a given predicate
     *
     * @see Parsers#terminalItem
     */
    public static <Tkn> Parser<Tkn, List<Tkn>, Tkn> token(
            Predicate<Tkn> predicate,
            Supplier<String> errorMessage) {
        return Parsers.terminalItem(predicate, errorMessage);
    }

    /**
     * WHAT: Parse the given Token by Token.equals
     *
     * @see StringParsers#character(Predicate, Supplier)
     */
    public static <Tkn> Parser<Tkn, List<Tkn>, Tkn> token(
            Tkn target, Supplier<String> errorMessage) {
        return Parsers.terminalItem(target::equals, errorMessage);
    }

    /**
     * WHAT: Parse Any Token from the collection by Token.equals
     *
     * @see StringParsers#character(Predicate, Supplier)
     */
    public static <Tkn> Parser<Tkn, List<Tkn>, Tkn> token(
            Collection<Tkn> tokens, Supplier<String> errorMessage) {
        return Parsers.terminalItem(tokens, errorMessage);
    }

    /**
     * WHAT: Parse List of Tokens by Token.equals
     *
     * @see Parsers#terminalSequence(Object, Function, Supplier)
     */
    public static <Tkn> Parser<List<Tkn>, List<Tkn>, Tkn> sequence(
            List<Tkn> sequence, Supplier<String> errorMessage) {
        return Parsers.terminalSequence(sequence, List::size, errorMessage);
    }

    /**
     * WHAT: Parse a run of Tokens that satisfy a given predicate, Will fail is nothing is found
     */
    public static <Tkn> Parser<List<Tkn>, List<Tkn>, Tkn> run(
            Predicate<Tkn> predicate,
            Supplier<String> errorMessage) {
        return Parsers.terminalRun(predicate, List::size, errorMessage);
    }

    /**
     * WHAT: Parse a run of the given Token using Token.equals, Will fail is nothing is found
     */
    public static <Tkn> Parser<List<Tkn>, List<Tkn>, Tkn> run(
            Tkn target, Supplier<String> errorMessage) {
        return Parsers.terminalRun(target, List::size, errorMessage);
    }

    /**
     * WHAT: Parse a run of any Token from the give Collection using Token.equals, Will fail is nothing is found
     */
    public static <Tkn> Parser<List<Tkn>, List<Tkn>, Tkn> run(
            Collection<Tkn> tokens,
            Supplier<String> errorMessage) {
        return Parsers.terminalRun(tokens, List::size, errorMessage);
    }

    /**
     * WHAT:  Parse a run of Tokens that satisfy a given predicate, Will always succeed
     */
    public static <Tkn> Parser<List<Tkn>, List<Tkn>, Tkn> optionalRun(Predicate<Tkn> predicate) {
        return Parsers.terminalOptionalRun(predicate, List::size);
    }

    /**
     * WHAT: Parse a run of the given Token using Token.equals, Will always succeed
     */
    public static <Tkn> Parser<List<Tkn>, List<Tkn>, Tkn> optionalRun(Tkn target) {
        return Parsers.terminalOptionalRun(target, List::size);
    }

    /**
     * WHAT: Parse a run of any Token from the give Collection using Token.equals, Will always succeed
     */
    public static <Tkn> Parser<List<Tkn>, List<Tkn>, Tkn> optionalRun(Collection<Tkn> tokens) {
        return Parsers.terminalOptionalRun(tokens, List::size);
    }
}
