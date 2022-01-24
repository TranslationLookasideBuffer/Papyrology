package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

/**
 * A {@link Declaration} that includes another script's global functions in this scripts namespace.
 */
@AutoValue
public abstract class Import implements Declaration {

  /** Returns the {@code Identifier} of the script being imported by this script. */
  public abstract Identifier getImportedScriptIdentifier();

  /** Returns a fresh {@code Import} builder. */
  public static Builder builder() {
    return new AutoValue_Import.Builder();
  }

  /** A builder of {@code Imports}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setImportedScriptIdentifier(Identifier id);

    public abstract Import build();
  }
}
