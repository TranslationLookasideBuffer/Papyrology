package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.DotAccessOrFunctionCallContext;

/**
 * An {@link Expression} that evaluates to the value of some variable or property belonging to
 * another object
 */
@AutoValue
public abstract class DotAccess implements Expression {

  /** Returns the {@link Expression} that evaluates to the reference being accessed. */
  public abstract Expression getReferenceExpression();

  /** Returns the {@link Identifier} of the property/variable being accessed. */
  public abstract Identifier getIdentifier();

  /** Returns a new {@code DocAccess} based on the given {@link DotAccessOrFunctionCallContext}. */
  public static DotAccess create(DotAccessOrFunctionCallContext ctx) {
    checkArgument(
        ctx.callParameters() == null,
        "DotAccess::create passed a DotAccessOrFunctionCallContext that maps to a function call:"
            + " %s",
        ctx);
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setReferenceExpression(Expression.create(ctx.expression()))
        .setIdentifier(Identifier.create(ctx.ID()))
        .build();
  }

  /** Returns a fresh {@code DotAccess} builder. */
  static Builder builder() {
    return new AutoValue_DotAccess.Builder();
  }

  /** A builder of {@code DotAccesses}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setReferenceExpression(Expression expression);

    abstract Builder setIdentifier(Identifier id);

    abstract DotAccess build();
  }
}
