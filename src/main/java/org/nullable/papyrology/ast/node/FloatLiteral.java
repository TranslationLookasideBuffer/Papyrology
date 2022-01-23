package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** A {@link Literal} float value (e.g. {@code 1.23}). */
@AutoValue
public abstract class FloatLiteral implements Literal {

  /** Returns the actual value of this literal. */
  public abstract float getValue();

  /** Returns the raw value of the literal (as it appears in source). */
  public abstract String getRawValue();

  /** Returns a fresh {@code FloatLiteral} builder. */
  static Builder builder() {
    return new AutoValue_FloatLiteral.Builder();
  }

  /** A builder of {@code FloatLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(float value);

    abstract Builder setRawValue(String rawValue);

    abstract FloatLiteral build();
  }
}
