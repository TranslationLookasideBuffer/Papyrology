package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import org.nullable.papyrology.grammar.PapyrusParser.ImportDeclarationContext;

/**
 * A {@link Declaration} that includes another script's global functions in this scripts namespace.
 */
@AutoValue
public abstract class Import implements Declaration {

  /** Returns the {@code Identifier} of the script being imported by this script. */
  public abstract Identifier getImportedScriptIdentifier();

  /** Returns a new {@code Import} based on the given {@link ImportDeclarationContext}. */
  public static Import create(ImportDeclarationContext ctx) {
    return builder().setImportedScriptIdentifier(Identifier.create(ctx.ID())).build();
  }

  /** Returns a fresh {@code Import} builder. */
  static Builder builder() {
    return new AutoValue_Import.Builder();
  }

  /** A builder of {@code Imports}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setImportedScriptIdentifier(Identifier id);

    abstract Import build();
  }
}
