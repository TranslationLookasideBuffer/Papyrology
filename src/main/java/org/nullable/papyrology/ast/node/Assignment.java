package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoOneOf;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import org.nullable.papyrology.common.SourceReference;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayAccessAssigneeContext;
import org.nullable.papyrology.grammar.PapyrusParser.AssigneeContext;
import org.nullable.papyrology.grammar.PapyrusParser.AssignmentContext;
import org.nullable.papyrology.grammar.PapyrusParser.DotAccessAssigneeContext;
import org.nullable.papyrology.grammar.PapyrusParser.IdentifierAssigneeContext;

/** A {@link Statement} that updates the value of a variable or property. */
@AutoValue
public abstract class Assignment implements Statement {

  /** Which "flavor" of assignment this represents. */
  public enum Operator {
    ASSIGN,
    ASSIGN_ADD,
    ASSIGN_SUBTRACT,
    ASSIGN_MULTIPLY,
    ASSIGN_DIVIDE,
    ASSIGN_MODULO
  }

  private static final ImmutableMap<Integer, Operator> TOKEN_TYPES_TO_OPERATORS =
      ImmutableMap.<Integer, Operator>builder()
          .put(PapyrusParser.O_ASSIGN, Operator.ASSIGN)
          .put(PapyrusParser.O_ASSIGN_ADD, Operator.ASSIGN_ADD)
          .put(PapyrusParser.O_ASSIGN_SUBTRACT, Operator.ASSIGN_SUBTRACT)
          .put(PapyrusParser.O_ASSIGN_MULTIPLY, Operator.ASSIGN_MULTIPLY)
          .put(PapyrusParser.O_ASSIGN_DIVIDE, Operator.ASSIGN_DIVIDE)
          .put(PapyrusParser.O_ASSIGN_MODULO, Operator.ASSIGN_MODULO)
          .build();

  /** Returns the {@link Assignee} that is being updated. */
  public abstract Assignee getAssignee();

  /** Returns the {@link Operator} that is being used for this assignment. */
  public abstract Operator getOperator();

  /** Returns the {@link Expression} that evaluates to the value being assigned. */
  public abstract Expression getValueExpression();

  /** Returns a new {@code Assignment} based on the given {@link AssignmentContext}. */
  public static Assignment create(AssignmentContext ctx) {
    Operator operator = TOKEN_TYPES_TO_OPERATORS.get(ctx.op.getType());
    checkState(operator != null, "Assignment::create was unable to resolve the operator");
    return builder()
        .setSourceReference(SourceReference.create(ctx))
        .setAssignee(Assignee.create(ctx.assignee()))
        .setOperator(operator)
        .setValueExpression(Expression.create(ctx.expression()))
        .build();
  }

  /** Returns a fresh {@code Assignment} builder. */
  static Builder builder() {
    return new AutoValue_Assignment.Builder();
  }

  /** A builder of {@code Assignments}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setSourceReference(SourceReference reference);

    abstract Builder setAssignee(Assignee assignee);

    abstract Builder setOperator(Operator operator);

    abstract Builder setValueExpression(Expression expression);

    abstract Assignment build();
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

    static Assignee create(AssigneeContext ctx) {
      if (ctx instanceof IdentifierAssigneeContext) {
        return create((IdentifierAssigneeContext) ctx);
      }
      if (ctx instanceof DotAccessAssigneeContext) {
        return create((DotAccessAssigneeContext) ctx);
      }
      if (ctx instanceof ArrayAccessAssigneeContext) {
        return create((ArrayAccessAssigneeContext) ctx);
      }
      throw new IllegalArgumentException(
          String.format("Assignee::create passed malformed AssigneeContext: %s", ctx));
    }

    static Assignee create(IdentifierAssigneeContext ctx) {
      return AutoOneOf_Assignment_Assignee.identifier(Identifier.create(ctx.ID()));
    }

    static Assignee create(DotAccessAssigneeContext ctx) {
      return AutoOneOf_Assignment_Assignee.dotAccess(
          DotAccess.builder()
              .setReferenceExpression(Expression.create(ctx.expression()))
              .setIdentifier(Identifier.create(ctx.ID()))
              .build());
    }

    static Assignee create(ArrayAccessAssigneeContext ctx) {
      return AutoOneOf_Assignment_Assignee.arrayAccess(
          ArrayAccess.builder()
              .setArrayExpression(Expression.create(ctx.expression(0)))
              .setIndexExpression(Expression.create(ctx.expression(1)))
              .build());
    }
  }
}
