package io.kpatel.parsers;

import java.util.List;

public interface ListParser<Tkn, T> extends Parser<T,ListStreamParser<Tkn>, List<Tkn>, Tkn> {

    default T parse(List<Tkn> sequence) {
        ListStreamParser<Tkn> stream = new ListStreamParser<>(sequence);
        Result<T, ListStreamParser<Tkn>> result = parse(stream);
        return result.getOrThrow();
    }
}
