package io.kpatel.parsers;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class Result<T, Strm extends ParserStream<Strm,?,?>> {
    Result() {}

    public static <T, Strm extends ParserStream<Strm,?,?>> Result<T, Strm> success(T result, Strm remaining){
        return new Success<>(result, remaining);
    }

    public static <T, Strm extends ParserStream<Strm,?,?>> Result<T, Strm> failure(String errorMessage){
        return new Failure<>(errorMessage);
    }

    public abstract <U> Result<U, Strm> map(BiFunction<T, Strm, U> mapper);
    public abstract <U> Result<U, Strm> chain(BiFunction<T, Strm, Result<U, Strm>> flatMapper);
    public abstract Result<T, Strm> orElse(Supplier<Result<T, Strm>> transform);

    public abstract T getOrThrow();
    public abstract T getOrElse(Supplier<T> otherwise);
}

class Success<T, Strm extends ParserStream<Strm,?,?>> extends Result<T, Strm> {
    private final T result;
    private final Strm remaining;

    Success(T result, Strm remaining) {
        this.result = result;
        this.remaining = remaining;
    }

    public T getResult() {
        return result;
    }

    public Strm getRemaining() {
        return remaining;
    }

    public <U> Result<U, Strm> map(BiFunction<T, Strm, U> transform) {
        U newResult = transform.apply(getResult(), getRemaining());
        return Result.success(newResult, getRemaining());
    }

    public <U> Result<U, Strm> chain(BiFunction<T, Strm, Result<U, Strm>> transform) {
        return transform.apply(getResult(), getRemaining());
    }

    public Result<T, Strm> orElse(Supplier<Result<T, Strm>> transform) {
        return this;
    }

    public T getOrThrow() {
        return getResult();
    }

    public T getOrElse(Supplier<T> otherwise) {
        return getResult();
    }
}

class Failure<T, Strm extends ParserStream<Strm,?,?>> extends Result<T, Strm> {
    private final String errorMessage;

    Failure(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public <U> Result<U, Strm> map(BiFunction<T, Strm, U> transform) {
        return Result.failure(getErrorMessage());
    }

    public <U> Result<U, Strm> chain(BiFunction<T, Strm, Result<U, Strm>> transform) {
        return Result.failure(getErrorMessage());
    }

    public Result<T, Strm> orElse(Supplier<Result<T, Strm>> transform) {
        return transform.get();
    }

    public T getOrThrow() {
        throw new ParserError(getErrorMessage());
    }

    public T getOrElse(Supplier<T> otherwise) {
        return otherwise.get();
    }
}