package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.errorprone.annotations.Immutable;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Literal} string value (e.g. {@code "foo"}). */
@Immutable
public record StringLiteral(SourceReference sourceReference, String value) implements Literal {

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code StringLiteral} based on the given {@link TerminalNode}. */
  static StringLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.L_STRING,
        " StringLiteral::create passed an unsupported TerminalNode: %s",
        node);
    return new StringLiteral(SourceReference.create(node), /* value= */ token.getText());
  }
}
