package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.ReturnContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** A {@link Statement} that causes execution of a function to cease, potentially with a value. */
@Immutable
public record Return(SourceReference sourceReference, Optional<Expression> valueExpression)
    implements Statement {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code Return} based on the given {@link ReturnContext}. */
  static Return create(ReturnContext ctx) {
    return new Return(
        SourceReference.create(ctx),
        Optionals.of(ctx.expression() != null, () -> Expression.create(ctx.expression())));
  }
}
