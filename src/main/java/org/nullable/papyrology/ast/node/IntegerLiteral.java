package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** A {@link Literal} integer value (e.g. {@code 42}). */
@AutoValue
public abstract class IntegerLiteral implements Literal {

  /** Returns the actual value of this literal. */
  public abstract int getValue();

  /** Returns the raw value of the literal (as it appears in source). */
  public abstract String getRawValue();

  /** Returns a fresh {@code IntegerLiteral} builder. */
  static Builder builder() {
    return new AutoValue_IntegerLiteral.Builder();
  }

  /** A builder of {@code IntegerLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(int value);

    abstract Builder setRawValue(String rawValue);

    abstract IntegerLiteral build();
  }
}
