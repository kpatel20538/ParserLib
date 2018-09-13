package io.kpatel.parsers;

public interface StringParser<T> extends Parser<T,StringParserStream,String,Character> {
    default T parse(String sequence) {
        StringParserStream stream = new StringParserStream(sequence);
        Result<T, StringParserStream> result = parse(stream);
        return result.getOrThrow();
    }
}
