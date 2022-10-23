package org.nullable.papyrology.ast;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ScriptContext;
import org.nullable.papyrology.source.SourceReference;

/** The top-level construct - an entire Papyrus script. */
@Immutable
public record Script(
    SourceReference sourceReference, Header header, ImmutableList<Declaration> declarations)
    implements Construct {

  @Override
  public final void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /** Returns a new {@code Script} based on the given {@link ScriptContext}. */
  static Script create(ScriptContext ctx) {
    SourceReference sourceReference = SourceReference.create(ctx);
    ImmutableList<Declaration> declarations =
        ctx.declaration().stream().map(Declaration::create).collect(toImmutableList());
    if (declarations.stream().filter(d -> d instanceof State s && s.isAuto()).count() > 1) {
      throw new SyntaxException(sourceReference, "Cannot specify multiple auto States.");
    }
    return new Script(sourceReference, Header.create(ctx.header()), declarations);
  }
}
