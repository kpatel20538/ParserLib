package io.kpatel.parsers.stream;

public final class SequenceHolder<Seq> {
    private final int length;
    private final Seq sequence;

    public SequenceHolder(int length, Seq sequence) {
        this.length = length;
        this.sequence = sequence;
    }

    public int getLength() {
        return length;
    }

    public Seq getSequence() {
        return sequence;
    }
}
