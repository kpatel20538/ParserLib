package io.kpatel.parsers;

import java.util.Optional;
import java.util.function.Predicate;

public class StringParserStream implements ParserStream<StringParserStream, String, Character> {
    private final String stream;
    private final int position;
    private final int lineNumber;
    private final int columnNumber;

    public StringParserStream(String stream) {
        this.stream = stream;
        this.position = 0;
        this.lineNumber = 1;
        this.columnNumber = 1;
    }

    private StringParserStream(String stream, int position, int lineNumber, int columnNumber) {
        this.stream = stream;
        this.position = position;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }


    @Override
    public Optional<Character> getLeadingItem() {
        return Optional.empty();
    }

    @Override
    public String getLeadingSequence(int length) {
        return null;
    }

    @Override
    public String getLeadingRun(Predicate<Character> predicate) {
        return null;
    }

    @Override
    public StringParserStream jump(int n) {
        return null;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
