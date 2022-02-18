package org.nullable.papyrology.ast;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ImportDeclarationContext;
import org.nullable.papyrology.source.SourceReference;

/**
 * A {@link Declaration} that includes another script's global functions in this scripts namespace.
 */
@AutoValue
@Immutable
public abstract class Import implements Declaration {

  /** Returns the {@code Identifier} of the script being imported by this script. */
  public abstract Identifier getImportedScriptIdentifier();

  /** Returns a new {@code Import} based on the given {@link ImportDeclarationContext}. */
  static Import create(ImportDeclarationContext ctx) {
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setImportedScriptIdentifier(Identifier.create(ctx.ID()))
        .build();
  }

  /** Returns a fresh {@code Import} builder. */
  static Builder builder() {
    return new AutoValue_Import.Builder();
  }

  /** A builder of {@code Imports}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setImportedScriptIdentifier(Identifier id);

    abstract Import build();
  }
}
