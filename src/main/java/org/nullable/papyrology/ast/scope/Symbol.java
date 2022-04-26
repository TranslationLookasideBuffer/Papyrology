package org.nullable.papyrology.ast.scope;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.ast.Identifier;

@Immutable
public record Symbol(Type type, boolean isGlobal, Identifier identifier) {
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
}
