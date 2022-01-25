package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;

/** A {@link Literal} float value (e.g. {@code 1.23}). */
@AutoValue
public abstract class FloatLiteral implements Literal {

  /** Returns the actual value of this literal. */
  public abstract float getValue();

  /** Returns the raw value of the literal (as it appears in source). */
  public abstract String getRawValue();

  /** Returns a new {@code FloatLiteral} based on the given {@link TerminalNode}. */
  public static FloatLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.L_FLOAT,
        " FloatLiteral::create passed an unsupported TerminalNode: %s",
        node);
    return builder().setRawValue(token.getText()).setValue(Float.valueOf(token.getText())).build();
  }

  /** Returns a fresh {@code FloatLiteral} builder. */
  static Builder builder() {
    return new AutoValue_FloatLiteral.Builder();
  }

  /** A builder of {@code FloatLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(float value);

    abstract Builder setRawValue(String rawValue);

    abstract FloatLiteral build();
  }
}
