package org.nullable.papyrology.ast.symbol;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.SyntaxException;

final class GlobalScope implements Resolver {
  private final Map<String, Symbol> symbols;
  private final SetMultimap<String, String> symbolIdsByScriptId;

  private GlobalScope() {
    this.symbols = new HashMap<>();
    this.symbolIdsByScriptId = HashMultimap.create();
  }

  /** Returns an empty {@code GlobalScope}. */
  static GlobalScope create() {
    return new GlobalScope();
  }

  @Override
  public Symbol resolve(Identifier identifier) {
    String key = toKey(identifier);
    if (!symbols.containsKey(key)) {
      throw new SyntaxException(
          identifier.sourceReference(), "Unable to resolve %s", identifier.value());
    }
    return symbols.get(key);
  }

  /**
   * Updates or inserts a {@link Scope} of type {@link Scope.Type#SCRIPT} into the global scope.
   *
   * <p>The {@code Script} {@link Symbol} itself and all of the {@code Symbols} returned by {@link
   * Scope#globals()} are added to the set of resolvable symbols.
   */
  void upsert(Scope scope) {
    checkArgument(
        scope.type().equals(Scope.Type.SCRIPT),
        "Scopes of type %s cannot be added to the global scope.",
        scope.type());
    String scriptId = toKey(scope.symbol().identifier());
    if (symbolIdsByScriptId.containsKey(scriptId)) {
      // Clean out old global symbols.
      symbolIdsByScriptId.get(scriptId).forEach(symbols::remove);
      symbolIdsByScriptId.removeAll(scriptId);
      symbols.remove(scriptId);
    }
    scope
        .globals()
        .forEach(
            symbol -> {
              String symbolId = toKey(symbol.identifier());
              symbols.put(symbolId, symbol);
              symbolIdsByScriptId.put(scriptId, symbolId);
            });
    symbols.put(scriptId, scope.symbol());
  }

  private static final String toKey(Identifier identifier) {
    return identifier.value().toUpperCase(Locale.US);
  }
}
