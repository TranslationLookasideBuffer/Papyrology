package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayAccessAssigneeContext;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayAccessContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to an element in an array. */
@Immutable
public record ArrayAccess(
    SourceReference sourceReference, Expression arrayExpression, Expression indexExpression)
    implements Expression {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code ArrayAccess} based on the given {@link ArrayAccessContext}. */
  static ArrayAccess create(ArrayAccessContext ctx) {
    return new ArrayAccess(
        SourceReference.create(ctx),
        Expression.create(ctx.expression(0)),
        Expression.create(ctx.expression(1)));
  }

  /** Returns a new {@code ArrayAccess} based on the given {@link ArrayAccessAssigneeContext}. */
  static ArrayAccess create(ArrayAccessAssigneeContext ctx) {
    return new ArrayAccess(
        SourceReference.create(ctx),
        Expression.create(ctx.expression(0)),
        Expression.create(ctx.expression(1)));
  }
}
