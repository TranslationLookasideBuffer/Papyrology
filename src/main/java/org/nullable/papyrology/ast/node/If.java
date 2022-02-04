package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.IfContext;

/** A {@link Statement} that defines some series of conditional execution of blocks of code. */
@AutoValue
public abstract class If implements Statement {

  /**
   * Returns the series of {@link ConditionalBlock ConditionalBlocks} that make up a if-elseif
   * chain.
   *
   * <p>This list is guaranteed to have at least one entry (the main if block).
   */
  public abstract ImmutableList<ConditionalBlock> getConditionalBlocks();

  /**
   * Returns the {@link Statement Statements} that make up the else block.
   *
   * <p>This list is empty if the else block is not present (or is just empty).
   */
  public abstract ImmutableList<Statement> getElseStatements();

  /** Returns a new {@code If} based on the given {@link IfContext}. */
  public static If create(IfContext ctx) {
    ImmutableList.Builder<ConditionalBlock> conditionalBlocks = ImmutableList.builder();
    int expressions = ctx.expression().size();
    for (int i = 0; i < expressions; i++) {
      conditionalBlocks.add(
          ConditionalBlock.create(
              Expression.create(ctx.expression(i)), Statement.create(ctx.statementBlock(i))));
    }
    // If an else statement is present, ctx.statementBlock() will have exactly one more element
    // than ctx.expression() and it will be the last element in the list.
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setConditionalBlocks(conditionalBlocks.build())
        .setElseStatements(
            ctx.statementBlock().size() > expressions
                ? Statement.create(ctx.statementBlock(expressions))
                : ImmutableList.of())
        .build();
  }

  /** Returns a fresh {@code If} builder. */
  static Builder builder() {
    return new AutoValue_If.Builder();
  }

  /** A builder of {@code Ifs}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setConditionalBlocks(ImmutableList<ConditionalBlock> conditionalBlocks);

    abstract Builder setElseStatements(ImmutableList<Statement> elseStatements);

    abstract If build();
  }

  @AutoValue
  public abstract static class ConditionalBlock {
    /** Returns the {@link Expression} that controls execution the block. */
    public abstract Expression getConditionalExpression();

    /** Returns the {@link Statement Statements} that make up the body of this conditional. */
    public abstract ImmutableList<Statement> getBodyStatements();

    static ConditionalBlock create(
        Expression conditionalExpression, ImmutableList<Statement> bodyStatements) {
      return new AutoValue_If_ConditionalBlock(conditionalExpression, bodyStatements);
    }
  }
}
