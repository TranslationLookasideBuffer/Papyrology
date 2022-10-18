package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.errorprone.annotations.Immutable;
import java.math.BigInteger;
import java.util.Locale;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.source.SourceReference;

/**
 * A {@link Literal} integer value (e.g. {@code 42}).
 *
 * <p>Note: Papyrus allows defining integers that are far outside the valid range of a 32-bit
 * integer. If {@link #isOutOfRange()} returns {@code true}, {@link #value()} will return either
 * {@link Integer#MAX_VALUE} or {@link Integer#MIN_VALUE}.
 */
@Immutable
public record IntegerLiteral(SourceReference sourceReference, int value, boolean isOutOfRange)
    implements Literal {

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code IntegerLiteral} based on the given {@link TerminalNode}. */
  static IntegerLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.L_UINT || token.getType() == PapyrusParser.L_INT,
        " IntegerLiteral::create passed an unsupported TerminalNode: %s",
        node);
    ParsedValue parsed = parseInteger(token.getText());
    return new IntegerLiteral(SourceReference.create(node), parsed.value(), parsed.isOutOfRange());
  }

  /**
   * Parses a string in a way that can handle values far outside the bounds of a 32-bit integer.
   *
   * <p>This is required because the reference compiler supports such numbers.
   */
  private static ParsedValue parseInteger(String raw) {
    String lower = raw.toLowerCase(Locale.US);
    BigInteger value =
        lower.contains("0x") ? new BigInteger(lower.replace("0x", ""), 16) : new BigInteger(lower);
    if (value.compareTo(MAX_VALUE) > 0) {
      return new ParsedValue(Integer.MAX_VALUE, /* isOutOfRange= */ true);
    }
    if (value.compareTo(MIN_VALUE) < 0) {
      return new ParsedValue(Integer.MIN_VALUE, /* isOutOfRange= */ true);
    }
    return new ParsedValue(value.intValue(), /* isOutOfRange= */ false);
  }

  private static final BigInteger MAX_VALUE = BigInteger.valueOf(Integer.MAX_VALUE);
  private static final BigInteger MIN_VALUE = BigInteger.valueOf(Integer.MIN_VALUE);

  private static record ParsedValue(int value, boolean isOutOfRange) {}
}
