package io.kpatel.parsers.list;

import io.kpatel.parsers.Parser;

import java.util.List;

import static io.kpatel.parsers.list.ListParsers.endOfStream;
import static io.kpatel.parsers.list.ListParsers.withPostfix;

/**
 * WHAT: Specialized Parser Interface for Strings
 */
public interface ListParser<T, Tkn> extends Parser<T, List<Tkn>, Tkn> {
    /**
     * WHAT: Helper Function to parse a full string
     */
    default T parse(List<Tkn> sequence) {
        return withPostfix(this, endOfStream())
                .parse(new ListParserStream<>(sequence))
                .getOrThrow();
    }
}
