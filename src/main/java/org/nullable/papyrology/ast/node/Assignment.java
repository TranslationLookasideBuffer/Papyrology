package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoOneOf;
import com.google.auto.value.AutoValue;

/** A {@link Statement} that updates the value of a variable or property. */
@AutoValue
public abstract class Assignment implements Statement {

  /** Returns the {@link Assignee} that is being updated. */
  public abstract Assignee getAssignee();

  /** Returns the {@link Expression} that evaluates to the value being assigned. */
  public abstract Expression getValueExpression();

  /** Returns a fresh {@code Assignment} builder. */
  public static Builder builder() {
    return new AutoValue_Assignment.Builder();
  }

  /** A builder of {@code Assignments}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setAssignee(Assignee assignee);

    public abstract Builder setValueExpression(Expression expression);

    public abstract Assignment build();
  }

  /** A one-of representing the variable/property being updated. */
  @AutoOneOf(Assignee.Type.class)
  public abstract static class Assignee {
    public enum Type {
      IDENTIFIER,
      DOT_ACCESS,
      ARRAY_ACCESS
    }

    public abstract Type getType();

    public abstract Identifier getIdentifier();

    public abstract DotAccess getDotAccess();

    public abstract ArrayAccess getArrayAccess();

    public static Assignee ofIdentifier(Identifier identifier) {
      return AutoOneOf_Assignment_Assignee.identifier(identifier);
    }

    public static Assignee ofDotAccess(DotAccess dotAccess) {
      return AutoOneOf_Assignment_Assignee.dotAccess(dotAccess);
    }

    public static Assignee ofArrayAccess(ArrayAccess arrayAccess) {
      return AutoOneOf_Assignment_Assignee.arrayAccess(arrayAccess);
    }
  }
}
