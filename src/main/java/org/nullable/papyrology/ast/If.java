package org.nullable.papyrology.ast;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.IfContext;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Statement} that defines some series of conditional execution of blocks of code. */
@Immutable
public record If(
    SourceReference sourceReference,
    ImmutableList<ConditionalBlock> conditionalBlocks,
    ImmutableList<Statement> elseStatements)
    implements Statement {

  /** Returns a new {@code If} based on the given {@link IfContext}. */
  static If create(IfContext ctx) {
    ImmutableList.Builder<ConditionalBlock> conditionalBlocks = ImmutableList.builder();
    int expressions = ctx.expression().size();
    for (int i = 0; i < expressions; i++) {
      conditionalBlocks.add(
          new ConditionalBlock(
              Expression.create(ctx.expression(i)), Statement.create(ctx.statementBlock(i))));
    }
    ImmutableList<Statement> elseStatements =
        ctx.statementBlock().size() > expressions
            ? Statement.create(ctx.statementBlock(expressions))
            : ImmutableList.of();
    // If an else statement is present, ctx.statementBlock() will have exactly one more element
    // than ctx.expression() and it will be the last element in the list.
    return new If(SourceReference.create(ctx), conditionalBlocks.build(), elseStatements);
  }

  @Immutable
  public record ConditionalBlock(
      Expression conditionalExpression, ImmutableList<Statement> bodyStatements) {}
}
