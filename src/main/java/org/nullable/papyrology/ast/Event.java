package org.nullable.papyrology.ast;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.EventContext;
import org.nullable.papyrology.grammar.PapyrusParser.EventDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.NativeEventContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** An {@link Invokable} that conforms to a game-defined callback function. */
@Immutable
public record Event(
    SourceReference sourceReference,
    Identifier identifier,
    ImmutableList<Parameter> parameters,
    ImmutableList<Statement> bodyStatements,
    Optional<String> comment,
    boolean isNative)
    implements Invokable {

  /** Returns a new {@code Event} based on the given {@link EventDeclarationContext}. */
  static Event create(EventDeclarationContext ctx) {
    if (ctx instanceof EventContext) {
      return create((EventContext) ctx);
    }
    if (ctx instanceof NativeEventContext) {
      return create((NativeEventContext) ctx);
    }
    throw new IllegalArgumentException(
        String.format("Event::create passed malformed EventDeclarationContext: %s", ctx));
  }

  private static Event create(EventContext ctx) {
    return new Event(
        SourceReference.create(ctx),
        Identifier.create(ctx.ID()),
        Parameter.create(ctx.parameters()),
        Statement.create(ctx.statementBlock()),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        false);
  }

  private static Event create(NativeEventContext ctx) {
    return new Event(
        SourceReference.create(ctx),
        Identifier.create(ctx.ID()),
        Parameter.create(ctx.parameters()),
        ImmutableList.of(),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        true);
  }
}
