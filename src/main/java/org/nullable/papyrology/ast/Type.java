package org.nullable.papyrology.ast;

import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.TypeContext;
import org.nullable.papyrology.source.SourceReference;

/** Defines a data type for a value. */
@AutoValue
@Immutable
public abstract class Type implements Construct {

  /** Returns the data type of this node. */
  public abstract DataType getDataType();

  /** Returns the {@code Identifier} if the {@code DataType} is {@link DataType#OBJECT}. */
  public abstract Optional<Identifier> getIdentifier();

  /** Returns a new {@code Type} based on the given {@link TypeContext}. */
  static Type create(TypeContext ctx) {
    Builder type = builder();
    boolean isArray = ctx.S_LBRAKET() != null;
    if (ctx.K_BOOL() != null) {
      type.setDataType(isArray ? DataType.BOOL_ARRAY : DataType.BOOL);
    }
    if (ctx.K_INT() != null) {
      type.setDataType(isArray ? DataType.INT_ARRAY : DataType.INT);
    }
    if (ctx.K_FLOAT() != null) {
      type.setDataType(isArray ? DataType.FLOAT_ARRAY : DataType.FLOAT);
    }
    if (ctx.K_STRING() != null) {
      type.setDataType(isArray ? DataType.STRING_ARRAY : DataType.STRING);
    }
    if (ctx.ID() != null) {
      type.setDataType(isArray ? DataType.OBJECT_ARRAY : DataType.OBJECT)
          .setIdentifier(Identifier.create(ctx.ID()));
    }
    return type.setSourceReference(SourceReference.create(ctx)).build();
  }

  /** Returns a fresh {@code Type} builder. */
  static Builder builder() {
    return new AutoValue_Type.Builder();
  }

  /** A builder of {@code Types}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setDataType(DataType dataType);

    abstract Builder setIdentifier(Identifier id);

    abstract Type build();
  }
}
