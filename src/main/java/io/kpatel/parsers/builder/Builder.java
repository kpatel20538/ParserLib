package io.kpatel.parsers.builder;

/**
 * INTENT: A Generic Interface for user defined types can implement so that
 * they can interact with parser factories in RepetitionParsers
 * REQUIREMENTS:
 * - Do not refer to global/external state (local/internal state is permitted)
 * - All return types must not be null
 * RECOMMENDATIONS:
 * - Define ether a default constructor or a no-arg factory methods to represent
 * an Empty Builder and employ method references to it when using the
 * factories in RepetitionParsers for ease of use.
 * - Prefer implementing the Visitor Pattern with the Part Type to interact with
 * the Builder over using the "instanceof" keyword to preserve type safety.
 * - Prefer Immutable Output types when appropriate to prevent dependant parsers
 * from relying on private implementation details over public interface guarantees.
 * NOTES:
 * - Prebuilt Builders for Lists, Map, and String are available.
 * - Builder are permitted to be either mutable or immutable.
 * - All of the provided Builders are mutable with immutable Outputs
 *
 * @param <Out> the Final Output of the Builder
 * @param <Prt> the Part type the Builder can make use to create Out Objects
 * @see io.kpatel.parsers.prebuilt.RepetitionParsers
 * @see ListBuilder
 * @see MapBuilder
 * @see TextBuilder
 */
public interface Builder<Out, Prt> {
    /**
     * INTENT: Update some internal state with a given part
     * REQUIREMENT: return type must not be null
     */
    Builder<Out, Prt> append(Prt part);

    /**
     * INTENT: Convert the internal state to some Output
     * REQUIREMENT: return type must not be null
     */
    Out toOutput();
}
