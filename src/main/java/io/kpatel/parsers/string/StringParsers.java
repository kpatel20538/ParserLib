package io.kpatel.parsers.string;

import io.kpatel.parsers.Parsers;
import io.kpatel.parsers.Result;

/**
 * WHAT: Specialized Helper Function for Parsing Strings
 * NEEDS:
 * - Specialize the Generic Helper Functions
 * - Provide String Specific Parser Composing Functions
 */
public final class StringParsers {
    /**
     * WHY: Prevent un-necessary instances of helper class
     */
    private StringParsers() {
    }

    /**
     * WHAT: Parse a given sequence of characters
     */
    public static StringParser<String> sequence(String word) {
        return stream -> stream.getLeadingSequence(word.length()).equals(word)
                ? Result.success(word, stream.jump(word.length()))
                : Result.failure("No Such Word", stream);
    }

    /** WHAT: Forward to Generic Helper Function
     *  @see Parsers#endOfStream
     */
    public static StringParser<Void> endOfStream() {
        return (StringParser<Void>) Parsers.<StringParserStream, String, Character>endOfStream();
    }

    /** WHAT: Forward to Generic Helper Function
     *  @see Parsers#withPrefix
     */
    public static <T> StringParser<T> withPrefix(
            StringParser<?> prefix,
            StringParser<T> parser) {
        return (StringParser<T>) Parsers.withPrefix(prefix, parser);
    }

    /** WHAT: Forward to Generic Helper Function
     *  @see Parsers#withPostfix
     */
    public static <T> StringParser<T> withPostfix(
            StringParser<T> parser,
            StringParser<?> postfix) {
        return (StringParser<T>) Parsers.withPostfix(parser, postfix);
    }

    /** WHAT: Forward to Generic Helper Function
     *  @see Parsers#between
     */
    public static <T> StringParser<T> between(
            StringParser<?> prefix,
            StringParser<T> parser,
            StringParser<?> postfix) {
        return (StringParser<T>) Parsers.between(prefix, parser, postfix);
    }
}
