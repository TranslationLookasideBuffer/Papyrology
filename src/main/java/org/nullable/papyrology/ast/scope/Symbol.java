package org.nullable.papyrology.ast.scope;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.ast.Identifier;

/** A symbol that can be referenced. */
@Immutable
public record Symbol(Type type, Identifier identifier, boolean isGlobal) {
  /** The type associated with a {@code Symbol}. */
  public enum Type {
    SCRIPT,
    STATE,
    FUNCTION,
    EVENT,
    BOOL,
    INT,
    FLOAT,
    STRING,
    OBJECT,
    BOOL_ARRAY,
    INT_ARRAY,
    FLOAT_ARRAY,
    STRING_ARRAY,
    OBJECT_ARRAY;
  }

  /** Returns a new {@code Symbol}. */
  public static Symbol create(Type type, Identifier identifier, boolean isGlobal) {
    checkArgument(!type.equals(Type.SCRIPT) || !isGlobal, "Symbols of Type SCRIPT must be global.");
    return new Symbol(type, identifier, isGlobal);
  }

  /** Returns a new local (i.e. non-global) {@code Symbol}. */
  public static Symbol createLocal(Type type, Identifier identifier) {
    return create(type, identifier, false);
  }

  /** Returns a new global {@code Symbol}. */
  public static Symbol createGlobal(Type type, Identifier identifier) {
    return create(type, identifier, true);
  }
}
