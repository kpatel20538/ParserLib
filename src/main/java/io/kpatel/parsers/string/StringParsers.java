package io.kpatel.parsers.string;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.Parsers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.kpatel.parsers.Parsers.endOfStream;
import static io.kpatel.parsers.Parsers.postfix;

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
     * WHAT: Helper Function to parse a full string
     */
    public static <T> T runParser(Parser<T, String, Character> parser, String sequence) {
        return postfix(parser, endOfStream())
                .parse(new StringParserStream(sequence))
                .getOrThrow();
    }

    /**
     * WHAT: Parse a Character that satisfy a given predicate
     *
     * @see Parsers#terminalItem
     */
    public static Parser<Character, String, Character> character(
            Predicate<Character> predicate,
            Supplier<String> errorMessage) {
        return Parsers.<String, Character>terminalItem(predicate, errorMessage);
    }

    /**
     * WHAT: Parse the given Character
     *
     * @see StringParsers#character(Predicate, Supplier)
     */
    public static Parser<Character, String, Character> character(Character target) {
        return character(target::equals, () -> String.format("Cannot find Character %s", target));
    }

    /**
     * WHAT: Parse Any Character from the given String
     *
     * @see StringParsers#character(Predicate, Supplier)
     */
    public static Parser<Character, String, Character> character(String characters) {
        Set<Character> characterSet = toCharacterSet(characters);
        return character(characterSet::contains, () -> String.format("Cannot find character from set '%s'", characters));
    }

    /**
     * WHAT: Parse the given String
     *
     * @see Parsers#terminalSequence(Object, Function, Supplier)
     */
    public static Parser<String, String, Character> string(String sequence) {
        return Parsers.<String, Character>
                terminalSequence(sequence, String::length, () -> String.format("Cannot Find String %s", sequence));
    }

    /**
     * WHAT: Parse a run of characters that satisfy a given predicate, Will fail is nothing is found
     */
    public static Parser<String, String, Character> run(Predicate<Character> predicate, Supplier<String> errorMessage) {
        return Parsers.terminalRun(predicate, String::length, errorMessage);
    }

    /**
     * WHAT: Parse a run of given character, Will fail is nothing is found
     */
    public static Parser<String, String, Character> run(Character target) {
        return Parsers.terminalRun(target, String::length, () -> String.format("Cannot find character '%s'", target));
    }

    /**
     * WHAT: Parse a run of any character from the give string, Will fail is nothing is found
     */
    public static Parser<String, String, Character> run(String characters) {
        Set<Character> characterSet = toCharacterSet(characters);
        return Parsers.terminalRun(characterSet, String::length, () -> String.format("Cannot find character from set '%s'", characters));
    }

    /**
     * WHAT: Parse a run of characters that satisfy a given predicate, Will always succeed
     */
    public static Parser<String, String, Character> optionalRun(Predicate<Character> predicate) {
        return Parsers.terminalOptionalRun(predicate, String::length);
    }

    /**
     * WHAT: Parse a run of given character, Will always succeed
     */
    public static Parser<String, String, Character> optionalRun(Character target) {
        return Parsers.terminalOptionalRun(target, String::length);
    }

    /**
     * WHAT: Parse a run of any character from the give string, Will always succeed
     */
    public static Parser<String, String, Character> optionalRun(String characters) {
        Set<Character> characterSet = toCharacterSet(characters);
        return Parsers.terminalOptionalRun(characterSet, String::length);
    }

    private static Set<Character> toCharacterSet(String characters) {
        Set<Character> characterSet = new HashSet<>(characters.length());
        for (Character c : characters.toCharArray()) {
            characterSet.add(c);
        }
        return characterSet;
    }
}
