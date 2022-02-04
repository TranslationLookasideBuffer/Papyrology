package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.ParentheticalContext;

/** An {@link Expression} that evaluates to a single subexpression. */
@AutoValue
public abstract class Parenthetical implements Expression {

  /** Returns the {@link Expression} that is surrounded by parentheses. */
  public abstract Expression getExpression();

  /** Returns a new {@code Parenthetical} based on the given {@link ParentheticalContext}. */
  public static Parenthetical create(ParentheticalContext ctx) {
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setExpression(Expression.create(ctx.expression()))
        .build();
  }

  /** Returns a fresh {@code Parenthetical} builder. */
  static Builder builder() {
    return new AutoValue_Parenthetical.Builder();
  }

  /** A builder of {@code Parentheticals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setExpression(Expression expression);

    abstract Parenthetical build();
  }
}
