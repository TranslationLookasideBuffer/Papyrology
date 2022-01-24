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
  public static Builder builder() {
    return new AutoValue_FloatLiteral.Builder();
  }

  /** A builder of {@code FloatLiterals}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setValue(float value);

    public abstract Builder setRawValue(String rawValue);

    public abstract FloatLiteral build();
  }
}
