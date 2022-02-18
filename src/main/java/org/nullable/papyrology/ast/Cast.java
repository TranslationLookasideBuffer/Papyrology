package org.nullable.papyrology.ast;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.CastContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that changes the {@code Type} and an {@code Expression}. */
@AutoValue
@Immutable
public abstract class Cast implements Expression {

  /** Returns the {@link Expression} being cast. */
  public abstract Expression getExpression();

  /** Returns the {@link Type} being cast to. */
  public abstract Type getType();

  /** Returns a new {@code Cast} based on the given {@link CastContext}. */
  static Cast create(CastContext ctx) {
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
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setExpression(Expression expression);

    abstract Builder setType(Type type);

    abstract Cast build();
  }
}
