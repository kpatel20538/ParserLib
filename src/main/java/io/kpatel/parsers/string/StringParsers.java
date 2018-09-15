package io.kpatel.parsers.string;

import io.kpatel.parsers.Parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

import static io.kpatel.parsers.Parsers.*;


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
                .parse(new StringStream(sequence))
                .getOrThrow();
    }

    public static Parser<Character, String, Character> letter() {
        return item(Character::isLetter, () -> "Expected a Letter Character");
    }

    public static Parser<Character, String, Character> digit() {
        return item(Character::isDigit, () -> "Expected a Digit Character");
    }

    public static Parser<Character, String, Character> alphanum() {
        return item(Character::isLetterOrDigit, () -> "Expected a Alpha Numeric Character");
    }

    public static Parser<String, String, Character> word(String term) {
        Predicate<Character> predicate = Character::isLetterOrDigit;
        return postfix(sequence(term, () -> String.format("Cannot Find Keyword '%s'", term)),
                peek(item(predicate.negate(), () -> String.format("Cannot Find Word Boundary for Keyword '%s'", term))));
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

    public static Parser<String, String, Character> keywords(Collection<String> words) {
        ArrayList<String> terms = new ArrayList<>(words);
        ArrayList<Parser<String, String, Character>> keywordParsers = new ArrayList<>();

        terms.sort(Comparator.comparing(String::length).reversed()
                .thenComparing(Comparator.naturalOrder()));

        for (String term : terms) {
            keywordParsers.add(word(term));
        }
        return alternate(keywordParsers);
    }
}
