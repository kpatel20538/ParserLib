package io.kpatel.parsers;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Parser<T, Strm extends ParserStream<Strm,Seq,Itm>, Seq, Itm> {
    Result<T, Strm> parse(Strm stream);

    default <U> Parser<U, Strm, Seq, Itm> chain(Function<T, Parser<U, Strm, Seq, Itm>> flatMapper) {
        return stream -> parse(stream).chain((t, remaining) -> flatMapper.apply(t).parse(remaining));
    }

    default <U> Parser<U, Strm, Seq, Itm> map(Function<T, U> mapper) {
        return stream -> parse(stream).chain((t, remaining) -> Result.success(mapper.apply(t), remaining));
    }

    default Parser<T, Strm, Seq, Itm> orElse(Supplier<Parser<T, Strm, Seq, Itm>> alternative) {
        return stream -> parse(stream).orElse(() -> alternative.get().parse(stream));
    }
}
