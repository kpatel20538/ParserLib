package io.kpatel.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * WHY:  Provide high-level generic parser composition operations
 * NEEDS:
 * - Top-Level Stateless Operations to combine Parsers
 * TOOLS:
 * - pure static functions
 * - Singleton Pattern (No Instance Variant)
 * - final class
 */
public final class Parsers {
    /**
     * WHY: Prevent un-necessary instances of helper class
     */
    private Parsers() {

    }

    /**
     * WHAT: Peek ahead and parse an item without changing the stream.
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm> peek(Parser<T, Strm, Seq, Itm> parser) {
        return stream1 -> parser.parse(stream1)
                .chain((item, stream2) -> Result.success(item, stream1));
    }

    /**
     * WHAT: A Parser that fails if their is still input to take in.
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Void, Strm, Seq, Itm> endOfStream() {
        return stream -> stream.atEndOfStream()
                ? Result.success(null, stream)
                : Result.failure("Expected End of Stream", stream);
    }

    /**
     * WHAT: Parse until one parse succeeds or all parsers fail
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm> alternate(List<? extends Parser<T, Strm, Seq, Itm>> parsers) {
        ArrayList<? extends Parser<T, Strm, Seq, Itm>> parserList = new ArrayList<>(parsers);
        return stream -> {
            Result<T, Strm> result = Result.failure("No Parsers To Alternate with.", stream);
            for (Parser<T, Strm, Seq, Itm> parser : parserList) {
                result = result.orElse(() -> parser.parse(stream));
            }
            return result;
        };
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty string
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<String, Strm, Seq, Itm> concatenateString(
            List<? extends Parser<String, Strm, Seq, Itm>> parsers) {
        return Parsers.concatenate(StringBuilder::append, StringBuilder::new, parsers)
                .map(StringBuilder::toString);
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty list
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<List<T>, Strm, Seq, Itm> concatenateList(
            List<? extends Parser<T, Strm, Seq, Itm>> parsers) {
        return Parsers.concatenate(
                (list, item) -> {
                    list.add(item);
                    return list;
                },
                () -> new ArrayList<T>(),
                parsers).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty case
     */
    public static <T, Bld, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Bld, Strm, Seq, Itm> concatenate(
            BiFunction<Bld, T, Bld> reduce, Supplier<Bld> empty,
            List<? extends Parser<T, Strm, Seq, Itm>> parsers) {
        ArrayList<? extends Parser<T, Strm, Seq, Itm>> parserList = new ArrayList<>(parsers);
        return stream -> {
            Result<Bld, Strm> result = Result.success(empty.get(), stream);
            for (Parser<T, Strm, Seq, Itm> parser : parserList) {
                result = result.chain((bld, remaining) -> parser.parse(remaining)
                        .map((itm, stream1) -> reduce.apply(bld, itm)));
            }
            return result;
        };
    }

    /**
     * WHAT: WHAT: Parse a parser until it fails and join the results to the empty string
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<String, Strm, Seq, Itm> zeroOrMoreString(
            Parser<String, Strm, Seq, Itm> parser) {
        return Parsers.zeroOrMore(StringBuilder::append, StringBuilder::new, parser)
                .map(StringBuilder::toString);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<List<T>, Strm, Seq, Itm> zeroOrMoreList(
            Parser<T, Strm, Seq, Itm> parser) {
        return Parsers.zeroOrMore(
                (list, item) -> {
                    list.add(item);
                    return list;
                },
                () -> new ArrayList<T>(),
                parser).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case
     */
    public static <T, Bld, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Bld, Strm, Seq, Itm> zeroOrMore(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            Parser<T, Strm, Seq, Itm> parser) {
        return stream -> {
            Result<Bld, Strm> prevResult = Result.success(empty.get(), stream);
            Result<Bld, Strm> result = prevResult;

            while (result.isSuccess()) {
                prevResult = result;
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> reduce.apply(bld, itm)));
            }
            return prevResult;
        };
    }

    /**
     * WHAT: WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<String, Strm, Seq, Itm> oneOrMoreString(
            Parser<String, Strm, Seq, Itm> parser) {
        return oneOrMore(StringBuilder::append, StringBuilder::new, parser)
                .map(StringBuilder::toString);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<List<T>, Strm, Seq, Itm> oneOrMoreList(
            Parser<T, Strm, Seq, Itm> parser) {
        return oneOrMore(
                (list, item) -> {
                    list.add(item);
                    return list;
                },
                () -> new ArrayList<T>(),
                parser).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <T, Bld, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Bld, Strm, Seq, Itm> oneOrMore(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            Parser<T, Strm, Seq, Itm> parser) {
        return parser.chain(item -> zeroOrMore(reduce, () -> reduce.apply(empty.get(), item), parser));
    }

    /**
     * WHAT: WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<String, Strm, Seq, Itm> delimitedString(
            Parser<String, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> delimiter) {
        return delimited(StringBuilder::append, StringBuilder::new, parser, delimiter)
                .map(StringBuilder::toString);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<List<T>, Strm, Seq, Itm> delimitedList(
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> delimiter) {
        return delimited(
                (list, item) -> {
                    list.add(item);
                    return list;
                },
                () -> new ArrayList<T>(),
                parser, delimiter
        ).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <T, Bld, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Bld, Strm, Seq, Itm> delimited(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> delimiter) {
        return optional(parser.chain(item -> zeroOrMore(reduce, () -> reduce.apply(empty.get(), item), prefix(delimiter, parser))), empty);
    }


    /**
     * WHAT: Parse an item with a prefix and omit the prefix
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm> prefix(
            Parser<?, Strm, Seq, Itm> before,
            Parser<T, Strm, Seq, Itm> parser) {
        return before.chain(pre -> parser);
    }

    /**
     * WHAT: Parse an item with a postfix and omit the postfix
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm> postfix(
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> after) {
        return parser.chain(item -> after.map(post -> item));
    }

    /**
     * WHAT: Parse an item with a prefix and a postfix and omit both the prefix and the postfix
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm> between(
            Parser<?, Strm, Seq, Itm> before,
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> after) {
        return before.chain(pre -> parser.chain(item -> after.map(post -> item)));
    }


    /**
     * WHAT: Attempt to parser a string or else yield an empty String
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<String, Strm, Seq, Itm> optional(Parser<String, Strm, Seq, Itm> parser) {
        return optional(parser, () -> "");
    }

    /**
     * WHAT: Attempt to parser an item or else yield a placeholder
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm> optional(Parser<T, Strm, Seq, Itm> parser, Supplier<T> placeholder) {
        return stream -> parser.parse(stream)
                .orElse(() -> Result.success(placeholder.get(), stream));
    }

    /**
     * WHAT: Parse an string an omit it
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<String, Strm, Seq, Itm> omit(Parser<String, Strm, Seq, Itm> parser) {
        return omit(parser, () -> "");
    }

    /**
     * WHAT: Parse an item an omit it
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm> omit(Parser<T, Strm, Seq, Itm> parser, Supplier<T> placeholder) {
        return parser.map(item -> placeholder.get());
    }

    /**
     * WHAT: Parse an item that satisfy a given predicate
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Itm, Strm, Seq, Itm> terminalItem(
            Predicate<Itm> predicate,
            Supplier<String> errorMessage) {
        return stream -> stream.getLeadingItem()
                .filter(predicate)
                .map(i -> Result.success(i, stream.jump(1)))
                .orElseGet(() -> Result.failure(errorMessage.get(), stream));
    }

    /**
     * WHAT: Parse as sequence of items
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Seq, Strm, Seq, Itm> terminalSequence(
            Seq sequence,
            Function<Seq, Integer> measureLength,
            Supplier<String> errorMessage) {
        return stream -> {
            int size = measureLength.apply(sequence);
            Seq leading = stream.getLeadingSequence(size);
            return leading.equals(sequence)
                    ? Result.success(leading, stream.jump(size))
                    : Result.failure(errorMessage.get(), stream);
        };
    }

    /**
     * WHAT: Parse a run of item that satisfy a given predicate, Will always succeed
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Seq, Strm, Seq, Itm> terminalRun(
            Predicate<Itm> predicate,
            Function<Seq, Integer> measureLength) {
        return stream -> {
            Seq run = stream.getLeadingRun(predicate);
            int size = measureLength.apply(run);
            return Result.success(run, stream.jump(size));
        };
    }
}
