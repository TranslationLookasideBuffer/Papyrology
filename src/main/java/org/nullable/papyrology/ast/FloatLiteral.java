package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Literal} float value (e.g. {@code 1.23}). */
@AutoValue
@Immutable
public abstract class FloatLiteral implements Literal {

  /** Returns the actual value of this literal. */
  public abstract float getValue();

  /** Returns a new {@code FloatLiteral} based on the given {@link TerminalNode}. */
  static FloatLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.L_FLOAT,
        " FloatLiteral::create passed an unsupported TerminalNode: %s",
        node);
    return builder()
        .setSourceReference(SourceReference.create(node))
        .setValue(Float.valueOf(token.getText()))
        .build();
  }

  /** Returns a fresh {@code FloatLiteral} builder. */
  static Builder builder() {
    return new AutoValue_FloatLiteral.Builder();
  }

  /** A builder of {@code FloatLiterals}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setValue(float value);

    abstract FloatLiteral build();
  }
}
