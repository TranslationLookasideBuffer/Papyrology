package org.nullable.papyrology.ast.symbol;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.SyntaxException;
import org.nullable.papyrology.source.SourceReference;

/**
 * A scope of {@code Symbols}.
 *
 * <p>NOTE: This class is <i>not</i> thread safe before {@link #lock()} has been called, thus a
 * {@code Scope} should only have {@link Symbol Symbols} inserted by one thread.
 */
final class Scope implements Resolver {
  /** The type associated with a {@code Scope}. */
  public enum Type {
    SCRIPT,
    STATE,
    FUNCTION,
    EVENT,
    ANONYMOUS_BLOCK;
  }

  private static final ImmutableMap<Symbol.Type, Type> TO_SCOPE_TYPE =
      ImmutableMap.<Symbol.Type, Type>builder()
          .put(Symbol.Type.SCRIPT, Type.SCRIPT)
          .put(Symbol.Type.STATE, Type.STATE)
          .put(Symbol.Type.FUNCTION, Type.FUNCTION)
          .put(Symbol.Type.GLOBAL_FUNCTION, Type.FUNCTION)
          .put(Symbol.Type.EVENT, Type.EVENT)
          .buildOrThrow();

  private final Type type;
  private final Resolver parent;
  private final Symbol symbol;
  private final Map<String, Symbol> symbols;
  private boolean lock;

  private Scope(Type type, Resolver parent, Symbol symbol) {
    this.type = type;
    this.parent = parent;
    this.symbol = symbol;
    this.symbols = new LinkedHashMap<>();
    this.lock = false;
  }

  /**
   * Returns an empty {@code Scope} identified by the given {@link Symbol} that is contained in a
   * {@code parent} {@code Resolver} and .
   */
  public static Scope create(Resolver parent, Symbol symbol) {
    checkArgument(
        TO_SCOPE_TYPE.containsKey(symbol.type()),
        "Scope::create passed a Symbol of an unsupported Type: %s",
        symbol);
    return new Scope(TO_SCOPE_TYPE.get(symbol.type()), parent, symbol);
  }

  /**
   * Returns an empty, anonymous {@code Scope} that is contained in a {@code parent} {@code
   * Resolver}.
   */
  public static Scope create(Resolver parent) {
    return new Scope(Type.ANONYMOUS_BLOCK, parent, null);
  }

  @Override
  public Symbol resolve(Identifier identifier) {
    checkState(lock, "Scope::resolve called before Script::lock.");
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

  /** Returns the {@link Type} of this {@code Scope}. */
  Type type() {
    return type;
  }

  /**
   * Returns whether or not this {@code Scope} is anonymous (i.e. it has a {@link Symbol} that
   * identifies it).
   */
  boolean isAnonymous() {
    return symbol == null;
  }

  /** Returns the {@link Symbol} that identifies this {@code Scope} (if present). */
  Symbol symbol() {
    checkState(!isAnonymous(), "Scope::symbol called on an anonymous Scope.");
    return symbol;
  }

  /**
   * Returns the set of {@link Symbol Symbols} that this {@code Scope} exports to the global scope.
   *
   * <p>This method will return an empty set if this {@code Scope} is not a {@link Type#SCRIPT}.
   */
  ImmutableSet<Symbol> globals() {
    checkState(lock, "Scope::globals called before Script::lock.");
    if (!type.equals(Type.SCRIPT)) {
      return ImmutableSet.of();
    }
    return symbols.values().stream()
        .filter(s -> s.type().equals(Symbol.Type.GLOBAL_FUNCTION))
        .collect(ImmutableSet.toImmutableSet());
  }

  /**
   * Locks this {@code Scope} so no more {@link Symbol Symbols} can be added and the {@Code Scope}
   * can be queried.
   */
  void lock() {
    lock = true;
  }

  /**
   * Inserts the given {@link Symbol} into this {@code Scope}.
   *
   * @throws SyntaxException if the {@code Symbol} is already defined in this {@code Scope}.
   */
  void insert(Symbol symbol) {
    checkState(!lock, "Scope::insert called after Script::lock.");
    String key = toKey(symbol.identifier());
    Symbol existing = symbols.get(key);
    if (existing != null) {
      SourceReference existingRef = existing.identifier().sourceReference();
      throw new SyntaxException(
          symbol.identifier().sourceReference(),
          "Identifier \"%s\" is already defined on line %d at column %d",
          symbol.identifier().value(),
          existingRef.getLine(),
          existingRef.getColumn());
    }
    symbols.put(key, symbol);
  }

  private static final String toKey(Identifier identifier) {
    return identifier.value().toUpperCase(Locale.US);
  }
}
