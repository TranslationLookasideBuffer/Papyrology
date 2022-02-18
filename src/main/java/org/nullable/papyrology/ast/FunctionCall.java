package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.DotAccessOrFunctionCallContext;
import org.nullable.papyrology.grammar.PapyrusParser.LocalFunctionCallContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to the return value of a function call. */
@AutoValue
@Immutable
public abstract class FunctionCall implements Expression {

  /**
   * Returns the {@link Expression} that evaluates to the reference being accessed, if present.
   *
   * <p>If absent, the function must be local to the script calling it.
   */
  public abstract Optional<Expression> getReferenceExpression();

  /** Returns the {@link Identifier} of the function being called. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link CallParameter CallParameters} in order. */
  public abstract ImmutableList<CallParameter> getCallParameters();

  /**
   * Returns a new {@code FunctionCall} based on the given {@link DotAccessOrFunctionCallContext}.
   */
  static FunctionCall create(DotAccessOrFunctionCallContext ctx) {
    checkArgument(
        ctx.callParameters() != null,
        "FunctionCall::create passed a DotAccessOrFunctionCallContext that maps to a dot access:"
            + " %s",
        ctx);
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setReferenceExpression(Expression.create(ctx.expression()))
        .setIdentifier(Identifier.create(ctx.ID()))
        .setCallParameters(CallParameter.create(ctx.callParameters()))
        .build();
  }

  /** Returns a new {@code FunctionCall} based on the given {@link LocalFunctionCallContext}. */
  static FunctionCall create(LocalFunctionCallContext ctx) {
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setIdentifier(Identifier.create(ctx.ID()))
        .setCallParameters(CallParameter.create(ctx.callParameters()))
        .build();
  }

  /** Returns a fresh {@code FunctionCall} builder. */
  static Builder builder() {
    return new AutoValue_FunctionCall.Builder();
  }

  /** A builder of {@code FunctionCalls}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setReferenceExpression(Expression expression);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setCallParameters(ImmutableList<CallParameter> callParameters);

    abstract FunctionCall build();
  }
}
