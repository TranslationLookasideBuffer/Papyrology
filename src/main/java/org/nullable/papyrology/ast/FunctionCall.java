package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.DotAccessOrFunctionCallContext;
import org.nullable.papyrology.grammar.PapyrusParser.LocalFunctionCallContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to the return value of a function call. */
@Immutable
public record FunctionCall(
    SourceReference sourceReference,
    Optional<Expression> referenceExpression,
    Identifier identifier,
    ImmutableList<CallParameter> callParameters)
    implements Expression {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Returns a new {@code FunctionCall} based on the given {@link DotAccessOrFunctionCallContext}.
   */
  static FunctionCall create(DotAccessOrFunctionCallContext ctx) {
    checkArgument(
        ctx.callParameters() != null,
        "FunctionCall::create passed a DotAccessOrFunctionCallContext that maps to a dot access:"
            + " %s",
        ctx);
    return new FunctionCall(
        SourceReference.create(ctx),
        Optional.of(Expression.create(ctx.expression())),
        Identifier.create(ctx.ID()),
        CallParameter.create(ctx.callParameters()));
  }

  /** Returns a new {@code FunctionCall} based on the given {@link LocalFunctionCallContext}. */
  static FunctionCall create(LocalFunctionCallContext ctx) {
    return new FunctionCall(
        SourceReference.create(ctx),
        Optional.empty(),
        Identifier.create(ctx.ID()),
        CallParameter.create(ctx.callParameters()));
  }
}
