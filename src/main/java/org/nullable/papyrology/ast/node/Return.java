package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.ReturnContext;

/** A {@link Statement} that causes execution of a function to cease, potentially with a value. */
@AutoValue
public abstract class Return implements Statement {

  /** Returns the {@link Expression} that is returned to the caller, if present. */
  public abstract Optional<Expression> getValueExpression();

  /** Returns a new {@code Return} based on the given {@link ReturnContext}. */
  public static Return create(ReturnContext ctx) {
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
  public abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setValueExpression(Expression expression);

    abstract Return build();
  }
}
