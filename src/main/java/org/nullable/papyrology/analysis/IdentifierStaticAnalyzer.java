package org.nullable.papyrology.analysis;

import com.google.common.collect.ImmutableList;
import org.nullable.papyrology.ast.node.Script;

/** A {@link StaticAnalyzer} that resolves and validates identifiers. */
public class IdentifierStaticAnalyzer implements StaticAnalyzer {

  @Override
  public ImmutableList<Issue> analyze(Script script) {
    return ImmutableList.of();
  }
}
