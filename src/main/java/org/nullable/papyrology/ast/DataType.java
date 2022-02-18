package org.nullable.papyrology.ast;

/** The five basic types non-array types in Papyrus. */
public enum DataType {
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
