package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/** A parameter defined by a function. */
@AutoValue
public abstract class Parameter implements Construct {

  /** Returns the {@link Type} of this parameter. */
  public abstract Type getType();

  /** Returns the {@link Identifier} of this parameter. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Literal} that is used as this parameter's default value, if present. */
  public abstract Optional<Literal> getDefaultValueLiteral();

  /** Returns a fresh {@code Parameter} builder. */
  static Builder builder() {
    return new AutoValue_Parameter.Builder();
  }

  /** A builder of {@code Parameters}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setType(Type type);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setDefaultValueLiteral(Literal defaultValueLiteral);

    abstract Parameter build();
  }
}
