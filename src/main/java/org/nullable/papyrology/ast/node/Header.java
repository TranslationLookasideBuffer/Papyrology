package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/** Metadata about a single script. */
@AutoValue
public abstract class Header implements Construct {

  /** Returns this script's {@code Identifier}. */
  public abstract Identifier getScriptIdentifier();

  /**
   * Returns the {@code Identifier} of the script that this script extends, if present (i.e. this
   * script's parent).
   */
  public abstract Optional<Identifier> getParentScriptIdentifier();

  /** Returns whether or not this script is hidden. */
  public abstract boolean isHidden();

  /** Returns whether or not this script is conditional. */
  public abstract boolean isConditional();

  /** Returns the content of this script's documentation comment if present. */
  public abstract Optional<String> getScriptComment();

  /** Returns a fresh {@code Header} builder. */
  static Builder builder() {
    return new AutoValue_Header.Builder();
  }

  /** A builder of {@code Headers}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setScriptIdentifier(Identifier id);

    abstract Builder setParentScriptIdentifier(Identifier id);

    abstract Builder setHidden(boolean isHidden);

    abstract Builder setConditional(boolean isConditional);

    abstract Builder setScriptComment(String comment);

    abstract Header build();
  }
}
