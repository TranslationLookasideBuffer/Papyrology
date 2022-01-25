package org.nullable.papyrology.ast.node;

import org.nullable.papyrology.grammar.PapyrusParser.LiteralContext;

/** An {@link Expression} that is a literal value defined by the source code. */
public interface Literal extends Expression {

  /** Returns a new {@code Literal} based on the given {@link LiteralContext}. */
  static Literal create(LiteralContext ctx) {
    if (ctx.K_TRUE() != null) {
      return BooleanLiteral.create(ctx.K_TRUE());
    }
    if (ctx.K_FALSE() != null) {
      return BooleanLiteral.create(ctx.K_FALSE());
    }
    if (ctx.L_FLOAT() != null) {
      return FloatLiteral.create(ctx.L_FLOAT());
    }
    if (ctx.L_UINT() != null) {
      return IntegerLiteral.create(ctx.L_UINT());
    }
    if (ctx.L_INT() != null) {
      return IntegerLiteral.create(ctx.L_INT());
    }
    if (ctx.L_STRING() != null) {
      return StringLiteral.create(ctx.L_STRING());
    }
    if (ctx.K_NONE() != null) {
      return ObjectLiteral.create(ctx.K_NONE());
    }
    if (ctx.K_SELF() != null) {
      return ObjectLiteral.create(ctx.K_SELF());
    }
    if (ctx.K_PARENT() != null) {
      return ObjectLiteral.create(ctx.K_PARENT());
    }
    throw new IllegalArgumentException(
        String.format("Literal::create passed malformed LiteralContext: %s", ctx));
  }
}
