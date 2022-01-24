package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/** Defines a data type for a value. */
@AutoValue
public abstract class Type implements Construct {

  /** Returns the data type of this node. */
  public abstract DataType getDataType();

  /** Returns the {@code Identifier} if the {@code DataType} is {@link DataType#OBJECT}. */
  public abstract Optional<Identifier> getIdentifier();

  /** Returns a fresh {@code Type} builder. */
  public static Builder builder() {
    return new AutoValue_Type.Builder();
  }

  /** A builder of {@code Types}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setDataType(DataType dataType);

    public abstract Builder setIdentifier(Identifier id);

    abstract Type autoBuild();

    public final Type build() {
      Type type = autoBuild();
      if (type.getDataType().equals(DataType.OBJECT)) {
        checkState(
            type.getIdentifier().isPresent(),
            "Identifier must be specified when DataType is OBJECT");
      } else {
        checkState(
            type.getIdentifier().isEmpty(),
            "Identifier must not be specified when DataType is not OBJECT");
      }
      return type;
    }
  }
}
