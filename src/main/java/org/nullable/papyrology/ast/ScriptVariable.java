package org.nullable.papyrology.ast;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.VariableDeclarationContext;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Declaration} that defines a variable at the script level. */
@AutoValue
@Immutable
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
  static ScriptVariable create(VariableDeclarationContext ctx) {
    Builder scriptVariable =
        builder()
            .setSourceReference(SourceReference.create(ctx))
            .setType(Type.create(ctx.type()))
            .setIdentifier(Identifier.create(ctx.ID()))
            .setConditional(!ctx.F_CONDITIONAL().isEmpty());
    if (ctx.literal() != null) {
      scriptVariable.setLiteral(Literal.create(ctx.literal()));
    }
    return scriptVariable.build();
  }

  /** Returns a fresh {@code ScriptVariable} builder. */
  static Builder builder() {
    return new AutoValue_ScriptVariable.Builder();
  }

  /** A builder of {@code ScriptVariables}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setType(Type type);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setLiteral(Literal literal);

    abstract Builder setConditional(boolean isConditional);

    abstract ScriptVariable build();
  }
}
