package io.kpatel.parsers.list;

import io.kpatel.parsers.Parser;

import java.util.List;

import static io.kpatel.parsers.list.ListParsers.endOfStream;
import static io.kpatel.parsers.list.ListParsers.withPostfix;

public interface ListParser<T, Tkn> extends Parser<T, ListStreamParser<Tkn>, List<Tkn>, Tkn> {

    default T parse(List<Tkn> sequence) {
        return withPostfix(this, endOfStream())
                .parse(new ListStreamParser<>(sequence))
                .getOrThrow();
    }
}
