package io.kpatel.parsers.list;

import io.kpatel.parsers.Parser;

import java.util.List;

import static io.kpatel.parsers.Parsers.endOfStream;
import static io.kpatel.parsers.Parsers.postfix;

/**
 * WHAT: Specialized Helper Function for Parsing List of Tokens
 * NEEDS:
 * - Specialize the Generic Helper Functions
 * - Provide List Specific Parser Composing Functions
 */
public final class ListParsers {
    /**
     * WHY: Prevent un-necessary instances of helper class
     */
    private ListParsers() {
    }

    /**
     * WHAT: Helper Function to parse a full string
     */
    public static <T, Tkn> T runParser(
            Parser<T, List<Tkn>, Tkn> parser,
            List<Tkn> sequence) {
        return postfix(parser, endOfStream())
                .parse(new ListStream<>(sequence))
                .getOrThrow();
    }
}
