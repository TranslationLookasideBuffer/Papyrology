package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.WhileContext;

/** A {@link Statement} that defines a repeated, conditional execution of a block of code. */
@AutoValue
public abstract class While implements Statement {

  /** Returns the {@link Expression} that controls loop execution. */
  public abstract Expression getConditionalExpression();

  /** Returns the {@link Statement Statements} that make up the body of this loop. */
  public abstract ImmutableList<Statement> getBodyStatements();

  /** Returns a new {@code While} based on the given {@link WhileContext}. */
  public static While create(WhileContext ctx) {
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setConditionalExpression(Expression.create(ctx.expression()))
        .setBodyStatements(Statement.create(ctx.statementBlock()))
        .build();
  }

  /** Returns a fresh {@code While} builder. */
  static Builder builder() {
    return new AutoValue_While.Builder();
  }

  /** A builder of {@code Whiles}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setConditionalExpression(Expression expression);

    abstract Builder setBodyStatements(ImmutableList<Statement> statements);

    abstract While build();
  }
}
