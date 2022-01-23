package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** A {@link Literal} boolean value (e.g. {@code True}). */
@AutoValue
public abstract class BooleanLiteral implements Literal {

  /** Returns the actual value of this literal. */
  public abstract boolean getValue();

  /** Returns the raw value of the literal (as it appears in source). */
  public abstract String getRawValue();

  /** Returns a fresh {@code BooleanLiteral} builder. */
  static Builder builder() {
    return new AutoValue_BooleanLiteral.Builder();
  }

  /** A builder of {@code BooleanLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(boolean value);

    abstract Builder setRawValue(String rawValue);

    abstract BooleanLiteral build();
  }
}
