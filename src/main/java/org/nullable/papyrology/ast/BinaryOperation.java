package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.grammar.PapyrusParser.BinaryOperationContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to the result of an operation on two inputs. */
@AutoValue
@Immutable
public abstract class BinaryOperation implements Expression {

  public enum Operator {
    LOGICAL_OR,
    LOGICAL_AND,
    EQUAL,
    NOT_EQUAL,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    MODULO
  }

  private static final ImmutableMap<Integer, Operator> TOKEN_TYPES_TO_OPERATORS =
      ImmutableMap.<Integer, Operator>builder()
          .put(PapyrusParser.O_LOGICAL_OR, Operator.LOGICAL_OR)
          .put(PapyrusParser.O_LOGICAL_AND, Operator.LOGICAL_AND)
          .put(PapyrusParser.O_EQUAL, Operator.EQUAL)
          .put(PapyrusParser.O_NOT_EQUAL, Operator.NOT_EQUAL)
          .put(PapyrusParser.O_GREATER, Operator.GREATER_THAN)
          .put(PapyrusParser.O_GREATER_OR_EQUAL, Operator.GREATER_THAN_OR_EQUAL)
          .put(PapyrusParser.O_LESS, Operator.LESS_THAN)
          .put(PapyrusParser.O_LESS_OR_EQUAL, Operator.LESS_THAN_OR_EQUAL)
          .put(PapyrusParser.O_ADD, Operator.ADD)
          .put(PapyrusParser.O_SUBTRACT, Operator.SUBTRACT)
          .put(PapyrusParser.O_MULTIPLY, Operator.MULTIPLY)
          .put(PapyrusParser.O_DIVIDE, Operator.DIVIDE)
          .put(PapyrusParser.O_MODULO, Operator.MODULO)
          .build();

  /** Returns the {@link Operator} of this operation. */
  public abstract Operator getOperator();

  /** Returns a new {@code BinaryOperation} based on the given {@link BinaryOperationContext}. */
  static BinaryOperation create(BinaryOperationContext ctx) {
    Operator operator = TOKEN_TYPES_TO_OPERATORS.get(ctx.op.getType());
    checkState(operator != null, "BinaryOperation::create was unable to resolve the operator");
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setOperator(operator)
        .setLeftExpression(Expression.create(ctx.left))
        .setRightExpression(Expression.create(ctx.right))
        .build();
  }

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
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setOperator(Operator operator);

    abstract Builder setLeftExpression(Expression expression);

    abstract Builder setRightExpression(Expression expression);

    abstract BinaryOperation build();
  }
}
