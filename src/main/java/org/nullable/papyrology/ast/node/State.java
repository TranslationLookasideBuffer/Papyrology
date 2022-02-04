package org.nullable.papyrology.ast.node;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.StateDeclarationContext;

/** A {@link Declaration} that defines a script state. */
@AutoValue
public abstract class State implements Declaration {

  /** Returns the {@link Identifier} of this state. */
  public abstract Identifier getIdentifier();

  /**
   * Returns the {@link Invokable Invokables} (i.e. {@code Functions} and {@link Events} of this
   * state.
   */
  public abstract ImmutableList<Invokable> getInvokables();

  /** Returns whether or not this state is native. */
  public abstract boolean isAuto();

  /** Returns a new {@code State} based on the given {@link StateDeclarationContext}. */
  public static State create(StateDeclarationContext ctx) {
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setIdentifier(Identifier.create(ctx.ID()))
        .setAuto(ctx.K_AUTO() != null)
        .setInvokables(ctx.invokable().stream().map(Invokable::create).collect(toImmutableList()))
        .build();
  }

  /** Returns a fresh {@code State} builder. */
  static Builder builder() {
    return new AutoValue_State.Builder();
  }

  /** A builder of {@code States}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setInvokables(ImmutableList<Invokable> invokables);

    abstract Builder setAuto(boolean isAuto);

    abstract State build();
  }
}
