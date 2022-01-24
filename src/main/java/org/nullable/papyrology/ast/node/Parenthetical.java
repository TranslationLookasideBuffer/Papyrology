package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An {@link Expression} that evaluates to a single subexpression. */
@AutoValue
public abstract class Parenthetical implements Expression {

  /** Returns the {@link Expression} that is surrounded by parentheses. */
  public abstract Expression getExpression();

  /** Returns a fresh {@code Parenthetical} builder. */
  public static Builder builder() {
    return new AutoValue_Parenthetical.Builder();
  }

  /** A builder of {@code Parentheticals}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setExpression(Expression expression);

    public abstract Parenthetical build();
  }
}
