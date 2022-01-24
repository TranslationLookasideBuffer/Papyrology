package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/** A {@link Statement} that defines a local variable (within an {@code Invokable}). */
@AutoValue
public abstract class Variable implements Statement {

  /** Returns this variable's {@link Type}. */
  public abstract Type getType();

  /** Returns this variable's {@link Identifier}. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Expression} that defines this variable's initial value, if present. */
  public abstract Optional<Expression> getValueExpression();

  /** Returns a fresh {@code Variable} builder. */
  public static Builder builder() {
    return new AutoValue_Variable.Builder();
  }

  /** A builder of {@code Variables}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setType(Type type);

    public abstract Builder setIdentifier(Identifier id);

    public abstract Builder setValueExpression(Expression expression);

    public abstract Variable build();
  }
}
