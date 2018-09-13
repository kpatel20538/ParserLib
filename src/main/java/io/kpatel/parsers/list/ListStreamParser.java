package io.kpatel.parsers.list;

import io.kpatel.parsers.ParserStream;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ListStreamParser<Tkn> implements ParserStream<ListStreamParser<Tkn>, List<Tkn>, Tkn> {
    private final List<Tkn> stream;
    private final int position;

    public ListStreamParser(List<Tkn> stream) {
        this.stream = stream;
        this.position = 0;
    }

    private ListStreamParser(List<Tkn> stream, int position) {
        this.stream = stream;
        this.position = position;
    }

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

    @Override
    public String getErrorHeader() {
        return null;
    }
}

