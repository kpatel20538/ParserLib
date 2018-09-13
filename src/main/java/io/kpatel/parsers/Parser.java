package io.kpatel.parsers;

public interface Parser<T, Strm extends ParserStream<Strm,Seq,Itm>, Seq, Itm> {
    Result<T, Strm> parse(Strm stream);

    default T parse(String text) {
        // TODO: Implement Parser<T>.parse(String) -> ParseResult<T>
        return null;
    }
}
