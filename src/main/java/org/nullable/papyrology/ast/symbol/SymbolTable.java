package org.nullable.papyrology.ast.symbol;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.nullable.papyrology.ast.Construct;
import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.Script;
import org.nullable.papyrology.ast.SyntaxException;
import org.nullable.papyrology.ast.WalkingVisitor;

/**
 * A mutable symbol table.
 *
 * <p>This symbol table only supports the {@link #upsert(Script)} operation for mutation which
 * allows for reloading a single {@link Script} without recomputing the entire symbol table.
 */
public final class SymbolTable {

  private final Map<Construct, Scope> scopesByConstruct;
  private final Map<String, ScriptScope> scriptScopesByName;
  private final GlobalResolver globalResolver;

  private SymbolTable() {
    scopesByConstruct = new HashMap<>();
    scriptScopesByName = new HashMap<>();
    globalResolver = new GlobalResolver(scriptScopesByName);
  }

  /** Returns an empty {@code SymbolTable}. */
  public static SymbolTable create() {
    return new SymbolTable();
  }

  /**
   * Returns a {@link Resolver} that can be used to resolve {@link Symbol Symbols} in the given
   * {@link Construct}.
   *
   * <p>Callers must take care to call this with the closest enclosing {@code Construct} that
   * contains the {@link Identifier} needing to be resolved.
   */
  public Resolver resolver(Construct construct) {
    checkArgument(
        scopesByConstruct.containsKey(construct),
        "SymbolTable::resolver called with an unknown construct: %s",
        construct);
    return scopesByConstruct.get(construct);
  }

  /**
   * Returns a {@link Resolver} that can be used to resolve {@link Symbol Symbols} in the {@link
   * Script} identified
   *
   * <p>Callers must take care to call this with the closest enclosing {@code Construct} that
   * contains the {@link Identifier} needing to be resolved.
   */
  public Resolver resolver(Identifier identifier) {
    String name = normalize(identifier);
    checkArgument(
        scriptScopesByName.containsKey(name),
        "SymbolTable::resolver called with an unknown Script identifier: %s",
        identifier);
    return scriptScopesByName.get(name).scope();
  }

  /**
   * Updates or inserts a {@link Script} into the {@code SymbolTable} by walking the {@code Script}
   * and collecting {@link Symbol Symbols}.
   *
   * <p>If the {@code Script} has a name that matches the name of a {@code Script} already known to
   * the {@code SymbolTable}, the {@code Symbol} information of the existing {@code Script} is
   * removed and replaced by the new {@code Script}.
   */
  public void upsert(Script script) {
    String name = normalize(script.header().scriptIdentifier());
    remove(name);
    ScriptWalker walker = ScriptWalker.create(globalResolver);
    WalkingVisitor.create(walker).visit(script);
    ScriptScope scriptScope = ScriptScope.create(walker.root(), walker.scopes().keySet());
    scriptScopesByName.put(name, scriptScope);
    scopesByConstruct.putAll(walker.scopes());
  }

  /**
   * Removes all {@link Symbol Symbols} from the {@code SymbolTable} that are associated with a
   * {@link Script} with given {@code name}.
   *
   * @return true if {@code Symbols} were removed, false otherwise.
   */
  @CanIgnoreReturnValue
  public boolean remove(String name) {
    name = normalize(name);
    if (!scriptScopesByName.containsKey(name)) {
      return false;
    }
    ScriptScope scriptScope = scriptScopesByName.remove(name);
    scriptScope.constructs().forEach(scopesByConstruct::remove);
    return true;
  }

  /**
   * A simple wrapper around a {@link Scope} (typed {@link Scope.Type#SCRIPT}), and the set of
   * {@link Construct Constructs} it has symbol information for.
   */
  private record ScriptScope(Scope scope, ImmutableSet<Construct> constructs) {
    static ScriptScope create(Scope scope, Set<Construct> constructs) {
      return new ScriptScope(scope, ImmutableSet.copyOf(constructs));
    }
  }

  /** {@link Resolver} that handles resolving {@link Script} {@link Identifier Identifiers}. */
  private static class GlobalResolver implements Resolver {
    private final Map<String, ScriptScope> scriptScopesByName;

    private GlobalResolver(Map<String, ScriptScope> scriptScopesByName) {
      this.scriptScopesByName = scriptScopesByName;
    }

    @Override
    public Symbol resolve(Identifier identifier) {
      String id = normalize(identifier);
      if (!scriptScopesByName.containsKey(id)) {
        throw new SyntaxException(
            identifier.sourceReference(), "Unable to resolve %s", identifier.value());
      }
      return scriptScopesByName.get(id).scope().symbol();
    }
  }

  private static final String normalize(Identifier identifier) {
    return normalize(identifier.value());
  }

  private static final String normalize(String identifier) {
    return identifier.toUpperCase(Locale.US);
  }
}
