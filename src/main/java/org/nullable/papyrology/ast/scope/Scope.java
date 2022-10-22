package org.nullable.papyrology.ast.scope;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.SyntaxException;
import org.nullable.papyrology.source.SourceReference;

/** A scope of {@code Symbols}.s */
public class Scope implements Resolver {
  /** The type associated with a {@code Scope}. */
  public enum Type {
    SCRIPT(false, true),
    STATE(true, true),
    FUNCTION(true, true),
    EVENT(true, true),
    CONDITIONAL_BLOCK(true, false);

    private boolean hasParent;
    private boolean hasSymbol;

    Type(boolean hasParent, boolean hasSymbol) {
      this.hasParent = hasParent;
      this.hasSymbol = hasSymbol;
    }

    /**
     * Whether or not this {@code Type} of {@code Scope} will always have a parent {@code Scope}.
     */
    public boolean hasParent() {
      return hasParent;
    }

    /**
     * Whether or not this {@code Type} of {@code Scope} will always have a {@link Symbol} that
     * identifies it.
     */
    public boolean hasSymbol() {
      return hasSymbol;
    }
  }

  private final Type type;
  private final Resolver parent;
  private final Symbol symbol;
  private final ImmutableMap<String, Symbol> symbols;

  private Scope(Builder builder) {
    this.type = builder.type;
    this.parent = builder.parent;
    this.symbol = builder.symbol;
    this.symbols = ImmutableMap.copyOf(builder.symbols);
  }

  /**
   * Returns a new {@link Builder} for a {@code Scope} with the given {@link Type} which is
   * contained in the {@code parent} {@code Resolver} and identified by the given {@link Symbol}.
   */
  public static Builder builder(Type type, Resolver parent, Symbol symbol) {
    checkArgument(
        type.hasParent(), "Scope::builder called with Type %s which cannot have a parent.", type);
    checkArgument(
        type.hasSymbol(), "Scope::builder called with Type %s which cannot have a Symbol", type);
    return new Builder(type, parent, symbol);
  }

  /**
   * Returns a new {@link Builder} for a {@code Scope} with the given {@link Type} which is
   * contained in the {@code parent} {@code Resolver}.
   */
  public static Builder builder(Type type, Resolver parent) {
    checkArgument(
        type.hasParent(), "Scope::builder called with Type %s which cannot have a parent.", type);
    checkArgument(
        !type.hasSymbol(), "Scope::builder called with Type %s which must have a Symbol", type);
    return new Builder(type, parent, null);
  }

  /**
   * Returns a new {@link Builder} for a {@code Scope} with the given {@link Type} which is
   * identified by the given {@link Symbol}.
   */
  public static Builder builder(Type type, Symbol symbol) {
    checkArgument(
        !type.hasParent(), "Scope::builder called with Type %s which must have a parent.", type);
    checkArgument(
        type.hasSymbol(), "Scope::builder called with Type %s which cannot have a Symbol", type);
    return new Builder(type, null, symbol);
  }

  /** Returns the {@link Type} of this {@code Scope}. */
  public Type type() {
    return type;
  }

  /** Returns the {@link Symbol} that identifies this {@code Scope} (if present). */
  public Optional<Symbol> symbol() {
    return Optional.ofNullable(symbol);
  }

  /**
   * Returns the set of {@link Symbol Symbols} that this {@code Scope} exports to the global scope.
   *
   * <p>This method will return an empty set if this {@code Scope} is not a {@link Type#SCRIPT}.
   */
  public ImmutableSet<Symbol> globals() {
    if (!type.equals(Type.SCRIPT)) {
      return ImmutableSet.of();
    }
    return symbols.values().stream()
        .filter(s -> s.isGlobal())
        .collect(ImmutableSet.toImmutableSet());
  }

  @Override
  public Symbol resolve(Identifier identifier) {
    String key = toKey(identifier);
    if (!symbols.containsKey(key)) {
      if (parent == null) {
        throw new SyntaxException(
            identifier.sourceReference(), "Unable to resolve %s", identifier.value());
      }
      return parent.resolve(identifier);
    }
    return symbols.get(key);
  }

  /** A builder of {@code Scope} instances. */
  public static class Builder {
    private final Type type;
    private final Resolver parent;
    private final Symbol symbol;
    private final Map<String, Symbol> symbols;

    private Builder(Type type, Resolver parent, Symbol symbol) {
      this.type = type;
      this.parent = parent;
      this.symbol = symbol;
      this.symbols = new LinkedHashMap<>();
    }

    /**
     * Inserts the given {@link Identifier} into this table with the given {@link Symbol.Type}.
     *
     * @throws SyntaxException if the {@code identifier} is already defined in this table.
     */
    public Builder put(Identifier identifier, Symbol.Type type, boolean isGlobal) {
      checkArgument(
          (isGlobal && this.type.equals(Type.SCRIPT)) || !isGlobal,
          "Symbols within a scope of type %s cannot be global.",
          this.type);
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
      symbols.put(key, new Symbol(type, identifier, isGlobal));
      return this;
    }

    public Scope build() {
      return new Scope(this);
    }
  }

  private static final String toKey(Identifier identifier) {
    return identifier.value().toUpperCase(Locale.US);
  }
}
