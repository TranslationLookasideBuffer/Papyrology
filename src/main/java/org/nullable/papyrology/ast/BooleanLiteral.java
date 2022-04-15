package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Literal} boolean value (e.g. {@code True}). */
@Immutable
public record BooleanLiteral(SourceReference sourceReference, boolean value) implements Literal {

  /** Returns a new {@code BooleanLiteral} based on the given {@link TerminalNode}. */
  static BooleanLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    SourceReference reference = SourceReference.create(node);
    if (token.getType() == PapyrusParser.K_TRUE) {
      return new BooleanLiteral(reference, true);
    }
    if (token.getType() == PapyrusParser.K_FALSE) {
      return new BooleanLiteral(reference, false);
    }
    throw new IllegalArgumentException(
        String.format("BooleanLiteral::create passed an unsupported TerminalNode: %s", node));
  }
}
