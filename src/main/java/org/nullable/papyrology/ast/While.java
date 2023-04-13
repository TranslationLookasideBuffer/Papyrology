package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.WhileContext;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Statement} that defines a repeated, conditional execution of a block of code. */
@Immutable
public record While(SourceReference sourceReference, Expression conditionalExpression, Block body)
    implements Statement {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code While} based on the given {@link WhileContext}. */
  static While create(WhileContext ctx) {
    return new While(
        SourceReference.create(ctx),
        Expression.create(ctx.expression()),
        Block.create(ctx.statementBlock()));
  }
}
