package io.kpatel.parsers;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
    /**
     * WHY: Prevent un-necessary instances of helper class
     */
    private Parsers() {

    }

    /**
     * WHAT: Peek ahead and parse an item without changing the stream.
     */
    public static <T, Prsr extends Parser<T, Strm, Seq, Itm>, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Prsr between(
            Prsr parser) {
        return (Prsr) (Parser<T, Strm, Seq, Itm>) stream1 -> parser.parse(stream1)
                .chain((item, stream2) -> Result.success(item, stream1));
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

    /**
     * WHAT: Parse an item with a postfix and omit the postfix
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<T, Strm, Seq, Itm> withPostfix(
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> postfix) {
        return parser.chain(item -> postfix.map(post -> item));
    }

    /**
     * WHAT: Parse an item with a prefix and a postfix and omit both the prefix and the postfix
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm> Parser<T, Strm, Seq, Itm> between(
            Parser<?, Strm, Seq, Itm> prefix,
            Parser<T, Strm, Seq, Itm> parser,
            Parser<?, Strm, Seq, Itm> postfix) {
        return prefix.chain(pre -> parser.chain(item -> postfix.map(post -> item)));
    }


    //TODO: EndOfStream
    //TODO: Peek
    //TODO: Alternate

    //TODO: Prefix
    //TODO: Postfix
    //TODO: Between

    //TODO: String Concatenate
    //TODO: List Concatenate
    //TODO: Generic Concatenate

    //TODO: String OneOrMore
    //TODO: List OneOrMore
    //TODO: Generic OneOrMore

    //TODO: String ZeroOrMore
    //TODO: List ZeroOrMore
    //TODO: Generic ZeroOrMore

    //TODO: String Delimited
    //TODO: List Delimited
    //TODO: Generic Delimited

    /**
     * WHAT: Attempt to parser a string or else yield an empty String
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<String, Strm, Seq, Itm>
    optional(Parser<String, Strm, Seq, Itm> parser) {
        return optional(parser, () -> "");
    }

    /**
     * WHAT: Attempt to parser an item or else yield a placeholder
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm>
    optional(Parser<T, Strm, Seq, Itm> parser, Supplier<T> placeholder) {
        return stream -> parser.parse(stream)
                .orElse(() -> Result.success(placeholder.get(), stream));
    }

    /**
     * WHAT: Parse an string an omit it
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<String, Strm, Seq, Itm>
    omit(Parser<String, Strm, Seq, Itm> parser) {
        return omit(parser, () -> "");
    }

    /**
     * WHAT: Parse an item an omit it
     */
    public static <T, Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<T, Strm, Seq, Itm>
    omit(Parser<T, Strm, Seq, Itm> parser, Supplier<T> placeholder) {
        return parser.map(item -> placeholder.get());
    }

    /**
     * WHAT: Parse an item that satisfy a given predicate
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Itm, Strm, Seq, Itm>
    item(Predicate<Itm> predicate, Supplier<String> errorMessage) {
        return stream -> stream.getLeadingItem()
                .filter(predicate)
                .map(i -> Result.success(i, stream.jump(1)))
                .orElseGet(() -> Result.failure(errorMessage.get(), stream));
    }

    /**
     * WHAT: Parse as sequence of items
     */
    public static <Strm extends ParserStream<Strm, Seq, Itm>, Seq, Itm>
    Parser<Seq, Strm, Seq, Itm>
    terminalSequence(Seq sequence, Function<Seq, Integer> measureLength, Supplier<String> errorMessage) {
        return stream -> {
            int size = measureLength.apply(sequence);
            Seq leading = stream.getLeadingSequence(size);
            return leading.equals(sequence)
                    ? Result.success(leading, stream.jump(size))
                    : Result.failure(errorMessage.get(), stream);
        };
    }
}
