package org.nullable.papyrology.ast;

/** A visitor of the Papyrus Script AST. */
public interface Visitor<T> {

  /** Visit an {@link ArrayAccess}. */
  T visit(ArrayAccess arrayAccess);

  /** Visit an {@link ArrayInitialization}. */
  T visit(ArrayInitialization arrayInitialization);

  /** Visit an {@link ArrayLength}. */
  T visit(ArrayLength arrayLength);

  /** Visit an {@link Assignment}. */
  T visit(Assignment assignment);

  /** Visit a {@link BinaryOperation}. */
  T visit(BinaryOperation binaryOperation);

  /** Visit a {@link BooleanLiteral}. */
  T visit(BooleanLiteral booleanLiteral);

  /** Visit a {@link CallParameter}. */
  T visit(CallParameter callParameter);

  /** Visit a {@link Cast}. */
  T visit(Cast cast);

  /** Visit a {@link DotAccess}. */
  T visit(DotAccess dotAccess);

  /** Visit an {@link Event}. */
  T visit(Event event);

  /** Visit a {@link FloatLiteral}. */
  T visit(FloatLiteral floatLiteral);

  /** Visit a {@link Function}. */
  T visit(Function function);

  /** Visit a {@link FunctionCall}. */
  T visit(FunctionCall functionCall);

  /** Visit a {@link Header}. */
  T visit(Header header);

  /** Visit an {@link Identifier}. */
  T visit(Identifier identifier);

  /** Visit an {@link If}. */
  T visit(If ifConstruct);

  /** Visit an {@link Import}. */
  T visit(Import importConstruct);

  /** Visit an {@link IntegerLiteral}. */
  T visit(IntegerLiteral integerLiteral);

  /** Visit an {@link ObjectLiteral}. */
  T visit(ObjectLiteral objectLiteral);

  /** Visit a {@link Parameter}. */
  T visit(Parameter parameter);

  /** Visit a {@link Parenthetical}. */
  T visit(Parenthetical parenthetical);

  /** Visit a {@link Property}. */
  T visit(Property property);

  /** Visit a {@link Return}. */
  T visit(Return returnConstruct);

  /** Visit a {@link Script}. */
  T visit(Script script);

  /** Visit a {@link ScriptVariable}. */
  T visit(ScriptVariable scriptVariable);

  /** Visit a {@link State}. */
  T visit(State state);

  /** Visit a {@link StringLiteral}. */
  T visit(StringLiteral stringLiteral);

  /** Visit a {@link Type}. */
  T visit(Type type);

  /** Visit a {@link UnaryOperation}. */
  T visit(UnaryOperation unaryOperation);

  /** Visit a {@link Variable}. */
  T visit(Variable variable);

  /** Visit a {@link While}. */
  T visit(While whileConstruct);
}
