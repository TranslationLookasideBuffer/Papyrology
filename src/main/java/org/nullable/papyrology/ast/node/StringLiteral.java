package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** A {@link Literal} string value (e.g. {@code "foo"}). */
@AutoValue
public abstract class StringLiteral implements Literal {

  /** Returns the value of this literal. */
  public abstract String getValue();

  /** Returns a fresh {@code StringLiteral} builder. */
  static Builder builder() {
    return new AutoValue_StringLiteral.Builder();
  }

  /** A builder of {@code StringLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(String value);

    abstract StringLiteral build();
  }
}
