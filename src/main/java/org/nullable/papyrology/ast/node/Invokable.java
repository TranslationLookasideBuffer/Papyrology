package org.nullable.papyrology.ast.node;

import org.nullable.papyrology.grammar.PapyrusParser.InvokableContext;

/**
 * Denotes a construct that can be invoked.
 *
 * <p>Specifically, a {@link Function} or {@link Event}.
 */
public interface Invokable extends Declaration {

  /** Returns a new {@code Invokable} based on the given {@link InvokableContext}. */
  static Invokable create(InvokableContext ctx) {
    if (ctx.functionDeclaration() != null) {
      return Function.create(ctx.functionDeclaration());
    }
    if (ctx.eventDeclaration() != null) {
      return Event.create(ctx.eventDeclaration());
    }
    throw new IllegalArgumentException(
        String.format("Invokable::create passed malformed InvokableContext: %s", ctx));
  }
}
