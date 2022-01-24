package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An {@link Expression} that evaluates to the result of an operation on a single input. */
@AutoValue
public abstract class UnaryOperation implements Expression {

  public enum Operator {
    NUMERIC_NEGATION,
    LOGICAL_NEGATION
  }

  /** Returns the {@link Operator} of this operation. */
  public abstract Operator getOperator();

  /** Returns the {@link Expression} that is being negated. */
  public abstract Expression getExpression();

  /** Returns a fresh {@code UnaryOperation} builder. */
  public static Builder builder() {
    return new AutoValue_UnaryOperation.Builder();
  }

  /** A builder of {@code UnaryOperations}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setOperator(Operator operator);

    public abstract Builder setExpression(Expression expression);

    public abstract UnaryOperation build();
  }
}
