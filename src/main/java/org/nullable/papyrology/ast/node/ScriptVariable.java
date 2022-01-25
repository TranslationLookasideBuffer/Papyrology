package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.VariableDeclarationContext;

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

  /** Returns a new {@code ScriptVariable} based on the given {@link VariableDeclarationContext}. */
  public static ScriptVariable create(VariableDeclarationContext ctx) {
    throw new UnsupportedOperationException();
  }

  /** Returns a fresh {@code ScriptVariable} builder. */
  static Builder builder() {
    return new AutoValue_ScriptVariable.Builder();
  }

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
