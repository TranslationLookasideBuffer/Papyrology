package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;

/** A {@link Literal} boolean value (e.g. {@code True}). */
@AutoValue
public abstract class BooleanLiteral implements Literal {

  /** Returns the actual value of this literal. */
  public abstract boolean getValue();

  /** Returns the raw value of the literal (as it appears in source). */
  public abstract String getRawValue();

  /** Creates a {@code BooleanLiteral} based on the given {@link TerminalNode}. */
  public static BooleanLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    if (token.getType() == PapyrusParser.K_TRUE) {
      return builder().setValue(true).setRawValue(token.getText()).build();
    } else if (token.getType() == PapyrusParser.K_FALSE) {
      return builder().setValue(false).setRawValue(token.getText()).build();
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
    abstract Builder setValue(boolean value);

    abstract Builder setRawValue(String rawValue);

    abstract BooleanLiteral build();
  }
}
