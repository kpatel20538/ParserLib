package io.kpatel.parsers.parsers;

import io.kpatel.parsers.stream.StringStream;
import org.junit.Test;

import java.util.List;

import static io.kpatel.parsers.prebuilt.AffixParsers.prefix;
import static io.kpatel.parsers.prebuilt.StringParsers.*;
import static org.junit.Assert.*;

public class StringTest {
    @Test
    public void testEndOfFileSuccess() {
        var stream = new StringStream("Hello World").jump(200);
        var result = endOfFile().get().parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('\0'), item);
    }

    @Test
    public void testEndOfFileFailure() {
        var stream = new StringStream("Hello World");
        var result = endOfFile().get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testEndOfLineSuccessLineEndLineFeed() {
        var stream = new StringStream("\n\rHello World");
        var result = prefix(endOfLine(), letter()).get().parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testEndOfLineSuccessLineEnd() {
        var stream = new StringStream("\nHello World");
        var result = prefix(endOfLine(), letter()).get().parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testEndOfLineSuccessLineFeed() {
        var stream = new StringStream("\rHello World");
        var result = prefix(endOfLine(), letter()).get().parse(stream);

        var item = result.getOrThrow();

        assertEquals(Character.valueOf('H'), item);
    }

    @Test
    public void testEndOfLineSuccessFileEnd() {
        var stream = new StringStream("Hello World").jump(200);
        var result = endOfLine().get().parse(stream);

        assertTrue(result.isSuccess());
    }

    @Test
    public void testEndOfLineFailure() {
        var stream = new StringStream("Hello World");
        var result = endOfLine().get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testWordSuccessBoundary() {
        var stream = new StringStream("Hello World");
        var result = term("Hello").get().parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testWordSuccessEndOfFile() {
        var stream = new StringStream("Hello");
        var result = term("Hello").get().parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testWordFailure() {
        var stream = new StringStream("Hello World");
        var result = term("World").get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testWordFailureLetter() {
        var stream = new StringStream("HelloWorld");
        var result = term("Hello").get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testWordFailureDigit() {
        var stream = new StringStream("Hello12345");
        var result = term("Hello").get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testKeywordSuccessBoundary() {
        var stream = new StringStream("Hello World");
        var result = keywords(List.of("Hello", "World")).get().parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testKeywordSuccessEndOfFile() {
        var stream = new StringStream("Hello");
        var result = keywords(List.of("Hello", "World")).get().parse(stream);

        var item = result.getOrThrow();

        assertEquals("Hello", item);
    }

    @Test
    public void testKeywordSuccessSecond() {
        var stream = new StringStream("World Hello");
        var result = keywords(List.of("Hello", "World")).get().parse(stream);

        var item = result.getOrThrow();

        assertEquals("World", item);
    }

    @Test
    public void testKeywordFailure() {
        var stream = new StringStream("HelloWorld");
        var result = keywords(List.of("Hello", "World")).get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testKeywordFailureLetter() {
        var stream = new StringStream("HelloWorld");
        var result = keywords(List.of("Hello", "World")).get().parse(stream);

        assertFalse(result.isSuccess());
    }

    @Test
    public void testKeywordFailureDigit() {
        var stream = new StringStream("Hello12345");
        var result = keywords(List.of("Hello", "World")).get().parse(stream);

        assertFalse(result.isSuccess());
    }
}
