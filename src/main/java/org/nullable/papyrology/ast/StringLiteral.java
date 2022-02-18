package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Literal} string value (e.g. {@code "foo"}). */
@AutoValue
@Immutable
public abstract class StringLiteral implements Literal {

  /** Returns the value of this literal. */
  public abstract String getValue();

  /** Returns a new {@code StringLiteral} based on the given {@link TerminalNode}. */
  static StringLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.L_STRING,
        " StringLiteral::create passed an unsupported TerminalNode: %s",
        node);
    return builder()
        .setSourceReference(SourceReference.create(node))
        .setValue(token.getText())
        .build();
  }

  /** Returns a fresh {@code StringLiteral} builder. */
  static Builder builder() {
    return new AutoValue_StringLiteral.Builder();
  }

  /** A builder of {@code StringLiterals}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setValue(String value);

    abstract StringLiteral build();
  }
}
