package org.nullable.papyrology.ast;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayLengthContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to the length of an array. */
@AutoValue
@Immutable
public abstract class ArrayLength implements Expression {

  /** Returns the {@link Expression} that evaluates to the array having its length accessed. */
  public abstract Expression getArrayExpression();

  /** Returns a new {@code ArrayLength} based on the given {@link ArrayLengthContext}. */
  static ArrayLength create(ArrayLengthContext ctx) {
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setArrayExpression(Expression.create(ctx.expression()))
        .build();
  }

  /** Returns a fresh {@code ArrayLength} builder. */
  static Builder builder() {
    return new AutoValue_ArrayLength.Builder();
  }

  /** A builder of {@code ArrayLengths}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setArrayExpression(Expression expression);

    abstract ArrayLength build();
  }
}
