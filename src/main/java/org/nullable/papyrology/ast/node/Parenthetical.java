package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An {@link Expression} that evaluates to a single subexpression. */
@AutoValue
public abstract class Parenthetical implements Expression {

  /** Returns the {@link Expression} that is surrounded by parentheses. */
  public abstract Expression getExpression();

  /** Returns a fresh {@code Parenthetical} builder. */
  static Builder builder() {
    return new AutoValue_Parenthetical.Builder();
  }

  /** A builder of {@code Parentheticals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setExpression(Expression expression);

    abstract Parenthetical build();
  }
}
