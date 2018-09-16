package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * INTENT: Top Level Generic Factories for Miscellaneous Parser and their combinations
 * @see Parser
 * @see io.kpatel.parsers.stream.ParserStream
 */
public final class MiscParsers {
    private MiscParsers() {

    }

    /**
     * USAGE: Concatenate Two Parsers and map combine their outputs
     */
    public static <K, V, T, Seq, Itm>
    Parser<T, Seq, Itm> pipe(
            Parser<K, Seq, Itm> left,
            Parser<V, Seq, Itm> right,
            BiFunction<K, V, T> mapper) {
        Objects.requireNonNull(left,
                "Left Parser must not be null");
        Objects.requireNonNull(right,
                "Right Parser must not be null");
        Objects.requireNonNull(mapper,
                "Mapping Function must not be null");
        return left.chain(l -> right.map(r -> mapper.apply(l, r)));
    }

    /**
     * USAGE: Concatenate Two Parsers to yield a Map.entry container their outputs
     */
    public static <K, V, Seq, Itm>
    Parser<Map.Entry<K, V>, Seq, Itm> entry(
            Parser<K, Seq, Itm> key,
            Parser<V, Seq, Itm> value) {
        Objects.requireNonNull(key,
                "Key Parser must not be null");
        Objects.requireNonNull(value,
                "Value Parser must not be null");
        return pipe(key, value, Map::entry);
    }

    /**
     * USAGE: Create a parser that parse the initial stream until one parser
     * success or all of them fail.
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> alternate(List<Parser<T, Seq, Itm>> parsers) {
        ArrayList<Parser<T, Seq, Itm>> parserList =
                new ArrayList<>(parsers);

        return stream -> {
            var result = Result.<T, Seq, Itm>failure(
                    stream.getErrorContext(),
                    () -> "No Parsers To Alternate with.");

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
     * USAGE: If the given parsers fails, supply the empty case. Succeed as normal
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> optional(
            Parser<T, Seq, Itm> parser,
            Supplier<T> placeholder) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(placeholder,
                "Supplier must not be null");
        return stream -> parser.parse(stream).orElse(() ->
                Result.success(placeholder.get(), stream));
    }

    /**
     * USAGE: If the given parsers fails, supply an empty string. Succeed as normal
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> optional(
            Parser<String, Seq, Itm> parser) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return optional(parser, () -> "");
    }

    /**
     * USAGE: If the given parsers succeed, supply an empty case. Fail as normal
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> omit(
            Parser<T, Seq, Itm> parser,
            Supplier<T> placeholder) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(placeholder,
                "Supplier must not be null");
        return parser.map(item -> placeholder.get());
    }

    /**
     * USAGE: If the given parsers succeed, supply an empty string. Fail as normal
     */
    public static <Seq, Itm>
    Parser<String, Seq, Itm> omit(
            Parser<String, Seq, Itm> parser) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return omit(parser, () -> "");
    }


    /**
     * USAGE: Peek a head and see given parser can accept the input.
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> peek(Parser<T, Seq, Itm> parser) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return stream -> parser.parse(stream)
                .chain((item, remaining) -> Result.success(item, stream));
    }

    /**
     * USAGE: Parse an item unless in falls in the exception case.
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> exception(
            Parser<T, Seq, Itm> parser,
            Predicate<T> except,
            Supplier<String> errorMessage) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(except,
                "Exception Predicate must not be null");

        return stream -> parser.parse(stream).chain(
                (item, remaining) -> except.test(item)
                        ? Result.failure(stream.getErrorContext(), errorMessage)
                        : Result.success(item, remaining));
    }

}
