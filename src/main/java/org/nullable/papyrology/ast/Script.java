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

  /** Returns a new {@code Script} based on the given {@link ScriptContext}. */
  static Script create(ScriptContext ctx) {
    return new Script(
        SourceReference.create(ctx),
        Header.create(ctx.header()),
        ctx.declaration().stream().map(Declaration::create).collect(toImmutableList()));
  }
}
