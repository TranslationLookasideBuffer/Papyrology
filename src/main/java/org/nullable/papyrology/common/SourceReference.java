package org.nullable.papyrology.common;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/** A reference to a particular place in source code. */
public final class SourceReference {

  private final TerminalNode node;
  private final ParserRuleContext ctx;

  private SourceReference(TerminalNode node, ParserRuleContext ctx) {
    this.node = node;
    this.ctx = ctx;
  }

  /** Returns a new {@code SourceReference} based on the given {@link TerminalNode}. */
  public static final SourceReference create(TerminalNode node) {
    return new SourceReference(node, /* ctx= */ null);
  }

  /** Returns a new {@code SourceReference} based on the given {@link ParserRuleContext}. */
  public static final SourceReference create(ParserRuleContext ctx) {
    return new SourceReference(/* node= */ null, ctx);
  }

  public String getText() {
    return node != null ? node.getSymbol().getText() : ctx.getText();
  }

  public int getLine() {
    return node != null ? node.getSymbol().getLine() : ctx.getStart().getLine();
  }

  public int getColumn() {
    return node != null
        ? node.getSymbol().getCharPositionInLine()
        : ctx.getStart().getCharPositionInLine();
  }
}
