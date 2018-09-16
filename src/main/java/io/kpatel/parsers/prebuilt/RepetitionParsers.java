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
import java.util.function.Supplier;

public final class RepetitionParsers {


    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty case
     */
    public static <Out, Prt, Seq, Itm>
    Parser<Out, Seq, Itm> concatenate(
            Supplier<Builder<Out, Prt>> provider,
            List<Parser<Prt, Seq, Itm>> parsers) {
        ArrayList<Parser<Prt, Seq, Itm>> parserList =
                new ArrayList<>(parsers);
        return (stream) -> {
            var result = Result.success(provider.get(), stream);

            for (Parser<Prt, Seq, Itm> parser : parserList) {
                result = appendResult(parser, result);
                if (!result.isSuccess()) {
                    break;
                }
            }
            return result.map(Builder::toOutput);
        };
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> concatenateString(
            List<Parser<String, Seq, Itm>> parsers) {
        return concatenate(TextBuilder::new, parsers);
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> concatenateList(
            List<Parser<T, Seq, Itm>> parsers) {
        return concatenate(ListBuilder::new, parsers);
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty map
     */
    public static <K, V, Seq, Itm>
    Parser<Map<K, V>, Seq, Itm> concatenateMap(
            List<Parser<Map.Entry<K, V>, Seq, Itm>> parsers) {
        return concatenate(MapBuilder::new, parsers);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case
     */
    public static <Out, Prt, Seq, Itm>
    Parser<Out, Seq, Itm> zeroOrMore(
            Supplier<Builder<Out, Prt>> provider,
            Parser<Prt, Seq, Itm> parser) {
        return (stream) -> {
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
     * WHAT: Parse a parser until it fails and join the results to the empty string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> zeroOrMoreString(Parser<String, Seq, Itm> parser) {
        return zeroOrMore(TextBuilder::new, parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> zeroOrMoreList(Parser<T, Seq, Itm> parser) {
        return zeroOrMore(ListBuilder::new, parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty map
     */
    public static <K, V, Seq, Itm>
    Parser<Map<K, V>, Seq, Itm> zeroOrMoreMap(Parser<Map.Entry<K, V>, Seq, Itm> parser) {
        return zeroOrMore(MapBuilder::new, parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <Out, Prt, Seq, Itm>
    Parser<Out, Seq, Itm> oneOrMore(
            Supplier<Builder<Out, Prt>> provider,
            Parser<Prt, Seq, Itm> parser) {
        return (stream) -> {
            var result = parser.parse(stream)
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
     * WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> oneOrMoreString(
            Parser<String, Seq, Itm> parser) {
        return oneOrMore(TextBuilder::new, parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> oneOrMoreList(
            Parser<T, Seq, Itm> parser) {
        return oneOrMore(ListBuilder::new, parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty map, requires at least one
     */
    public static <K, V, Seq, Itm>
    Parser<Map<K, V>, Seq, Itm> oneOrMoreMap(
            Parser<Map.Entry<K, V>, Seq, Itm> parser) {
        return oneOrMore(MapBuilder::new, parser);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <Out, Prt, Seq, Itm>
    Parser<Out, Seq, Itm> delimited(
            Supplier<Builder<Out, Prt>> provider,
            Parser<Prt, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        var prefixedParser = AffixParsers.prefix(delimiter, parser);
        Parser<Builder<Out, Prt>, Seq, Itm> delimitedParser = stream -> {
            var result = parser.parse(stream)
                    .map(part -> provider.get().append(part));
            var nextResult = result;

            while (nextResult.isSuccess()) {
                result = nextResult;
                nextResult = appendResult(prefixedParser, nextResult);
            }
            return result;
        };
        return Parsers
                .optional(delimitedParser, provider)
                .map(Builder::toOutput);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> delimitedString(
            Parser<String, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        return delimited(TextBuilder::new, parser, delimiter);
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> delimitedList(
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        return delimited(ListBuilder::new, parser, delimiter);
    }

    public static <K, V, Seq, Itm>
    Parser<Map<K, V>, Seq, Itm> delimitedMap(
            Parser<Map.Entry<K, V>, Seq, Itm> parser,
            Parser<?, Seq, Itm> delimiter) {
        return delimited(MapBuilder::new, parser, delimiter);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them together
     */
    public static <Out, Prt, Seq, Itm>
    Parser<Out, Seq, Itm> repeat(
            Supplier<Builder<Out, Prt>> provider,
            Parser<Prt, Seq, Itm> parser,
            int count) {
        return stream -> {
            var result = Result.success(provider.get(), stream);

            for (int i = 0; i < count && result.isSuccess(); i++) {
                result = appendResult(parser, result);
            }
            return result.map(Builder::toOutput);
        };
    }

    /**
     * WHAT: Parses an exact number of an item and joins them to a string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> repeatString(
            Parser<String, Seq, Itm> parser,
            int count) {
        return repeat(TextBuilder::new, parser, count);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them to a list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> repeatList(
            Parser<T, Seq, Itm> parser,
            int count) {
        return repeat(ListBuilder::new, parser, count);
    }

    /**
     * WHAT: Parses an exact number of an item and joins them to a list
     */
    public static <K, V, Seq, Itm>
    Parser<Map<K, V>, Seq, Itm> repeatMap(
            Parser<Map.Entry<K, V>, Seq, Itm> parser,
            int count) {
        return repeat(MapBuilder::new, parser, count);
    }


    /**
     * WHAT: Parses a range of an item and joins them together
     */
    public static <Out, Prt, Seq, Itm>
    Parser<Out, Seq, Itm> rangedRepeat(
            Supplier<Builder<Out, Prt>> provider,
            Parser<Prt, Seq, Itm> parser,
            int inclusiveLow, int inclusiveHigh) {
        return (stream) -> {
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
     * WHAT: Parses a range of an item and joins them to a string
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> rangedRepeatString(
            Parser<String, Seq, Itm> parser,
            int inclusiveLow, int inclusiveHigh) {
        return rangedRepeat(
                TextBuilder::new, parser,
                inclusiveLow, inclusiveHigh);
    }

    /**
     * WHAT: Parses a range of an item and joins them to a list
     */
    public static <T, Seq, Itm>
    Parser<List<T>, Seq, Itm> rangedRepeatList(
            Parser<T, Seq, Itm> parser,
            int inclusiveLow, int inclusiveHigh) {
        return rangedRepeat(
                ListBuilder::new, parser,
                inclusiveLow, inclusiveHigh);
    }

    /**
     * WHAT: Parses a range of an item and joins them to a map
     */
    public static <K, V, Seq, Itm>
    Parser<Map<K, V>, Seq, Itm> rangedRepeatMap(
            Parser<Map.Entry<K, V>, Seq, Itm> parser,
            int inclusiveLow, int inclusiveHigh) {
        return rangedRepeat(
                MapBuilder::new, parser,
                inclusiveLow, inclusiveHigh);
    }

    private static <Out, Prt, Seq, Itm>
    Result<Builder<Out, Prt>, Seq, Itm> appendResult(
            Parser<Prt, Seq, Itm> parser,
            Result<Builder<Out, Prt>, Seq, Itm> result) {
        return result.chain((bld, remaining) ->
                parser.parse(remaining).map(bld::append));
    }


}
