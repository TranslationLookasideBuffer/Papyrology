package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/** An {@link Expression} that evaluates to some scoped identifier (e.g. variable name). */
@AutoValue
public abstract class Identifier implements Expression {

  /** Returns the value of this identifier. */
  public abstract String getValue();

  /** Returns a fresh {@code IdentifierNode} builder. */
  static Builder builder() {
    return new AutoValue_Identifier.Builder();
  }

  /** A builder of {@code Identifiers}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(String value);

    abstract Identifier build();
  }
}
