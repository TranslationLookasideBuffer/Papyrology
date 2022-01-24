package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An {@link Expression} that evaluates to an element in an array. */
@AutoValue
public abstract class ArrayAccess implements Expression {

  /** Returns the {@link Expression} that evaluates to the array being accessed. */
  public abstract Expression getArrayExpression();

  /** Returns the {@link Expression} that evaluates to the index being accessed. */
  public abstract Expression getIndexExpression();

  /** Returns a fresh {@code ArrayAccess} builder. */
  public static Builder builder() {
    return new AutoValue_ArrayAccess.Builder();
  }

  /** A builder of {@code ArrayAccesses}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setArrayExpression(Expression expression);

    public abstract Builder setIndexExpression(Expression expression);

    public abstract ArrayAccess build();
  }
}
