package io.kpatel.parsers.string;

import io.kpatel.parsers.ParserStream;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * WHY: Specialize ParserStream for Stream
 */
public class StringParserStream implements ParserStream<String, Character> {
    private final String stream;
    private final int position;
    private final int lineNumber;
    private final int columnNumber;

    public StringParserStream(String stream) {
        this.stream = stream;
        this.position = 0;
        this.lineNumber = 1;
        this.columnNumber = 0;
    }

    private StringParserStream(String stream, int position, int lineNumber, int columnNumber) {
        this.stream = stream;
        this.position = position;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public Optional<Character> getLeadingItem() {
        return position < stream.length()
                ? Optional.of(stream.charAt(position))
                : Optional.empty();
    }

    @Override
    public String getLeadingSequence(int length) {
        if (0 < length && position < stream.length()) {
            int endPosition = position + length;
            if (stream.length() <= endPosition) {
                endPosition = stream.length();
            }
            return stream.substring(position, endPosition);
        }
        return "";
    }

    @Override
    public String getLeadingRun(Predicate<Character> predicate) {
        if (position < stream.length()) {
            int endPosition = position;
            while (endPosition < stream.length() && predicate.test(stream.charAt(endPosition)))
                endPosition++;
            return stream.substring(position, endPosition);
        }
        return "";
    }

    @Override
    public ParserStream<String, Character> jump(int n) {
        if (0 < n) {
            String stage = getLeadingSequence(n);
            int lineCount = 0;
            int colCount = 0;
            for (int idx = stage.length() - 1; idx >= 0; idx--) {
                if (stage.charAt(idx) == '\n') {
                    lineCount++;
                }
                if (lineCount == 0) {
                    colCount++;
                }
            }
            if (lineCount == 0) {
                colCount += columnNumber;
            } else {
                lineCount += lineNumber;
            }
            return new StringParserStream(
                    stream, position + n,
                    lineCount, colCount);
        }
        return this;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getErrorHeader() {
        return String.format("(Line: %d, Col: %d)", getLineNumber(), getColumnNumber());
    }
}
