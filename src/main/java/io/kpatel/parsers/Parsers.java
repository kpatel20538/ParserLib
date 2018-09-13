package io.kpatel.parsers;

public final class Parsers {
    public StringParser<String> stringParser(String word) {
        return stream -> stream.getLeadingSequence(word.length()).equals(word)
                ? Result.success(word, stream.jump(word.length()))
                : Result.failure("No Such Word");
    }
}
