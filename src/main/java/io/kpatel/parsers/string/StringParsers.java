package io.kpatel.parsers.string;

import io.kpatel.parsers.Parsers;
import io.kpatel.parsers.Result;

public final class StringParsers {
    private StringParsers() {
    }

    public static StringParser<String> sequence(String word) {
        return stream -> stream.getLeadingSequence(word.length()).equals(word)
                ? Result.success(word, stream.jump(word.length()))
                : Result.failure("No Such Word", stream);
    }

    public static StringParser<Void> endOfStream() {
        return (StringParser<Void>) Parsers.<StringParserStream, String, Character>endOfStream();
    }

    public static <T> StringParser<T> withPrefix(
            StringParser<?> prefix,
            StringParser<T> parser) {
        return (StringParser<T>) Parsers.withPrefix(prefix, parser);
    }

    public static <T> StringParser<T> withPostfix(
            StringParser<T> parser,
            StringParser<?> postfix) {
        return (StringParser<T>) Parsers.withPostfix(parser, postfix);
    }

    public static <T> StringParser<T> between(
            StringParser<?> prefix,
            StringParser<T> parser,
            StringParser<?> postfix) {
        return (StringParser<T>) Parsers.between(prefix, parser, postfix);
    }
}
