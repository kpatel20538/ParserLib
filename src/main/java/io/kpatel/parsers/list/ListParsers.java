package io.kpatel.parsers.list;

import io.kpatel.parsers.Parsers;

import java.util.List;

public class ListParsers {
    public static <Tkn> ListParser<Void, Tkn> endOfStream() {
        return (ListParser<Void, Tkn>) Parsers.<ListStreamParser<Tkn>, List<Tkn>, Tkn>endOfStream();
    }

    public static <T, Tkn> ListParser<T, Tkn> withPrefix(
            ListParser<?, Tkn> prefix,
            ListParser<T, Tkn> parser) {
        return (ListParser<T, Tkn>) Parsers.withPrefix(prefix, parser);
    }

    public static <T, Tkn> ListParser<T, Tkn> withPostfix(
            ListParser<T, Tkn> parser,
            ListParser<?, Tkn> postfix) {
        return (ListParser<T, Tkn>) Parsers.withPostfix(parser, postfix);
    }

    public static <T, Tkn> ListParser<T, Tkn> between(
            ListParser<?, Tkn> prefix,
            ListParser<T, Tkn> parser,
            ListParser<?, Tkn> postfix) {
        return (ListParser<T, Tkn>) Parsers.between(prefix, parser, postfix);
    }
}
