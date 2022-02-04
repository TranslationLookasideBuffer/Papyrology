package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.CastContext;

/** An {@link Expression} that changes the {@code Type} and an {@code Expression}. */
@AutoValue
public abstract class Cast implements Expression {

  /** Returns the {@link Expression} being cast. */
  public abstract Expression getExpression();

  /** Returns the {@link Type} being cast to. */
  public abstract Type getType();

  /** Returns a new {@code Cast} based on the given {@link CastContext}. */
  public static Cast create(CastContext ctx) {
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setExpression(Expression.create(ctx.expression()))
        .setType(Type.create(ctx.type()))
        .build();
  }

  /** Returns a fresh {@code Cast} builder. */
  static Builder builder() {
    return new AutoValue_Cast.Builder();
  }

  /** A builder of {@code Castes}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setExpression(Expression expression);

    abstract Builder setType(Type type);

    abstract Cast build();
  }
}
