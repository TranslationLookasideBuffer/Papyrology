package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.errorprone.annotations.Immutable;
import java.util.Locale;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to some scoped identifier (e.g. variable name). */
@Immutable
public record Identifier(SourceReference sourceReference, String value) implements Expression {

  /** Returns whether or not this {@link Identifier} refers to the same entity as the given one. */
  public final boolean isEquivalent(Identifier other) {
    return value().toUpperCase(Locale.US).equals(other.value().toUpperCase(Locale.US));
  }

  /**
   * Returns whether or not this {@link Identifier} refers to the same entity as the given literal.
   */
  public final boolean isEquivalent(String other) {
    return value().toUpperCase(Locale.US).equals(other.toUpperCase(Locale.US));
  }

  /** Returns a new {@code Identifier} based on the given {@link TerminalNode}. */
  static Identifier create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.ID,
        "Identifier::create passed an unsupported TerminalNode: %s",
        node);
    return new Identifier(SourceReference.create(node), token.getText());
  }
}
