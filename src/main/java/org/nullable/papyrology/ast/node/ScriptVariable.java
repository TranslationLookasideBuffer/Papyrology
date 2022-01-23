package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/** A {@link Declaration} that defines a variable at the script level. */
@AutoValue
public abstract class ScriptVariable implements Declaration {

  /** Returns this script variable's {@link Type}. */
  public abstract Type getType();

  /** Returns this script variable's {@link Identifier}. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Literal} that defines this script variable's initial value, if present. */
  public abstract Optional<Literal> getLiteral();

  /** Returns whether or not this script variable is conditional. */
  public abstract boolean isConditional();

  /** A builder of {@code ScriptVariables}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setType(Type type);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setLiteral(Literal literal);

    abstract Builder setConditional(boolean isConditional);

    abstract ScriptVariable build();
  }
}
