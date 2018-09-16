package io.kpatel.parsers.stream;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * INTENT: A Generic Interface for user defined linear streams can implement so
 *   that they can interact with {@link io.kpatel.parsers.Parser}
 * REQUIREMENTS:
 * - All methods must be effectively pure
 * - No method return null
 * - No Backtracking through the Stream.
 * RECOMMENDATIONS:
 * - Use {@link #getErrorContext} to represent the current stream state
 */
public interface ParserStream<Seq, Itm> {

    /**
     * INTENT: Non Destructive Query Operation for the leading item in the stream
     * REQUIREMENTS:
     * - must be effectively pure
     * - return type must not be null
     * - If the stream is empty, return Optional.empty
     */
    Optional<Itm> getLeadingItem();

    /**
     * INTENT: Non Destructive Query Operation for checking if a Stream is empty
     * REQUIREMENTS:
     * - must be effectively pure
     * - return type must not be null
     */
    default boolean atEndOfStream() {
        return !getLeadingItem().isPresent();
    }

    //TODO: Reconsider to Throw Exception for Negative Lengths
    /**
     * INTENT: Non Destructive Query Operation for a Sequence up to the given length
     * starting at the Stream's current "position"
     * REQUIREMENTS:
     * - must be effectively pure
     * - return type must not be null
     * - the length given to SequenceHolder must represent the length of
     *   Sequence
     * - Should the length of Sequence requested exceed the Number of Items in
     *   the stream, return a SequenceHolder with all the remaining items, and
     *   the number of remaining items.
     * - Negative lengths are floored to 0
     * RECOMMENDATION:
     * - When Implementing, make use of the holdSequence method to measure
     *   your Sequences with consistency
     */
    SequenceHolder<Seq> getLeadingSequence(int length);

    /**
     * INTENT: Non Destructive Query Operation for a Sequence of Items who satisfy
     * the condition starting at the Stream's current "position"
     * REQUIREMENTS:
     * - must be effectively pure
     * - return type must not be null
     * - the length given to SequenceHolder must represent the length of
     *   Sequence
     * RECOMMENDATION:
     * - When Implementing, make use of the holdSequence method to measure
     *   your Sequences with consistency
     */
    SequenceHolder<Seq> getLeadingRun(Predicate<Itm> predicate);

    /**
     * INTENT: Helper Function to get metadata about a sequence
     * REQUIREMENTS:
     * - must be effectively pure
     * - return type must not be null
     * - the length given to SequenceHolder must represent the length of
     *   Sequence
     * RECOMMENDATION:
     * - When Implementing, use this method as factory for SequenceHolder
     *   to measure your Sequences with consistency
     */
    SequenceHolder<Seq> holdSequence(Seq sequence);

    //TODO: Reconsider to Throw Exception for Negative Jumps
    /**
     * INTENT: Create a new ParserStream which is n Items "ahead" of this ParserStream
     * REQUIREMENTS:
     * - must be effectively pure
     * - return type must not be null
     * - Should the requested jump exceed the Number of Items in the stream,
     *   return a ParserStream representing an empty Stream.
     * - Negative Jump Requests are floored to 0
     * - may only return self is n <= 0
     * RECOMMENDATION:
     * - When Implementing, Consider Updating any debugging information here.
     */
    ParserStream<Seq, Itm> jump(int n);

    /**
     * INTENT: Lazily Provide Context When an Error Occurs, For use in
     * {@link io.kpatel.parsers.Result}
     *
     * REQUIREMENT:
     * - return type must not be null
     * - must be effectively pure
     * - supplier return type must not be null
     * - supplier must be pure
     */
    Supplier<String> getErrorContext();
}
