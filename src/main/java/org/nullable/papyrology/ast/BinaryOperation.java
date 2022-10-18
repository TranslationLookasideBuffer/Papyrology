package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.grammar.PapyrusParser.BinaryOperationContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to the result of an operation on two inputs. */
@Immutable
public record BinaryOperation(
    SourceReference sourceReference,
    Operator operator,
    Expression leftExpression,
    Expression rightExpression)
    implements Expression {

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

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code BinaryOperation} based on the given {@link BinaryOperationContext}. */
  static BinaryOperation create(BinaryOperationContext ctx) {
    Operator operator = TOKEN_TYPES_TO_OPERATORS.get(ctx.op.getType());
    checkState(operator != null, "BinaryOperation::create was unable to resolve the operator");
    return new BinaryOperation(
        SourceReference.create(ctx),
        operator,
        Expression.create(ctx.left),
        Expression.create(ctx.right));
  }
}
