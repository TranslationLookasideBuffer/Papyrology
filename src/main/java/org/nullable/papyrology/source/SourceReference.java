package org.nullable.papyrology.source;

import com.google.errorprone.annotations.Immutable;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/** A reference to a particular segment of source code. */
@Immutable
public final class SourceReference {

  private final String text;
  private final int line;
  private final int column;

  private SourceReference(String text, int line, int column) {
    this.text = text;
    this.line = line;
    this.column = column;
  }

  /** Returns a new {@code SourceReference} based on the given {@link TerminalNode}. */
  public static final SourceReference create(TerminalNode node) {
    return new SourceReference(
        node.getSymbol().getText(),
        node.getSymbol().getLine(),
        node.getSymbol().getCharPositionInLine());
  }

  /** Returns a new {@code SourceReference} based on the given {@link ParserRuleContext}. */
  public static final SourceReference create(ParserRuleContext ctx) {
    return new SourceReference(
        ctx.getText(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
  }

  /** Returns the text of this source segment. */
  public String getText() {
    return text;
  }

  /** Returns the line in the source file where this segment starts. */
  public int getLine() {
    return line;
  }

  /** Returns the column in the source file where this segment starts. */
  public int getColumn() {
    return column;
  }
}
