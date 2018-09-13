package io.kpatel.parsers;

public class ParserError extends RuntimeException {
    public ParserError(String errorMessage) {
        super(errorMessage);
    }
}
