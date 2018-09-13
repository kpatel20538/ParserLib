package io.kpatel.parsers;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class Result<T, Strm extends ParserStream<Strm,?,?>> {
    protected Result() {}

    public static <T, Strm extends ParserStream<Strm,?,?>> Result<T, Strm> success(T result, Strm remaining){
        return null;
    }

    public static <T, Strm extends ParserStream<Strm,?,?>> Result<T, Strm> failure(String msg){
        return null;
    }

    public abstract <U> Result<U, Strm> map(BiFunction<T, Strm, U> transform);
    public abstract <U> Result<U, Strm> mapFailure(Supplier<String> transform);
    public abstract <U> Result<U, Strm> chain(BiFunction<T, Strm, Result<U, Strm>> transform);
    public abstract <U> Result<U, Strm> chainFailure(Supplier<Result<U, Strm>> transform);
    public abstract void raiseIfFailure();
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
        return null;
    }

    public <U> Result<U, Strm> mapFailure(Supplier<String> transform) {
        return null;
    }

    public <U> Result<U, Strm> chain(BiFunction<T, Strm, Result<U, Strm>> transform){
        return null;
    }

    public <U> Result<U, Strm> chainFailure(Supplier<Result<U, Strm>> transform){
        return null;
    }

    public void raiseIfFailure() {

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
        return null;
    }

    public <U> Result<U, Strm> mapFailure(Supplier<String> transform) {
        return null;
    }

    public <U> Result<U, Strm> chain(BiFunction<T, Strm, Result<U, Strm>> transform){
        return null;
    }

    public <U> Result<U, Strm> chainFailure(Supplier<Result<U, Strm>> transform){
        return null;
    }

    public void raiseIfFailure() {

    }
}