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
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> peek(Parser<T, Seq, Itm> parser) {
        return stream1 -> parser.parse(stream1)
                .chain((item, stream2) -> Result.success(item, stream1));
    }

    /**
     * WHAT: A Parser that fails if their is still input to take in.
     */
    public static <Seq, Itm>
    Parser<Void, Seq, Itm> endOfStream() {
        return stream -> stream.atEndOfStream()
                ? Result.success(null, stream)
                : Result.failure("Expected End of Stream", stream);
    }

    /**
     * WHAT: Parse an item unless in falls in the exception case.
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> exception(
            Parser<T, Seq, Itm> parser,
            Predicate<T> except) {
        return stream -> parser.parse(stream).chain((item, stream1) ->
                except.test(item)
                        ? Result.failure("Parser result was in exception case", stream)
                        : Result.success(item, stream1)
        );
    }

    /**
     * WHAT: Parse until one parse succeeds or all parsers fail
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> alternate(List<? extends Parser<T, Seq, Itm>> parsers) {
        ArrayList<? extends Parser<T, Seq, Itm>> parserList = new ArrayList<>(parsers);
        return stream -> {
            Result<T, ParserStream<Seq, Itm>> result = Result.failure("No Parsers To Alternate with.", stream);
            for (Parser<T, Seq, Itm> parser : parserList) {
                result = result.orElse(() -> parser.parse(stream));
            }
            return result;
        };
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> concatenateString(
            List<? extends Parser<String, Seq, Itm>> parsers) {
        return Parsers.concatenate(
                StringBuilder::append,
                StringBuilder::new,
                parsers
        ).map(StringBuilder::toString);
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> concatenateList(
            List<? extends Parser<T, Seq, Itm>> parsers) {
        return Parsers.concatenate(
                Parsers::appendList,
                Parsers::<T>newList,
                parsers
        ).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty case
     */
    public static <T, Bld, Seq, Itm>
    Parser<Bld, Seq, Itm> concatenate(
            BiFunction<Bld, T, Bld> reduce, Supplier<Bld> empty,
            List<? extends Parser<T, Seq, Itm>> parsers) {
        ArrayList<? extends Parser<T, Seq, Itm>> parserList = new ArrayList<>(parsers);
        return stream -> {
            Result<Bld, ParserStream<Seq, Itm>> result = Result.success(empty.get(), stream);
            for (Parser<T, Seq, Itm> parser : parserList) {
                result = result.chain((bld, remaining) -> parser.parse(remaining)
                        .map((itm, stream1) -> reduce.apply(bld, itm)));
            }
            return result;
        };
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> zeroOrMoreString(
            Parser<String, Seq, Itm> parser) {
        return Parsers.zeroOrMore(
                StringBuilder::append,
                StringBuilder::new,
                parser
        ).map(StringBuilder::toString);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> zeroOrMoreList(
            Parser<T, Seq, Itm> parser) {
        return Parsers.zeroOrMore(
                Parsers::appendList,
                Parsers::<T>newList,
                parser
        ).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case
     */
    public static <T, Bld, Seq, Itm>
    Parser<Bld, Seq, Itm> zeroOrMore(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            Parser<T, Seq, Itm> parser) {
        return stream -> {
            Result<Bld, ParserStream<Seq, Itm>> prevResult = Result.success(empty.get(), stream);
            Result<Bld, ParserStream<Seq, Itm>> result = prevResult;

            while (result.isSuccess()) {
                prevResult = result;
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> reduce.apply(bld, itm)));
            }
            return prevResult;
        };
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> oneOrMoreString(
            Parser<String, Seq, Itm> parser) {
        return oneOrMore(
                StringBuilder::append,
                StringBuilder::new,
                parser
        ).map(StringBuilder::toString);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> oneOrMoreList(
            Parser<T, Seq, Itm> parser) {
        return oneOrMore(
                Parsers::appendList,
                Parsers::<T>newList,
                parser).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <T, Bld, Seq, Itm>
    Parser<Bld, Seq, Itm> oneOrMore(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            Parser<T, Seq, Itm> parser) {
        return parser.chain(item -> zeroOrMore(reduce, () -> reduce.apply(empty.get(), item), parser));
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> delimitedString(
            Parser<String, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        return delimited(
                StringBuilder::append,
                StringBuilder::new,
                parser, delimiter
        ).map(StringBuilder::toString);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> delimitedList(
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        return delimited(
                Parsers::appendList,
                Parsers::<T>newList,
                parser, delimiter
        ).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <T, Bld, Seq, Itm>
    Parser<Bld, Seq, Itm> delimited(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        return optional(parser.chain(item -> zeroOrMore(reduce, () -> reduce.apply(empty.get(), item), prefix(delimiter, parser))), empty);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them to a string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> repetitionString(
            int count,
            Parser<String, Seq, Itm> parser) {
        return repetition(
                count,
                StringBuilder::append,
                StringBuilder::new,
                parser
        ).map(StringBuilder::toString);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them to a list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> repetitionList(
            int count,
            Parser<T, Seq, Itm> parser) {
        return repetition(
                count,
                Parsers::appendList,
                Parsers::<T>newList,
                parser).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them together
     */
    public static <T, Bld, Seq, Itm>
    Parser<Bld, Seq, Itm> repetition(
            int count,
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            Parser<T, Seq, Itm> parser) {
        return stream -> {
            Result<Bld, ParserStream<Seq, Itm>> result = Result.success(empty.get(), stream);
            for (int i = 0; i < count && result.isSuccess(); i++) {
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> reduce.apply(bld, itm)));
            }
            return result;
        };
    }

    /**
     * WHAT: Parses a range of an item and joins them to a string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> repetitionString(
            int inclusiveLow, int inclusiveHigh,
            Parser<String, Seq, Itm> parser) {
        return repetition(
                inclusiveLow, inclusiveHigh,
                StringBuilder::append,
                StringBuilder::new,
                parser
        ).map(StringBuilder::toString);
    }

    /**
     * WHAT: Parses a range of an item and joins them to a list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> repetitionList(
            int inclusiveLow, int inclusiveHigh,
            Parser<T, Seq, Itm> parser) {
        return repetition(
                inclusiveLow, inclusiveHigh,
                Parsers::appendList,
                Parsers::<T>newList,
                parser).map(Collections::unmodifiableList);
    }

    /**
     * WHAT: Parses a range of an item and joins them together
     */
    public static <T, Bld, Seq, Itm>
    Parser<Bld, Seq, Itm> repetition(
            int inclusiveLow, int inclusiveHigh,
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            Parser<T, Seq, Itm> parser) {
        return stream -> {
            Result<Bld, ParserStream<Seq, Itm>> result = Result.success(empty.get(), stream);
            for (int i = 0; i < inclusiveLow && result.isSuccess(); i++) {
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> reduce.apply(bld, itm)));
            }
            if (!result.isSuccess()) {
                return result;
            }
            Result<Bld, ParserStream<Seq, Itm>> prevResult = result;
            for (int i = inclusiveLow; i < inclusiveHigh && result.isSuccess(); i++) {
                prevResult = result;
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> reduce.apply(bld, itm)));
            }
            return prevResult;
        };
    }

    /**
     * WHAT: Parse an item with a prefix and omit the prefix
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> prefix(
            Parser<?, Seq, Itm> before,
            Parser<T, Seq, Itm> parser) {
        return before.chain(pre -> parser);
    }

    /**
     * WHAT: Parse an item with a postfix and omit the postfix
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> postfix(
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> after) {
        return parser.chain(item -> after.map(post -> item));
    }

    /**
     * WHAT: Parse an item with a prefix and a postfix and omit both the prefix and the postfix
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> between(
            Parser<?, Seq, Itm> before,
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> after) {
        return before.chain(pre -> parser.chain(item -> after.map(post -> item)));
    }


    /**
     * WHAT: Attempt to parser a string or else yield an empty String
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> optional(Parser<String, Seq, Itm> parser) {
        return optional(parser, () -> "");
    }

    /**
     * WHAT: Attempt to parser an item or else yield a placeholder
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> optional(Parser<T, Seq, Itm> parser, Supplier<T> placeholder) {
        return stream -> parser.parse(stream)
                .orElse(() -> Result.success(placeholder.get(), stream));
    }

    /**
     * WHAT: Parse an string an omit it
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> omit(Parser<String, Seq, Itm> parser) {
        return omit(parser, () -> "");
    }

    /**
     * WHAT: Parse an item an omit it
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> omit(Parser<T, Seq, Itm> parser, Supplier<T> placeholder) {
        return parser.map(item -> placeholder.get());
    }

    /**
     * WHAT: Parse an item that satisfy a given predicate
     */
    public static <Seq, Itm>
    Parser<Itm, Seq, Itm> terminalItem(
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
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> terminalSequence(
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
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> terminalRun(
            Predicate<Itm> predicate,
            Function<Seq, Integer> measureLength) {
        return stream -> {
            Seq run = stream.getLeadingRun(predicate);
            int size = measureLength.apply(run);
            return Result.success(run, stream.jump(size));
        };
    }

    private static <T> List<T> newList() {
        return new ArrayList<>();
    }

    private static <T> List<T> appendList(List<T> list, T item) {
        list.add(item);
        return list;
    }
}
