package org.nullable.papyrology.analysis;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Function;
import org.nullable.papyrology.ast.Declaration;
import org.nullable.papyrology.ast.Script;

/** A {@link StaticAnalyzer} that resolves and validates identifiers. */
public class IdentifierStaticAnalyzer implements StaticAnalyzer {

  private final IdentifierResolver resolver;

  private IdentifierStaticAnalyzer(IdentifierResolver resolver) {
    this.resolver = resolver;
  }

  public static IdentifierStaticAnalyzer create(Function<String, Script> scriptLoader) {
    return new IdentifierStaticAnalyzer(new IdentifierResolver(scriptLoader));
  }

  @Override
  public ImmutableList<Issue> analyze(Script script) {
    return script.declarations().stream()
        .map(this::analyze)
        .flatMap(List::stream)
        .collect(toImmutableList());
  }

  private List<Issue> analyze(Declaration declaration) {
    return null;
  }

  private static class IdentifierResolver {

    private final Function<String, Script> scriptLoader;

    private IdentifierResolver(Function<String, Script> scriptLoader) {
      this.scriptLoader = scriptLoader;
    }
  }
}
