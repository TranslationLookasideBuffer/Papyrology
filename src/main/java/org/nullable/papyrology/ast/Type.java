package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.TypeContext;
import org.nullable.papyrology.source.SourceReference;

/**
 * Defines a data type for a value.
 *
 * <p>An {@link Identifier} is only present for {@code Types} where the {@code DataType} is {@link
 * DataType#OBJECT}.
 */
@Immutable
public record Type(
    SourceReference sourceReference, DataType dataType, Optional<Identifier> identifier)
    implements Construct {

  /** Returns a new {@code Type} based on the given {@link TypeContext}. */
  static Type create(TypeContext ctx) {
    boolean isArray = ctx.S_LBRAKET() != null;
    DataType dataType = null;
    Optional<Identifier> identifier = Optional.empty();
    if (ctx.K_BOOL() != null) {
      dataType = isArray ? DataType.BOOL_ARRAY : DataType.BOOL;
    } else if (ctx.K_INT() != null) {
      dataType = isArray ? DataType.INT_ARRAY : DataType.INT;
    } else if (ctx.K_FLOAT() != null) {
      dataType = isArray ? DataType.FLOAT_ARRAY : DataType.FLOAT;
    } else if (ctx.K_STRING() != null) {
      dataType = isArray ? DataType.STRING_ARRAY : DataType.STRING;
    } else if (ctx.ID() != null) {
      dataType = isArray ? DataType.OBJECT_ARRAY : DataType.OBJECT;
      identifier = Optional.of(Identifier.create(ctx.ID()));
    } else {
      throw new IllegalArgumentException(
          String.format("Type::create passed malformed TypeContext: %s", ctx));
    }
    return new Type(SourceReference.create(ctx), dataType, identifier);
  }
}
