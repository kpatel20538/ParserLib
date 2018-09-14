package io.kpatel.parsers;

/**
 * WHY:   An Indication the parser had a fatal error
 * NEEDS:
 * - A non intrusive, but aggressive error.
 * - Should compose poorly with ParserResult
 * TOOLS:
 * - Subclassing RuntimeException
 * CONSIDER:
 * - Subclassing Exception and using Checked Exceptions
 */
public class ParserError extends RuntimeException {
    public ParserError(String errorMessage) {
        super(errorMessage);
    }
}
