package io.kpatel.parsers.string;

import io.kpatel.parsers.Parsers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * WHAT: Specialized Helper Function for Parsing Strings
 * NEEDS:
 * - Specialize the Generic Helper Functions
 * - Provide String Specific Parser Composing Functions
 */
public final class StringParsers {
    /**
     * WHY: Prevent un-necessary instances of helper class
     */
    private StringParsers() {

    }

    /**
     * WHAT: Peek ahead and parse an item without changing the stream.
     */
    public static <T> StringParser<T> peek(StringParser<T> parser) {
        return Parsers.peek(parser)::parse;
    }

    /**
     * WHAT: A Parser that fails if their is still input to take in.
     *
     * @see Parsers#endOfStream
     */
    public static StringParser<Void> endOfStream() {
        return Parsers.<String, Character>
                endOfStream()::parse;
    }

    /**
     * WHAT: Parse an item unless in falls in the exception case.
     *
     * @see Parsers#exception
     */
    public static <T> StringParser<T> exception(
            StringParser<T> parser,
            Predicate<T> except) {
        return Parsers.exception(parser, except)::parse;
    }

    /**
     * WHAT: Parse until until one parse succeeds or all parsers fail
     *
     * @see Parsers#alternate
     */
    public static <T> StringParser<T> alternate(List<StringParser<T>> parsers) {
        return Parsers.alternate(parsers)::parse;
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty string
     */
    public static StringParser<String> concatenateString(
            List<? extends StringParser<String>> parsers) {
        return Parsers.concatenateString(parsers)::parse;
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty list
     */
    public static <T> StringParser<List<T>> concatenateList(
            List<? extends StringParser<T>> parsers) {
        return Parsers.concatenateList(parsers)::parse;
    }

    /**
     * WHAT: Parse until all parsers succeeds or one parser fail and join the results to the empty case
     */
    public static <T, Bld> StringParser<Bld> concatenate(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            List<? extends StringParser<T>> parsers) {
        return Parsers.concatenate(reduce, empty, parsers)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty string
     */
    public static StringParser<String> zeroOrMoreString(
            StringParser<String> parser) {
        return Parsers.zeroOrMoreString(parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list
     */
    public static <T> StringParser<List<T>> zeroOrMoreList(
            StringParser<T> parser) {
        return Parsers.zeroOrMoreList(parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case
     */
    public static <T, Bld> StringParser<Bld> zeroOrMore(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            StringParser<T> parser) {
        return Parsers.zeroOrMore(reduce, empty, parser)::parse;
    }

    /**
     * WHAT: WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static StringParser<String> oneOrMoreString(
            StringParser<String> parser) {
        return Parsers.oneOrMoreString(parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T> StringParser<List<T>> oneOrMoreList(
            StringParser<T> parser) {
        return Parsers.oneOrMoreList(parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <T, Bld> StringParser<Bld> oneOrMore(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            StringParser<T> parser) {
        return Parsers.oneOrMore(reduce, empty, parser)::parse;
    }

    /**
     * WHAT: WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static StringParser<String> delimitedString(
            StringParser<String> parser,
            StringParser<?> delimiter) {
        return Parsers.delimitedString(parser, delimiter)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <T, Bld> StringParser<Bld> repetition(
            int inclusiveLow, int inclusiveHigh,
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            StringParser<T> parser) {
        return Parsers.repetition(inclusiveLow, inclusiveHigh, reduce, empty, parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T> StringParser<List<T>> delimitedList(
            StringParser<T> parser,
            StringParser<?> delimiter) {
        return Parsers.delimitedList(parser, delimiter)::parse;
    }

    /**
     * WHAT: WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static StringParser<String> repetitionString(
            int count,
            StringParser<String> parser) {
        return Parsers.repetitionString(count, parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T> StringParser<List<T>> repetitionList(
            int count,
            StringParser<T> parser) {
        return Parsers.repetitionList(count, parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <T, Bld> StringParser<Bld> repetition(
            int count,
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            StringParser<T> parser) {
        return Parsers.repetition(count, reduce, empty, parser)::parse;
    }

    /**
     * WHAT: WHAT: Parse a parser until it fails and join the results to the empty string, requires at least one
     */
    public static StringParser<String> repetitionString(
            int inclusiveLow, int inclusiveHigh,
            StringParser<String> parser) {
        return Parsers.repetitionString(inclusiveLow, inclusiveHigh, parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty list, requires at least one
     */
    public static <T> StringParser<List<T>> repetitionList(
            int inclusiveLow, int inclusiveHigh,
            StringParser<T> parser) {
        return Parsers.repetitionList(inclusiveLow, inclusiveHigh, parser)::parse;
    }

    /**
     * WHAT: Parse a parser until it fails and join the results to the empty case, requires at least one
     */
    public static <T, Bld>
    StringParser<Bld> delimited(
            BiFunction<Bld, T, Bld> reduce,
            Supplier<Bld> empty,
            StringParser<T> parser,
            StringParser<?> delimiter) {
        return Parsers.delimited(reduce, empty, parser, delimiter)::parse;
    }

    /**
     * WHAT: Parse an item with a prefix and omit the prefix
     *
     * @see Parsers#prefix
     */
    public static <T> StringParser<T> prefix(
            StringParser<?> before,
            StringParser<T> parser) {
        return Parsers.prefix(before, parser)::parse;
    }

    /**
     * WHAT: Parse an item with a postfix and omit the postfix
     *
     * @see Parsers#postfix
     */
    public static <T> StringParser<T> postfix(
            StringParser<T> parser,
            StringParser<?> after) {
        return Parsers.postfix(parser, after)::parse;
    }

    /**
     * WHAT: Parse an item with a prefix and a postfix and omit both the prefix and the postfix
     *
     * @see Parsers#between
     */
    public static <T> StringParser<T> between(
            StringParser<?> before,
            StringParser<T> parser,
            StringParser<?> after) {
        return Parsers.between(before, parser, after)::parse;
    }



    /**
     * WHAT: Attempt to parser a string or else yield an empty String
     */
    public static StringParser<String> optional(StringParser<String> parser) {
        return Parsers.optional(parser)::parse;
    }

    /**
     * WHAT: Attempt to parser an item or else yield a placeholder
     */
    public static <T> StringParser<T> optional(StringParser<T> parser, Supplier<T> placeholder) {
        return Parsers.optional(parser, placeholder)::parse;
    }

    /**
     * WHAT: Parse an string an omit it
     */
    public static StringParser<String> omit(
            StringParser<String> parser) {
        return Parsers.omit(parser)::parse;
    }

    /**
     * WHAT: Parse an item an omit it
     */
    public static <T> StringParser<T> omit(
            StringParser<T> parser,
            Supplier<T> placeholder) {
        return Parsers.omit(parser, placeholder)::parse;
    }

    /**
     * WHAT: Parse a Character that satisfy a given predicate
     *
     * @see Parsers#terminalItem
     */
    public static StringParser<Character> character(
            Predicate<Character> predicate,
            Supplier<String> errorMessage) {
        return Parsers.<String, Character>terminalItem(predicate, errorMessage)::parse;
    }

    /**
     * WHAT: Parse the given Character
     *
     * @see StringParsers#character(Predicate, Supplier)
     */
    public static StringParser<Character> character(Character target) {
        return character(target::equals, () -> String.format("Cannot find Character %s", target));
    }

    /**
     * WHAT: Parse Any Character from the given String
     *
     * @see StringParsers#character(Predicate, Supplier)
     */
    public static StringParser<Character> character(
            String characters, Supplier<String> errorMessage) {
        Set<Character> characterSet = toCharacterSet(characters);
        return character(characterSet::contains, errorMessage);
    }

    /**
     * WHAT: Parse the given String
     *
     * @see Parsers#terminalSequence(Object, Function, Supplier)
     */
    public static StringParser<String> string(String sequence) {
        return Parsers.<String, Character>
                terminalSequence(sequence, String::length, () -> String.format("Cannot Find String %s", sequence))
                ::parse;
    }

    /**
     * WHAT: Parse a run of characters that satisfy a given predicate, Will always succeed
     */
    public static StringParser<String> run(Predicate<Character> predicate) {
        return Parsers.terminalRun(predicate, String::length)::parse;
    }

    /**
     * WHAT: Parse a run of given character, Will always succeed
     */
    public static StringParser<String> run(Character target) {
        return run(target::equals);
    }

    /**
     * WHAT: Parse a run of any character from the give string, Will always succeed
     */
    public static StringParser<String> run(String characters) {
        Set<Character> characterSet = toCharacterSet(characters);
        return run(characterSet::contains);
    }

    /**
     * WHAT: Parse a run of characters that satisfy a given predicate, Will fail is nothing is found
     */
    public static StringParser<String> nonEmptyRun(Predicate<Character> predicate, Supplier<String> errorMessage) {
        return characterStringConcat(character(predicate, errorMessage), run(predicate));
    }

    /**
     * WHAT: Parse a run of given character, Will fail is nothing is found
     */
    public static StringParser<String> nonEmptyRun(Character target) {
        return characterStringConcat(character(target), run(target));
    }

    /**
     * WHAT: Parse a run of any character from the give string, Will fail is nothing is found
     */
    public static StringParser<String> nonEmptyRun(String characters, Supplier<String> errorMessage) {
        Set<Character> characterSet = toCharacterSet(characters);
        return characterStringConcat(character(characterSet::contains, errorMessage), run(characterSet::contains));
    }

    private static Set<Character> toCharacterSet(String characters) {
        Set<Character> characterSet = new HashSet<>(characters.length());
        for (Character c : characters.toCharArray()) {
            characterSet.add(c);
        }
        return characterSet;
    }

    private static StringParser<String> characterStringConcat(
            StringParser<Character> charParser,
            StringParser<String> strParser) {
        return charParser.chain(c -> strParser.map(str -> c.toString() + str))::parse;
    }
}
