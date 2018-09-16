package io.kpatel.parsers;

/**
 * WHAT: An Indication the parser had a fatal error
 * INTENT:
 * - A non intrusive, but aggressive error.
 * - Should compose poorly with ParserResult to prevent a
 *   parsing on an invalid stream
 * TECHNIQUES:
 * - Subclassing RuntimeException
 * CONSIDER:
 * - Subclassing Exception and using Checked Exceptions
 */
public final class ParserError extends RuntimeException {
    public ParserError(String errorMessage) {
        super(errorMessage);
    }
}
