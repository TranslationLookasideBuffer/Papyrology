package org.nullable.papyrology.ast.node;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import org.nullable.papyrology.grammar.PapyrusParser.StatementBlockContext;
import org.nullable.papyrology.grammar.PapyrusParser.StatementContext;

/** Denotes a single portion of executable code. */
public interface Statement extends Construct {

  static ImmutableList<Statement> create(StatementBlockContext ctx) {
    if (ctx.statement() == null) {
      return ImmutableList.of();
    }
    return ctx.statement().stream().map(Statement::create).collect(toImmutableList());
  }

  static Statement create(StatementContext ctx) {
    return null;
  }
}
