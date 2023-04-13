package org.nullable.papyrology.ast;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.nullable.papyrology.ast.symbol.SymbolTable;
import org.nullable.papyrology.grammar.PapyrusLexer;
import org.nullable.papyrology.grammar.PapyrusParser;

/**
 * Validates that known good Papyrus scripts are parsable by the {@code ScriptFactory}.
 *
 * <p>This test will grab all files in {@code src/test/scripts} and run them through the visitor.
 */
@RunWith(Parameterized.class)
public class ScriptFactoryTest {

  /** Returns the {@link Path Paths} of all scripts under {@code src/test/scripts}. */
  @Parameters(name = "{0}")
  public static Object[] data() {
    try (Stream<Path> paths = Files.walk(Paths.get("src/test/scripts"))) {
      return paths.filter(Files::isRegularFile).toArray();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load script files.", e);
    }
  }

  @Parameter(0)
  public Path path;

  @Before
  public void mark() {
    System.out.printf("Parsing: %s\n", path);
  }

  @Test
  public void create() throws IOException {
    String content = new String(Files.readAllBytes(path)) + "\n";
    PapyrusLexer lexer = new PapyrusLexer(CharStreams.fromString(content));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    PapyrusParser parser = new PapyrusParser(tokens);
    parser.addErrorListener(new ErrorListener());
    ParseTree tree = parser.script();

    Script script = ScriptFactory.create(tree);

    SymbolTable symbolTable = SymbolTable.create();
    symbolTable.upsert(script);
  }

  /**
   * A {@link BaseErrorListener} that causes the enclosing test to fail if ambiguity or a syntax
   * error is found.
   */
  private static class ErrorListener extends DiagnosticErrorListener {
    @Override
    public void syntaxError(
        Recognizer<?, ?> recognizer,
        Object offendingSymbol,
        int line,
        int charPositionInLine,
        String msg,
        RecognitionException e) {
      if (e != null) {
        fail(
            String.format(
                "Encountered syntax error on line %d at char %d: %s",
                line, charPositionInLine, e.getMessage()));
      }
    }
  }
}
