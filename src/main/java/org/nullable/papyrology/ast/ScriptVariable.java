package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.VariableDeclarationContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** A {@link Declaration} that defines a variable at the script level. */
@Immutable
public record ScriptVariable(
    SourceReference sourceReference,
    Type type,
    Identifier identifier,
    Optional<Literal> literal,
    boolean isConditional)
    implements Declaration {

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code ScriptVariable} based on the given {@link VariableDeclarationContext}. */
  static ScriptVariable create(VariableDeclarationContext ctx) {
    return new ScriptVariable(
        SourceReference.create(ctx),
        Type.create(ctx.type()),
        Identifier.create(ctx.ID()),
        Optionals.of(ctx.literal() != null, () -> Literal.create(ctx.literal())),
        !ctx.F_CONDITIONAL().isEmpty());
  }
}
