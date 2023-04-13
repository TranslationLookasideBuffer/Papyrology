package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.grammar.PapyrusParser.UnaryOperationContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to the result of an operation on a single input. */
@Immutable
public record UnaryOperation(
    SourceReference sourceReference, Operator operator, Expression expression)
    implements Expression {

  public enum Operator {
    NUMERIC_NEGATION,
    LOGICAL_NEGATION
  }

  private static final ImmutableMap<Integer, Operator> TOKEN_TYPES_TO_OPERATORS =
      ImmutableMap.<Integer, Operator>builder()
          .put(PapyrusParser.O_SUBTRACT, Operator.NUMERIC_NEGATION)
          .put(PapyrusParser.O_LOGICAL_NOT, Operator.LOGICAL_NEGATION)
          .build();

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code UnaryOperation} based on the given {@link UnaryOperationContext}. */
  static UnaryOperation create(UnaryOperationContext ctx) {
    Operator operator = TOKEN_TYPES_TO_OPERATORS.get(ctx.op.getType());
    checkState(operator != null, "UnaryOperation::create was unable to resolve the operator");
    return new UnaryOperation(SourceReference.create(ctx), operator, Expression.create(ctx.value));
  }
}
