package org.nullable.papyrology.ast.symbol;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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

  private final GlobalScope global;
  private final Map<Construct, Scope> scopesByConstruct;
  private final SetMultimap<String, Construct> constructsByScriptId;

  private SymbolTable() {
    this.global = GlobalScope.create();
    this.scopesByConstruct = new HashMap<>();
    this.constructsByScriptId = HashMultimap.create();
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
        "resolver called with an unknown construct: %s",
        construct);
    return scopesByConstruct.get(construct);
  }

  /**
   * Returns a {@link Resolver} that can be used to resolve {@link Symbol Symbols} at the global
   * level.
   *
   * <p>Callers must take care to call this only when needing to resolve an {@link Identifier} at
   * the top-level within a {@code Script}.
   */
  public Resolver globalResolver() {
    return global;
  }

  /**
   * Updates or inserts a {@link Script} into the {@code SymbolTable} by walking the {@code Script}
   * and collecting {@link Symbol Symbols}.
   *
   * <p>If the {@code Script} has a name that matches the name of a {@code Script} already known to
   * the {@code SymbolTable}, the {@code Symbol} information of the existing {@code Script} is
   * removed and replaced by the new {@code Script}.
   *
   * @throws SyntaxException if upserting fails, e.g. due to a global symbol clash.
   */
  public void upsert(Script script) {
    String scriptId = toKey(script.header().scriptIdentifier());
    if (constructsByScriptId.containsKey(scriptId)) {
      constructsByScriptId.get(scriptId).forEach(scopesByConstruct::remove);
    }
    ScriptWalker walker = ScriptWalker.create(global);
    WalkingVisitor.create(walker).visit(script);
    constructsByScriptId.putAll(scriptId, walker.scopes().keySet());
    scopesByConstruct.putAll(walker.scopes());
  }

  /** Returns an empty {@code SymbolTable}. */
  public static SymbolTable create() {
    return new SymbolTable();
  }

  private static final String toKey(Identifier identifier) {
    return identifier.value().toUpperCase(Locale.US);
  }
}
