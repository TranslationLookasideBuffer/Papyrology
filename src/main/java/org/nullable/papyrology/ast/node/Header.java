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
  public static Builder builder() {
    return new AutoValue_Header.Builder();
  }

  /** A builder of {@code Headers}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setScriptIdentifier(Identifier id);

    public abstract Builder setParentScriptIdentifier(Identifier id);

    public abstract Builder setHidden(boolean isHidden);

    public abstract Builder setConditional(boolean isConditional);

    public abstract Builder setScriptComment(String comment);

    public abstract Header build();
  }
}
