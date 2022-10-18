package org.nullable.papyrology.ast;

/** A visitor of the Papyrus Script AST. */
public interface Visitor {

  /** Visit an {@link ArrayAccess}. */
  void visit(ArrayAccess arrayAccess);

  /** Visit an {@link ArrayInitialization}. */
  void visit(ArrayInitialization arrayInitialization);

  /** Visit an {@link ArrayLength}. */
  void visit(ArrayLength arrayLength);

  /** Visit an {@link Assignment}. */
  void visit(Assignment assignment);

  /** Visit a {@link BinaryOperation}. */
  void visit(BinaryOperation binaryOperation);

  /** Visit a {@link Block}. */
  void visit(Block block);

  /** Visit a {@link BooleanLiteral}. */
  void visit(BooleanLiteral booleanLiteral);

  /** Visit a {@link CallParameter}. */
  void visit(CallParameter callParameter);

  /** Visit a {@link Cast}. */
  void visit(Cast cast);

  /** Visit a {@link DotAccess}. */
  void visit(DotAccess dotAccess);

  /** Visit an {@link Event}. */
  void visit(Event event);

  /** Visit a {@link FloatLiteral}. */
  void visit(FloatLiteral floatLiteral);

  /** Visit a {@link Function}. */
  void visit(Function function);

  /** Visit a {@link FunctionCall}. */
  void visit(FunctionCall functionCall);

  /** Visit a {@link Header}. */
  void visit(Header header);

  /** Visit an {@link Identifier}. */
  void visit(Identifier identifier);

  /** Visit an {@link If}. */
  void visit(If ifStatement);

  /** Visit an {@link Import}. */
  void visit(Import importDeclaration);

  /** Visit an {@link IntegerLiteral}. */
  void visit(IntegerLiteral integerLiteral);

  /** Visit an {@link ObjectLiteral}. */
  void visit(ObjectLiteral objectLiteral);

  /** Visit a {@link Parameter}. */
  void visit(Parameter parameter);

  /** Visit a {@link Parenthetical}. */
  void visit(Parenthetical parenthetical);

  /** Visit a {@link Property}. */
  void visit(Property property);

  /** Visit a {@link Return}. */
  void visit(Return returnStatement);

  /** Visit a {@link Script}. */
  void visit(Script script);

  /** Visit a {@link ScriptVariable}. */
  void visit(ScriptVariable scriptVariable);

  /** Visit a {@link State}. */
  void visit(State state);

  /** Visit a {@link StringLiteral}. */
  void visit(StringLiteral stringLiteral);

  /** Visit a {@link Type}. */
  void visit(Type type);

  /** Visit a {@link UnaryOperation}. */
  void visit(UnaryOperation unaryOperation);

  /** Visit a {@link Variable}. */
  void visit(Variable variable);

  /** Visit a {@link While}. */
  void visit(While whileStatement);
}
