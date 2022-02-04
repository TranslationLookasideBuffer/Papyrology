package org.nullable.papyrology.ast.node;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Optional;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.ParameterContext;
import org.nullable.papyrology.grammar.PapyrusParser.ParametersContext;

/** A parameter defined by a function. */
@AutoValue
public abstract class Parameter implements Construct {

  /** Returns the {@link Type} of this parameter. */
  public abstract Type getType();

  /** Returns the {@link Identifier} of this parameter. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Literal} that is used as this parameter's default value, if present. */
  public abstract Optional<Literal> getDefaultValueLiteral();

  /** Returns a list of {@code Parameters} based on the given {@link ParametersContext}. */
  public static ImmutableList<Parameter> create(ParametersContext ctx) {
    if (ctx.parameter() == null) {
      return ImmutableList.of();
    }
    return ctx.parameter().stream().map(Parameter::create).collect(toImmutableList());
  }

  /** Returns a new {@code Parameter} based on the given {@link ParameterContext}. */
  public static Parameter create(ParameterContext ctx) {
    Builder parameter =
        builder()
            .setSourceReference(SourceReference.create(ctx))
            .setType(Type.create(ctx.type()))
            .setIdentifier(Identifier.create(ctx.ID()));
    if (ctx.literal() != null) {
      parameter.setDefaultValueLiteral(Literal.create(ctx.literal()));
    }
    return parameter.build();
  }

  /** Returns a fresh {@code Parameter} builder. */
  static Builder builder() {
    return new AutoValue_Parameter.Builder();
  }

  /** A builder of {@code Parameters}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setType(Type type);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setDefaultValueLiteral(Literal defaultValueLiteral);

    abstract Parameter build();
  }
}
