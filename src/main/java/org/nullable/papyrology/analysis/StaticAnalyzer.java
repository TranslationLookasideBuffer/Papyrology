package org.nullable.papyrology.analysis;

public interface StaticAnalyzer {

  ImmutableList<Result> analyze(Script script);
}
