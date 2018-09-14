package io.kpatel.parsers;

/**
 * WHY:  Provide high-level generic parser composition operations
 * NEEDS:
 * - Top-Level Stateless Operations to combine Parsers
 * TOOLS:
 * - pure static functions
 * - Singleton Pattern (No Instance Variant)
 * - final class
 */
public final class Parsers {
    /** WHY: Prevent un-necessary instances of helper class */
    private Parsers() {
    }

    /**
     * WHAT: A Parser that fails if their is still input to take in.
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<Void, Strm, Seq, Itm> endOfStream() {
        return stream -> stream.atEndOfStream()
                ? Result.success(null, stream)
                : Result.failure("Expected End of Stream", stream);
    }

    /**
     * WHAT: Parse an item with a prefix and omit the prefix
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<T, Strm, Seq, Itm> withPrefix(
            Parser<?, Strm, Seq, Itm> prefix,
            Parser<T, Strm, Seq, Itm> parser) {
        return prefix.chain(pre -> parser);
    }

    /** WHAT: Parse an item with a postfix and omit the postfix */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<T, Strm, Seq, Itm> withPostfix(
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> postfix) {
        return parser.chain(item -> postfix.map(post -> item));
    }

    /** WHAT: Parse an item with a prefix and a postfix and omit both the prefix and the postfix */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<T, Strm, Seq, Itm> between(
            Parser<?, Strm, Seq, Itm> prefix,
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> postfix) {
        return prefix.chain(pre -> parser.chain(item -> postfix.map(post -> item)));
    }
}
