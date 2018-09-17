package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
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
     * USAGE: Transform any accepted value without altering the stream with
     * no chance of recoverable failure
     */
    public static <T, U, Seq, Itm>
    Supplier<Parser<U, Seq, Itm>> map(
            Supplier<Parser<T, Seq, Itm>> parser,
            Function<T, U> mapper) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(mapper,
                "Mapping Function must not be null");
        return () -> stream -> parser.get().parse(stream).map(mapper);
    }


    /**
     * USAGE: Transform any accepted value without altering the stream with
     * a chance of recoverable failure
     */
    public static <T, U, Seq, Itm>
    Supplier<Parser<U, Seq, Itm>> flatMap(
            Supplier<Parser<T, Seq, Itm>> parser,
            Function<T, Supplier<Parser<U, Seq, Itm>>> flatMapper) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(flatMapper,
                "Flat Mapping Function must not be null");
        return () -> stream -> parser.get().parse(stream)
                .chain((t, remaining) -> flatMapper.apply(t).get().parse(remaining).map(u -> u));
    }


    /**
     * USAGE: Transform any recoverable failure without altering the stream
     * with a chance of recoverable failure.
     */
    public static <T, Seq, Itm>
    Supplier<Parser<T, Seq, Itm>> otherwise(
            Supplier<Parser<T, Seq, Itm>> parser,
            Supplier<Parser<T, Seq, Itm>> alternative) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(alternative,
                "Alternative Parser must not be null");
        return () -> stream -> parser.get().parse(stream).orElse(() ->
                alternative.get().parse(stream));
    }

    /**
     * USAGE: Parse an item and accepts it if the predicate yields true
     */
    public static <T, Seq, Itm>
    Supplier<Parser<T, Seq, Itm>> filter(
            Supplier<Parser<T, Seq, Itm>> parser,
            Predicate<T> predicate,
            Supplier<String> errorMessage) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(predicate,
                "Predicate must not be null");

        return () -> stream -> parser.get().parse(stream).chain(
                (item, remaining) -> predicate.test(item)
                        ? Result.success(item, remaining)
                        : Result.failure(stream.getErrorContext(), errorMessage));
    }

    /**
     * USAGE: Concatenate Two Parsers and map combine their outputs
     */
    public static <K, V, T, Seq, Itm>
    Supplier<Parser<T, Seq, Itm>> pipe(
            Supplier<Parser<K, Seq, Itm>> left,
            Supplier<Parser<V, Seq, Itm>> right,
            BiFunction<K, V, T> mapper) {
        Objects.requireNonNull(left,
                "Left Parser must not be null");
        Objects.requireNonNull(right,
                "Right Parser must not be null");
        Objects.requireNonNull(mapper,
                "Mapping Function must not be null");
        return flatMap(left, l -> map(right, r -> mapper.apply(l, r)));
    }

    /**
     * USAGE: Concatenate Two Parsers to yield a Map.entry container their outputs
     */
    public static <K, V, Seq, Itm>
    Supplier<Parser<Map.Entry<K, V>, Seq, Itm>> entry(
            Supplier<Parser<K, Seq, Itm>> key,
            Supplier<Parser<V, Seq, Itm>> value) {
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
    Supplier<Parser<T, Seq, Itm>> alternate(
            List<Supplier<? extends Parser<? extends T, Seq, Itm>>> parsers) {

        ArrayList<Supplier<? extends Parser<? extends T, Seq, Itm>>> parserList =
                new ArrayList<>(parsers);
        return () -> stream -> {
            var result = Result.<T, Seq, Itm>failure(
                    stream.getErrorContext(),
                    () -> "No Parsers To Alternate with.");

            for (Supplier<? extends Parser<? extends T, Seq, Itm>> parser : parserList) {
                result = result.orElse(() -> parser.get().parse(stream).map(t -> t));
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
    Supplier<Parser<T, Seq, Itm>> optional(
            Supplier<Parser<T, Seq, Itm>> parser,
            Supplier<T> placeholder) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(placeholder,
                "Supplier must not be null");
        return otherwise(parser, () -> stream -> Result.success(placeholder.get(), stream));
    }

    /**
     * USAGE: If the given parsers fails, supply an empty string. Succeed as normal
     */
    public static <Seq, Itm>
    Supplier<Parser<String, Seq, Itm>> optional(
            Supplier<Parser<String, Seq, Itm>> parser) {
        Objects.requireNonNull(parser,
                "Parser must not be null");

        return optional(parser, () -> "");
    }

    /**
     * USAGE: If the given parsers succeed, supply an empty case. Fail as normal
     */
    public static <T, Seq, Itm>
    Supplier<Parser<T, Seq, Itm>> omit(
            Supplier<Parser<T, Seq, Itm>> parser,
            Supplier<T> placeholder) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        Objects.requireNonNull(placeholder,
                "Supplier must not be null");

        return map(parser, item -> placeholder.get());
    }

    /**
     * USAGE: If the given parsers succeed, supply an empty string. Fail as normal
     */
    public static <Seq, Itm>
    Supplier<Parser<String, Seq, Itm>> omit(
            Supplier<Parser<String, Seq, Itm>> parser) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return omit(parser, () -> "");
    }


    /**
     * USAGE: Peek a head and see given parser can accept the input.
     */
    public static <T, Seq, Itm>
    Supplier<Parser<T, Seq, Itm>> peek(
            Supplier<Parser<T, Seq, Itm>> parser) {
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return () -> stream -> parser.get().parse(stream)
                .chain((item, remaining) -> Result.success(item, stream));
    }



}
