package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ParentheticalContext;
import org.nullable.papyrology.source.SourceReference;

/** An {@link Expression} that evaluates to a single subexpression. */
@Immutable
public record Parenthetical(SourceReference sourceReference, Expression expression)
    implements Expression {

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code Parenthetical} based on the given {@link ParentheticalContext}. */
  static Parenthetical create(ParentheticalContext ctx) {
    return new Parenthetical(SourceReference.create(ctx), Expression.create(ctx.expression()));
  }
}
