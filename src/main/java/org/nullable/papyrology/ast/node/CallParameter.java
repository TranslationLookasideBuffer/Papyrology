package org.nullable.papyrology.ast.node;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.CallParameterContext;
import org.nullable.papyrology.grammar.PapyrusParser.CallParametersContext;

/** A parameter set in a function call. */
@AutoValue
public abstract class CallParameter implements Construct {

  /** Returns the {@link Identifier} that is being set. */
  public abstract Optional<Identifier> getIdentifier();

  /** Returns the {@link Expression} that evaluates value being passed. */
  public abstract Expression getExpression();

  /** Returns a list of {@code CallParameters} based on the given {@link CallParametersContext}. */
  public static ImmutableList<CallParameter> create(CallParametersContext ctx) {
    if (ctx.callParameter() == null) {
      return ImmutableList.of();
    }
    return ctx.callParameter().stream().map(CallParameter::create).collect(toImmutableList());
  }

  /** Returns a new {@code CallParameter} based on the given {@link CallParameterContext}. */
  public static CallParameter create(CallParameterContext ctx) {
    Builder parameter = builder().setExpression(Expression.create(ctx.expression()));
    if (ctx.ID() != null) {
      parameter.setIdentifier(Identifier.create(ctx.ID()));
    }
    return parameter.build();
  }

  /** Returns a fresh {@code CallParameter} builder. */
  static Builder builder() {
    return new AutoValue_CallParameter.Builder();
  }

  /** A builder of {@code CallParameters}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setIdentifier(Identifier id);

    abstract Builder setExpression(Expression expression);

    abstract CallParameter build();
  }
}
