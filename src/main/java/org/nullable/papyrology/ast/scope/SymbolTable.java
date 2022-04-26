package org.nullable.papyrology.ast.scope;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.SyntaxException;
import org.nullable.papyrology.source.SourceReference;

@Immutable
public final class SymbolTable {

  private final ImmutableMap<String, Symbol> symbols;

  private SymbolTable(Builder builder) {
    this.symbols = ImmutableMap.copyOf(builder.symbols);
  }

  public Optional<Symbol> get(Identifier identifier) {
    return Optional.ofNullable(symbols.get(toKey(identifier)));
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final Map<String, Symbol> symbols;

    private Builder() {
      this.symbols = new HashMap<>();
    }

    /**
     * Inserts the given non-global {@link Identifier} into this table with the given {@link
     * Symbol.Type}.
     *
     * @throws SyntaxException if the {@code identifier} is already defined in this table.
     */
    public Builder put(Identifier identifier, Symbol.Type type) {
      return put(identifier, type, /* isGlobal= */ false);
    }

    /**
     * Inserts the given {@link Identifier} into this table with the given {@link Symbol.Type}.
     *
     * @throws SyntaxException if the {@code identifier} is already defined in this table.
     */
    public Builder put(Identifier identifier, Symbol.Type type, boolean isGlobal) {
      String key = toKey(identifier);
      Symbol existing = symbols.get(key);
      if (existing != null) {
        SourceReference existingRef = existing.identifier().sourceReference();
        throw new SyntaxException(
            identifier.sourceReference(),
            "Identifier \"%s\" is already defined on line %d at column %d",
            identifier.value(),
            existingRef.getLine(),
            existingRef.getColumn());
      }
      symbols.put(key, new Symbol(type, isGlobal, identifier));
      return this;
    }

    public SymbolTable build() {
      return new SymbolTable(this);
    }
  }

  private static final String toKey(Identifier identifier) {
    return identifier.value().toUpperCase(Locale.US);
  }
}
