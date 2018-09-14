package io.kpatel.parsers.string;

import io.kpatel.parsers.Parser;

import static io.kpatel.parsers.string.StringParsers.endOfStream;
import static io.kpatel.parsers.string.StringParsers.postfix;

/**
 * WHAT: Specialized Parser Interface for Strings
 */
public interface StringParser<T> extends Parser<T, String, Character> {
    /**
     * WHAT: Helper Function to parse a full string
     */
    default T parse(String sequence) {
        return postfix(this, endOfStream())
                .parse(new StringParserStream(sequence))
                .getOrThrow();
    }
}
