package io.kpatel.parsers.list;

import io.kpatel.parsers.Parsers;

import java.util.List;

/**
 * WHAT: Specialized Helper Function for Parsing List of Tokens
 * NEEDS:
 * - Specialize the Generic Helper Functions
 * - Provide List Specific Parser Composing Functions
 */
public final class ListParsers {
    /** WHY: Prevent un-necessary instances of helper class */
    private ListParsers() {
    }

    /** WHAT: Forward to Generic Helper Function
     *  @see Parsers#endOfStream
     */
    public static <Tkn> ListParser<Void, Tkn> endOfStream() {
        return (ListParser<Void, Tkn>) Parsers.<ListParserStream<Tkn>, List<Tkn>, Tkn>endOfStream();
    }

    /**
     * WHAT: Forward to Generic Helper Function
     *
     * @see Parsers#withPrefix
     */
    public static <T, Tkn> ListParser<T, Tkn> withPrefix(
            ListParser<?, Tkn> prefix,
            ListParser<T, Tkn> parser) {
        return (ListParser<T, Tkn>) Parsers.withPrefix(prefix, parser);
    }

    /** WHAT: Forward to Generic Helper Function
     *  @see Parsers#withPostfix
     */
    public static <T, Tkn> ListParser<T, Tkn> withPostfix(
            ListParser<T, Tkn> parser,
            ListParser<?, Tkn> postfix) {
        return (ListParser<T, Tkn>) Parsers.withPostfix(parser, postfix);
    }

    /** WHAT: Forward to Generic Helper Function
     *  @see Parsers#between
     */
    public static <T, Tkn> ListParser<T, Tkn> between(
            ListParser<?, Tkn> prefix,
            ListParser<T, Tkn> parser,
            ListParser<?, Tkn> postfix) {
        return (ListParser<T, Tkn>) Parsers.between(prefix, parser, postfix);
    }
}
