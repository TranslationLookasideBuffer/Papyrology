package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.AutoPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.AutoReadOnlyPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.FullPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.GetPropertyFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.PropertyDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.PropertyFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.SetPropertyFunctionContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/**
 * A {@link Declaration} that defines a property.
 *
 * <p>Parameters may have a "Set" function; this {@code Function} will not have a comment or return
 * {@code Type}, will have an {@code Identifier} equivalent to "Set", and will have exactly one
 * {@code Parameter} which in turn has the same {@code Type} as this property.
 *
 * <p>Parameters may also have a "Get" function; this {@code Function} will not have a comment or
 * any {@code Parameters} and will have an {@code Identifier} equivalent to "Get" and a return
 * {@code Type} that is the same as this property.
 *
 * <p>A function that {@link #isAuto()} or {@link #isAutoReadOnly()} will have neither a "Get" or a
 * "Set" function.
 *
 * <p>Properties that are {@link #isAutoReadOnly()} must have a {@link #defaultValueLiteral()} and
 * cannot be conditional.
 */
@Immutable
public record Property(
    SourceReference sourceReference,
    Type type,
    Identifier identifier,
    Optional<Literal> defaultValueLiteral,
    Optional<Function> setFunction,
    Optional<Function> getFunction,
    Optional<String> comment,
    boolean isAuto,
    boolean isAutoReadOnly,
    boolean isHidden,
    boolean isConditional)
    implements Declaration {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code Property} based on the given {@link PropertyDeclarationContext}. */
  static Property create(PropertyDeclarationContext ctx) {
    if (ctx instanceof FullPropertyContext) {
      return create((FullPropertyContext) ctx);
    }
    if (ctx instanceof AutoPropertyContext) {
      return create((AutoPropertyContext) ctx);
    }
    if (ctx instanceof AutoReadOnlyPropertyContext) {
      return create((AutoReadOnlyPropertyContext) ctx);
    }
    throw new IllegalArgumentException(
        String.format(
            "Property::create passed an unsupported PropertyDeclarationContext: %s", ctx));
  }

  private static Property create(FullPropertyContext ctx) {
    Optional<Function> getFunction = Optional.empty();
    Optional<Function> setFunction = Optional.empty();
    for (PropertyFunctionContext funcCtx : ctx.propertyFunction()) {
      if (funcCtx instanceof GetPropertyFunctionContext) {
        if (getFunction.isPresent()) {
          throw new SyntaxException(
              SourceReference.create(funcCtx),
              "Cannot specify get function for full property twice");
        }
        getFunction = Optional.of(Function.create((GetPropertyFunctionContext) funcCtx));
      } else if (funcCtx instanceof SetPropertyFunctionContext) {
        if (setFunction.isPresent()) {
          throw new SyntaxException(
              SourceReference.create(funcCtx),
              "Cannot specify set function for full property twice");
        }
        setFunction = Optional.of(Function.create((SetPropertyFunctionContext) funcCtx));
      } else {
        throw new IllegalArgumentException(
            String.format(
                "Property::create passed malformed PropertyFunctionContext: %s", funcCtx));
      }
    }
    return new Property(
        SourceReference.create(ctx),
        Type.create(ctx.type()),
        Identifier.create(ctx.ID()),
        /* defaultValueLiteral= */ Optional.empty(),
        setFunction,
        getFunction,
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        /* isAuto= */ false,
        /* isAutoReadOnly= */ false,
        ctx.F_HIDDEN() != null,
        /* isConditional= */ false);
  }

  private static Property create(AutoPropertyContext ctx) {
    return new Property(
        SourceReference.create(ctx),
        Type.create(ctx.type()),
        Identifier.create(ctx.ID()),
        Optionals.of(ctx.literal() != null, () -> Literal.create(ctx.literal())),
        /* setFunction= */ Optional.empty(),
        /* getFunction= */ Optional.empty(),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        /* isAuto= */ true,
        /* isAutoReadOnly= */ false,
        !ctx.F_HIDDEN().isEmpty(),
        !ctx.F_CONDITIONAL().isEmpty());
  }

  private static Property create(AutoReadOnlyPropertyContext ctx) {
    return new Property(
        SourceReference.create(ctx),
        Type.create(ctx.type()),
        Identifier.create(ctx.ID()),
        Optional.of(Literal.create(ctx.literal())),
        /* setFunction= */ Optional.empty(),
        /* getFunction= */ Optional.empty(),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        /* isAuto= */ false,
        /* isAutoReadOnly= */ true,
        ctx.F_HIDDEN() != null,
        /* isConditional= */ false);
  }
}
