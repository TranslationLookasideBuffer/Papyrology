package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.HeaderContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** Metadata about a single script. */
@Immutable
public record Header(
    SourceReference sourceReference,
    Identifier scriptIdentifier,
    Optional<Identifier> parentScriptIdentifier,
    boolean isHidden,
    boolean isConditional,
    Optional<String> comment)
    implements Construct {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code Header} based on the given {@link HeaderContext}. */
  static Header create(HeaderContext ctx) {
    return new Header(
        SourceReference.create(ctx),
        Identifier.create(ctx.ID(0)),
        Optionals.of(ctx.ID().size() > 1, () -> Identifier.create(ctx.ID(1))),
        !ctx.F_HIDDEN().isEmpty(),
        !ctx.F_CONDITIONAL().isEmpty(),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()));
  }
}
