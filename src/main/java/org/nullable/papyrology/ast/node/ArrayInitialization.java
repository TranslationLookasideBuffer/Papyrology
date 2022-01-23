package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An expression that evaluates to a newly initialized array. */
@AutoValue
public abstract class ArrayInitialization implements Expression {

  /** Returns the {@link Type} of this array initialization. */
  public abstract Type getType();

  /** Returns the size of the array being initialized. */
  public abstract int getSize();

  /** Returns a fresh {@code ArrayInitialization} builder. */
  static Builder builder() {
    return new AutoValue_ArrayInitialization.Builder();
  }

  /** A builder of {@code ArrayInitializations}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setType(Type type);

    abstract Builder setSize(int size);

    abstract ArrayInitialization build();
  }
}
