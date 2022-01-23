package org.nullable.papyrology.grammar;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Validates that known good Papyrus scripts are parsable by the {@code Papyrus.g4} grammar.
 *
 * <p>This test will grab all files in {@code src/test/scripts} and run them through the parser.
 */
@RunWith(Parameterized.class)
public class GrammarTest {

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
  public void parse() throws IOException {
    String script = new String(Files.readAllBytes(path)) + "\n";
    PapyrusLexer lexer = new PapyrusLexer(CharStreams.fromString(script));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    PapyrusParser parser = new PapyrusParser(tokens);
    parser.addErrorListener(new ErrorListener());
    ParseTree tree = parser.script();
    ParseTreeWalker walker = new ParseTreeWalker();

    walker.walk(new NullParseTreeListener(), tree);
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

  /** A {@link ParseTreeListener} that does nothing. */
  private static class NullParseTreeListener implements ParseTreeListener {
    @Override
    public void enterEveryRule(ParserRuleContext ctx) {}

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {}

    @Override
    public void visitErrorNode(ErrorNode node) {}

    @Override
    public void visitTerminal(TerminalNode node) {}
  }
}
