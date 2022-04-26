package org.nullable.papyrology.ast;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.FunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.FunctionDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.GetPropertyFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.NativeFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.SetPropertyFunctionContext;
import org.nullable.papyrology.source.SourceReference;
import org.nullable.papyrology.util.Optionals;

/** An {@link Invokable} that defines a unit of work that may be called by scripts. */
@Immutable
public record Function(
    SourceReference sourceReference,
    Optional<Type> returnType,
    Identifier identifier,
    ImmutableList<Parameter> parameters,
    ImmutableList<Statement> bodyStatements,
    Optional<String> comment,
    boolean isGlobal,
    boolean isNative)
    implements Invokable {

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code Function} based on the given {@link FunctionDeclarationContext}. */
  static Function create(FunctionDeclarationContext ctx) {
    if (ctx instanceof FunctionContext) {
      return create((FunctionContext) ctx);
    }
    if (ctx instanceof NativeFunctionContext) {
      return create((NativeFunctionContext) ctx);
    }
    throw new IllegalArgumentException(
        String.format("Function::create passed malformed FunctionDeclarationContext: %s", ctx));
  }

  private static Function create(FunctionContext ctx) {
    return new Function(
        SourceReference.create(ctx),
        Optionals.of(ctx.type() != null, () -> Type.create(ctx.type())),
        Identifier.create(ctx.ID()),
        Parameter.create(ctx.parameters()),
        Statement.create(ctx.statementBlock()),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        ctx.K_GLOBAL() != null,
        /* isNative= */ false);
  }

  private static Function create(NativeFunctionContext ctx) {
    return new Function(
        SourceReference.create(ctx),
        Optionals.of(ctx.type() != null, () -> Type.create(ctx.type())),
        Identifier.create(ctx.ID()),
        Parameter.create(ctx.parameters()),
        /* bodyStatements= */ ImmutableList.of(),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        !ctx.K_GLOBAL().isEmpty(),
        /* isNative= */ true);
  }

  static Function create(GetPropertyFunctionContext ctx) {
    Identifier identifier = Identifier.create(ctx.ID());
    if (!identifier.isEquivalent(GET_IDENTIFIER)) {
      throw new SyntaxException(
          SourceReference.create(ctx), "Property function must be named \"Get\" or equivalent");
    }
    return new Function(
        SourceReference.create(ctx),
        Optional.of(Type.create(ctx.type())),
        identifier,
        /* parameters= */ ImmutableList.of(),
        Statement.create(ctx.statementBlock()),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        /* isGlobal= */ false,
        /* isNative= */ false);
  }

  private static final String GET_IDENTIFIER = "Get";

  static Function create(SetPropertyFunctionContext ctx) {
    Identifier identifier = Identifier.create(ctx.ID());
    if (!identifier.isEquivalent(SET_IDENTIFIER)) {
      throw new SyntaxException(
          SourceReference.create(ctx), "Property function must be named \"Set\" or equivalent");
    }
    return new Function(
        SourceReference.create(ctx),
        /* returnType= */ Optional.empty(),
        identifier,
        ImmutableList.of(Parameter.create(ctx.parameter())),
        Statement.create(ctx.statementBlock()),
        Optionals.of(
            ctx.docComment() != null, () -> ctx.docComment().DOC_COMMENT().getSymbol().getText()),
        /* isGlobal= */ false,
        /* isNative= */ false);
  }

  private static final String SET_IDENTIFIER = "Set";
}
