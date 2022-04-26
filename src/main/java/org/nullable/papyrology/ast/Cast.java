package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.CastContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that changes the {@code Type} and an {@code Expression}. */
@Immutable
public record Cast(SourceReference sourceReference, Type type, Expression expression)
    implements Expression {

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code Cast} based on the given {@link CastContext}. */
  static Cast create(CastContext ctx) {
    return new Cast(
        SourceReference.create(ctx), Type.create(ctx.type()), Expression.create(ctx.expression()));
  }
}
