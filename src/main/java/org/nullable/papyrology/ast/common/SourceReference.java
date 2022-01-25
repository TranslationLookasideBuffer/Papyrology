package org.nullable.papyrology.ast.common;

import com.google.auto.value.AutoValue;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

@AutoValue
public abstract class SourceReference {

  public abstract String getText();

  public abstract int getLine();

  public abstract int getColumn();

  public static final SourceReference create(Token token) {
    return new AutoValue_SourceReference(
        token.getText(), token.getLine(), token.getCharPositionInLine());
  }

  public static final SourceReference create(TerminalNode node) {
    return create(node.getSymbol());
  }

  public static final SourceReference create(ParserRuleContext ctx) {
    return new AutoValue_SourceReference(
        ctx.getText(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
  }
}
