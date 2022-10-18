package org.nullable.papyrology.ast;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.CallParameterContext;
import org.nullable.papyrology.grammar.PapyrusParser.CallParametersContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** A parameter set in a function call. */
@Immutable
public record CallParameter(
    SourceReference sourceReference, Optional<Identifier> identifier, Expression expression)
    implements Construct {

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a list of {@code CallParameters} based on the given {@link CallParametersContext}. */
  static ImmutableList<CallParameter> create(CallParametersContext ctx) {
    if (ctx.callParameter() == null) {
      return ImmutableList.of();
    }
    return ctx.callParameter().stream().map(CallParameter::create).collect(toImmutableList());
  }

  /** Returns a new {@code CallParameter} based on the given {@link CallParameterContext}. */
  static CallParameter create(CallParameterContext ctx) {
    return new CallParameter(
        SourceReference.create(ctx),
        Optionals.of(ctx.ID() != null, () -> Identifier.create(ctx.ID())),
        Expression.create(ctx.expression()));
  }
}
