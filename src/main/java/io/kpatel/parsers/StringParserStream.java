package io.kpatel.parsers;

import java.util.Optional;
import java.util.function.Predicate;

public class StringParserStream implements ParserStream<StringParserStream, String, Character> {
    @Override
    public Optional<Character> getLeadingItem() {
        return Optional.empty();
    }

    @Override
    public String getLeadingSequence(int length) {
        return null;
    }

    @Override
    public String getLeadingRun(Predicate<Character> predicate) {
        return null;
    }

    @Override
    public StringParserStream jump(int n) {
        return null;
    }
}
