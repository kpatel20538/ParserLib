package io.kpatel.parsers;

public interface Parser<T, Strm extends ParserStream<Strm,Seq,Itm>, Seq, Itm> {
    Result<T, Strm> parse(Strm stream);

    T parse(Seq sequence);
}
