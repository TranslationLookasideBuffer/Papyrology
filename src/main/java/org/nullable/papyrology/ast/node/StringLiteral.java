package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;

/** A {@link Literal} string value (e.g. {@code "foo"}). */
@AutoValue
public abstract class StringLiteral implements Literal {

  /** Returns the value of this literal. */
  public abstract String getValue();

  /** Returns a new {@code StringLiteral} based on the given {@link TerminalNode}. */
  public static StringLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.L_STRING,
        " StringLiteral::create passed an unsupported TerminalNode: %s",
        node);
    return builder().setValue(token.getText()).build();
  }

  /** Returns a fresh {@code StringLiteral} builder. */
  static Builder builder() {
    return new AutoValue_StringLiteral.Builder();
  }

  /** A builder of {@code StringLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(String value);

    abstract StringLiteral build();
  }
}
