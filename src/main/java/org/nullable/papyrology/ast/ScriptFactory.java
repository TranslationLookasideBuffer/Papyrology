package org.nullable.papyrology.ast;

import org.antlr.v4.runtime.tree.ParseTree;
import org.nullable.papyrology.grammar.PapyrusBaseVisitor;
import org.nullable.papyrology.grammar.PapyrusParser.ScriptContext;

/** A factory of {@link Script Scripts}. */
public final class ScriptFactory {

  /** Returns a new {@link Script} built from the given {@link ParseTree}. */
  public static Script create(ParseTree tree) {
    return tree.accept(new ScriptVisitor());
  }

  private static class ScriptVisitor extends PapyrusBaseVisitor<Script> {

    @Override
    public Script visitScript(ScriptContext ctx) {
      return Script.create(ctx);
    }
  }

  private ScriptFactory() {}
}
