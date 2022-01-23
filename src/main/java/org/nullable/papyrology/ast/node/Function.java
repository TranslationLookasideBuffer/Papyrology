package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Optional;

/** An {@link Invokable} that defines a unit of work that may be called by scripts. */
@AutoValue
public abstract class Function implements Invokable {

  /** Returns the return {@link Type} of this function, if present. */
  public abstract Optional<Type> getReturnType();

  /** Returns the {@link Identifier} of this function. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Parameter Parameters} of this function. */
  public abstract ImmutableList<Parameter> getParameters();

  /** Returns the {@link Statement Statements} that make up the body of this function. */
  public abstract ImmutableList<Statement> getBodyStatements();

  /** Returns the documentation comment of this function, if present. */
  public abstract Optional<String> getComment();

  /** Returns whether or not this function is global. */
  public abstract boolean isGlobal();

  /**
   * Returns whether or not this function is native.
   *
   * <p>If true, {@link #getBodyStatements()} will return an empty list.s
   */
  public abstract boolean isNative();

  /** Returns a fresh {@code Function} builder. */
  static Builder builder() {
    return new AutoValue_Function.Builder();
  }

  /** A builder of {@code Functions}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setReturnType(Type type);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setParameters(ImmutableList<Parameter> parameters);

    abstract Builder setBodyStatements(ImmutableList<Statement> bodyStatements);

    abstract Builder setComment(String comment);

    abstract Builder setGlobal(boolean isGlobal);

    abstract Builder setNative(boolean isNative);

    abstract Function autoBuild();

    final Function build() {
      Function function = autoBuild();
      checkState(
          !function.isNative() || function.getBodyStatements().isEmpty(),
          "Native Function cannot specify body Statements");
      return function;
    }
  }
}
