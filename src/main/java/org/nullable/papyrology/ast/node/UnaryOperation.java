package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.grammar.PapyrusParser.UnaryOperationContext;

/** An {@link Expression} that evaluates to the result of an operation on a single input. */
@AutoValue
public abstract class UnaryOperation implements Expression {

  public enum Operator {
    NUMERIC_NEGATION,
    LOGICAL_NEGATION
  }

  private static final ImmutableMap<Integer, Operator> TOKEN_TYPES_TO_OPERATORS =
      ImmutableMap.<Integer, Operator>builder()
          .put(PapyrusParser.O_SUBTRACT, Operator.NUMERIC_NEGATION)
          .put(PapyrusParser.O_LOGICAL_NOT, Operator.LOGICAL_NEGATION)
          .build();

  /** Returns the {@link Operator} of this operation. */
  public abstract Operator getOperator();

  /** Returns the {@link Expression} that is being negated. */
  public abstract Expression getExpression();

  /** Returns a new {@code UnaryOperation} based on the given {@link UnaryOperationContext}. */
  public static UnaryOperation create(UnaryOperationContext ctx) {
    Operator operator = TOKEN_TYPES_TO_OPERATORS.get(ctx.op.getType());
    checkState(operator != null, "UnaryOperation::create was unable to resolve the operator");
    return builder().setOperator(operator).setExpression(Expression.create(ctx.value)).build();
  }

  /** Returns a fresh {@code UnaryOperation} builder. */
  static Builder builder() {
    return new AutoValue_UnaryOperation.Builder();
  }

  /** A builder of {@code UnaryOperations}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setOperator(Operator operator);

    abstract Builder setExpression(Expression expression);

    abstract UnaryOperation build();
  }
}
