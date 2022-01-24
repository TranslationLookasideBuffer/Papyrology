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

  /** Returns a fresh {@code ScriptVariable} builder. */
  public static Builder builder() {
    return new AutoValue_ScriptVariable.Builder();
  }

  /** A builder of {@code ScriptVariables}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setType(Type type);

    public abstract Builder setIdentifier(Identifier id);

    public abstract Builder setLiteral(Literal literal);

    public abstract Builder setConditional(boolean isConditional);

    public abstract ScriptVariable build();
  }
}
