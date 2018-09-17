package io.kpatel.parsers;

import io.kpatel.parsers.stream.ParserStream;

/**
 * INTENT: A Composable Generic Interface conversion of a Stream to some Non-Null Object
 * REQUIREMENTS:
 * - Must be Pure Function
 * - Must return and a Success is the target Object was found
 * - Must return and a Failure if a Recoverable Error occurred
 * - Must throw a Runtime Exception for Unrecoverable Error.
 *   (preference is toward {@link ParserError})
 * RECOMMENDATIONS:
 * - Do not capture {@link ParserError} in {@link Parser}, The Stream may not
 *   guaranteed to be in a valid state for Parsing.
 * - Prefer Lambda Expressions over Class Implementation to help reduce
 *   dependence on external state.
 */
@FunctionalInterface
public interface Parser<T, Seq, Itm> {
    /**
     * WHAT: A SAM Interface
     * WHY: Make use of Lambda Expressions for Simple Construction
     */
    Result<T, Seq, Itm> parse(ParserStream<Seq, Itm> stream);

}
