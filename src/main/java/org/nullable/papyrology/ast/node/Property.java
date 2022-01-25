package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Optional;
import org.nullable.papyrology.ast.common.SourceReference;
import org.nullable.papyrology.ast.common.SyntaxException;
import org.nullable.papyrology.grammar.PapyrusParser.AutoPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.AutoReadOnlyPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.FullPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.GetPropertyFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.PropertyDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.PropertyFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.SetPropertyFunctionContext;

/** A {@link Declaration} that defines a property. */
@AutoValue
public abstract class Property implements Declaration {

  /** Returns the {@link Type} of this property. */
  public abstract Type getType();

  /** Returns the {@link Identifier} of this property. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Literal} that defines this property's default value, if present. */
  public abstract Optional<Literal> getDefaultValueLiteral();

  /**
   * Returns the {@link Function} that defines this property's setter, if present.
   *
   * <p>This {@code Function} will not have a comment or return {@code Type}, will have an {@code
   * Identifier} equivalent to "Set", and will have exactly one {@code Parameter} which in turn has
   * the same {@code Type} as this property.
   */
  public abstract Optional<Function> getSetFunction();

  /**
   * Returns the {@link Function} that defines this property's getter, if present.
   *
   * <p>This {@code Function} will not have a comment or any {@code Parameters} and will have an
   * {@code Identifier} equivalent to "Get" and a return {@code Type} that is the same as this
   * property.
   */
  public abstract Optional<Function> getGetFunction();

  /** Returns the documentation comment of this property, if present. */
  public abstract Optional<String> getComment();

  /**
   * Returns whether or not this property is an auto property.
   *
   * <p>If true, {@link #getSetFunction()} and {@link #getGetFunction()} will both return empty.
   * Likewise, {@link #isAutoReadOnly()} will return false.
   */
  public abstract boolean isAuto();

  /**
   * Returns whether or not this property is an auto read-only property.
   *
   * <p>If true, {@link #getSetFunction()} and {@link #getGetFunction()} will both return empty.
   * Likewise, {@link #isAuto()} and {@link #isConditional()} will return false. {@link
   * #getDefaultValueLiteral()} will return a present {@link Literal}.
   */
  public abstract boolean isAutoReadOnly();

  /** Returns whether or not this property is hidden. */
  public abstract boolean isHidden();

  /** Returns whether or not this property is hidden. */
  public abstract boolean isConditional();

  /** Returns a new {@code Property} based on the given {@link PropertyDeclarationContext}. */
  public static Property create(PropertyDeclarationContext ctx) {
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
    Builder property =
        builder()
            .setAuto(false)
            .setAutoReadOnly(false)
            .setType(Type.create(ctx.type()))
            .setIdentifier(Identifier.create(ctx.ID()))
            .setHidden(ctx.F_HIDDEN() != null);
    if (ctx.docComment() != null) {
      property.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    boolean getPresent = false;
    boolean setPresent = false;
    for (PropertyFunctionContext funcCtx : ctx.propertyFunction()) {
      if (funcCtx instanceof GetPropertyFunctionContext) {
        if (getPresent) {
          throw new SyntaxException(
              SourceReference.create(funcCtx),
              "Cannot specify get function for full property twice");
        }
        property.setGetFunction(create((GetPropertyFunctionContext) funcCtx));
        getPresent = true;
      } else if (funcCtx instanceof SetPropertyFunctionContext) {
        if (setPresent) {
          throw new SyntaxException(
              SourceReference.create(funcCtx),
              "Cannot specify set function for full property twice");
        }
        property.setSetFunction(create((SetPropertyFunctionContext) funcCtx));
        setPresent = true;
      } else {
        throw new IllegalArgumentException(
            String.format(
                "Property::create passed malformed PropertyFunctionContext: %s", funcCtx));
      }
    }
    return property.build();
  }

  private static Function create(GetPropertyFunctionContext ctx) {
    Identifier identifier = Identifier.create(ctx.ID());
    if (!identifier.isEquivalent(GET_IDENTIFIER)) {
      throw new SyntaxException(
          SourceReference.create(ctx), "Property function must be named \"Get\" or equivalent");
    }
    Function.Builder function =
        Function.builder()
            .setReturnType(Type.create(ctx.type()))
            .setIdentifier(identifier)
            .setParameters(ImmutableList.of())
            .setBodyStatements(Statement.create(ctx.statementBlock()))
            .setGlobal(false)
            .setNative(false);
    if (ctx.docComment() != null) {
      function.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return function.build();
  }

  private static final Identifier GET_IDENTIFIER = Identifier.builder().setValue("Get").build();

  private static Function create(SetPropertyFunctionContext ctx) {
    Identifier identifier = Identifier.create(ctx.ID());
    if (!identifier.isEquivalent(SET_IDENTIFIER)) {
      throw new SyntaxException(
          SourceReference.create(ctx), "Property function must be named \"Set\" or equivalent");
    }
    Function.Builder function =
        Function.builder()
            .setIdentifier(identifier)
            .setParameters(ImmutableList.of(Parameter.create(ctx.parameter())))
            .setBodyStatements(Statement.create(ctx.statementBlock()))
            .setGlobal(false)
            .setNative(false);
    if (ctx.docComment() != null) {
      function.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return function.build();
  }

  private static final Identifier SET_IDENTIFIER = Identifier.builder().setValue("Set").build();

  private static Property create(AutoPropertyContext ctx) {
    Builder property =
        builder()
            .setAuto(true)
            .setAutoReadOnly(false)
            .setType(Type.create(ctx.type()))
            .setIdentifier(Identifier.create(ctx.ID()))
            .setHidden(!ctx.F_HIDDEN().isEmpty())
            .setConditional(!ctx.F_CONDITIONAL().isEmpty());
    if (ctx.docComment() != null) {
      property.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    if (ctx.literal() != null) {
      property.setDefaultValueLiteral(Literal.create(ctx.literal()));
    }
    return property.build();
  }

  private static Property create(AutoReadOnlyPropertyContext ctx) {
    Builder property =
        builder()
            .setAuto(false)
            .setAutoReadOnly(true)
            .setType(Type.create(ctx.type()))
            .setIdentifier(Identifier.create(ctx.ID()))
            .setDefaultValueLiteral(Literal.create(ctx.literal()))
            .setHidden(ctx.F_HIDDEN() != null);
    if (ctx.docComment() != null) {
      property.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return property.build();
  }

  /** Returns a fresh {@code Property} builder. */
  static Builder builder() {
    return new AutoValue_Property.Builder();
  }

  /** A builder of {@code Properties}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setType(Type type);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setDefaultValueLiteral(Literal defaultValueLiteral);

    abstract Builder setSetFunction(Function function);

    abstract Builder setGetFunction(Function function);

    abstract Builder setComment(String comment);

    abstract Builder setAuto(boolean isAuto);

    abstract Builder setAutoReadOnly(boolean isAutoReadOnly);

    abstract Builder setHidden(boolean isHidden);

    abstract Builder setConditional(boolean isConditional);

    abstract Property build();
  }
}
