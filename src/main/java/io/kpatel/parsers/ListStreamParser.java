package io.kpatel.parsers;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ListStreamParser<Tkn> implements ParserStream<ListStreamParser<Tkn>, List<Tkn>, Tkn> {
    @Override
    public Optional<Tkn> getLeadingItem() {
        return Optional.empty();
    }

    @Override
    public List<Tkn> getLeadingSequence(int length) {
        return null;
    }

    @Override
    public List<Tkn> getLeadingRun(Predicate<Tkn> predicate) {
        return null;
    }

    @Override
    public ListStreamParser<Tkn> jump(int n) {
        return null;
    }
}

