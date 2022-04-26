package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.DotAccessAssigneeContext;
import org.nullable.papyrology.grammar.PapyrusParser.DotAccessOrFunctionCallContext;
import org.nullable.papyrology.source.SourceReference;

/**
 * An {@link Expression} that evaluates to the value of some variable or property belonging to
 * another object
 */
@Immutable
public record DotAccess(
    SourceReference sourceReference, Expression referenceExpression, Identifier identifier)
    implements Expression {

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code DocAccess} based on the given {@link DotAccessOrFunctionCallContext}. */
  static DotAccess create(DotAccessOrFunctionCallContext ctx) {
    checkArgument(
        ctx.callParameters() == null,
        "DotAccess::create passed a DotAccessOrFunctionCallContext that maps to a function call:"
            + " %s",
        ctx);
    return new DotAccess(
        SourceReference.create(ctx),
        Expression.create(ctx.expression()),
        Identifier.create(ctx.ID()));
  }

  /** Returns a new {@code DocAccess} based on the given {@link DotAccessAssigneeContext}. */
  static DotAccess create(DotAccessAssigneeContext ctx) {
    return new DotAccess(
        SourceReference.create(ctx),
        Expression.create(ctx.expression()),
        Identifier.create(ctx.ID()));
  }
}
