package org.nullable.papyrology.ast;

import com.google.errorprone.annotations.ForOverride;

/**
 * A {@link Visitor} that walks all constructs in a {@code Script} in order.
 *
 * <p>This {@code Visitor} walks through every {@code Construct} and invokes a {@link Walker} while
 * visiting.
 */
public final class WalkingVisitor implements Visitor {

  private final Walker walker;

  private WalkingVisitor(Walker walker) {
    this.walker = walker;
  }

  /** Returns a new {@code WalkingVisitor} that uses the given {@link Walker}. */
  public static WalkingVisitor create(Walker walker) {
    return new WalkingVisitor(walker);
  }

  @Override
  public final void visit(ArrayAccess arrayAccess) {
    walker.enter(arrayAccess);
    visit(arrayAccess.arrayExpression());
    visit(arrayAccess.indexExpression());
    walker.exit(arrayAccess);
  }

  @Override
  public final void visit(ArrayInitialization arrayInitialization) {
    walker.enter(arrayInitialization);
    visit(arrayInitialization.type());
    visit(arrayInitialization.size());
    walker.exit(arrayInitialization);
  }

  @Override
  public final void visit(ArrayLength arrayLength) {
    walker.enter(arrayLength);
    visit(arrayLength.arrayExpression());
    walker.exit(arrayLength);
  }

  @Override
  public final void visit(Assignment assignment) {
    walker.enter(assignment);
    switch (assignment.assignee().type()) {
      case IDENTIFIER:
        visit(assignment.assignee().identifier());
        break;
      case DOT_ACCESS:
        visit(assignment.assignee().dotAccess());
        break;
      case ARRAY_ACCESS:
        visit(assignment.assignee().arrayAccess());
        break;
    }
    visit(assignment.valueExpression());
    walker.exit(assignment);
  }

  @Override
  public final void visit(BinaryOperation binaryOperation) {
    walker.enter(binaryOperation);
    visit(binaryOperation.leftExpression());
    visit(binaryOperation.rightExpression());
    walker.exit(binaryOperation);
  }

  @Override
  public final void visit(Block block) {
    walker.enter(block);
    block.bodyStatements().forEach(this::visit);
    walker.exit(block);
  }

  @Override
  public final void visit(BooleanLiteral booleanLiteral) {
    walker.touch(booleanLiteral);
  }

  @Override
  public final void visit(CallParameter callParameter) {
    walker.enter(callParameter);
    callParameter.identifier().ifPresent(this::visit);
    visit(callParameter.expression());
    walker.exit(callParameter);
  }

  @Override
  public final void visit(Cast cast) {
    walker.enter(cast);
    visit(cast.type());
    visit(cast.expression());
    walker.exit(cast);
  }

  @Override
  public final void visit(DotAccess dotAccess) {
    walker.enter(dotAccess);
    visit(dotAccess.referenceExpression());
    visit(dotAccess.identifier());
    walker.exit(dotAccess);
  }

  @Override
  public final void visit(Event event) {
    walker.enter(event);
    visit(event.identifier());
    event.parameters().forEach(this::visit);
    event.body().ifPresent(this::visit);
    walker.exit(event);
  }

  @Override
  public final void visit(FloatLiteral floatLiteral) {
    walker.touch(floatLiteral);
  }

  @Override
  public final void visit(Function function) {
    walker.enter(function);
    visit(function.identifier());
    function.returnType().ifPresent(this::visit);
    function.parameters().forEach(this::visit);
    function.body().ifPresent(this::visit);
    walker.exit(function);
  }

  @Override
  public final void visit(FunctionCall functionCall) {
    walker.enter(functionCall);
    functionCall.referenceExpression().ifPresent(this::visit);
    visit(functionCall.identifier());
    functionCall.callParameters().forEach(this::visit);
    walker.exit(functionCall);
  }

  @Override
  public final void visit(Header header) {
    walker.enter(header);
    visit(header.scriptIdentifier());
    header.parentScriptIdentifier().ifPresent(this::visit);
    walker.exit(header);
  }

  @Override
  public final void visit(Identifier identifier) {
    walker.touch(identifier);
  }

  @Override
  public final void visit(If ifStatement) {
    walker.enter(ifStatement);
    ifStatement
        .conditionalBlocks()
        .forEach(
            cb -> {
              visit(cb.conditionalExpression());
              visit(cb.body());
            });
    ifStatement.elseBlock().ifPresent(this::visit);
    walker.exit(ifStatement);
  }

  @Override
  public final void visit(Import importDeclaration) {
    walker.enter(importDeclaration);
    visit(importDeclaration.importedScriptIdentifier());
    walker.exit(importDeclaration);
  }

  @Override
  public final void visit(IntegerLiteral integerLiteral) {
    walker.touch(integerLiteral);
  }

  @Override
  public final void visit(ObjectLiteral objectLiteral) {
    walker.touch(objectLiteral);
  }

  @Override
  public final void visit(Parameter parameter) {
    walker.enter(parameter);
    visit(parameter.type());
    visit(parameter.identifier());
    parameter.defaultValueLiteral().ifPresent(this::visit);
    walker.exit(parameter);
  }

  @Override
  public final void visit(Parenthetical parenthetical) {
    walker.enter(parenthetical);
    visit(parenthetical.expression());
    walker.exit(parenthetical);
  }

  @Override
  public final void visit(Property property) {
    walker.enter(property);
    visit(property.type());
    visit(property.identifier());
    property.defaultValueLiteral().ifPresent(this::visit);
    property.setFunction().ifPresent(this::visit);
    property.getFunction().ifPresent(this::visit);
    walker.exit(property);
  }

  @Override
  public final void visit(Return returnStatement) {
    walker.enter(returnStatement);
    returnStatement.valueExpression().ifPresent(this::visit);
    walker.exit(returnStatement);
  }

  @Override
  public final void visit(Script script) {
    walker.enter(script);
    visit(script.header());
    script.declarations().forEach(this::visit);
    walker.exit(script);
  }

  @Override
  public final void visit(ScriptVariable scriptVariable) {
    walker.enter(scriptVariable);
    visit(scriptVariable.type());
    visit(scriptVariable.identifier());
    scriptVariable.literal().ifPresent(this::visit);
    walker.exit(scriptVariable);
  }

  @Override
  public final void visit(State state) {
    walker.enter(state);
    visit(state.identifier());
    state.invokables().forEach(this::visit);
    walker.exit(state);
  }

  @Override
  public final void visit(StringLiteral stringLiteral) {
    walker.touch(stringLiteral);
  }

  @Override
  public final void visit(Type type) {
    walker.enter(type);
    type.identifier().ifPresent(this::visit);
    walker.exit(type);
  }

  @Override
  public final void visit(UnaryOperation unaryOperation) {
    walker.enter(unaryOperation);
    visit(unaryOperation.expression());
    walker.exit(unaryOperation);
  }

  @Override
  public final void visit(Variable variable) {
    walker.enter(variable);
    visit(variable.type());
    visit(variable.identifier());
    variable.valueExpression().ifPresent(this::visit);
    walker.exit(variable);
  }

  @Override
  public final void visit(While whileStatement) {
    walker.enter(whileStatement);
    visit(whileStatement.body());
    walker.exit(whileStatement);
  }

  // The following methods are needed to satisfy the compiler, but are never actually invoked.

  private final void visit(Literal literal) {
    switch (literal) {
      case BooleanLiteral b -> visit(b);
      case FloatLiteral f -> visit(f);
      case IntegerLiteral i -> visit(i);
      case StringLiteral s -> visit(s);
      case ObjectLiteral o -> visit(o);
      default -> throw new IllegalArgumentException(
          String.format(
              "ScriptWalker::visit passed an unsupported Literal: %s",
              literal.getClass().getName()));
    }
  }

  private final void visit(Declaration declaration) {
    switch (declaration) {
      case Event e -> visit(e);
      case Function f -> visit(f);
      case Import i -> visit(i);
      case Property p -> visit(p);
      case ScriptVariable v -> visit(v);
      case State s -> visit(s);
      default -> throw new IllegalArgumentException(
          String.format(
              "ScriptWalker::visit passed an unsupported Declaration: %s",
              declaration.getClass().getName()));
    }
  }

  private final void visit(Expression expression) {
    switch (expression) {
      case ArrayAccess a -> visit(a);
      case ArrayInitialization i -> visit(i);
      case ArrayLength l -> visit(l);
      case BinaryOperation b -> visit(b);
      case Cast c -> visit(c);
      case DotAccess d -> visit(d);
      case FunctionCall f -> visit(f);
      case Identifier i -> visit(i);
      case Literal l -> visit(l);
      case Parenthetical p -> visit(p);
      case UnaryOperation u -> visit(u);
      default -> throw new IllegalArgumentException(
          String.format(
              "ScriptWalker::visit passed an unsupported Expression: %s",
              expression.getClass().getName()));
    }
  }

  private final void visit(Invokable invokable) {
    switch (invokable) {
      case Event e -> visit(e);
      case Function f -> visit(f);
      default -> throw new IllegalArgumentException(
          String.format(
              "ScriptWalker::visit passed an unsupported Invokable: %s",
              invokable.getClass().getName()));
    }
  }

  private final void visit(Statement statement) {
    switch (statement) {
      case Assignment a -> visit(a);
      case Expression e -> visit(e);
      case If i -> visit(i);
      case Return r -> visit(r);
      case Variable v -> visit(v);
      case While w -> visit(w);
      default -> throw new IllegalArgumentException(
          String.format(
              "ScriptWalker::visit passed an unsupported Statement: %s",
              statement.getClass().getName()));
    }
  }

  /**
   * Class that has its methods invoked by a {@link WalkingVisitor} as it walks through a {@link
   * Script}.
   *
   * <p>For {@link Construct Constructs} that are composed of other {@code Constructs}, an {@code
   * enter} and {@code exit} method exist; these are invoked before and after the composed {@code
   * Constructs} are visited respectively.
   *
   * <p>For {@code Constructs} that are <i>not</i> composed of any other {@code Constructs}, a
   * {@code touch} method exists; this is called whenever that {@code Construct} is encountered.
   */
  public abstract static class Walker {
    /** Performs some operation(s) when an {@link ArrayAccess} is first encountered. */
    @ForOverride
    protected void enter(ArrayAccess arrayAccess) {}

    /** Performs some operation(s) when an {@link ArrayInitialization} is first encountered. */
    @ForOverride
    protected void enter(ArrayInitialization arrayInitialization) {}

    /** Performs some operation(s) when an {@link ArrayLength} is first encountered. */
    @ForOverride
    protected void enter(ArrayLength arrayLength) {}

    /** Performs some operation(s) when an {@link Assignment} is first encountered. */
    @ForOverride
    protected void enter(Assignment assignment) {}

    /** Performs some operation(s) when a {@link BinaryOperation} is first encountered. */
    @ForOverride
    protected void enter(BinaryOperation binaryOperation) {}

    /** Performs some operation(s) when a {@link Block} is first encountered. */
    @ForOverride
    protected void enter(Block block) {}

    /** Performs some operation(s) when a {@link CallParameter} is first encountered. */
    @ForOverride
    protected void enter(CallParameter callParameter) {}

    /** Performs some operation(s) when a {@link Cast} is first encountered. */
    @ForOverride
    protected void enter(Cast cast) {}

    /** Performs some operation(s) when a {@link DotAccess} is first encountered. */
    @ForOverride
    protected void enter(DotAccess dotAccess) {}

    /** Performs some operation(s) when a {@link Event} is first encountered. */
    @ForOverride
    protected void enter(Event event) {}

    /** Performs some operation(s) when a {@link Function} is first encountered. */
    @ForOverride
    protected void enter(Function function) {}

    /** Performs some operation(s) when a {@link FunctionCall} is first encountered. */
    @ForOverride
    protected void enter(FunctionCall functionCall) {}

    /** Performs some operation(s) when a {@link Header} is first encountered. */
    @ForOverride
    protected void enter(Header header) {}

    /** Performs some operation(s) when an {@link If} is first encountered. */
    @ForOverride
    protected void enter(If ifStatement) {}

    /** Performs some operation(s) when an {@link Import} is first encountered. */
    @ForOverride
    protected void enter(Import importDeclaration) {}

    /** Performs some operation(s) when a {@link Parameter} is first encountered. */
    @ForOverride
    protected void enter(Parameter parameter) {}

    /** Performs some operation(s) when a {@link Parenthetical} is first encountered. */
    @ForOverride
    protected void enter(Parenthetical parenthetical) {}

    /** Performs some operation(s) when a {@link Property} is first encountered. */
    @ForOverride
    protected void enter(Property property) {}

    /** Performs some operation(s) when a {@link Return} is first encountered. */
    @ForOverride
    protected void enter(Return returnStatement) {}

    /** Performs some operation(s) when a {@link Script} is first encountered. */
    @ForOverride
    protected void enter(Script script) {}

    /** Performs some operation(s) when a {@link ScriptVariable} is first encountered. */
    @ForOverride
    protected void enter(ScriptVariable scriptVariable) {}

    /** Performs some operation(s) when a {@link State} is first encountered. */
    @ForOverride
    protected void enter(State state) {}

    /** Performs some operation(s) when a {@link Type} is first encountered. */
    @ForOverride
    protected void enter(Type type) {}

    /** Performs some operation(s) when a {@link UnaryOperation} is first encountered. */
    @ForOverride
    protected void enter(UnaryOperation unaryOperation) {}

    /** Performs some operation(s) when a {@link Variable} is first encountered. */
    @ForOverride
    protected void enter(Variable variable) {}

    /** Performs some operation(s) when a {@link While} is first encountered. */
    @ForOverride
    protected void enter(While whileStatement) {}

    /**
     * Performs some operation(s) when all sub-components of an {@link ArrayAccess} have been
     * visited.
     */
    @ForOverride
    protected void exit(ArrayAccess arrayAccess) {}

    /**
     * Performs some operation(s) when all sub-components of an {@link ArrayInitialization} have
     * been visited.
     */
    @ForOverride
    protected void exit(ArrayInitialization arrayInitialization) {}

    /**
     * Performs some operation(s) when all sub-components of an {@link ArrayLength} have been
     * visited.
     */
    @ForOverride
    protected void exit(ArrayLength arrayLength) {}

    /**
     * Performs some operation(s) when all sub-components of an {@link Assignment} have been
     * visited.
     */
    @ForOverride
    protected void exit(Assignment assignment) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link BinaryOperation} have been
     * visited.
     */
    @ForOverride
    protected void exit(BinaryOperation binaryOperation) {}

    /** Performs some operation(s) when all sub-components of a {@link Block} have been visited. */
    @ForOverride
    protected void exit(Block block) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link CallParameter} have been
     * visited.
     */
    @ForOverride
    protected void exit(CallParameter callParameter) {}

    /** Performs some operation(s) when all sub-components of a {@link Cast} have been visited. */
    @ForOverride
    protected void exit(Cast cast) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link DotAccess} have been visited.
     */
    @ForOverride
    protected void exit(DotAccess dotAccess) {}

    /** Performs some operation(s) when all sub-components of a {@link Event} have been visited. */
    @ForOverride
    protected void exit(Event event) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link Function} have been visited.
     */
    @ForOverride
    protected void exit(Function function) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link FunctionCall} have been
     * visited.
     */
    @ForOverride
    protected void exit(FunctionCall functionCall) {}

    /** Performs some operation(s) when all sub-components of a {@link Header} have been visited. */
    @ForOverride
    protected void exit(Header header) {}

    /** Performs some operation(s) when all sub-components of an {@link If} have been visited. */
    @ForOverride
    protected void exit(If ifStatement) {}

    /**
     * Performs some operation(s) when all sub-components of an {@link Import} have been visited.
     */
    @ForOverride
    protected void exit(Import importDeclaration) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link Parameter} have been visited.
     */
    @ForOverride
    protected void exit(Parameter parameter) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link Parenthetical} have been
     * visited.
     */
    @ForOverride
    protected void exit(Parenthetical parenthetical) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link Property} have been visited.
     */
    @ForOverride
    protected void exit(Property property) {}

    /** Performs some operation(s) when all sub-components of a {@link Return} have been visited. */
    @ForOverride
    protected void exit(Return returnStatement) {}

    /** Performs some operation(s) when all sub-components of a {@link Script} have been visited. */
    @ForOverride
    protected void exit(Script script) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link ScriptVariable} have been
     * visited.
     */
    @ForOverride
    protected void exit(ScriptVariable scriptVariable) {}

    /** Performs some operation(s) when all sub-components of a {@link State} have been visited. */
    @ForOverride
    protected void exit(State state) {}

    /** Performs some operation(s) when all sub-components of a {@link Type} have been visited. */
    @ForOverride
    protected void exit(Type type) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link UnaryOperation} have been
     * visited.
     */
    @ForOverride
    protected void exit(UnaryOperation unaryOperation) {}

    /**
     * Performs some operation(s) when all sub-components of a {@link Variable} have been visited.
     */
    @ForOverride
    protected void exit(Variable variable) {}

    /** Performs some operation(s) when all sub-components of a {@link While} have been visited. */
    @ForOverride
    protected void exit(While whileStatement) {}

    /** Performs some operation(s) when a {@link BooleanLiteral} is encountered. */
    @ForOverride
    protected void touch(BooleanLiteral booleanLiteral) {}

    /** Performs some operation(s) when a {@link FloatLiteral} is encountered. */
    @ForOverride
    protected void touch(FloatLiteral floatLiteral) {}

    /** Performs some operation(s) when an {@link Identifier} is encountered. */
    @ForOverride
    protected void touch(Identifier identifier) {}

    /** Performs some operation(s) when an {@link IntegerLiteral} is encountered. */
    @ForOverride
    protected void touch(IntegerLiteral integerLiteral) {}

    /** Performs some operation(s) when an {@link ObjectLiteral} is encountered. */
    @ForOverride
    protected void touch(ObjectLiteral objectLiteral) {}

    /** Performs some operation(s) when a {@link StringLiteral} is encountered. */
    @ForOverride
    protected void touch(StringLiteral stringLiteral) {}
  }
}
