package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayInitializationContext;
import org.nullable.papyrology.source.SourceReference;

/** An expression that evaluates to a newly initialized array. */
@Immutable
public record ArrayInitialization(SourceReference sourceReference, Type type, IntegerLiteral size)
    implements Expression {

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Returns a new {@code ArrayInitialization} based on the given {@link
   * ArrayInitializationContext}.
   */
  static ArrayInitialization create(ArrayInitializationContext ctx) {
    IntegerLiteral sizeLiteral = IntegerLiteral.create(ctx.L_UINT());
    if (sizeLiteral.value() > 128) {
      throw new SyntaxException(
          SourceReference.create(ctx.L_UINT()), "Array size cannot be greater than 128");
    }
    return new ArrayInitialization(
        SourceReference.create(ctx), Type.create(ctx.type()), sizeLiteral);
  }
}
