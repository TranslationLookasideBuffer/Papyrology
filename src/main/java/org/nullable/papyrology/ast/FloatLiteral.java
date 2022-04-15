package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.errorprone.annotations.Immutable;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Literal} float value (e.g. {@code 1.23}). */
@Immutable
public record FloatLiteral(SourceReference sourceReference, float value) implements Literal {

  /** Returns a new {@code FloatLiteral} based on the given {@link TerminalNode}. */
  static FloatLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.L_FLOAT,
        " FloatLiteral::create passed an unsupported TerminalNode: %s",
        node);
    return new FloatLiteral(SourceReference.create(node), Float.valueOf(token.getText()));
  }
}
