package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.DeclarationContext;

/**
 * Denotes a top-level script element definition.
 *
 * <p>Specifically one of the following:
 *
 * <ul>
 *   <li>{@link Import}
 *   <li>{@link ScriptVariable}
 *   <li>{@link Property}
 *   <li>{@link State}
 *   <li>{@link Function}
 *   <li>{@link Event}
 * </ul>
 */
@Immutable
public interface Declaration extends Construct {

  static Declaration create(DeclarationContext ctx) {
    if (ctx.propertyDeclaration() != null) {
      return Property.create(ctx.propertyDeclaration());
    }
    if (ctx.functionDeclaration() != null) {
      return Function.create(ctx.functionDeclaration());
    }
    if (ctx.eventDeclaration() != null) {
      return Event.create(ctx.eventDeclaration());
    }
    if (ctx.stateDeclaration() != null) {
      return State.create(ctx.stateDeclaration());
    }
    if (ctx.variableDeclaration() != null) {
      return ScriptVariable.create(ctx.variableDeclaration());
    }
    if (ctx.importDeclaration() != null) {
      return Import.create(ctx.importDeclaration());
    }
    throw new IllegalArgumentException(
        String.format("Declaration::create passed malformed DeclarationContext: %s", ctx));
  }
}
