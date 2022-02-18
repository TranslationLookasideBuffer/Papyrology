package org.nullable.papyrology.ast;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser.ScriptContext;
import org.nullable.papyrology.source.SourceReference;

/** The top-level construct - an entire Papyrus script. */
@AutoValue
@Immutable
public abstract class Script implements Construct {

  /** Returns this script's {@link Header}. */
  public abstract Header getHeader();

  /** Returns this script's {@link Declaration Declarations} in order. */
  public abstract ImmutableList<Declaration> getDeclarations();

  /** Returns a new {@code Script} based on the given {@link ScriptContext}. */
  static Script create(ScriptContext ctx) {
    return Script.builder()
        .setSourceReference(SourceReference.create(ctx))
        .setHeader(Header.create(ctx.header()))
        .setDeclarations(
            ctx.declaration().stream().map(Declaration::create).collect(toImmutableList()))
        .build();
  }

  /** Returns a fresh {@code Script} builder. */
  static Builder builder() {
    return new AutoValue_Script.Builder();
  }

  /** A builder of {@code ScriptNodes}. */
  @AutoValue.Builder
  @CanIgnoreReturnValue
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setHeader(Header header);

    abstract Builder setDeclarations(ImmutableList<Declaration> declarations);

    abstract Script build();
  }
}
