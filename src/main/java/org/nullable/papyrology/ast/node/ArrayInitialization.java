package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import org.nullable.papyrology.ast.common.SyntaxException;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayInitializationContext;

/** An expression that evaluates to a newly initialized array. */
@AutoValue
public abstract class ArrayInitialization implements Expression {

  /** Returns the {@link Type} of this array initialization. */
  public abstract Type getType();

  /** Returns the {@link IntegerLiteral} representing the size of the array being initialized. */
  public abstract IntegerLiteral getSize();

  /**
   * Returns a new {@code ArrayInitialization} based on the given {@link
   * ArrayInitializationContext}.
   */
  public static ArrayInitialization create(ArrayInitializationContext ctx) {
    IntegerLiteral sizeLiteral = IntegerLiteral.create(ctx.L_UINT());
    if (sizeLiteral.getValue() > 128) {
      throw new SyntaxException(
          SourceReference.create(ctx.L_UINT()), "Array size cannot be greater than 128");
    }
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setType(Type.create(ctx.type()))
        .setSize(sizeLiteral)
        .build();
  }

  /** Returns a fresh {@code ArrayInitialization} builder. */
  static Builder builder() {
    return new AutoValue_ArrayInitialization.Builder();
  }

  /** A builder of {@code ArrayInitializations}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setType(Type type);

    abstract Builder setSize(IntegerLiteral sizeLiteral);

    abstract ArrayInitialization build();
  }
}
