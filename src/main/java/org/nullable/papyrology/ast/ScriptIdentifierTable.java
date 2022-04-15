package org.nullable.papyrology.ast;

import com.google.common.collect.ImmutableList;

public final class ScriptIdentifierTable {

  public abstract static class Scope {

    /** Returns the list of {@link Identifier Identifiers} in the order encountered. */
    public ImmutableList<Identifier> getIdentifiers() {
      return null;
    }

    /** Returns the list of {@code Scopes} contained within this scope in the order encountered. */
    public ImmutableList<Scope> getInnerScopes() {
      return null;
    }
  }

  public static final class IdentifiedScope extends Scope {}
}
