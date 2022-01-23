package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An {@link Expression} that evaluates to the result of an operation on two inputs. */
@AutoValue
public abstract class BinaryOperation implements Expression {

  public enum Operator {
    LOGICAL_OR,
    LOGICAL_AND,
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    MODULO;
  }

  /** Returns the {@link Operator} of this operation. */
  public abstract Operator getOperator();

  /** Returns the {@link Expression} that on the left of the {@code Operator}. */
  public abstract Expression getLeftExpression();

  /** Returns the {@link Expression} that on the right of the {@code Operator}. */
  public abstract Expression getRightExpression();

  /** Returns a fresh {@code BinaryOperation} builder. */
  static Builder builder() {
    return new AutoValue_BinaryOperation.Builder();
  }

  /** A builder of {@code BinaryOperations}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setOperator(Operator operator);

    abstract Builder setLeftExpression(Expression expression);

    abstract Builder setRightExpression(Expression expression);

    abstract BinaryOperation build();
  }
}
