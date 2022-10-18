package org.nullable.papyrology.ast;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.ParameterContext;
import org.nullable.papyrology.grammar.PapyrusParser.ParametersContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** A parameter defined by a function. */
@Immutable
public record Parameter(
    SourceReference sourceReference,
    Type type,
    Identifier identifier,
    Optional<Literal> defaultValueLiteral)
    implements Construct {

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a list of {@code Parameters} based on the given {@link ParametersContext}. */
  static ImmutableList<Parameter> create(ParametersContext ctx) {
    if (ctx.parameter() == null) {
      return ImmutableList.of();
    }
    return ctx.parameter().stream().map(Parameter::create).collect(toImmutableList());
  }

  /** Returns a new {@code Parameter} based on the given {@link ParameterContext}. */
  static Parameter create(ParameterContext ctx) {
    return new Parameter(
        SourceReference.create(ctx),
        Type.create(ctx.type()),
        Identifier.create(ctx.ID()),
        Optionals.of(ctx.literal() != null, () -> Literal.create(ctx.literal())));
  }
}
