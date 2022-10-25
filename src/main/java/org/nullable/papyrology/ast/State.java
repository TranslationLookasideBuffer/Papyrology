package org.nullable.papyrology.ast;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.StateDeclarationContext;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Declaration} that defines a script state. */
@Immutable
public record State(
    SourceReference sourceReference,
    Identifier identifier,
    ImmutableList<Invokable> invokables,
    boolean isAuto)
    implements Declaration {

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code State} based on the given {@link StateDeclarationContext}. */
  static State create(StateDeclarationContext ctx) {
    SourceReference sourceReference = SourceReference.create(ctx);
    ImmutableList<Invokable> invokables =
        ctx.invokable().stream().map(Invokable::create).collect(toImmutableList());
    if (invokables.stream().anyMatch(i -> i instanceof Function f && f.isGlobal())) {
      throw new SyntaxException(sourceReference, "Cannot define global Functions within a State.");
    }
    return new State(
        sourceReference, Identifier.create(ctx.ID()), invokables, ctx.K_AUTO() != null);
  }
}
