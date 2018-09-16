package io.kpatel.parsers.builder;

public final class TextBuilder implements Builder<String, String> {
    private final StringBuilder builder;

    public TextBuilder() {
        builder = new StringBuilder();
    }

    @Override
    public Builder<String, String> append(String item) {
        builder.append(item);
        return this;
    }

    @Override
    public String toOutput() {
        return builder.toString();
    }
}
