package io.kpatel.parsers.string;

import io.kpatel.parsers.Parsers;

import java.util.HashSet;
import java.util.Set;
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
     * WHAT: Forward to Generic Helper Function
     *
     * @see Parsers#endOfStream
     */
    public static StringParser<Void> endOfStream() {
        return (StringParser<Void>) Parsers.<StringParserStream, String, Character>
                endOfStream();
    }

    /**
     * WHAT: Forward to Generic Helper Function
     *
     * @see Parsers#withPrefix
     */
    public static <T> StringParser<T> withPrefix(
            StringParser<?> prefix,
            StringParser<T> parser) {
        return (StringParser<T>) Parsers.withPrefix(prefix, parser);
    }

    /**
     * WHAT: Forward to Generic Helper Function
     *
     * @see Parsers#withPostfix
     */
    public static <T> StringParser<T> withPostfix(
            StringParser<T> parser,
            StringParser<?> postfix) {
        return (StringParser<T>) Parsers.withPostfix(parser, postfix);
    }

    /**
     * WHAT: Forward to Generic Helper Function
     *
     * @see Parsers#between
     */
    public static <T> StringParser<T> between(
            StringParser<?> prefix,
            StringParser<T> parser,
            StringParser<?> postfix) {
        return (StringParser<T>) Parsers.between(prefix, parser, postfix);
    }

    //TODO: EndOfStream
    //TODO: Peek
    //TODO: Alternate

    //TODO: Prefix
    //TODO: Postfix
    //TODO: Between

    //TODO: String Concatenate
    //TODO: List Concatenate
    //TODO: Generic Concatenate

    //TODO: String OneOrMore
    //TODO: List OneOrMore
    //TODO: Generic OneOrMore

    //TODO: String ZeroOrMore
    //TODO: List ZeroOrMore
    //TODO: Generic ZeroOrMore

    //TODO: String Delimited
    //TODO: List Delimited
    //TODO: Generic Delimited

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
     * @see Parsers#item
     */
    public static StringParser<Character> character(
            Predicate<Character> predicate,
            Supplier<String> errorMessage) {
        return Parsers.<StringParserStream, String, Character>item(predicate, errorMessage)::parse;
    }

    /**
     * WHAT: Parse the given Character
     *
     * @see StringParsers#character(Predicate, Supplier)
     */
    public static StringParser<Character> character(
            Character target) {
        return character(target::equals, () -> String.format("Cannot find Character %s", target));
    }

    /**
     * WHAT: Parse Any Character from the given String
     *
     * @see StringParsers#character(Predicate, Supplier)
     */
    public static StringParser<Character> character(
            String characters,
            Supplier<String> errorMessage) {
        Set<Character> characterSet = new HashSet<>(characters.length());
        for (Character c : characters.toCharArray()) {
            characterSet.add(c);
        }
        return character(characterSet::contains, errorMessage);
    }

    /**
     * WHAT: Parse the given String
     *
     * @see Parsers#terminalSequence(Object, Function, Supplier)
     */
    public static StringParser<String> string(String sequence) {
        return Parsers.<StringParserStream, String, Character>
                terminalSequence(sequence, String::length, () -> String.format("Cannot Find String %s", sequence))
                ::parse;
    }
}
