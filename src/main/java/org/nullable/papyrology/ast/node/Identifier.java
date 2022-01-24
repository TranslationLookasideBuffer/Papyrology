package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Locale;

/** An {@link Expression} that evaluates to some scoped identifier (e.g. variable name). */
@AutoValue
public abstract class Identifier implements Expression {

  /** Returns the value of this identifier. */
  public abstract String getValue();

  /** Returns a fresh {@code IdentifierNode} builder. */
  public static Builder builder() {
    return new AutoValue_Identifier.Builder();
  }

  /** Returns whether or not this {@link Identifier} refers to the same entity as the given one. */
  public final boolean isEquivalent(Identifier other) {
    return getValue().toUpperCase(Locale.US).equals(other.getValue().toUpperCase(Locale.US));
  }

  /** A builder of {@code Identifiers}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setValue(String value);

    public abstract Identifier build();
  }
}
