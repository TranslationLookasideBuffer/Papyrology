package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser;

/** A {@link Literal} boolean value (e.g. {@code True}). */
@AutoValue
public abstract class BooleanLiteral implements Literal {

  /** Returns the actual value of this literal. */
  public abstract boolean getValue();

  /** Returns a new {@code BooleanLiteral} based on the given {@link TerminalNode}. */
  public static BooleanLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    SourceReference reference = SourceReference.create(node);
    if (token.getType() == PapyrusParser.K_TRUE) {
      return builder().setSourceReference(reference).setValue(true).build();
    }
    if (token.getType() == PapyrusParser.K_FALSE) {
      return builder().setSourceReference(reference).setValue(false).build();
    }
    throw new IllegalArgumentException(
        String.format("BooleanLiteral::create passed an unsupported TerminalNode: %s", node));
  }

  /** Returns a fresh {@code BooleanLiteral} builder. */
  static Builder builder() {
    return new AutoValue_BooleanLiteral.Builder();
  }

  /** A builder of {@code BooleanLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setValue(boolean value);

    abstract BooleanLiteral build();
  }
}
