package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayAccessContext;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayInitializationContext;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayLengthContext;
import org.nullable.papyrology.grammar.PapyrusParser.BinaryOperationContext;
import org.nullable.papyrology.grammar.PapyrusParser.CastContext;
import org.nullable.papyrology.grammar.PapyrusParser.DotAccessOrFunctionCallContext;
import org.nullable.papyrology.grammar.PapyrusParser.ExpressionContext;
import org.nullable.papyrology.grammar.PapyrusParser.IDContext;
import org.nullable.papyrology.grammar.PapyrusParser.LiteralValueContext;
import org.nullable.papyrology.grammar.PapyrusParser.LocalFunctionCallContext;
import org.nullable.papyrology.grammar.PapyrusParser.ParentheticalContext;
import org.nullable.papyrology.grammar.PapyrusParser.UnaryOperationContext;

/** Denotes a {@link Statement} that evaluates to some value. */
@Immutable
public interface Expression extends Statement {

  /** Returns a new {@code Expression} based on the given {@link ExpressionContext}. */
  static Expression create(ExpressionContext ctx) {
    if (ctx instanceof BinaryOperationContext) {
      return BinaryOperation.create((BinaryOperationContext) ctx);
    }
    if (ctx instanceof UnaryOperationContext) {
      return UnaryOperation.create((UnaryOperationContext) ctx);
    }
    if (ctx instanceof CastContext) {
      return Cast.create((CastContext) ctx);
    }
    if (ctx instanceof LocalFunctionCallContext) {
      return FunctionCall.create((LocalFunctionCallContext) ctx);
    }
    if (ctx instanceof DotAccessOrFunctionCallContext orCtx) {
      return orCtx.callParameters() != null ? FunctionCall.create(orCtx) : DotAccess.create(orCtx);
    }
    if (ctx instanceof ArrayLengthContext) {
      return ArrayLength.create((ArrayLengthContext) ctx);
    }
    if (ctx instanceof ParentheticalContext) {
      return Parenthetical.create((ParentheticalContext) ctx);
    }
    if (ctx instanceof ArrayAccessContext) {
      return ArrayAccess.create((ArrayAccessContext) ctx);
    }
    if (ctx instanceof ArrayInitializationContext) {
      return ArrayInitialization.create((ArrayInitializationContext) ctx);
    }
    if (ctx instanceof LiteralValueContext) {
      return Literal.create(((LiteralValueContext) ctx).literal());
    }
    if (ctx instanceof IDContext) {
      return Identifier.create(((IDContext) ctx).ID());
    }
    throw new IllegalArgumentException(
        String.format("Expression::create passed malformed ExpressionContext: %s", ctx));
  }
}
