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

/**
 * A {@link WalkingVisitor.Walker} that builds a {@link Scope} by walking a {@link Script}.
 *
 * <p>NOTE: This class is <i>not</i> thread-safe in any state.
 */
final class ScriptWalker extends WalkingVisitor.Walker {
  private final Resolver global;
  private final Map<Construct, Scope> scopesByConstruct;
  private final Deque<Scope> scopes;
  private Scope root;
  private boolean complete;
  private boolean isIf;

  private ScriptWalker(Resolver global) {
    this.global = global;
    this.root = null;
    this.scopesByConstruct = new HashMap<>();
    this.scopes = new ArrayDeque<>();
    this.isIf = false;
  }

  /** Returns a new {@code ScriptWalker} ready to walk a {@link Script}. */
  static ScriptWalker create(Resolver global) {
    return new ScriptWalker(global);
  }

  @Override
  protected void enter(Script script) {
    checkState(root == null, "A ScriptWalker can only be used once.");
    Scope scope =
        Scope.create(
            global, Symbol.createGlobal(Symbol.Type.SCRIPT, script.header().scriptIdentifier()));
    scopes.push(scope);
  }

  @Override
  protected void exit(Script script) {
    root = scopes.pop();
    root.lock();
    scopesByConstruct.put(script, root);
  }

  @Override
  protected void enter(State state) {
    Symbol symbol = Symbol.createLocal(Symbol.Type.STATE, state.identifier());
    scopes.peek().insert(symbol);
    Scope scope = Scope.create(scopes.peek(), symbol);
    scopes.push(scope);
  }

  @Override
  protected void exit(State state) {
    Scope scope = scopes.pop();
    scope.lock();
    scopesByConstruct.put(state, scope);
  }

  @Override
  protected void enter(Event event) {
    Symbol symbol = Symbol.createLocal(Symbol.Type.EVENT, event.identifier());
    scopes.peek().insert(symbol);
    Scope scope = Scope.create(scopes.peek(), symbol);
    scopes.push(scope);
  }

  @Override
  protected void exit(Event event) {
    Scope scope = scopes.pop();
    scope.lock();
    scopesByConstruct.put(event, scope);
    event.body().ifPresent(body -> scopesByConstruct.put(body, scope));
  }

  @Override
  protected void enter(Function function) {
    Symbol symbol = Symbol.create(Symbol.Type.FUNCTION, function.identifier(), function.isGlobal());
    scopes.peek().insert(symbol);
    Scope scope = Scope.create(scopes.peek(), symbol);
    scopes.push(scope);
  }

  @Override
  protected void exit(Function function) {
    Scope scope = scopes.pop();
    scope.lock();
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
    checkState(root != null, "ScriptWalker::scope called before walk.");
    return scopesByConstruct;
  }

  public Scope root() {
    checkState(root != null, "ScriptWalker::scope called before walk.");
    return root;
  }
}
