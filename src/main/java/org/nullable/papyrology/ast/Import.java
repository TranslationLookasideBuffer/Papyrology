package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ImportDeclarationContext;
import org.nullable.papyrology.source.SourceReference;

/**
 * A {@link Declaration} that includes another script's global functions in this scripts namespace.
 */
@Immutable
public record Import(SourceReference sourceReference, Identifier importedScriptIdentifier)
    implements Declaration {

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code Import} based on the given {@link ImportDeclarationContext}. */
  static Import create(ImportDeclarationContext ctx) {
    return new Import(SourceReference.create(ctx), Identifier.create(ctx.ID()));
  }
}
