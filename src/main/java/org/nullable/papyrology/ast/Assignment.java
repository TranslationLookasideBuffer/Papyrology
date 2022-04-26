package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoOneOf;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import org.nullable.papyrology.grammar.PapyrusParser;
import org.nullable.papyrology.grammar.PapyrusParser.ArrayAccessAssigneeContext;
import org.nullable.papyrology.grammar.PapyrusParser.AssigneeContext;
import org.nullable.papyrology.grammar.PapyrusParser.AssignmentContext;
import org.nullable.papyrology.grammar.PapyrusParser.DotAccessAssigneeContext;
import org.nullable.papyrology.grammar.PapyrusParser.IdentifierAssigneeContext;
import org.nullable.papyrology.source.SourceReference;

/** A {@link Statement} that updates the value of a variable or property. */
@Immutable
public record Assignment(
    SourceReference sourceReference,
    Assignee assignee,
    Operator operator,
    Expression valueExpression)
    implements Statement {

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

  @Override
  public final <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  /** Returns a new {@code Assignment} based on the given {@link AssignmentContext}. */
  static Assignment create(AssignmentContext ctx) {
    Operator operator = TOKEN_TYPES_TO_OPERATORS.get(ctx.op.getType());
    checkState(operator != null, "Assignment::create was unable to resolve the operator");
    return new Assignment(
        SourceReference.create(ctx),
        Assignee.create(ctx.assignee()),
        operator,
        Expression.create(ctx.expression()));
  }

  /** A one-of representing the variable/property being updated. */
  @AutoOneOf(Assignee.Type.class)
  @Immutable
  public abstract static class Assignee {
    public enum Type {
      IDENTIFIER,
      DOT_ACCESS,
      ARRAY_ACCESS
    }

    public abstract Type type();

    public abstract Identifier identifier();

    public abstract DotAccess dotAccess();

    public abstract ArrayAccess arrayAccess();

    public final <T> T accept(Visitor<T> visitor) {
      switch (type()) {
        case IDENTIFIER:
          return identifier().accept(visitor);
        case DOT_ACCESS:
          return dotAccess().accept(visitor);
        case ARRAY_ACCESS:
          return arrayAccess().accept(visitor);
        default:
          throw new IllegalStateException(
              String.format("Assignee:::accept encountered invalid type: %s", type()));
      }
    }

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
      return AutoOneOf_Assignment_Assignee.dotAccess(DotAccess.create(ctx));
    }

    static Assignee create(ArrayAccessAssigneeContext ctx) {
      return AutoOneOf_Assignment_Assignee.arrayAccess(ArrayAccess.create(ctx));
    }
  }
}
