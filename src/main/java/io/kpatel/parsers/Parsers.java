package io.kpatel.parsers;


public final class Parsers {
    private Parsers() {
    }

    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<Void, Strm, Seq, Itm> endOfStream() {
        return stream -> stream.atEndOfStream()
                ? Result.success(null, stream)
                : Result.failure("Expected End of Stream", stream);
    }

    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<T, Strm, Seq, Itm> withPrefix(
            Parser<?, Strm, Seq, Itm> prefix,
            Parser<T, Strm, Seq, Itm> parser) {
        return prefix.chain(pre -> parser);
    }

    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<T, Strm, Seq, Itm> withPostfix(
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> postfix) {
        return parser.chain(item -> postfix.map(post -> item));
    }

    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<T, Strm, Seq, Itm> between(
            Parser<?, Strm, Seq, Itm> prefix,
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> postfix) {
        return prefix.chain(pre -> parser.chain(item -> postfix.map(post -> item)));
    }
}
