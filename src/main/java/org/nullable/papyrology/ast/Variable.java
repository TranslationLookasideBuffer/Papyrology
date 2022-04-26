package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.LocalVariableContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** A {@link Statement} that defines a local variable (within an {@code Invokable}). */
@Immutable
public record Variable(
    SourceReference sourceReference,
    Type type,
    Identifier identifier,
    Optional<Expression> valueExpression)
    implements Statement {

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code Variable} based on the given {@link LocalVariableContext}. */
  static Variable create(LocalVariableContext ctx) {
    return new Variable(
        SourceReference.create(ctx),
        Type.create(ctx.type()),
        Identifier.create(ctx.ID()),
        Optionals.of(ctx.expression() != null, () -> Expression.create(ctx.expression())));
  }
}
