package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;

import java.util.Objects;
import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.MiscParsers.flatMap;
import static io.kpatel.parsers.prebuilt.MiscParsers.map;

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
    public static <T, U, Seq, Itm>
    Supplier<Parser<T, Seq, Itm>> prefix(
            Supplier<Parser<U, Seq, Itm>> before,
            Supplier<Parser<T, Seq, Itm>> parser) {
        Objects.requireNonNull(before,
                "Before Parser must not be null");
        Objects.requireNonNull(parser,
                "Parser must not be null");
        return flatMap(before, p -> parser);
    }

    /**
     * USAGE: Create a parser that will accept a root and a suffix, and yield the root
     */
    public static <T, V, Seq, Itm>
    Supplier<Parser<T, Seq, Itm>> suffix(
            Supplier<Parser<T, Seq, Itm>> parser,
            Supplier<Parser<V, Seq, Itm>> after) {
        Objects.requireNonNull(parser,
                "Before Parser must not be null");
        Objects.requireNonNull(after,
                "Before Parser must not be null");
        return flatMap(parser, p -> map(after, a -> p));
    }

    /**
     * USAGE: Create a parser that will accept a prefix, a root and a suffix, and yield the root
     */
    public static <T, U, V, Seq, Itm>
    Supplier<Parser<T, Seq, Itm>> between(
            Supplier<Parser<U, Seq, Itm>> before,
            Supplier<Parser<T, Seq, Itm>> parser,
            Supplier<Parser<V, Seq, Itm>> after) {
        Objects.requireNonNull(before,
                "Before Parser must not be null");
        Objects.requireNonNull(parser,
                "Before Parser must not be null");
        Objects.requireNonNull(after,
                "Before Parser must not be null");
        return prefix(before, suffix(parser, after));
    }
}
