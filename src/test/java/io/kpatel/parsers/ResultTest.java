package io.kpatel.parsers;

import io.kpatel.parsers.string.StringParserStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResultTest {

    @Test
    public void testGetOrThrowSuccess() {
        Result<String, StringParserStream> result = Result.success("Hello", new StringParserStream(""));
        assertEquals("Hello", result.getOrThrow());
    }
}
