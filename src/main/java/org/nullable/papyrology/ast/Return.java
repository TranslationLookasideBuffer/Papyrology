package org.nullable.papyrology.ast;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.ReturnContext;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Statement} that causes execution of a function to cease, potentially with a value. */
@AutoValue
@Immutable
public abstract class Return implements Statement {

  /** Returns the {@link Expression} that is returned to the caller, if present. */
  public abstract Optional<Expression> getValueExpression();

  /** Returns a new {@code Return} based on the given {@link ReturnContext}. */
  static Return create(ReturnContext ctx) {
    Builder returnStatement = builder().setSourceReference(SourceReference.create(ctx));
    if (ctx.expression() != null) {
      returnStatement.setValueExpression(Expression.create(ctx.expression()));
    }
    return returnStatement.build();
  }

  /** Returns a fresh {@code Return} builder. */
  static Builder builder() {
    return new AutoValue_Return.Builder();
  }

  /** A builder of {@code Returns}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  public abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setValueExpression(Expression expression);

    abstract Return build();
  }
}
