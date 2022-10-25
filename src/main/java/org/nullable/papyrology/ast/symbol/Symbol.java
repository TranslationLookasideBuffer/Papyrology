package org.nullable.papyrology.ast.symbol;

import static com.google.common.base.Preconditions.checkState;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.ast.DataType;
import org.nullable.papyrology.ast.Identifier;

/** A symbol that can be referenced. */
@Immutable
public record Symbol(Type type, Identifier identifier, DataType dataType) {
  /** The type associated with a {@code Symbol}. */
  public enum Type {
    SCRIPT(false),
    STATE(false),
    EVENT(false),
    FUNCTION(true),
    GLOBAL_FUNCTION(true),
    READ_ONLY_PROPERTY(true),
    WRITE_ONLY_PROPERTY(true),
    READ_WRITE_PROPERTY(true),
    VARIABLE(true);

    private final boolean hasDataType;

    private Type(boolean hasDataType) {
      this.hasDataType = hasDataType;
    }
  }

  @Override
  public DataType dataType() {
    checkState(type.hasDataType, "This Symbol of Type \"%s\" does not have a DataType.", type);
    return dataType;
  }

  /** Returns a new {@link Type#SCRIPT} {@code Symbol}. */
  public static Symbol script(Identifier identifier) {
    return new Symbol(Type.SCRIPT, identifier, null);
  }

  /** Returns a new {@link Type#STATE} {@code Symbol}. */
  public static Symbol state(Identifier identifier) {
    return new Symbol(Type.STATE, identifier, null);
  }

  /** Returns a new {@link Type#EVENT} {@code Symbol}. */
  public static Symbol event(Identifier identifier) {
    return new Symbol(Type.EVENT, identifier, null);
  }

  /** Returns a new {@link Type#FUNCTION} {@code Symbol}. */
  public static Symbol function(Identifier identifier, DataType dataType) {
    return new Symbol(Type.FUNCTION, identifier, dataType);
  }

  /** Returns a new {@link Type#GLOBAL_FUNCTION} {@code Symbol}. */
  public static Symbol globalFunction(Identifier identifier, DataType dataType) {
    return new Symbol(Type.GLOBAL_FUNCTION, identifier, dataType);
  }

  /** Returns a new {@link Type#READ_ONLY_PROPERTY} {@code Symbol}. */
  public static Symbol readOnlyProperty(Identifier identifier, DataType dataType) {
    return new Symbol(Type.READ_ONLY_PROPERTY, identifier, dataType);
  }

  /** Returns a new {@link Type#WRITE_ONLY_PROPERTY} {@code Symbol}. */
  public static Symbol writeOnlyProperty(Identifier identifier, DataType dataType) {
    return new Symbol(Type.WRITE_ONLY_PROPERTY, identifier, dataType);
  }

  /** Returns a new {@link Type#READ_WRITE_PROPERTY} {@code Symbol}. */
  public static Symbol readWriteProperty(Identifier identifier, DataType dataType) {
    return new Symbol(Type.READ_WRITE_PROPERTY, identifier, dataType);
  }

  /** Returns a new {@link Type#VARIABLE} {@code Symbol}. */
  public static Symbol variable(Identifier identifier, DataType dataType) {
    return new Symbol(Type.VARIABLE, identifier, dataType);
  }
}
