package org.nullable.papyrology.ast.scope;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.nullable.papyrology.ast.Block;
import org.nullable.papyrology.ast.Construct;
import org.nullable.papyrology.ast.Identifier;
import org.nullable.papyrology.ast.Script;
import org.nullable.papyrology.ast.SyntaxException;
import org.nullable.papyrology.ast.WalkingVisitor;

public final class SymbolTable {

  private final GlobalScope global;
  private final Map<Construct, Scope> scopesByConstruct;
  private final SetMultimap<String, Construct> constructsByScriptId;

  private SymbolTable() {
    this.global = new GlobalScope();
    this.scopesByConstruct = new HashMap<>();
    this.constructsByScriptId = HashMultimap.create();
  }

  /**
   * Returns a {@link Resolver} that can be used to resolve {@link Symbol Symbols} in the given
   * {@link Block}.
   *
   * <p>Callers must take care to call this with the closest enclosing {@code Block} that contains
   * the {@link Identifier} needing to be resolved.
   */
  public Resolver resolver(Block block) {
    checkArgument(
        scopesByConstruct.containsKey(block),
        "getResolver called with an unknown block: %s",
        block);
    return scopesByConstruct.get(block);
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
    ScriptWalker walker = new ScriptWalker(global);
    WalkingVisitor.create(walker).visit(script);
    constructsByScriptId.putAll(scriptId, walker.scopes().keySet());
    scopesByConstruct.putAll(walker.scopes());
  }

  /** Returns an empty {@code SymbolTable}. */
  public static SymbolTable create() {
    return new SymbolTable();
  }

  private static final class GlobalScope implements Resolver {
    private final Map<String, Symbol> symbols;
    private final SetMultimap<String, String> symbolIdsByScriptId;

    private GlobalScope() {
      this.symbols = new HashMap<>();
      this.symbolIdsByScriptId = HashMultimap.create();
    }

    /**
     * Updates or inserts a {@link Scope} of type {@link Scope.Type#SCRIPT} into the global scope.
     *
     * <p>The {@code Script} {@link Symbol} itself and all of the {@code Symbols} returned by {@link
     * Scope#globals()} are added to the set of resolvable symbols.
     */
    public void upsert(Scope scope) {
      checkArgument(
          scope.type().equals(Scope.Type.SCRIPT),
          "Scopes of type %s cannot be added to the global scope.",
          scope.type());
      String scriptId = toKey(scope.symbol().get().identifier());
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
      symbols.put(scriptId, scope.symbol().get());
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
  }

  private static final class ScriptWalker extends WalkingVisitor.Walker {
    private final GlobalScope global;
    private final Map<Construct, Scope> scopesByConstruct;
    private Scope.Builder current;
    private boolean complete;

    private ScriptWalker(GlobalScope global) {
      this.global = global;
      this.scopesByConstruct = new HashMap<>();
      this.current = null;
      this.complete = false;
    }

    @Override
    protected void enter(Script script) {
      checkState(!complete, "A ScriptWalker can only be used once.");
      current =
          Scope.builder(
              Scope.Type.SCRIPT,
              global,
              Symbol.createGlobal(Symbol.Type.SCRIPT, script.header().scriptIdentifier()));
    }

    @Override
    protected void exit(Script script) {
      Scope scope = current.build();
      global.upsert(scope);
      scopesByConstruct.put(script, scope);
      this.complete = true;
    }

    public Map<Construct, Scope> scopes() {
      checkState(complete, "ScriptWalker::scope called before walk.");
      return scopesByConstruct;
    }
  }

  private static final String toKey(Identifier identifier) {
    return identifier.value().toUpperCase(Locale.US);
  }
}
