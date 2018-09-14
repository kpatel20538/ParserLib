package io.kpatel.parsers.list;

import io.kpatel.parsers.ParserStream;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * WHY: Specialize ParserStream for Stream
 */
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
        return position < stream.size()
                ? Optional.of(stream.get(position))
                : Optional.empty();
    }

    @Override
    public List<Tkn> getLeadingSequence(int length) {
        if (0 < length && position < stream.size()) {
            int endPosition = position + length;
            if (stream.size() <= endPosition) {
                endPosition = stream.size() - 1;
            }
            return Collections.unmodifiableList(stream.subList(position, endPosition));
        }
        return Collections.emptyList();
    }

    @Override
    public List<Tkn> getLeadingRun(Predicate<Tkn> predicate) {
        if (position < stream.size()) {
            int endPosition = position;
            while (endPosition < stream.size() && predicate.test(stream.get(endPosition)))
                endPosition++;
            return Collections.unmodifiableList(stream.subList(position, endPosition));
        }
        return Collections.emptyList();
    }

    @Override
    public ListStreamParser<Tkn> jump(int n) {
        if (0 < n) {
            return new ListStreamParser<>(stream, position + n);
        }
        return this;
    }

    @Override
    public String getErrorHeader() {
        return getLeadingItem()
                .map(Object::toString)
                .orElse("End Of Stream");
    }
}

