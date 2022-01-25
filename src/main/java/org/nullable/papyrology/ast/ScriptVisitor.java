package org.nullable.papyrology.ast;

import org.nullable.papyrology.ast.node.Script;
import org.nullable.papyrology.grammar.PapyrusBaseVisitor;
import org.nullable.papyrology.grammar.PapyrusParser.ScriptContext;

public final class ScriptVisitor extends PapyrusBaseVisitor<Script> {

  @Override
  public Script visitScript(ScriptContext ctx) {
    return Script.create(ctx);
  }
}
