package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;

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

  /** Returns a fresh {@code DotAccess} builder. */
  public static Builder builder() {
    return new AutoValue_DotAccess.Builder();
  }

  /** A builder of {@code DotAccesses}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setReferenceExpression(Expression expression);

    public abstract Builder setIdentifier(Identifier id);

    public abstract DotAccess build();
  }
}
