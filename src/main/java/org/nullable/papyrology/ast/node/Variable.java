package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.LocalVariableContext;

/** A {@link Statement} that defines a local variable (within an {@code Invokable}). */
@AutoValue
public abstract class Variable implements Statement {

  /** Returns this variable's {@link Type}. */
  public abstract Type getType();

  /** Returns this variable's {@link Identifier}. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Expression} that defines this variable's initial value, if present. */
  public abstract Optional<Expression> getValueExpression();

  /** Returns a new {@code Variable} based on the given {@link LocalVariableContext}. */
  public static Variable create(LocalVariableContext ctx) {
    Builder variable =
        builder()
            .setSourceReference(SourceReference.create(ctx))
            .setType(Type.create(ctx.type()))
            .setIdentifier(Identifier.create(ctx.ID()));
    if (ctx.expression() != null) {
      variable.setValueExpression(Expression.create(ctx.expression()));
    }
    return variable.build();
  }

  /** Returns a fresh {@code Variable} builder. */
  static Builder builder() {
    return new AutoValue_Variable.Builder();
  }

  /** A builder of {@code Variables}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setType(Type type);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setValueExpression(Expression expression);

    abstract Variable build();
  }
}
