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
  public static Builder builder() {
    return new AutoValue_BooleanLiteral.Builder();
  }

  /** A builder of {@code BooleanLiterals}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setValue(boolean value);

    public abstract Builder setRawValue(String rawValue);

    public abstract BooleanLiteral build();
  }
}
