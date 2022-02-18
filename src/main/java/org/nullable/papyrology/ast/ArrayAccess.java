package org.nullable.papyrology.ast;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayAccessContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to an element in an array. */
@AutoValue
@Immutable
public abstract class ArrayAccess implements Expression {

  /** Returns the {@link Expression} that evaluates to the array being accessed. */
  public abstract Expression getArrayExpression();

  /** Returns the {@link Expression} that evaluates to the index being accessed. */
  public abstract Expression getIndexExpression();

  /** Returns a new {@code ArrayAccess} based on the given {@link ArrayAccessContext}. */
  static ArrayAccess create(ArrayAccessContext ctx) {
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setArrayExpression(Expression.create(ctx.expression(0)))
        .setIndexExpression(Expression.create(ctx.expression(1)))
        .build();
  }

  /** Returns a fresh {@code ArrayAccess} builder. */
  static Builder builder() {
    return new AutoValue_ArrayAccess.Builder();
  }

  /** A builder of {@code ArrayAccesses}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setArrayExpression(Expression expression);

    abstract Builder setIndexExpression(Expression expression);

    abstract ArrayAccess build();
  }
}
