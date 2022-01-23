package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Optional;

/** An {@link Invokable} that conforms to a game-defined callback function. */
@AutoValue
public abstract class Event implements Invokable {

  /** Returns the {@link Identifier} of this event. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Parameter Parameters} of this event. */
  public abstract ImmutableList<Parameter> getParameters();

  /** Returns the {@link Statement Statements} that make up the body of this event. */
  public abstract ImmutableList<Statement> getBodyStatements();

  /** Returns the documentation comment of this event, if present. */
  public abstract Optional<String> getComment();

  /**
   * Returns whether or not this event is native.
   *
   * <p>If true, {@link #getBodyStatements()} will return an empty list.s
   */
  public abstract boolean isNative();

  /** Returns a fresh {@code Event} builder. */
  static Builder builder() {
    return new AutoValue_Event.Builder();
  }

  /** A builder of {@code Events}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setIdentifier(Identifier id);

    abstract Builder setParameters(ImmutableList<Parameter> parameters);

    abstract Builder setBodyStatements(ImmutableList<Statement> bodyStatements);

    abstract Builder setComment(String comment);

    abstract Builder setNative(boolean isNative);

    abstract Event autoBuild();

    final Event build() {
      Event event = autoBuild();
      checkState(
          !event.isNative() || event.getBodyStatements().isEmpty(),
          "Native Event cannot specify body Statements");
      return event;
    }
  }
}
