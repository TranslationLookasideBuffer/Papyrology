package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

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

  /** Returns a fresh {@code State} builder. */
  public static Builder builder() {
    return new AutoValue_State.Builder();
  }

  /** A builder of {@code States}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setIdentifier(Identifier id);

    public abstract Builder setInvokables(ImmutableList<Invokable> invokables);

    public abstract Builder setAuto(boolean isAuto);

    public abstract State build();
  }
}
