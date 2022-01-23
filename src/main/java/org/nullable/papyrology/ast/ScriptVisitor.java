package org.nullable.papyrology.ast;

import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.grammar.PapyrusBaseVisitor;
import org.nullable.papyrology.ast.node.Script;

public final class ScriptVisitor extends PapyrusBaseVisitor<Script> {

  @Override
  public Script visitScript(PapyrusParser.ScriptContext ctx) {
    return null;
  }
}
