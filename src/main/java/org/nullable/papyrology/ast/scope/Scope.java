package org.nullable.papyrology.ast.scope;

import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.SyntaxException;

@Immutable
public class Scope {
  /** The type associated with a {@code Scope}. */
  public enum Type {
    SCRIPT,
    STATE,
    FUNCTION,
    EVENT,
    CONDITIONAL_BLOCK;
  }

  private final Type type;
  private final Scope parent;
  private final Symbol symbol;
  private final SymbolTable symbolTable;

  private Scope(Type type, Scope parent, Symbol symbol, SymbolTable symbolTable) {
    this.type = type;
    this.parent = parent;
    this.symbol = symbol;
    this.symbolTable = symbolTable;
  }

  public Type type() {
    return type;
  }

  public Optional<Symbol> symbol() {
    return Optional.ofNullable(symbol);
  }

  public Symbol resolve(Identifier identifier) {
    return symbolTable
        .get(identifier)
        .orElseGet(
            () -> {
              if (parent == null) {
                throw new SyntaxException(
                    identifier.sourceReference(), "Unable to resolve %s", identifier.value());
              }
              return parent.resolve(identifier);
            });
  }
}
