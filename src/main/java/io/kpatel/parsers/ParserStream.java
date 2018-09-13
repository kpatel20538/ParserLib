package io.kpatel.parsers;

import java.util.Optional;
import java.util.function.Predicate;

public interface ParserStream<Self, Seq, Itm> {
    Optional<Itm> getLeadingItem();
    Seq getLeadingSequence(int length);
    Seq getLeadingRun(Predicate<Itm> predicate);
    Self jump(int n);

    String getErrorHeader();

    default boolean atEndOfStream() {return !getLeadingItem().isPresent();}
}
