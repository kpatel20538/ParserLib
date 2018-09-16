package io.kpatel.parsers.builder;

/**
 * WHAT: Specialized Builder for Text
 * NOTE:
 * - This Implementation has Mutable internal states.
 * - This Implementation has Immutable Output.
 *
 * @see Builder
 */
public final class TextBuilder implements Builder<String, String> {
    private final StringBuilder builder;

    public TextBuilder() {
        builder = new StringBuilder();
    }

    @Override
    public Builder<String, String> append(String part) {
        builder.append(part);
        return this;
    }

    @Override
    public String toOutput() {
        return builder.toString();
    }
}
