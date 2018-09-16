package io.kpatel.parsers;

import io.kpatel.parsers.stream.ParserStream;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

//TODO: Consider Adding Default Methods, filter, omit, optional, peek
//TODO: Consider Removing All Default Methods

/**
 * INTENT: A Composable Generic Interface conversion of a Stream to some Non-Null Object
 * REQUIREMENTS:
 * - Must be Pure Function
 * - Must return and a Success is the target Object was found
 * - Must return and a Failure if a Recoverable Error occurred
 * - Must throw a Runtime Exception for Unrecoverable Error.
 *   (preference is toward {@link ParserError})
 * RECOMMENDATIONS:
 * - Do not capture {@link ParserError} in {@link Parser}, The Stream may not
 *   guaranteed to be in a valid state for Parsing.
 * - Prefer Lambda Expressions over Class Implementation to help reduce
 *   dependence on external state.
 */
@FunctionalInterface
public interface Parser<T, Seq, Itm> {
    /**
     * WHAT: A SAM Interface
     * WHY: Make use of Lambda Expressions for Simple Construction
     */
    Result<T, Seq, Itm> parse(ParserStream<Seq, Itm> stream);

    /**
     * INTENT: Transform any accepted value without altering the stream with
     *         no chance of recoverable failure
     * @see Result#map
     */
    default <U> Parser<U, Seq, Itm> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper,
                "Mapping Function must not be null");
        return stream -> parse(stream).chain((t, remaining) ->
                Result.success(mapper.apply(t), remaining));
    }

    /**
     * INTENT: Transform any accepted value without altering the stream with
     *         a chance of recoverable failure
     * @see Result#chain
     */
    default <U> Parser<U, Seq, Itm> chain(Function<T, Parser<U, Seq, Itm>> flatMapper) {
        Objects.requireNonNull(flatMapper,
                "Flat Mapping Function must not be null");
        return stream -> parse(stream).chain((t, remaining) ->
                flatMapper.apply(t).parse(remaining));
    }

    /**
     * INTENT: Transform any recoverable failure without altering the stream
     *         with a chance of recoverable failure.
     * @see Result#orElse
     */
    default Parser<T, Seq, Itm> orElse(Supplier<Parser<T, Seq, Itm>> alternative) {
        Objects.requireNonNull(alternative,
                "Alternative Supplier Function must not be null");
        return stream -> parse(stream).orElse(() ->
                alternative.get().parse(stream));
    }
}
