package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Result;
import io.kpatel.parsers.stream.SequenceHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class TerminalParsers {
    private final static Object eofSentinel = new Object();

    private TerminalParsers() {
    }

    /**
     * WHAT: A Parser that fails if their is still input to take in.
     */
    public static <Seq, Itm>
    Parser<Object, Seq, Itm> endOfStream() {
        return stream -> stream.atEndOfStream()
                ? Result.success(eofSentinel, stream)
                : Result.failure("Expected End of Stream", stream);
    }

    /**
     * WHAT: Parse an item that satisfy a given predicate
     */
    public static <Seq, Itm>
    Parser<Itm, Seq, Itm> item(
            Predicate<Itm> predicate,
            Supplier<String> errorMessage) {
        return stream -> stream.getLeadingItem()
                .filter(predicate)
                .map(i -> Result.success(i, stream.jump(1)))
                .orElseGet(() -> Result.failure(errorMessage.get(), stream));
    }

    /**
     * WHAT: Parse the given Character
     *
     * @see StringParsers(Predicate, Supplier)
     */
    public static <Seq, Itm>
    Parser<Itm, Seq, Itm> item(
            Itm target,
            Supplier<String> errorMessage) {
        return item(target::equals, errorMessage);
    }

    /**
     * WHAT: Parse Any Character from the given String
     *
     * @see StringParsers(Predicate, Supplier)
     */
    public static <Seq, Itm>
    Parser<Itm, Seq, Itm> item(
            Collection<Itm> items,
            Supplier<String> errorMessage) {
        Set<Itm> itemSet = new HashSet<>(items);
        return item(itemSet::contains, errorMessage);
    }

    /**
     * WHAT: Parse as sequence of items
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> sequence(
            Seq sequence,
            Supplier<String> errorMessage) {
        return stream -> {
            SequenceHolder<Seq> sequenceHolder = stream.holdSequence(sequence);
            int size = sequenceHolder.getLength();
            SequenceHolder<Seq> leadingHolder = stream.getLeadingSequence(size);
            Seq leading = leadingHolder.getSequence();
            return leading.equals(sequence)
                    ? Result.success(leading, stream.jump(leadingHolder.getLength()))
                    : Result.failure(errorMessage.get(), stream);
        };
    }

    /**
     * WHAT: Parse a run of item that satisfy a given predicate, Will always succeed
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> optionalRun(
            Predicate<Itm> predicate) {
        return stream -> {
            SequenceHolder<Seq> holder = stream.getLeadingRun(predicate);
            int size = holder.getLength();
            return Result.success(holder.getSequence(), stream.jump(size));
        };
    }

    /**
     * WHAT: Parse a run of given character, Will always succeed
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> optionalRun(
            Itm target) {
        return optionalRun(target::equals);
    }

    /**
     * WHAT: Parse a run of any character from the give string, Will always succeed
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> optionalRun(
            Collection<Itm> items) {
        Set<Itm> itemSet = new HashSet<>(items);
        return optionalRun(itemSet::contains);
    }

    /**
     * WHAT: Parse a run of characters that satisfy a given predicate, Will fail is nothing is found
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> run(
            Predicate<Itm> predicate,
            Supplier<String> errorMessage) {
        return Parsers.<Itm, Seq, Itm>peek(item(predicate, errorMessage))
                .chain(t -> optionalRun(predicate));
    }

    /**
     * WHAT: Parse a run of given character, Will fail is nothing is found
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> run(
            Itm target,
            Supplier<String> errorMessage) {
        return Parsers.<Itm, Seq, Itm>peek(item(target, errorMessage))
                .chain(t -> optionalRun(target));
    }

    /**
     * WHAT: Parse a run of any character from the give string, Will fail is nothing is found
     */
    public static <Seq, Itm>
    Parser<Seq, Seq, Itm> run(
            Collection<Itm> items,
            Supplier<String> errorMessage) {
        Set<Itm> itemSet = new HashSet<>(items);
        return Parsers.<Itm, Seq, Itm>peek(item(itemSet::contains, errorMessage))
                .chain(t -> optionalRun(itemSet::contains));
    }

}
