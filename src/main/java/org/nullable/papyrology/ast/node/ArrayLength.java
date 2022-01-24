package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An {@link Expression} that evaluates to the length of an array. */
@AutoValue
public abstract class ArrayLength implements Expression {

  /** Returns the {@link Expression} that evaluates to the array having its length accessed. */
  public abstract Expression getArrayExpression();

  /** Returns a fresh {@code ArrayLength} builder. */
  public static Builder builder() {
    return new AutoValue_ArrayLength.Builder();
  }

  /** A builder of {@code ArrayLengths}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setArrayExpression(Expression expression);

    public abstract ArrayLength build();
  }
}
