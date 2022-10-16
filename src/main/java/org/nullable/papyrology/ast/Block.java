package org.nullable.papyrology.ast;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.StatementBlockContext;
import org.nullable.papyrology.source.SourceReference;

/**
 * A {@link Construct} that represents a block of {@link Statement Statements} that forms a scope.
 */
@Immutable
public record Block(SourceReference sourceReference, ImmutableList<Statement> bodyStatements)
    implements Construct {

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code Block} based on the given {@link StatementBlockContext}. */
  static Block create(StatementBlockContext ctx) {
    SourceReference sourceReference = SourceReference.create(ctx);
    ImmutableList<Statement> statements =
        ctx.statement() != null
            ? ctx.statement().stream().map(Statement::create).collect(toImmutableList())
            : ImmutableList.of();
    return new Block(sourceReference, statements);
  }
}
