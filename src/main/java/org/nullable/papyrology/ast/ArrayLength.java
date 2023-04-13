package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayLengthContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to the length of an array. */
@Immutable
public record ArrayLength(SourceReference sourceReference, Expression arrayExpression)
    implements Expression {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code ArrayLength} based on the given {@link ArrayLengthContext}. */
  static ArrayLength create(ArrayLengthContext ctx) {
    return new ArrayLength(SourceReference.create(ctx), Expression.create(ctx.expression()));
  }
}
