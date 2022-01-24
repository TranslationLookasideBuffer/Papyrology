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
  public static Builder builder() {
    return new AutoValue_Parameter.Builder();
  }

  /** A builder of {@code Parameters}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setType(Type type);

    public abstract Builder setIdentifier(Identifier id);

    public abstract Builder setDefaultValueLiteral(Literal defaultValueLiteral);

    public abstract Parameter build();
  }
}
