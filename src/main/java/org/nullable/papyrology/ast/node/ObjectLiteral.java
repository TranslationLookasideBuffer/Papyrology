package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser;

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
@AutoValue
public abstract class ObjectLiteral implements Literal {

  /** The type of this node. */
  public enum Reference {
    NONE,
    SELF,
    PARENT
  }

  /** Returns the actual value of this literal. */
  public abstract Reference getValue();

  /** Returns a new {@code ObjectLiteral} based on the given {@link TerminalNode}. */
  public static ObjectLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    Builder literal = builder().setSourceReference(SourceReference.create(node));
    if (token.getType() == PapyrusParser.K_NONE) {
      return literal.setValue(Reference.NONE).build();
    }
    if (token.getType() == PapyrusParser.K_SELF) {
      return literal.setValue(Reference.SELF).build();
    }
    if (token.getType() == PapyrusParser.K_PARENT) {
      return literal.setValue(Reference.PARENT).build();
    }
    throw new IllegalArgumentException(
        String.format("ObjectLiteral::create passed an unsupported TerminalNode: %s", node));
  }

  /** Returns a fresh {@code ObjectLiteral} builder. */
  static Builder builder() {
    return new AutoValue_ObjectLiteral.Builder();
  }

  /** A builder of {@code ObjectLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setValue(Reference value);

    abstract ObjectLiteral build();
  }
}
