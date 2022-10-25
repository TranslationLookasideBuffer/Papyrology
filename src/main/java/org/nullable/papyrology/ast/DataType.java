package org.nullable.papyrology.ast;

/** The five basic types non-array types in Papyrus. */
public enum DataType {
  /** Special {@code DataType} used by {@link Function Functions} that don't have a return type. */
  VOID(false),
  BOOL(false),
  INT(false),
  FLOAT(false),
  STRING(false),
  OBJECT(false),
  BOOL_ARRAY(true),
  INT_ARRAY(true),
  FLOAT_ARRAY(true),
  STRING_ARRAY(true),
  OBJECT_ARRAY(true);

  private final boolean isArray;

  private DataType(boolean isArray) {
    this.isArray = isArray;
  }

  public boolean isArray() {
    return isArray;
  }
}
