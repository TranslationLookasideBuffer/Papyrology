package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Optional;

/** An {@link Expression} that evaluates to the return value of a function call. */
@AutoValue
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

  /** Returns a fresh {@code FunctionCall} builder. */
  static Builder builder() {
    return new AutoValue_FunctionCall.Builder();
  }

  /** A builder of {@code FunctionCalls}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setReferenceExpression(Expression expression);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setCallParameters(ImmutableList<CallParameter> callParameters);

    abstract FunctionCall build();
  }
}
