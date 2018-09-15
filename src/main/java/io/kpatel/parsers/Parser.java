package io.kpatel.parsers;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * WHY: Generalize the the conversion of a Linear Structure to some Object
 * NEEDS:
 * - Simple Construction
 * - Possibility of Failure
 * - A Generalized Immutable Linear Structure
 * - Composing Operations
 * TOOLS:
 * - Lambda Expressions
 * - Result, A Closed Hierarchy that can compose success and failures states.
 * - ParserStream, A Generalization of Immutable Linear Structures
 */
@FunctionalInterface
public interface Parser<T, Seq, Itm> {
    /**
     * WHAT: A SAM Interface
     * WHY: Make use of Lambda Expressions for Simple Construction
     */
    Result<T, ParserStream<Seq, Itm>> parse(ParserStream<Seq, Itm> stream);

    /**
     * WHAT: Deferring to Result object
     * Transform result value w/o altering the stream w/ no chance of Failure
     * WHY: Composing Operations for Success to Success Case
     *
     * @see Result#map
     */
    default <U> Parser<U, Seq, Itm> map(Function<T, U> mapper) {
        return (stream) -> parse(stream).chain((t, remaining) -> Result.success(mapper.apply(t), remaining));
    }

    /**
     * WHAT: Deferring to Result object
     * Transform result value and stream w/ chance of Failure
     * WHY: Composing Operations for Success to Success/Failure Case
     *
     * @see Result#chain
     */
    default <U> Parser<U, Seq, Itm> chain(Function<T, Parser<U, Seq, Itm>> flatMapper) {
        return (stream) -> parse(stream).chain((t, remaining) -> flatMapper.apply(t).parse(remaining));
    }

    /**
     * WHAT: Deferring to Result object
     * Transform Failure to Success and rollback stream
     * WHY: Composing Operations for Failure to Success/Failure Case
     *
     * @see Result#orElse
     */
    default Parser<T, Seq, Itm> orElse(Supplier<Parser<T, Seq, Itm>> alternative) {
        return (stream) -> parse(stream).orElse(() -> alternative.get().parse(stream));
    }
}
