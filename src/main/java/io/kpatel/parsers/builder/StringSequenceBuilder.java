package io.kpatel.parsers.builder;

import io.kpatel.parsers.SequenceBuilder;

public class StringSequenceBuilder implements SequenceBuilder<StringBuilder, String, String> {
    @Override
    public StringBuilder getNewBuilder() {
        return new StringBuilder();
    }

    @Override
    public StringBuilder appendPart(StringBuilder builder, String part) {
        return builder.append(part);
    }

    @Override
    public String toOutput(StringBuilder builder) {
        return builder.toString();
    }
}
