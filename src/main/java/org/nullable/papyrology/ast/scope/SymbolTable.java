package org.nullable.papyrology.ast.scope;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.nullable.papyrology.ast.Construct;

@Immutable
public final class SymbolTable {

  private final Scope global;
  private final ImmutableMap<Construct, Scope> scopesByConstruct;

  private SymbolTable(Builder builder) {
    this.global = builder.global.build();
    this.scopesByConstruct = ImmutableMap.copyOf(builder.scopesByConstruct);
  }

  public Scope getGlobalScope() {
    return global;
  }

  public Optional<Scope> getScope(Construct construct) {
    return Optional.ofNullable(scopesByConstruct.get(construct));
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final Scope.Builder global;
    private final Map<Construct, Scope> scopesByConstruct;

    private Builder() {
      this.global = Scope.builder(Scope.Type.GLOBAL);
      this.scopesByConstruct = new HashMap<>();
    }

    public SymbolTable build() {
      return new SymbolTable(this);
    }
  }
}
