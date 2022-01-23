package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An {@link Expression} that changes the {@code Type} and an {@code Expression}. */
@AutoValue
public abstract class Cast implements Expression {

  /** Returns the {@link Expression} being cast. */
  public abstract Expression getExpression();

  /** Returns the {@link Type} being cast to. */
  public abstract Type getType();

  /** Returns a fresh {@code Cast} builder. */
  static Builder builder() {
    return new AutoValue_Cast.Builder();
  }

  /** A builder of {@code Castes}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setExpression(Expression expression);

    abstract Builder setType(Type type);

    abstract Cast build();
  }
}
