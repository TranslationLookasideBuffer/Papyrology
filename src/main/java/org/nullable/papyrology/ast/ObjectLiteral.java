package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.source.SourceReference;

/**
 * A {@link Literal} object reference
 *
 * <p>Since Papyrus doesn't allow for object creation, this really just consists of three values:
 *
 * <ul>
 *   <li>{@code None}, the "empty" reference
 *   <li>{@code Self}, the reference to the current script
 *   <li>{@code Parent}, the reference to the script the current one extends
 * </ul>
 */
@Immutable
public record ObjectLiteral(SourceReference sourceReference, Reference value) implements Literal {

  /** The type of this node. */
  public enum Reference {
    NONE,
    SELF,
    PARENT
  }

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code ObjectLiteral} based on the given {@link TerminalNode}. */
  static ObjectLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    SourceReference sourceReference = SourceReference.create(node);
    if (token.getType() == PapyrusParser.K_NONE) {
      return new ObjectLiteral(sourceReference, Reference.NONE);
    }
    if (token.getType() == PapyrusParser.K_SELF) {
      return new ObjectLiteral(sourceReference, Reference.SELF);
    }
    if (token.getType() == PapyrusParser.K_PARENT) {
      return new ObjectLiteral(sourceReference, Reference.PARENT);
    }
    throw new IllegalArgumentException(
        String.format("ObjectLiteral::create passed an unsupported TerminalNode: %s", node));
  }
}
