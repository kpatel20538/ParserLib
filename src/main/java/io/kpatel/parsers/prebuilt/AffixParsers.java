package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;

public final class AffixParsers {
    private AffixParsers() {

    }

    /**
     * WHAT: Parse an item with a prefix and omit the prefix
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> prefix(
            Parser<?, Seq, Itm> before,
            Parser<T, Seq, Itm> parser) {
        return Parsers.pipe(before, parser,
                (pre, root) -> root);
    }

    /**
     * WHAT: Parse an item with a postfix and omit the postfix
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> postfix(
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> after) {
        return Parsers.pipe(parser, after,
                (root, post) -> root);
    }

    /**
     * WHAT: Parse an item with a prefix and a postfix and omit both the prefix and the postfix
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> between(
            Parser<?, Seq, Itm> before,
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> after) {
        return before.chain(
                pre -> parser.chain(
                        item -> after.map(
                                post -> item)));
    }
}
