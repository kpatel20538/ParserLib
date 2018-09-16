package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
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
     * WHAT: Parse an item unless in falls in the exception case.
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> exception(
            Parser<T, Seq, Itm> parser,
            Predicate<T> except,
            Supplier<String> errorMessage) {
        return stream -> parser.parse(stream).chain(
                (item, remaining) -> except.test(item)
                        ? Result.failure(errorMessage.get(), stream)
                        : Result.success(item, remaining));
    }

    /**
     * WHAT: Parse with entry
     */
    public static <K, V, T, Seq, Itm>
    Parser<T, Seq, Itm> pipe(
            Parser<K, Seq, Itm> left,
            Parser<V, Seq, Itm> right,
            BiFunction<K, V, T> mapper) {
        return left.chain(
                l -> right.map(
                        r -> mapper.apply(l, r)));
    }

    /**
     * WHAT: Parse with entry
     */
    public static <K, V, Seq, Itm>
    Parser<Map.Entry<K, V>, Seq, Itm> entry(
            Parser<K, Seq, Itm> key,
            Parser<V, Seq, Itm> value) {
        return pipe(key, value, Map::entry);
    }

    /**
     * WHAT: Parse until one parse succeeds or all parsers fail
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> alternate(List<? extends Parser<T, Seq, Itm>> parsers) {
        ArrayList<? extends Parser<T, Seq, Itm>> parserList =
                new ArrayList<>(parsers);

        return (stream) -> {
            var result = Result.<T, Seq, Itm>
                    failure("No Parsers To Alternate with.", stream);

            for (Parser<T, Seq, Itm> parser : parserList) {
                result = result.orElse(() -> parser.parse(stream));
                if (result.isSuccess()) {
                    return result;
                }
            }
            return result;
        };
    }

    /**
     * WHAT: Attempt to parser an item or else yield a placeholder
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> optional(
            Parser<T, Seq, Itm> parser,
            Supplier<T> placeholder) {
        return (stream) -> parser.parse(stream).orElse(
                () -> Result.success(placeholder.get(), stream));
    }

    /**
     * WHAT: Attempt to parser a string or else yield an empty String
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> optional(
            Parser<String, Seq, Itm> parser) {
        return optional(parser, () -> "");
    }

    /**
     * WHAT: Parse an item an omit it
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> omit(
            Parser<T, Seq, Itm> parser,
            Supplier<T> placeholder) {
        return parser.map(item -> placeholder.get());
    }

    /**
     * WHAT: Parse an string an omit it
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> omit(
            Parser<String, Seq, Itm> parser) {
        return omit(parser, () -> "");
    }


    /**
     * WHAT: Peek ahead and parse an item without changing the stream.
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> peek(Parser<T, Seq, Itm> parser) {
        return stream -> parser.parse(stream)
                .chain((item, remaining) -> Result.success(item, stream));
    }
}
