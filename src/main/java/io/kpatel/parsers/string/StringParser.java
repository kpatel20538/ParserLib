package io.kpatel.parsers.string;

import io.kpatel.parsers.Parser;

import static io.kpatel.parsers.string.StringParsers.endOfStream;
import static io.kpatel.parsers.string.StringParsers.withPostfix;

/**
 * WHAT: Specialized Parser Interface for Strings
 */
public interface StringParser<T> extends Parser<T,StringParserStream,String,Character> {
    /**
     * WHAT: Helper Function to parse a full string
     */
    default T parse(String sequence) {
        return withPostfix(this, endOfStream())
                .parse(new StringParserStream(sequence))
                .getOrThrow();
    }
}
