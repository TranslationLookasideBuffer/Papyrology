package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.Optional;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser.EventContext;
import org.nullable.papyrology.grammar.PapyrusParser.EventDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.NativeEventContext;

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

  /** Returns a new {@code Event} based on the given {@link EventDeclarationContext}. */
  public static Event create(EventDeclarationContext ctx) {
    if (ctx instanceof EventContext) {
      return create((EventContext) ctx);
    }
    if (ctx instanceof NativeEventContext) {
      return create((NativeEventContext) ctx);
    }
    throw new IllegalArgumentException(
        String.format("Event::create passed malformed EventDeclarationContext: %s", ctx));
  }

  private static Event create(EventContext ctx) {
    Builder event =
        Event.builder()
            .setSourceReference(SourceReference.create(ctx))
            .setIdentifier(Identifier.create(ctx.ID()))
            .setParameters(Parameter.create(ctx.parameters()))
            .setBodyStatements(Statement.create(ctx.statementBlock()))
            .setNative(false);
    if (ctx.docComment() != null) {
      event.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return event.build();
  }

  private static Event create(NativeEventContext ctx) {
    Builder event =
        Event.builder()
            .setSourceReference(SourceReference.create(ctx))
            .setIdentifier(Identifier.create(ctx.ID()))
            .setParameters(Parameter.create(ctx.parameters()))
            .setNative(true);
    if (ctx.docComment() != null) {
      event.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return event.build();
  }

  /** Returns a fresh {@code Event} builder. */
  static Builder builder() {
    return new AutoValue_Event.Builder();
  }

  /** A builder of {@code Events}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setParameters(ImmutableList<Parameter> parameters);

    abstract Builder setBodyStatements(ImmutableList<Statement> bodyStatements);

    abstract Builder setComment(String comment);

    abstract Builder setNative(boolean isNative);

    abstract Event build();
  }
}
