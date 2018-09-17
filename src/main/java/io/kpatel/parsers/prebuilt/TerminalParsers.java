package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.stream.SequenceHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.MiscParsers.flatMap;
import static io.kpatel.parsers.prebuilt.MiscParsers.peek;

/**
 * INTENT: Top Level Generic Factories for Parsers handling ParserStream Primitives
 *
 * @see Parser
 * @see io.kpatel.parsers.stream.ParserStream
 */
public final class TerminalParsers {
    private final static Object eofSentinel = new Object();

    private TerminalParsers() {
    }

    /**
     * USAGE: Create a parser that will fails if their is still input to take in.
     */
    public static <Seq, Itm>
    Supplier<Parser<Object, Seq, Itm>> endOfStream() {
        return () -> stream -> stream.atEndOfStream()
                ? Result.success(eofSentinel, stream)
                : Result.failure(stream.getErrorContext(), () -> "Expected End of Stream");
    }

    /**
     * USAGE: Create a parser that will accept any item
     * - Will fail if at end of stream
     */
    public static <Seq, Itm>
    Supplier<Parser<Itm, Seq, Itm>> item() {
        return () -> stream -> stream.getLeadingItem()
                .map(i -> Result.success(i, stream.jump(1)))
                .orElseGet(() -> Result.failure(stream.getErrorContext(), () -> "Unexpected End of Stream"));
    }

    /**
     * USAGE: Create a parser that will accept an item that satisfy a given predicate
     * - Will fail if at end of stream
     */
    public static <Seq, Itm>
    Supplier<Parser<Itm, Seq, Itm>> item(
            Predicate<Itm> predicate,
            Supplier<String> errorMessage) {
        Objects.requireNonNull(predicate,
                "Predicate must not be null");
        return () -> stream -> stream.getLeadingItem()
                .filter(predicate)
                .map(i -> Result.success(i, stream.jump(1)))
                .orElseGet(() -> Result.failure(stream.getErrorContext(), errorMessage));
    }

    /**
     * USAGE: Create a parser that will accept an item that satisfy a given predicate
     * - Will fail if at end of stream
     * - items are checked with {@link Object#equals(Object)}
     */
    public static <Seq, Itm>
    Supplier<Parser<Itm, Seq, Itm>> item(
            Itm target,
            Supplier<String> errorMessage) {
        Objects.requireNonNull(target,
                "Item must not be null");
        return item(target::equals, errorMessage);
    }

    /**
     * USAGE: Create a parser that will accept an item that is within the given collection
     * - Will fail if at end of stream
     * - items are checked with {@link Object#hashCode()} and {@link Object#equals(Object)}
     */
    public static <Seq, Itm>
    Supplier<Parser<Itm, Seq, Itm>> item(
            Collection<Itm> items,
            Supplier<String> errorMessage) {
        Set<Itm> itemSet = new HashSet<>(items);
        return item(itemSet::contains, errorMessage);
    }

    /**
     * USAGE: Create a parser that will accept the given sequence
     * - items are checked with {@link Object#equals(Object)}
     */
    public static <Seq, Itm>
    Supplier<Parser<Seq, Seq, Itm>> sequence(
            Seq sequence,
            Supplier<String> errorMessage) {
        Objects.requireNonNull(sequence,
                "Sequence must not be null");
        return () -> stream -> {
            SequenceHolder<Seq> holder = stream.holdSequence(sequence);
            int size = holder.getLength();
            SequenceHolder<Seq> leading = stream.getLeadingSequence(size);
            Seq seq = leading.getSequence();
            return seq.equals(sequence)
                    ? Result.success(seq, stream.jump(leading.getLength()))
                    : Result.failure(stream.getErrorContext(), errorMessage);
        };
    }

    /**
     * USAGE: Create a parser that will accept a run of items that satisfy a given predicate,
     * - Will always succeed
     */
    public static <Seq, Itm>
    Supplier<Parser<Seq, Seq, Itm>> optionalRun(
            Predicate<Itm> predicate) {
        Objects.requireNonNull(predicate,
                "Predicate must not be null");
        return () -> stream -> {
            SequenceHolder<Seq> holder = stream.getLeadingRun(predicate);
            int size = holder.getLength();
            return Result.success(holder.getSequence(), stream.jump(size)); };
    }


    /**
     * USAGE: Create a parser that will accept a run of given items
     * - Will always succeed
     * - items are checked with {@link Object#equals(Object)}
     */
    public static <Seq, Itm>
    Supplier<Parser<Seq, Seq, Itm>> optionalRun(
            Itm target) {
        Objects.requireNonNull(target,
                "Item must not be null");
        return optionalRun(target::equals);
    }

    /**
     * USAGE: Create a parser that will accept a run of any items from the give collection
     * - Will always succeed
     * - items are checked with {@link Object#hashCode()} and {@link Object#equals(Object)}
     */
    public static <Seq, Itm>
    Supplier<Parser<Seq, Seq, Itm>> optionalRun(
            Collection<Itm> items) {
        Set<Itm> itemSet = new HashSet<>(items);
        return optionalRun(itemSet::contains);
    }

    /**
     * USAGE: Create a parser that will accept a run of items that satisfy a given predicate,
     * - Will fail if no items satisfy a given predicate
     */
    public static <Seq, Itm>
    Supplier<Parser<Seq, Seq, Itm>> run(
            Predicate<Itm> predicate,
            Supplier<String> errorMessage) {
        Objects.requireNonNull(predicate,
                "Predicate must not be null");
        return flatMap(peek(item(predicate, errorMessage)), t -> optionalRun(predicate));
    }

    /**
     * USAGE: Create a parser that will accept a run of a given item
     * - Will fail if no items match the given item
     * - items are checked with {@link Object#equals(Object)}
     */
    public static <Seq, Itm>
    Supplier<Parser<Seq, Seq, Itm>> run(
            Itm target,
            Supplier<String> errorMessage) {
        Objects.requireNonNull(target,
                "Item must not be null");
        return flatMap(peek(item(target, errorMessage)), t -> optionalRun(target));

    }

    /**
     * USAGE: Create a parser that will accept a run of any items from the give collection
     * - Will fail if no items are found in the collection
     * - items are checked with {@link Object#hashCode()} and {@link Object#equals(Object)}
     */
    public static <Seq, Itm>
    Supplier<Parser<Seq, Seq, Itm>> run(
            Collection<Itm> items,
            Supplier<String> errorMessage) {
        Set<Itm> itemSet = new HashSet<>(items);
        return flatMap(peek(item(itemSet::contains, errorMessage)), t -> optionalRun(itemSet::contains));
    }

}
