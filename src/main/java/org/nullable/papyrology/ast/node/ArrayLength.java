package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayLengthContext;

/** An {@link Expression} that evaluates to the length of an array. */
@AutoValue
public abstract class ArrayLength implements Expression {

  /** Returns the {@link Expression} that evaluates to the array having its length accessed. */
  public abstract Expression getArrayExpression();

  /** Returns a new {@code ArrayLength} based on the given {@link ArrayLengthContext}. */
  public static ArrayLength create(ArrayLengthContext ctx) {
    return builder().setArrayExpression(Expression.create(ctx.expression())).build();
  }

  /** Returns a fresh {@code ArrayLength} builder. */
  static Builder builder() {
    return new AutoValue_ArrayLength.Builder();
  }

  /** A builder of {@code ArrayLengths}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setArrayExpression(Expression expression);

    abstract ArrayLength build();
  }
}
