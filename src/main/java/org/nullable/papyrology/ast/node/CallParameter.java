package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/** A parameter set in a function call. */
@AutoValue
public abstract class CallParameter implements Construct {

  /** Returns the {@link Identifier} that is being set. */
  public abstract Optional<Identifier> getIdentifier();

  /** Returns the {@link Expression} that evaluates value being passed. */
  public abstract Expression getExpression();

  /** Returns a fresh {@code CallParameter} builder. */
  static Builder builder() {
    return new AutoValue_CallParameter.Builder();
  }

  /** A builder of {@code CallParameters}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setIdentifier(Identifier id);

    abstract Builder setExpression(Expression expression);

    abstract CallParameter build();
  }
}
