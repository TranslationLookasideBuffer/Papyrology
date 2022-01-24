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
  public static Builder builder() {
    return new AutoValue_ArrayInitialization.Builder();
  }

  /** A builder of {@code ArrayInitializations}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setType(Type type);

    public abstract Builder setSize(int size);

    public abstract ArrayInitialization build();
  }
}
