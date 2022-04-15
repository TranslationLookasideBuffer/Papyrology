package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.CastContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that changes the {@code Type} and an {@code Expression}. */
@Immutable
public record Cast(SourceReference sourceReference, Expression expression, Type type)
    implements Expression {

  /** Returns a new {@code Cast} based on the given {@link CastContext}. */
  static Cast create(CastContext ctx) {
    return new Cast(
        SourceReference.create(ctx), Expression.create(ctx.expression()), Type.create(ctx.type()));
  }
}
