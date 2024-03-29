package org.nullable.papyrology.ast.symbol;

import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.SyntaxException;

/** Common interface for resolving an {@link Identifier} to a {@link Symbol}. */
@FunctionalInterface
public interface Resolver {
  /**
   * Resolves the given {@link Identifier} to a {@link Symbol}.
   *
   * @throws SyntaxException if no {@code Symbol} could be found.
   */
  Symbol resolve(Identifier identifier);
}
