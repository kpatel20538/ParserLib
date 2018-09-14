package io.kpatel.parsers;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * WHY: Generalize the Forward Iteration of a Linear Structure
 * NEEDS:
 * - Rollback by Reference
 * - Forward Seek
 * - Non Destructive Query Operations
 * - Provide Context when Error Occurs
 * - Extensibility
 * TOOLS:
 * - Lambda Expressions
 * - Self Recurring Generics
 * - Immutable to Rollback by Reference
 * CONSIDER:
 * - Deferring ParserStream::getLeadingSequence and ParserStream::getLeadingRun to StringParserStream
 */
public interface ParserStream<Self, Seq, Itm> {
    /**
     * WHY: Non Destructive Query Operation for Items
     */
    Optional<Itm> getLeadingItem();

    /**
     * WHY: Non Destructive Query Operation for End of Stream
     */
    default boolean atEndOfStream() {
        return !getLeadingItem().isPresent();
    }

    /**
     * WHY: Non Destructive Query Operation for Fixed-Length SubSequences
     */
    Seq getLeadingSequence(int length);

    /**
     * WHY: Non Destructive Query Operation for Variable-Length SubSequences
     */
    Seq getLeadingRun(Predicate<Itm> predicate);

    /**
     * WHY: Forward Seek, Needs to Self-Recurring Generics to Preserve Extensibility
     */
    Self jump(int n);

    /**
     * WHY: Provide Context When  an Error Occurs
     */
    String getErrorHeader();
}
