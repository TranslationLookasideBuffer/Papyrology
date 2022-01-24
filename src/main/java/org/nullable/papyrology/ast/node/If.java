package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.beans.Expression;

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

  /** Returns a fresh {@code If} builder. */
  public static Builder builder() {
    return new AutoValue_If.Builder();
  }

  /** A builder of {@code Ifs}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setConditionalBlocks(ImmutableList<ConditionalBlock> conditionalBlocks);

    public abstract Builder setElseStatements(ImmutableList<Statement> elseStatements);

    public abstract If build();
  }

  @AutoValue
  public abstract static class ConditionalBlock {
    /** Returns the {@link Expression} that controls execution the block. */
    public abstract Expression getConditionalExpression();

    /** Returns the {@link Statement Statements} that make up the body of this conditional. */
    public abstract ImmutableList<Statement> getBodyStatements();

    public static ConditionalBlock create(
        Expression conditionalExpression, ImmutableList<Statement> bodyStatements) {
      return new AutoValue_If_ConditionalBlock(conditionalExpression, bodyStatements);
    }
  }
}