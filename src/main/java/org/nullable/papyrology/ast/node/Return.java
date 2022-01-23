package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/** A {@link Statement} that causes execution of a function to cease, potentially with a value. */
@AutoValue
public abstract class Return implements Statement {

  /** Returns the {@link Expression} that is returned to the caller, if present. */
  public abstract Optional<Expression> getValueExpression();

  /** Returns a fresh {@code Return} builder. */
  static Builder builder() {
    return new AutoValue_Return.Builder();
  }

  /** A builder of {@code Returns}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValueExpression(Expression expression);

    abstract Return build();
  }
}
