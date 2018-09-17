package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.builder.Builder;
import io.kpatel.parsers.builder.ListBuilder;
import io.kpatel.parsers.builder.MapBuilder;
import io.kpatel.parsers.builder.TextBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.MiscParsers.map;

/**
 * INTENT: Top Level Generic Factories for Repeating/Concatenating Parsers and
 * joining their results to a builder
 *
 * @see Parser
 * @see Builder
 */
public final class RepetitionParsers {
    private RepetitionParsers() {

    }

    /**
     * USAGE: Parse until all parsers succeeds or one parser fail and join the results with a builder.
     * @see Builder
     */
    public static <Out, Prt, Seq, Itm>
    Supplier<Parser<Out, Seq, Itm>> concatenate(
            Supplier<Builder<Out, Prt>> provider,
            List<Supplier<? extends Parser<? extends Prt, Seq, Itm>>> parsers) {
        Objects.requireNonNull(provider,
                "Builder Supplier must not be null");
        var parserList = new ArrayList<>(parsers);
        return () -> stream -> {
            var result = Result.success(provider.get(), stream);

            for (Supplier<? extends Parser<? extends Prt, Seq, Itm>> parser : parserList) {
                result = appendResult(parser, result);
                if (!result.isSuccess()) {
                    break;
                }
            }
            return result.map(Builder::toOutput);
        };
    }

    /**
     * USAGE: Parse until all parsers succeeds or one parser fail and join the results to a string
     */
    public static <Seq, Itm>
    Supplier<Parser<String, Seq, Itm>> concatenateString(
            List<Supplier<? extends Parser<? extends String, Seq, Itm>>> parsers) {
        return concatenate(TextBuilder::new, parsers);
    }

    /**
     * USAGE: Parse until all parsers succeeds or one parser fail and join the results to a list
     */
    public static <T, Seq, Itm>
    Supplier<Parser<List<T>, Seq, Itm>> concatenateList(
            List<Supplier<? extends Parser<? extends T, Seq, Itm>>> parsers) {
        return concatenate(ListBuilder::new, parsers);
    }

    /**
     * USAGE: Parse until all parsers succeeds or one parser fail and join the results to a map
     */
    public static <K, V, Seq, Itm>
    Supplier<Parser<Map<K, V>, Seq, Itm>> concatenateMap(
            List<Supplier<? extends Parser<? extends Map.Entry<K, V>, Seq, Itm>>> parsers) {
        return concatenate(MapBuilder::new, parsers);
    }

    /**
     * USAGE: Parse a parser until it fails and join the join the results with a builder.
     * @see Builder
     */
    public static <Out, Prt, Seq, Itm>
    Supplier<Parser<Out, Seq, Itm>> zeroOrMore(
            Supplier<Builder<Out, Prt>> provider,
            Supplier<Parser<Prt, Seq, Itm>> parser) {
        Objects.requireNonNull(provider,
                "Builder Supplier must not be null");
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return () -> stream -> {
            var result = Result.success(provider.get(), stream);
            var nextResult = result;

            while (nextResult.isSuccess()) {
                result = nextResult;
                nextResult = appendResult(parser, nextResult);
            }
            return result.map(Builder::toOutput);
        };
    }

    /**
     * USAGE: Parse a parser until it fails and join the join the results with to a string.
     */
    public static <Seq, Itm>
    Supplier<Parser<String, Seq, Itm>> zeroOrMoreString(
            Supplier<Parser<String, Seq, Itm>> parser) {
        return zeroOrMore(TextBuilder::new, parser);
    }

    /**
     * USAGE: Parse a parser until it fails and join the join the results with to a list.
     */
    public static <T, Seq, Itm>
    Supplier<Parser<List<T>, Seq, Itm>> zeroOrMoreList(
            Supplier<Parser<T, Seq, Itm>> parser) {
        return zeroOrMore(ListBuilder::new, parser);
    }

    /**
     * USAGE: Parse a parser until it fails and join the join the results with to a map.
     */
    public static <K, V, Seq, Itm>
    Supplier<Parser<Map<K, V>, Seq, Itm>> zeroOrMoreMap(
            Supplier<Parser<Map.Entry<K, V>, Seq, Itm>> parser) {
        return zeroOrMore(MapBuilder::new, parser);
    }

    /**
     * USAGE: Parse a parser until it fails and join the results with a builder.
     * - Requires at least one
     * @see Builder
     */
    public static <Out, Prt, Seq, Itm>
    Supplier<Parser<Out, Seq, Itm>> oneOrMore(
            Supplier<Builder<Out, Prt>> provider,
            Supplier<Parser<Prt, Seq, Itm>> parser) {
        Objects.requireNonNull(provider,
                "Builder Supplier must not be null");
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return () -> stream -> {
            var result = parser.get().parse(stream)
                    .map(part -> provider.get().append(part));
            var nextResult = result;

            while (nextResult.isSuccess()) {
                result = nextResult;
                nextResult = appendResult(parser, nextResult);
            }
            return result.map(Builder::toOutput);
        };
    }

    /**
     * USAGE: Parse a parser until it fails and join the results with to a string.
     * - Requires at least one
     */
    public static <Seq, Itm>
    Supplier<Parser<String, Seq, Itm>> oneOrMoreString(
            Supplier<Parser<String, Seq, Itm>> parser) {
        return oneOrMore(TextBuilder::new, parser);
    }

    /**
     * USAGE: Parse a parser until it fails and join the results with to a list.
     * - Requires at least one
     */
    public static <T, Seq, Itm>
    Supplier<Parser<List<T>, Seq, Itm>> oneOrMoreList(
            Supplier<Parser<T, Seq, Itm>> parser) {
        return oneOrMore(ListBuilder::new, parser);
    }

    /**
     * USAGE: Parse a parser until it fails and join the results with to a map.
     * - Requires at least one
     */
    public static <K, V, Seq, Itm>
    Supplier<Parser<Map<K, V>, Seq, Itm>> oneOrMoreMap(
            Supplier<Parser<Map.Entry<K, V>, Seq, Itm>> parser) {
        return oneOrMore(MapBuilder::new, parser);
    }

    /**
     * USAGE: Parse with parser with a delimiter between each value and join the results with a builder.
     * @see Builder
     */
    public static <Out, Prt, Del, Seq, Itm>
    Supplier<Parser<Out, Seq, Itm>> delimited(
            Supplier<Builder<Out, Prt>> provider,
            Supplier<Parser<Prt, Seq, Itm>> parser,
            Supplier<Parser<Del, Seq, Itm>> delimiter) {
        Objects.requireNonNull(provider,
                "Builder Supplier must not be null");
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(delimiter,
                "Delimiter must not be null");
        Supplier<Parser<Builder<Out, Prt>, Seq, Itm>> delimitedParser = () -> stream -> {
            var result = parser.get().parse(stream)
                    .map(part -> provider.get().append(part));
            var nextResult = result;

            while (nextResult.isSuccess()) {
                result = nextResult;
                nextResult = appendDelimiterResult(delimiter, nextResult);
                nextResult = appendResult(parser, nextResult);
            }
            return result;
        };
        var optDelimitedParser = MiscParsers.optional(delimitedParser, provider);

        return map(optDelimitedParser, Builder::toOutput);
    }

    /**
     * USAGE: Parse with parser with a delimiter between each value and join the results with to a string.
     */
    public static <Del, Seq, Itm>
    Supplier<Parser<String, Seq, Itm>> delimitedString(
            Supplier<Parser<String, Seq, Itm>> parser,
            Supplier<Parser<Del, Seq, Itm>> delimiter) {
        return delimited(TextBuilder::new, parser, delimiter);
    }

    /**
     * USAGE: Parse with parser with a delimiter between each value and join the results with to a list.
     */
    public static <T, Del, Seq, Itm>
    Supplier<Parser<List<T>, Seq, Itm>> delimitedList(
            Supplier<Parser<T, Seq, Itm>> parser,
            Supplier<Parser<Del, Seq, Itm>> delimiter) {
        return delimited(ListBuilder::new, parser, delimiter);
    }

    /**
     * USAGE: Parse with parser with a delimiter between each value and join the results with to a map.
     */
    public static <K, V, Del, Seq, Itm>
    Supplier<Parser<Map<K, V>, Seq, Itm>> delimitedMap(
            Supplier<Parser<Map.Entry<K, V>, Seq, Itm>> parser,
            Supplier<Parser<Del, Seq, Itm>> delimiter) {
        return delimited(MapBuilder::new, parser, delimiter);
    }

    /**
     * USAGE: Parses an exact number of an items and join the results with a builder.
     * @see Builder
     */
    public static <Out, Prt, Seq, Itm>
    Supplier<Parser<Out, Seq, Itm>> repeat(
            Supplier<Builder<Out, Prt>> provider,
            Supplier<Parser<Prt, Seq, Itm>> parser,
            int count) {
        Objects.requireNonNull(provider,
                "Builder Supplier must not be null");
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return () -> stream -> {
            var result = Result.success(provider.get(), stream);

            for (int i = 0; i < count && result.isSuccess(); i++) {
                result = appendResult(parser, result);
            }
            return result.map(Builder::toOutput);
        };
    }

    /**
     * USAGE: Parses an exact number of an items and join the results with to a string.
     */
    public static <Seq, Itm>
    Supplier<Parser<String, Seq, Itm>> repeatString(
            Supplier<Parser<String, Seq, Itm>> parser,
            int count) {
        return repeat(TextBuilder::new, parser, count);
    }

    /**
     * USAGE: Parses an exact number of an items and join the results with to a list.
     */
    public static <T, Seq, Itm>
    Supplier<Parser<List<T>, Seq, Itm>> repeatList(
            Supplier<Parser<T, Seq, Itm>> parser,
            int count) {
        return repeat(ListBuilder::new, parser, count);
    }

    /**
     * USAGE: Parses an exact number of an items and join the results with to a map.
     */
    public static <K, V, Seq, Itm>
    Supplier<Parser<Map<K, V>, Seq, Itm>> repeatMap(
            Supplier<Parser<Map.Entry<K, V>, Seq, Itm>> parser,
            int count) {
        return repeat(MapBuilder::new, parser, count);
    }


    /**
     * USAGE: Parses a range of an item and join the results with a builder.
     * @see Builder
     */
    public static <Out, Prt, Seq, Itm>
    Supplier<Parser<Out, Seq, Itm>> rangedRepeat(
            Supplier<Builder<Out, Prt>> provider,
            Supplier<Parser<Prt, Seq, Itm>> parser,
            int inclusiveLow, int inclusiveHigh) {
        Objects.requireNonNull(provider,
                "Builder Supplier must not be null");
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return () -> stream -> {
            var result = Result.success(provider.get(), stream);
            for (int i = 0; i < inclusiveLow && result.isSuccess(); i++) {
                result = appendResult(parser, result);
            }

            if (result.isSuccess()) {
                var nextResult = result;
                for (int i = inclusiveLow; i < inclusiveHigh && nextResult.isSuccess(); i++) {
                    result = nextResult;
                    nextResult = appendResult(parser, result);
                }
            }
            return result.map(Builder::toOutput);
        };
    }

    /**
     * USAGE: Parses a range of an item and join the results to a string.
     */
    public static <Seq, Itm>
    Supplier<Parser<String, Seq, Itm>> rangedRepeatString(
            Supplier<Parser<String, Seq, Itm>> parser,
            int inclusiveLow, int inclusiveHigh) {
        return rangedRepeat(
                TextBuilder::new, parser,
                inclusiveLow, inclusiveHigh);
    }

    /**
     * USAGE: Parses a range of an item and join the results to a list.
     */
    public static <T, Seq, Itm>
    Supplier<Parser<List<T>, Seq, Itm>> rangedRepeatList(
            Supplier<Parser<T, Seq, Itm>> parser,
            int inclusiveLow, int inclusiveHigh) {
        return rangedRepeat(
                ListBuilder::new, parser,
                inclusiveLow, inclusiveHigh);
    }

    /**
     *WHAT: Parses a range of an item and join the results to a map.
     */
    public static <K, V, Seq, Itm>
    Supplier<Parser<Map<K, V>, Seq, Itm>> rangedRepeatMap(
            Supplier<Parser<Map.Entry<K, V>, Seq, Itm>> parser,
            int inclusiveLow, int inclusiveHigh) {
        return rangedRepeat(
                MapBuilder::new, parser,
                inclusiveLow, inclusiveHigh);
    }

    /**
     * HELPER: Parse a part and append it to the builder, otherwise fail as normal
     */
    private static <Out, Prt, Seq, Itm>
    Result<Builder<Out, Prt>, Seq, Itm> appendResult(
            Supplier<? extends Parser<? extends Prt, Seq, Itm>> parser,
            Result<Builder<Out, Prt>, Seq, Itm> result) {
        return result.chain((bld, remaining) ->
                parser.get().parse(remaining).map(bld::append));
    }

    private static <Out, Prt, Del, Seq, Itm>
    Result<Builder<Out, Prt>, Seq, Itm> appendDelimiterResult(
            Supplier<Parser<Del, Seq, Itm>> parser,
            Result<Builder<Out, Prt>, Seq, Itm> result) {
        return result.chain((bld, remaining) ->
                parser.get().parse(remaining).map(delimiter -> bld));
    }


}
