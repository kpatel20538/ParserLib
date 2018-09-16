package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;

import java.util.Objects;

import static io.kpatel.parsers.prebuilt.MiscParsers.pipe;

/**
 * INTENT: Top Level Generic Factories for Parsers handling Prefixes, and Suffixes
 *
 * @see Parser
 */
public final class AffixParsers {
    private AffixParsers() {

    }

    /**
     * USAGE: Create a parser that will accept a prefix and a root, and yield the root
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> prefix(
            Parser<?, Seq, Itm> before,
            Parser<T, Seq, Itm> parser) {
        Objects.requireNonNull(before,
                "Before Parser must not be null");
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return pipe(before, parser, (pre, root) -> root);
    }

    /**
     * USAGE: Create a parser that will accept a root and a suffix, and yield the root
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> suffix(
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> after) {
        Objects.requireNonNull(parser,
                "Before Parser must not be null");
        Objects.requireNonNull(after,
                "Before Parser must not be null");
        return pipe(parser, after, (root, post) -> root);
    }

    /**
     * USAGE: Create a parser that will accept a prefix, a root and a suffix, and yield the root
     */
    public static <T, Seq, Itm>
    Parser<T, Seq, Itm> between(
            Parser<?, Seq, Itm> before,
            Parser<T, Seq, Itm> parser,
            Parser<?, Seq, Itm> after) {
        Objects.requireNonNull(before,
                "Before Parser must not be null");
        Objects.requireNonNull(parser,
                "Before Parser must not be null");
        Objects.requireNonNull(after,
                "Before Parser must not be null");
        return prefix(before, suffix(parser, after));
    }
}
