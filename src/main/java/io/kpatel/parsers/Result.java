package io.kpatel.parsers;

import io.kpatel.parsers.stream.ParserStream;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * WHY: To Indicate whether a parser succeed or failure
 * NEEDS:
 * - A Total Disjoint between Success and Failure Cases
 * - Only one source for pure Success and Failure Cases
 * - Composing Operations with other Results
 * - Unwrapping Operations to retrieve actual value
 * TOOLS:
 * - Package-Private Constructor w/ Abstract Base Class and Final Child Classes
 * - Static Factory Methods
 * - Visitor Pattern
 */
public abstract class Result<T, Seq, Itm> {
    /**
     * WHAT: A package-private constructor of an abstract class to ensure a closed
     * inheritance hierarchy and no base class instances.
     * WHY: Result must either a Success Instance, or a Failure Instance
     */
    Result() {

    }

    /**
     * WHAT: Factory Operation for Success Case
     * WHY: Only source for pure Success Case
     */
    public static <T, Seq, Itm> Result<T, Seq, Itm> success(T result, ParserStream<Seq, Itm> remaining) {
        return new Success<>(result, remaining);
    }

    /**
     * WHAT: Factory Operation for Failure Case
     * WHY: Only source for pure Failure Case
     */
    public static <T, Seq, Itm> Result<T, Seq, Itm> failure(String errorMessage, ParserStream<Seq, Itm> remaining) {
        return new Failure<>(errorMessage, remaining);
    }

    /**
     * WHAT: Visitor Pattern seeking the Success Case.
     * Transform result value w/o altering the stream w/ no chance of Failure
     * WHY: Composing Operations for Success to Success Case
     *
     * @see Success#map
     */
    public abstract <U> Result<U, Seq, Itm> map(
            Function<T, U> mapper);

    /**
     * WHAT: Visitor Pattern seeking the Success Case.
     * Transform result value and stream w/ chance of Failure
     * WHY: Composing Operations for Success to Success/Failure Case
     *
     * @see Success#chain
     */
    public abstract <U> Result<U, Seq, Itm> chain(
            BiFunction<T, ParserStream<Seq, Itm>, Result<U, Seq, Itm>> flatMapper);

    /**
     * WHAT: Visitor Pattern seeking the Failure Case
     * Transform Failure to Success and rollback stream
     * WHY: Composing Operations for Failure to Success/Failure Case
     *
     * @see Failure#orElse
     */
    public abstract Result<T, Seq, Itm> orElse(
            Supplier<Result<T, Seq, Itm>> alternative);

    /**
     * WHAT: Visitor Pattern seeking the Success Case
     * WHY: Exception Prone Unwrapping Operation
     */
    public abstract T getOrThrow();

    /**
     * WHAT: Visitor Pattern seeking the Success Case
     * WHY: Exception Prone Unwrapping Operation
     */
    public abstract T getOrElse(Supplier<T> otherwise);

    /**
     * WHAT: Visitor Pattern seeking the Success Case
     * WHY: Indicates Success Case
     */


    public abstract Optional<T> get();

    public final boolean isSuccess() {
        return get().isPresent();
    }
}


final class Success<T, Seq, Itm> extends Result<T, Seq, Itm> {
    /**
     * WHY: Storage for actual value
     */
    private final T result;
    /**
     * WHY: For use in composition operations
     */
    private final ParserStream<Seq, Itm> remaining;

    /**
     * WHAT: A package-private constructor of an abstract class to ensure a closed
     * inheritance hierarchy and no base class instances.
     * WHY: Result must either a Success Instance, or a Failure Instance
     */
    Success(T result, ParserStream<Seq, Itm> remaining) {
        this.result = result;
        this.remaining = remaining;
    }

    public T getResult() {
        return result;
    }

    public ParserStream<Seq, Itm> getRemaining() {
        return remaining;
    }

    /**
     * WHAT: Transform result value w/o altering the stream w/ no chance of Failure
     *
     * @see Result#map
     */
    public <U> Result<U, Seq, Itm> map(
            Function<T, U> mapper) {
        var newResult = mapper.apply(getResult());
        return Result.success(newResult, getRemaining());
    }

    /**
     * WHAT: Transform result value and stream w/ chance of Failure
     *
     * @see Result#chain
     */
    public <U> Result<U, Seq, Itm> chain(
            BiFunction<T, ParserStream<Seq, Itm>, Result<U, Seq, Itm>> flatMapper) {
        return flatMapper.apply(getResult(), getRemaining());
    }

    /**
     * WHAT: Nothing in this case
     *
     * @see Result#orElse
     * @see Failure#orElse
     */
    public Result<T, Seq, Itm> orElse(Supplier<Result<T, Seq, Itm>> alternative) {
        return this;
    }

    /**
     * WHAT: return result
     *
     * @see Result#getOrThrow
     * @see Failure#getOrThrow
     */
    public T getOrThrow() {
        return getResult();
    }

    /**
     * WHAT: return result
     *
     * @see Result#getOrElse
     * @see Failure#getOrElse
     */
    public T getOrElse(Supplier<T> otherwise) {
        return getResult();
    }

    /**
     * WHAT: return true
     *
     * @see Result#isSuccess
     * @see Failure#isSuccess
     */
    public Optional<T> get() {
        return Optional.of(getResult());
    }
}

final class Failure<T, Seq, Itm> extends Result<T, Seq, Itm> {
    /**
     * WHY: Explanation of Error
     */
    private final String errorMessage;
    /**
     * WHY: For use in Introspection and Error Handling
     */
    private final ParserStream<Seq, Itm> remaining;

    /**
     * WHAT: A package-private constructor of an abstract class to ensure a closed
     * inheritance hierarchy and no base class instances.
     * WHY: Result must either a Success Instance, or a Failure Instance
     */
    Failure(String errorMessage, ParserStream<Seq, Itm> remaining) {
        this.errorMessage = errorMessage;
        this.remaining = remaining;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ParserStream<Seq, Itm> getRemaining() {
        return remaining;
    }

    /**
     * WHAT: Nothing in this case
     *
     * @see Result#map
     * @see Success#map
     */
    public <U> Result<U, Seq, Itm> map(Function<T, U> mapper) {
        return Result.failure(getErrorMessage(), getRemaining());
    }

    /**
     * WHAT: Nothing in this case
     *
     * @see Result#chain
     * @see Success#chain
     */
    public <U> Result<U, Seq, Itm> chain(
            BiFunction<T, ParserStream<Seq, Itm>, Result<U, Seq, Itm>> flatMapper) {
        return Result.failure(getErrorMessage(), getRemaining());
    }

    /**
     * WHAT: Transform Failure to Success and rollback stream
     *
     * @see Result#orElse
     */
    public Result<T, Seq, Itm> orElse(Supplier<Result<T, Seq, Itm>> alternative) {
        return alternative.get();
    }

    /**
     * WHAT: No Result, Throw Error
     *
     * @see Result#orElse
     * @see Success#orElse
     */
    public T getOrThrow() {
        throw new ParserError(String.format("[%s] : %s",
                getRemaining().getErrorHeader(), getErrorMessage()));
    }

    /**
     * WHAT: No Result, consult supplier
     *
     * @see Result#orElse
     * @see Success#orElse
     */
    public T getOrElse(Supplier<T> otherwise) {
        return otherwise.get();
    }

    /**
     * WHAT: return false
     *
     * @see Result#isSuccess
     * @see Failure#isSuccess
     */
    public Optional<T> get() {
        return Optional.empty();
    }
}