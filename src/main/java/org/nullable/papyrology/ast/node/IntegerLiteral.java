package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import java.util.Locale;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.nullable.papyrology.grammar.PapyrusParser;

/** A {@link Literal} integer value (e.g. {@code 42}). */
@AutoValue
public abstract class IntegerLiteral implements Literal {

  /** Returns the actual value of this literal. */
  public abstract int getValue();

  /** Returns the raw value of the literal (as it appears in source). */
  public abstract String getRawValue();

  /** Returns a new {@code IntegerLiteral} based on the given {@link TerminalNode}. */
  public static IntegerLiteral create(TerminalNode node) {
    Token token = node.getSymbol();
    checkArgument(
        token.getType() == PapyrusParser.L_UINT || token.getType() == PapyrusParser.L_INT,
        " IntegerLiteral::create passed an unsupported TerminalNode: %s",
        node);
    return builder().setValue(parseInteger(token.getText())).setRawValue(token.getText()).build();
  }

  private static int parseInteger(String raw) {
    String lower = raw.toLowerCase(Locale.US);
    return lower.contains("0x")
        ? Integer.parseInt(lower.replace("0x", ""), 16)
        : Integer.parseInt(lower);
  }

  /** Returns a fresh {@code IntegerLiteral} builder. */
  static Builder builder() {
    return new AutoValue_IntegerLiteral.Builder();
  }

  /** A builder of {@code IntegerLiterals}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setValue(int value);

    abstract Builder setRawValue(String rawValue);

    abstract IntegerLiteral build();
  }
}
