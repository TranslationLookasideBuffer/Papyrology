package org.nullable.papyrology.ast.symbol;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import org.nullable.papyrology.ast.Construct;
import org.nullable.papyrology.ast.Event;
import org.nullable.papyrology.ast.Function;
import org.nullable.papyrology.ast.If;
import org.nullable.papyrology.ast.Script;
import org.nullable.papyrology.ast.State;
import org.nullable.papyrology.ast.WalkingVisitor;

/** */
final class ScriptWalker extends WalkingVisitor.Walker {
  private final GlobalScope global;
  private final Map<Construct, Scope> scopesByConstruct;
  private final Deque<Scope> scopes;
  private boolean complete;
  private boolean isIf;

  private ScriptWalker(GlobalScope global) {
    this.global = global;
    this.scopesByConstruct = new HashMap<>();
    this.scopes = new ArrayDeque<>();
    this.complete = false;
    this.isIf = false;
  }

  /**
   * Returns a new {@code ScriptWalker} that exports data to and references a {@link GlobalScope}.
   */
  static ScriptWalker create(GlobalScope global) {
    return new ScriptWalker(global);
  }

  @Override
  protected void enter(Script script) {
    checkState(!complete, "A ScriptWalker can only be used once.");
    Scope scope =
        Scope.create(
            global, Symbol.createGlobal(Symbol.Type.SCRIPT, script.header().scriptIdentifier()));
    scopes.push(scope);
  }

  @Override
  protected void exit(Script script) {
    Scope scope = scopes.pop();
    global.upsert(scope);
    scopesByConstruct.put(script, scope);
    this.complete = true;
  }

  @Override
  protected void enter(State state) {
    Symbol symbol = Symbol.createLocal(Symbol.Type.STATE, state.identifier());
    scopes.peek().put(symbol);
    Scope scope = Scope.create(scopes.peek(), symbol);
    scopes.push(scope);
  }

  @Override
  protected void exit(State state) {
    scopesByConstruct.put(state, scopes.pop());
  }

  @Override
  protected void enter(Event event) {
    Symbol symbol = Symbol.createLocal(Symbol.Type.EVENT, event.identifier());
    scopes.peek().put(symbol);
    Scope scope = Scope.create(scopes.peek(), symbol);
    scopes.push(scope);
  }

  @Override
  protected void exit(Event event) {
    Scope scope = scopes.pop();
    scopesByConstruct.put(event, scope);
    event.body().ifPresent(body -> scopesByConstruct.put(body, scope));
  }

  @Override
  protected void enter(Function function) {
    Symbol symbol = Symbol.create(Symbol.Type.FUNCTION, function.identifier(), function.isGlobal());
    scopes.peek().put(symbol);
    Scope scope = Scope.create(scopes.peek(), symbol);
    scopes.push(scope);
  }

  @Override
  protected void exit(Function function) {
    Scope scope = scopes.pop();
    scopesByConstruct.put(function, scope);
    function.body().ifPresent(body -> scopesByConstruct.put(body, scope));
  }

  @Override
  protected void enter(If ifStatement) {
    isIf = true;
  }

  @Override
  protected void exit(If ifStatement) {
    isIf = false;
  }

  public Map<Construct, Scope> scopes() {
    checkState(complete, "ScriptWalker::scope called before walk.");
    return scopesByConstruct;
  }
}
