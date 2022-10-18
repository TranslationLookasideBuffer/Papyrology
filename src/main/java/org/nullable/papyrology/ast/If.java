package org.nullable.papyrology.ast;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.IfContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** A {@link Statement} that defines some series of conditional execution of blocks of code. */
@Immutable
public record If(
    SourceReference sourceReference,
    ImmutableList<ConditionalBlock> conditionalBlocks,
    Optional<Block> elseBlock)
    implements Statement {

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code If} based on the given {@link IfContext}. */
  static If create(IfContext ctx) {
    ImmutableList.Builder<ConditionalBlock> conditionalBlocks = ImmutableList.builder();
    int expressions = ctx.expression().size();
    for (int i = 0; i < expressions; i++) {
      conditionalBlocks.add(
          new ConditionalBlock(
              Expression.create(ctx.expression(i)), Block.create(ctx.statementBlock(i))));
    }
    Optional<Block> elseBlock =
        Optionals.of(
            ctx.statementBlock().size() > expressions,
            () -> Block.create(ctx.statementBlock(expressions)));
    // If an else statement is present, ctx.statementBlock() will have exactly one more element
    // than ctx.expression() and it will be the last element in the list.
    return new If(SourceReference.create(ctx), conditionalBlocks.build(), elseBlock);
  }

  @Immutable
  public record ConditionalBlock(Expression conditionalExpression, Block body) {}
}
