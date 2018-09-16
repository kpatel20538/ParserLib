package io.kpatel.parsers.stream;

import java.util.Objects;

/**
 * WHAT: A generic wrapper for sequences to capture length
 *
 * @see ParserStream
 */
public final class SequenceHolder<Seq> {
    private final int length;
    private final Seq sequence;

    public SequenceHolder(int length, Seq sequence) {
        this.length = length;
        this.sequence = Objects.requireNonNull(sequence,
                "Sequences may not be null");
    }

    public int getLength() {
        return length;
    }

    public Seq getSequence() {
        return sequence;
    }
}
