package org.nullable.papyrology.ast.node;

import org.nullable.papyrology.grammar.PapyrusParser.ExpressionContext;

/** Denotes a {@link Statement} that evaluates to some value. */
public interface Expression extends Statement {

  static Expression create(ExpressionContext ctx) {
    throw new UnsupportedOperationException();
  }
}
