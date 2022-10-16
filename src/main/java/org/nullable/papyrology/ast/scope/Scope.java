package org.nullable.papyrology.ast.scope;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.SyntaxException;
import org.nullable.papyrology.source.SourceReference;

/** A scope of {@code Symbols}.s */
@Immutable
public class Scope {
  /** The type associated with a {@code Scope}. */
  public enum Type {
    GLOBAL(false, false),
    SCRIPT(true, true),
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

    public boolean hasParent() {
      return hasParent;
    }

    public boolean hasSymbol() {
      return hasSymbol;
    }
  }

  private final Type type;
  private final Scope parent;
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
   * contained in the {@code Parent} {@code Scope} and identified by the given {@link Symbol}.
   */
  public static Builder builder(Type type, Scope parent, Symbol symbol) {
    checkArgument(
        type.hasParent(),
        "Scope::builder called with Type %s which cannot have a parent Scope",
        type);
    checkArgument(
        type.hasSymbol(), "Scope::builder called with Type %s which cannot have a Symbol", type);
    return new Builder(type, parent, symbol);
  }

  /**
   * Returns a new {@link Builder} for a {@code Scope} with the given {@link Type} which is
   * contained in the {@code Parent} {@code Scope}.
   */
  public static Builder builder(Type type, Scope parent) {
    checkArgument(
        type.hasParent(),
        "Scope::builder called with Type %s which cannot have a parent Scope",
        type);
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
        !type.hasParent(),
        "Scope::builder called with Type %s which must have a parent Scope",
        type);
    checkArgument(
        type.hasSymbol(), "Scope::builder called with Type %s which cannot have a Symbol", type);
    return new Builder(type, null, symbol);
  }

  /** Returns a new {@link Builder} for a {@code Scope} with the given {@link Type}. */
  public static Builder builder(Type type) {
    checkArgument(
        !type.hasParent(),
        "Scope::builder called with Type %s which must have a parent Scope",
        type);
    checkArgument(
        !type.hasSymbol(), "Scope::builder called with Type %s which must have a Symbol", type);
    return new Builder(type, null, null);
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
   * Resolves the given {@link Identifier} to a {@link Symbol}.
   *
   * @throws SyntaxException if no {@code Symbol} could be found.
   */
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
    private final Scope parent;
    private final Symbol symbol;
    private final Map<String, Symbol> symbols;

    private Builder(Type type, Scope parent, Symbol symbol) {
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
    public Builder put(Identifier identifier, Symbol.Type type) {
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
      symbols.put(key, new Symbol(type, identifier));
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
