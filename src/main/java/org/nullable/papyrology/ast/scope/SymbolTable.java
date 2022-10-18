package org.nullable.papyrology.ast.scope;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.nullable.papyrology.ast.Construct;
import org.nullable.papyrology.ast.Script;

@Immutable
public final class SymbolTable {

  private final Scope global;
  private final ImmutableMap<Construct, Scope> scopesByConstruct;

  private SymbolTable(ScriptVisitor visitor) {
    this.global = visitor.global.build();
    this.scopesByConstruct = ImmutableMap.copyOf(visitor.scopesByConstruct);
  }

  public Scope getGlobalScope() {
    return global;
  }

  public Optional<Scope> getScope(Construct construct) {
    return Optional.ofNullable(scopesByConstruct.get(construct));
  }

  public static SymbolTable create(Collection<Script> scripts) {
    ScriptVisitor visitor = new ScriptVisitor();
    return null;
  }

  private static class ScriptVisitor {
    private final Scope.Builder global;
    private final Map<Construct, Scope> scopesByConstruct;
    private Scope.Builder current;

    private ScriptVisitor() {
      this.global = Scope.builder(Scope.Type.GLOBAL);
      this.scopesByConstruct = new HashMap<>();
      this.current = this.global;
    }

    public SymbolTable build() {
      return new SymbolTable(this);
    }
  }
}
