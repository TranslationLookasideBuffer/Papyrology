package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import java.util.Locale;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;

/** An {@link Expression} that evaluates to some scoped identifier (e.g. variable name). */
@AutoValue
public abstract class Identifier implements Expression {

  /** Returns the value of this identifier. */
  public abstract String getValue();

  /** Returns whether or not this {@link Identifier} refers to the same entity as the given one. */
  public final boolean isEquivalent(Identifier other) {
    return getValue().toUpperCase(Locale.US).equals(other.getValue().toUpperCase(Locale.US));
  }

  /** Returns a new {@code Identifier} based on the given {@link TerminalNode}. */
  public static Identifier create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.ID,
        "Identifier::create passed an unsupported TerminalNode: %s",
        node);
    return builder().setValue(token.getText()).build();
  }

  /** Returns a fresh {@code IdentifierNode} builder. */
  static Builder builder() {
    return new AutoValue_Identifier.Builder();
  }

  /** A builder of {@code Identifiers}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(String value);

    abstract Identifier build();
  }
}
