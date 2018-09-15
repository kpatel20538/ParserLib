package io.kpatel.parsers;

import io.kpatel.parsers.list.ListSequenceBuilder;
import io.kpatel.parsers.string.StringParsers;
import io.kpatel.parsers.string.StringSequenceBuilder;

import java.util.*;
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
        return (stream1) -> parser.parse(stream1)
                .chain((item, stream2) -> Result.success(item, stream1));
    }

    /**
     * WHAT: A Parser that fails if their is still input to take in.
     */
    public static <Seq, Itm>
    Parser<Void, Seq, Itm> endOfStream() {
        return (stream) -> stream.atEndOfStream()
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
        return (stream) -> parser.parse(stream).chain((item, stream1) ->
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
        return (stream) -> {
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
        return Parsers.concatenate(new StringSequenceBuilder(), parsers);
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> concatenateList(
            List<? extends Parser<T, Seq, Itm>> parsers) {
        return Parsers.concatenate(new ListSequenceBuilder<>(), parsers);
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty case
     */
    public static <Bld, T, Out, Seq, Itm>
    Parser<Out, Seq, Itm> concatenate(
            SequenceBuilder<Bld, T, Out> sequenceBuilder,
            List<? extends Parser<T, Seq, Itm>> parsers) {
        ArrayList<? extends Parser<T, Seq, Itm>> parserList = new ArrayList<>(parsers);
        return (stream) -> {
            Result<Bld, ParserStream<Seq, Itm>> result = Result.success(sequenceBuilder.getNewBuilder(), stream);
            for (Parser<T, Seq, Itm> parser : parserList) {
                result = result.chain((bld, remaining) -> parser.parse(remaining)
                        .map((itm, stream1) -> sequenceBuilder.appendPart(bld, itm)));
            }
            return result.map((bld, stream1) -> sequenceBuilder.toOutput(bld));
        };
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> zeroOrMoreString(Parser<String, Seq, Itm> parser) {
        return Parsers.zeroOrMore(new StringSequenceBuilder(), parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> zeroOrMoreList(Parser<T, Seq, Itm> parser) {
        return Parsers.zeroOrMore(new ListSequenceBuilder<>(), parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case
     */

    public static <Bld, T, Out, Seq, Itm>
    Parser<Out, Seq, Itm> zeroOrMore(
            SequenceBuilder<Bld, T, Out> sequenceBuilder,
            Parser<T, Seq, Itm> parser) {
        return (stream) -> {
            Bld builder = sequenceBuilder.getNewBuilder();
            Result<Bld, ParserStream<Seq, Itm>> prevResult = Result.success(builder, stream);
            Result<Bld, ParserStream<Seq, Itm>> result = prevResult;

            while (result.isSuccess()) {
                prevResult = result;
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> sequenceBuilder.appendPart(bld, itm)));
            }
            return prevResult.map((bld, stream1) -> sequenceBuilder.toOutput(bld));
        };
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> oneOrMoreString(
            Parser<String, Seq, Itm> parser) {
        return oneOrMore(new StringSequenceBuilder(), parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> oneOrMoreList(
            Parser<T, Seq, Itm> parser) {
        return oneOrMore(new ListSequenceBuilder<>(), parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <Bld, T, Out, Seq, Itm>
    Parser<Out, Seq, Itm> oneOrMore(
            SequenceBuilder<Bld, T, Out> sequenceBuilder,
            Parser<T, Seq, Itm> parser) {
        return (stream) -> {
            Result<Bld, ParserStream<Seq, Itm>> prevResult = parser.parse(stream)
                    .map((part, stream1) -> sequenceBuilder.getNewBuilder(part));
            Result<Bld, ParserStream<Seq, Itm>> result = prevResult;

            while (result.isSuccess()) {
                prevResult = result;
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> sequenceBuilder.appendPart(bld, itm)));
            }
            return prevResult.map((bld, stream1) -> sequenceBuilder.toOutput(bld));
        };
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> delimitedString(
            Parser<String, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        return delimited(new StringSequenceBuilder(), parser, delimiter);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> delimitedList(
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        return delimited(new ListSequenceBuilder<>(), parser, delimiter);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <Bld, T, Out, Seq, Itm>
    Parser<Out, Seq, Itm> delimited(
            SequenceBuilder<Bld, T, Out> sequenceBuilder,
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        Parser<T, Seq, Itm> prefixedParser = prefix(delimiter, parser);
        Parser<Bld, Seq, Itm> delimitedParser = stream -> {
            Result<Bld, ParserStream<Seq, Itm>> prevResult = parser.parse(stream)
                    .map((part, stream1) -> sequenceBuilder.getNewBuilder(part));
            Result<Bld, ParserStream<Seq, Itm>> result = prevResult;

            while (result.isSuccess()) {
                prevResult = result;
                result = result.chain((bld, stream1) -> prefixedParser.parse(stream1)
                        .map((itm, stream2) -> sequenceBuilder.appendPart(bld, itm)));
            }
            return prevResult;
        };
        return optional(delimitedParser, sequenceBuilder::getNewBuilder)
                .map(sequenceBuilder::toOutput);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them to a string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> repetitionString(
            int count, Parser<String, Seq, Itm> parser) {
        return repetition(count, new StringSequenceBuilder(), parser);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them to a list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> repetitionList(
            int count, Parser<T, Seq, Itm> parser) {
        return repetition(count, new ListSequenceBuilder<>(), parser);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them together
     */
    public static <Bld, T, Out, Seq, Itm>
    Parser<Out, Seq, Itm> repetition(
            int count,
            SequenceBuilder<Bld, T, Out> sequenceBuilder,
            Parser<T, Seq, Itm> parser) {
        return (stream) -> {
            Bld builder = sequenceBuilder.getNewBuilder();
            Result<Bld, ParserStream<Seq, Itm>> result = Result.success(builder, stream);
            for (int i = 0; i < count && result.isSuccess(); i++) {
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> sequenceBuilder.appendPart(bld, itm)));
            }
            return result.map(((bld, stream1) -> sequenceBuilder.toOutput(bld)));
        };
    }

    /**
     * WHAT: Parses a range of an item and joins them to a string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> repetitionString(
            int inclusiveLow, int inclusiveHigh,
            Parser<String, Seq, Itm> parser) {
        return repetition(inclusiveLow, inclusiveHigh, new StringSequenceBuilder(), parser);
    }

    /**
     * WHAT: Parses a range of an item and joins them to a list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> repetitionList(
            int inclusiveLow, int inclusiveHigh,
            Parser<T, Seq, Itm> parser) {
        return repetition(inclusiveLow, inclusiveHigh, new ListSequenceBuilder<>(), parser);
    }

    /**
     * WHAT: Parses a range of an item and joins them together
     */
    public static <Bld, T, Out, Seq, Itm>
    Parser<Out, Seq, Itm> repetition(
            int inclusiveLow, int inclusiveHigh,
            SequenceBuilder<Bld, T, Out> sequenceBuilder,
            Parser<T, Seq, Itm> parser) {
        return (stream) -> {
            Bld builder = sequenceBuilder.getNewBuilder();
            Result<Bld, ParserStream<Seq, Itm>> result = Result.success(builder, stream);
            for (int i = 0; i < inclusiveLow && result.isSuccess(); i++) {
                result = result.chain((bld, stream1) -> parser.parse(stream1)
                        .map((itm, stream2) -> sequenceBuilder.appendPart(bld, itm)));
            }
            if (result.isSuccess()) {
                Result<Bld, ParserStream<Seq, Itm>> nextResult = result;
                for (int i = inclusiveLow; i < inclusiveHigh && nextResult.isSuccess(); i++) {
                    result = nextResult;
                    nextResult = result.chain((bld, stream1) -> parser.parse(stream1)
                            .map((itm, stream2) -> sequenceBuilder.appendPart(bld, itm)));
                }
            }
            return result.map((bld, stream1) -> sequenceBuilder.toOutput(bld));
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
        return (stream) -> parser.parse(stream)
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
    Parser<Itm, Seq, Itm> item(
            Predicate<Itm> predicate,
            Supplier<String> errorMessage) {
        return (stream) -> stream.getLeadingItem()
                .filter(predicate)
                .map(i -> Result.success(i, stream.jump(1)))
                .orElseGet(() -> Result.failure(errorMessage.get(), stream));
    }

    /**
     * WHAT: Parse the given Character
     *
     * @see StringParsers(Predicate, Supplier)
     */
    public static <Seq, Itm> Parser<Itm, Seq, Itm> item(Itm target, Supplier<String> errorMessage) {
        return item(target::equals, errorMessage);
    }

    /**
     * WHAT: Parse Any Character from the given String
     *
     * @see StringParsers(Predicate, Supplier)
     */
    public static <Seq, Itm> Parser<Itm, Seq, Itm> item(Collection<Itm> items, Supplier<String> errorMessage) {
        Set<Itm> itemSet = new HashSet<>(items);
        return item(itemSet::contains, errorMessage);
    }

    /**
     * WHAT: Parse as sequence of items
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> sequence(Seq sequence, Supplier<String> errorMessage) {
        return (stream) -> {
            SequenceHolder<Seq> sequenceHolder = stream.holdSequence(sequence);
            int size = sequenceHolder.getLength();
            SequenceHolder<Seq> leadingHolder = stream.getLeadingSequence(size);
            Seq leading = leadingHolder.getSequence();
            return leading.equals(sequence)
                    ? Result.success(leading, stream.jump(leadingHolder.getLength()))
                    : Result.failure(errorMessage.get(), stream);
        };
    }

    /**
     * WHAT: Parse a run of item that satisfy a given predicate, Will always succeed
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> optionalRun(Predicate<Itm> predicate) {
        return (stream) -> {
            SequenceHolder<Seq> holder = stream.getLeadingRun(predicate);
            int size = holder.getLength();
            return Result.success(holder.getSequence(), stream.jump(size));
        };
    }

    /**
     * WHAT: Parse a run of given character, Will always succeed
     */
    public static <Seq, Itm> Parser<Seq, Seq, Itm> optionalRun(Itm target) {
        return optionalRun(target::equals);
    }

    /**
     * WHAT: Parse a run of any character from the give string, Will always succeed
     */
    public static <Seq, Itm> Parser<Seq, Seq, Itm> optionalRun(Collection<Itm> items) {
        Set<Itm> itemSet = new HashSet<>(items);
        return optionalRun(itemSet::contains);
    }

    /**
     * WHAT: Parse a run of characters that satisfy a given predicate, Will fail is nothing is found
     */
    public static <Seq, Itm> Parser<Seq, Seq, Itm> run(Predicate<Itm> predicate, Supplier<String> errorMessage) {
        return Parsers.<Itm, Seq, Itm>peek(Parsers.item(predicate, errorMessage))
                .chain(t -> Parsers.optionalRun(predicate));
    }

    /**
     * WHAT: Parse a run of given character, Will fail is nothing is found
     */
    public static <Seq, Itm> Parser<Seq, Seq, Itm> run(Itm target, Supplier<String> errorMessage) {
        return Parsers.<Itm, Seq, Itm>peek(Parsers.item(target, errorMessage))
                .chain(t -> Parsers.optionalRun(target));
    }

    /**
     * WHAT: Parse a run of any character from the give string, Will fail is nothing is found
     */
    public static <Seq, Itm> Parser<Seq, Seq, Itm> run(Collection<Itm> items, Supplier<String> errorMessage) {
        Set<Itm> itemSet = new HashSet<>(items);
        return Parsers.<Itm, Seq, Itm>peek(Parsers.item(itemSet::contains, errorMessage))
                .chain(t -> Parsers.optionalRun(itemSet::contains));
    }


    private static <T> List<T> newList() {
        return new ArrayList<>();
    }

    private static <T> List<T> appendList(List<T> list, T item) {
        list.add(item);
        return list;
    }
}
