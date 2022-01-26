package org.nullable.papyrology.ast.node;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import org.nullable.papyrology.grammar.PapyrusParser.AssignmentContext;
import org.nullable.papyrology.grammar.PapyrusParser.IfContext;
import org.nullable.papyrology.grammar.PapyrusParser.LocalVariableContext;
import org.nullable.papyrology.grammar.PapyrusParser.ReturnContext;
import org.nullable.papyrology.grammar.PapyrusParser.StandaloneExpressionContext;
import org.nullable.papyrology.grammar.PapyrusParser.StatementBlockContext;
import org.nullable.papyrology.grammar.PapyrusParser.StatementContext;
import org.nullable.papyrology.grammar.PapyrusParser.WhileContext;

/** Denotes a single portion of executable code. */
public interface Statement extends Construct {

  /** Returns a list of {@code Statements} based on the given {@link StatementBlockContext}. */
  static ImmutableList<Statement> create(StatementBlockContext ctx) {
    if (ctx.statement() == null) {
      return ImmutableList.of();
    }
    return ctx.statement().stream().map(Statement::create).collect(toImmutableList());
  }

  /** Returns a new {@code Statement} based on the given {@link StatementContext}. */
  static Statement create(StatementContext ctx) {
    if (ctx instanceof LocalVariableContext) {
      return Variable.create((LocalVariableContext) ctx);
    }
    if (ctx instanceof AssignmentContext) {
      return Assignment.create((AssignmentContext) ctx);
    }
    if (ctx instanceof ReturnContext) {
      return Return.create((ReturnContext) ctx);
    }
    if (ctx instanceof IfContext) {
      return If.create((IfContext) ctx);
    }
    if (ctx instanceof WhileContext) {
      return While.create((WhileContext) ctx);
    }
    if (ctx instanceof StandaloneExpressionContext) {
      return Expression.create(((StandaloneExpressionContext) ctx).expression());
    }
    throw new IllegalArgumentException(
        String.format("Statement::create passed malformed StatementContext: %s", ctx));
  }
}
