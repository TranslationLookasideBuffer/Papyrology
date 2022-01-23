package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/**
 * A {@link Literal} object reference
 *
 * <p>Since Papyrus doesn't allow for object creation, this really just consists of three values:
 *
 * <ul>
 *   <li>{@code None}, the "empty" reference
 *   <li>{@code Self}, the reference to the current script
 *   <li>{@code Parent}, the reference to the script the current one extends
 * </ul>
 */
@AutoValue
public abstract class ObjectLiteral implements Literal {

  /** The type of this node. */
  public enum Reference {
    NONE,
    SELF,
    PARENT
  }

  /** Returns the actual value of this literal. */
  public abstract Reference getValue();

  /** Returns a fresh {@code ObjectLiteral} builder. */
  static Builder builder() {
    return new AutoValue_ObjectLiteral.Builder();
  }

  /** A builder of {@code ObjectLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(Reference value);

    abstract ObjectLiteral build();
  }
}
