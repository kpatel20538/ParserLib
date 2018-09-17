package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.kpatel.parsers.prebuilt.AffixParsers.suffix;
import static io.kpatel.parsers.prebuilt.MiscParsers.*;
import static io.kpatel.parsers.prebuilt.TerminalParsers.*;


/**
 * INTENT: Top Level Generic Factories for Parsers working with Strings and Character
 * @see Parser
 */
public final class StringParsers {
    private StringParsers() {

    }

    /**
     * USAGE: Create a parser map accepts the end of stream and yield the NULL character.
     */
    public static Supplier<Parser<Character, String, Character>> endOfFile() {
        return map(endOfStream(), v -> '\0');
    }

    /**
     * USAGE: Create a parser map accepts newlines groupings as well as end of file.
     */
    public static Supplier<Parser<Character, String, Character>> endOfLine() {
        var lineEnd = TerminalParsers.<String, Character>item('\n', () -> "Cannot Find Newline");
        var lineFeed = TerminalParsers.<String, Character>item('\r', () -> "Cannot Find LineFeed");
        var optLineFeed = optional(lineFeed, () -> '\0');
        return alternate(List.of(
                suffix(lineEnd, optLineFeed),
                lineFeed,
                endOfFile()
        ));
    }

    /**
     * USAGE: Create a parser that accepts the given full term, not just a prefix.
     */
    public static Supplier<Parser<String, String, Character>> term(String word) {
        Objects.requireNonNull(word, "Word must not be null");
        return suffix(sequence(
                word, () -> String.format("Cannot Find Word '%s'", word)),
                peek(wordBoundary()));
    }

    /**
     * USAGE: Create a parser that accepts one of the given words
     */
    public static Supplier<Parser<String, String, Character>> keywords(
            Collection<String> words) {
        var wordList = new ArrayList<>(words);
        List<Supplier<? extends Parser<? extends String, String, Character>>>
                keywordParsers = new ArrayList<>();

        // Sort by Length Descending order, then Natural Order in Ascending Order
        wordList.sort(Comparator.comparing(String::length).reversed()
                .thenComparing(Comparator.naturalOrder()));

        for (String word : wordList) {
            keywordParsers.add(term(word));
        }

        return alternate(keywordParsers);
    }

    /**
     * USAGE: Create a parser that accepts an Letter Characters
     */
    public static Supplier<Parser<Character, String, Character>> letter() {
        return item(Character::isLetter,
                () -> "Expected a Letter Character");
    }

    /**
     * USAGE: Create a parser that accepts an Digit Characters
     */
    public static Supplier<Parser<Character, String, Character>> digit() {
        return item(Character::isDigit,
                () -> "Expected a Digit Character");
    }

    /**
     * USAGE: Create a parser that accepts an Alphanumeric Characters
     */
    public static Supplier<Parser<Character, String, Character>> alphanumeric() {
        return item(Character::isLetterOrDigit,
                () -> "Expected a Alpha Numeric Character");
    }


    /**
     * USAGE: Create a parser that accepts a run of Whitespace Characters, (including newlines)
     */
    public static Supplier<Parser<String, String, Character>> whitespace() {
        return optionalRun(Character::isSpaceChar);
    }

    /**
     * USAGE: Create a parser that accepts a run of Letter Characters
     */
    public static Supplier<Parser<String, String, Character>> letters() {
        return optionalRun(Character::isLetter);
    }

    /**
     * USAGE: Create a parser that accepts a run of Digit Characters
     */
    public static Supplier<Parser<String, String, Character>> digits() {
        return optionalRun(Character::isDigit);
    }

    /**
     * USAGE: Create a parser that accepts a run of Alphanumeric Characters
     */
    public static Supplier<Parser<String, String, Character>> alphanumerics() {
        return optionalRun(Character::isLetterOrDigit);
    }

    /**
     * USAGE: Create the edge of word, must be used when last character matched was alphanumeric
     */
    private static Supplier<Parser<Character, String, Character>> wordBoundary() {
        return otherwise(nonAlphanumeric(), endOfFile());
    }

    /**
     * USAGE: Create a parser that does not accepts an Alphanumeric Characters
     */
    private static Supplier<Parser<Character, String, Character>> nonAlphanumeric() {
        Predicate<Character> predicate = Character::isLetterOrDigit;
        return item(predicate.negate(),
                () -> "Cannot Find Word Boundary");
    }
}
