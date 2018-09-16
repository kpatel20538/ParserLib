package io.kpatel.parsers.prebuilt;

import io.kpatel.parsers.Parser;
import io.kpatel.parsers.stream.StringStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

import static io.kpatel.parsers.prebuilt.AffixParsers.postfix;
import static io.kpatel.parsers.prebuilt.Parsers.alternate;
import static io.kpatel.parsers.prebuilt.Parsers.peek;
import static io.kpatel.parsers.prebuilt.TerminalParsers.*;


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
        return postfix(parser, eof())
                .parse(new StringStream(sequence))
                .getOrThrow();
    }

    public static Parser<Character, String, Character> eof() {
        return TerminalParsers.<String, Character>
                endOfStream().map(v -> '\0');
    }

    public static Parser<Character, String, Character> wordBoundary() {
        return nonAlphanum().orElse(StringParsers::eof);
    }

    public static Parser<Character, String, Character> letter() {
        return item(Character::isLetter,
                () -> "Expected a Letter Character");
    }

    public static Parser<Character, String, Character> digit() {
        return item(Character::isDigit,
                () -> "Expected a Digit Character");
    }

    public static Parser<Character, String, Character> alphanum() {
        return item(Character::isLetterOrDigit,
                () -> "Expected a Alpha Numeric Character");
    }

    public static Parser<Character, String, Character> nonAlphanum() {
        Predicate<Character> predicate = Character::isLetterOrDigit;
        return item(predicate.negate(),
                () -> "Cannot Find Word Boundary");
    }

    public static Parser<String, String, Character> word(String term) {
        return postfix(sequence(
                term, () -> String.format("Cannot Find Word '%s'", term)),
                peek(wordBoundary()));
    }

    public static Parser<String, String, Character> whitespace() {
        return optionalRun(Character::isSpaceChar);
    }

    public static Parser<String, String, Character> letters() {
        return optionalRun(Character::isLetter);
    }

    public static Parser<String, String, Character> digits() {
        return optionalRun(Character::isDigit);
    }

    public static Parser<String, String, Character> alphanums() {
        return optionalRun(Character::isLetterOrDigit);
    }

    public static Parser<String, String, Character> keywords(
            Collection<String> words) {
        var terms = new ArrayList<>(words);
        var keywordParsers = new ArrayList<Parser<String, String, Character>>();

        // Sort by Length Descending order, then Natural Order in Ascending Order
        terms.sort(Comparator.comparing(String::length).reversed()
                .thenComparing(Comparator.naturalOrder()));

        for (String term : terms) {
            keywordParsers.add(word(term));
        }

        return alternate(keywordParsers);
    }
}
