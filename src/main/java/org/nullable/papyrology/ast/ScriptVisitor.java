package org.nullable.papyrology.ast;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Locale;
import org.antlr.v4.runtime.Token;
import org.nullable.papyrology.ast.node.BooleanLiteral;
import org.nullable.papyrology.ast.node.DataType;
import org.nullable.papyrology.ast.node.Declaration;
import org.nullable.papyrology.ast.node.Event;
import org.nullable.papyrology.ast.node.FloatLiteral;
import org.nullable.papyrology.ast.node.Function;
import org.nullable.papyrology.ast.node.Header;
import org.nullable.papyrology.ast.node.Identifier;
import org.nullable.papyrology.ast.node.Import;
import org.nullable.papyrology.ast.node.IntegerLiteral;
import org.nullable.papyrology.ast.node.Literal;
import org.nullable.papyrology.ast.node.ObjectLiteral;
import org.nullable.papyrology.ast.node.Parameter;
import org.nullable.papyrology.ast.node.Property;
import org.nullable.papyrology.ast.node.Script;
import org.nullable.papyrology.ast.node.ScriptVariable;
import org.nullable.papyrology.ast.node.State;
import org.nullable.papyrology.ast.node.Statement;
import org.nullable.papyrology.ast.node.StringLiteral;
import org.nullable.papyrology.ast.node.Type;
import org.nullable.papyrology.grammar.PapyrusBaseVisitor;
import org.nullable.papyrology.grammar.PapyrusParser.AutoPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.AutoReadOnlyPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.EventContext;
import org.nullable.papyrology.grammar.PapyrusParser.EventDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.FullPropertyContext;
import org.nullable.papyrology.grammar.PapyrusParser.FunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.FunctionDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.GetPropertyFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.HeaderContext;
import org.nullable.papyrology.grammar.PapyrusParser.ImportDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.LiteralContext;
import org.nullable.papyrology.grammar.PapyrusParser.NativeEventContext;
import org.nullable.papyrology.grammar.PapyrusParser.NativeFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.ParameterContext;
import org.nullable.papyrology.grammar.PapyrusParser.ParametersContext;
import org.nullable.papyrology.grammar.PapyrusParser.PropertyDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.PropertyFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.ScriptContext;
import org.nullable.papyrology.grammar.PapyrusParser.ScriptLineContext;
import org.nullable.papyrology.grammar.PapyrusParser.SetPropertyFunctionContext;
import org.nullable.papyrology.grammar.PapyrusParser.StateDeclarationContext;
import org.nullable.papyrology.grammar.PapyrusParser.StatementBlockContext;
import org.nullable.papyrology.grammar.PapyrusParser.TypeContext;
import org.nullable.papyrology.grammar.PapyrusParser.VariableDeclarationContext;

public final class ScriptVisitor extends PapyrusBaseVisitor<Script> {

  @Override
  public Script visitScript(ScriptContext ctx) {
    return Script.builder()
        .setHeader(create(ctx.header()))
        .setDeclarations(create(ctx.scriptLine()))
        .build();
  }

  private static Header create(HeaderContext ctx) {
    Header.Builder header =
        Header.builder()
            .setScriptIdentifier(create(ctx.id))
            .setHidden(!ctx.F_HIDDEN().isEmpty())
            .setConditional(!ctx.F_CONDITIONAL().isEmpty());
    if (ctx.parent != null) {
      header.setParentScriptIdentifier(create(ctx.parent));
    }
    if (ctx.docComment() != null) {
      header.setScriptComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return header.build();
  }

  private static ImmutableList<Declaration> create(List<ScriptLineContext> ctxs) {
    ImmutableList.Builder<Declaration> declarations = ImmutableList.builder();
    for (ScriptLineContext ctx : ctxs) {
      if (ctx.propertyDeclaration() != null) {
        declarations.add(create(ctx.propertyDeclaration()));
      } else if (ctx.functionDeclaration() != null) {
        declarations.add(create(ctx.functionDeclaration()));
      } else if (ctx.eventDeclaration() != null) {
        declarations.add(create(ctx.eventDeclaration()));
      } else if (ctx.stateDeclaration() != null) {
        declarations.add(create(ctx.stateDeclaration()));
      } else if (ctx.variableDeclaration() != null) {
        declarations.add(create(ctx.variableDeclaration()));
      } else if (ctx.importDeclaration() != null) {
        declarations.add(create(ctx.importDeclaration()));
      }
    }
    return declarations.build();
  }

  private static Property create(PropertyDeclarationContext ctx) {
    if (ctx instanceof FullPropertyContext) {
      return create((FullPropertyContext) ctx);
    } else if (ctx instanceof AutoPropertyContext) {
      return create((AutoPropertyContext) ctx);
    } else if (ctx instanceof AutoReadOnlyPropertyContext) {
      return create((AutoReadOnlyPropertyContext) ctx);
    } else {
      throw new AssertionError();
    }
  }

  private static Property create(FullPropertyContext ctx) {
    Property.Builder property =
        Property.builder()
            .setAuto(false)
            .setAutoReadOnly(false)
            .setType(create(ctx.type()))
            .setIdentifier(create(ctx.ID().getSymbol()))
            .setHidden(ctx.F_HIDDEN() != null);
    if (ctx.docComment() != null) {
      property.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    boolean getPresent = false;
    boolean setPresent = false;
    for (PropertyFunctionContext funcCtx : ctx.propertyFunction()) {
      if (funcCtx instanceof GetPropertyFunctionContext) {
        checkState(!getPresent, "Cannot specify Get function for full property twice");
        property.setGetFunction(create((GetPropertyFunctionContext) funcCtx));
        getPresent = true;
      } else if (funcCtx instanceof SetPropertyFunctionContext) {
        checkState(!setPresent, "Cannot specify Set function for full property twice");
        property.setSetFunction(create((SetPropertyFunctionContext) funcCtx));
        setPresent = true;
      } else {
        throw new AssertionError();
      }
    }
    return property.build();
  }

  private static Function create(GetPropertyFunctionContext ctx) {
    Identifier identifier = create(ctx.ID().getSymbol());
    checkState(
        identifier.isEquivalent(GET_IDENTIFIER),
        "Accessor Property function must be named \"Get\"");
    Function.Builder function =
        Function.builder()
            .setReturnType(create(ctx.type()))
            .setIdentifier(identifier)
            .setParameters(ImmutableList.of())
            .setBodyStatements(create(ctx.statementBlock()))
            .setGlobal(false)
            .setNative(false);
    if (ctx.docComment() != null) {
      function.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return function.build();
  }

  private static final Identifier GET_IDENTIFIER = Identifier.builder().setValue("Get").build();

  private static Function create(SetPropertyFunctionContext ctx) {
    Identifier identifier = create(ctx.ID().getSymbol());
    checkState(
        identifier.isEquivalent(SET_IDENTIFIER), "Mutator Property function must be named \"Set\"");
    Function.Builder function =
        Function.builder()
            .setIdentifier(identifier)
            .setParameters(ImmutableList.of(create(ctx.parameter())))
            .setBodyStatements(create(ctx.statementBlock()))
            .setGlobal(false)
            .setNative(false);
    if (ctx.docComment() != null) {
      function.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return function.build();
  }

  private static final Identifier SET_IDENTIFIER = Identifier.builder().setValue("Set").build();

  private static Property create(AutoPropertyContext ctx) {
    Property.Builder property =
        Property.builder()
            .setAuto(true)
            .setAutoReadOnly(false)
            .setType(create(ctx.type()))
            .setIdentifier(create(ctx.ID().getSymbol()))
            .setHidden(!ctx.F_HIDDEN().isEmpty())
            .setConditional(!ctx.F_CONDITIONAL().isEmpty());
    if (ctx.docComment() != null) {
      property.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    if (ctx.literal() != null) {
      property.setDefaultValueLiteral(create(ctx.literal()));
    }
    return property.build();
  }

  private static Property create(AutoReadOnlyPropertyContext ctx) {
    Property.Builder property =
        Property.builder()
            .setAuto(false)
            .setAutoReadOnly(true)
            .setType(create(ctx.type()))
            .setIdentifier(create(ctx.ID().getSymbol()))
            .setDefaultValueLiteral(create(ctx.literal()))
            .setHidden(ctx.F_HIDDEN() != null);
    if (ctx.docComment() != null) {
      property.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return property.build();
  }

  public static Function create(FunctionDeclarationContext ctx) {
    if (ctx instanceof FunctionContext) {
      return create((FunctionContext) ctx);
    } else if (ctx instanceof NativeFunctionContext) {
      return create((NativeFunctionContext) ctx);
    } else {
      throw new AssertionError();
    }
  }

  public static Function create(FunctionContext ctx) {
    Function.Builder function =
        Function.builder()
            .setIdentifier(create(ctx.ID().getSymbol()))
            .setParameters(create(ctx.parameters()))
            .setBodyStatements(create(ctx.statementBlock()))
            .setGlobal(ctx.K_GLOBAL() != null)
            .setNative(false);
    if (ctx.docComment() != null) {
      function.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    if (ctx.type() != null) {
      function.setReturnType(create(ctx.type()));
    }
    return function.build();
  }

  public static Function create(NativeFunctionContext ctx) {
    Function.Builder function =
        Function.builder()
            .setIdentifier(create(ctx.ID().getSymbol()))
            .setParameters(create(ctx.parameters()))
            .setGlobal(ctx.K_GLOBAL() != null)
            .setNative(true);
    if (ctx.docComment() != null) {
      function.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    if (ctx.type() != null) {
      function.setReturnType(create(ctx.type()));
    }
    return function.build();
  }

  private static Event create(EventDeclarationContext ctx) {
    if (ctx instanceof EventContext) {
      return create((EventContext) ctx);
    } else if (ctx instanceof NativeEventContext) {
      return create((NativeEventContext) ctx);
    } else {
      throw new AssertionError();
    }
  }

  public static Event create(EventContext ctx) {
    Event.Builder event =
        Event.builder()
            .setIdentifier(create(ctx.ID().getSymbol()))
            .setParameters(create(ctx.parameters()))
            .setBodyStatements(create(ctx.statementBlock()))
            .setNative(false);
    if (ctx.docComment() != null) {
      event.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return event.build();
  }

  public static Event create(NativeEventContext ctx) {
    Event.Builder event =
        Event.builder()
            .setIdentifier(create(ctx.ID().getSymbol()))
            .setParameters(create(ctx.parameters()))
            .setNative(true);
    if (ctx.docComment() != null) {
      event.setComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return event.build();
  }

  private static State create(StateDeclarationContext ctx) {
    return null;
  }

  private static ScriptVariable create(VariableDeclarationContext ctx) {
    return null;
  }

  private static Import create(ImportDeclarationContext ctx) {
    return null;
  }

  private static ImmutableList<Parameter> create(ParametersContext ctx) {
    if (ctx.parameter() == null) {
      return ImmutableList.of();
    }
    return ctx.parameter().stream().map(ScriptVisitor::create).collect(toImmutableList());
  }

  private static Parameter create(ParameterContext ctx) {
    Parameter.Builder parameter =
        Parameter.builder().setType(create(ctx.type())).setIdentifier(create(ctx.ID().getSymbol()));
    if (ctx.literal() != null) {
      parameter.setDefaultValueLiteral(create(ctx.literal()));
    }
    return parameter.build();
  }

  private static ImmutableList<Statement> create(StatementBlockContext ctx) {
    return ImmutableList.of(); // TODO
  }

  private static Literal create(LiteralContext ctx) {
    if (ctx.K_TRUE() != null) {
      return BooleanLiteral.builder().setRawValue(ctx.K_TRUE().getText()).setValue(true).build();
    } else if (ctx.K_FALSE() != null) {
      return BooleanLiteral.builder().setRawValue(ctx.K_FALSE().getText()).setValue(false).build();
    } else if (ctx.L_FLOAT() != null) {
      return FloatLiteral.builder()
          .setRawValue(ctx.L_FLOAT().getText())
          .setValue(Float.valueOf(ctx.L_FLOAT().getText()))
          .build();
    } else if (ctx.L_UINT() != null) {
      return IntegerLiteral.builder()
          .setRawValue(ctx.L_UINT().getText())
          .setValue(parseInteger(ctx.L_UINT().getText()))
          .build();
    } else if (ctx.L_INT() != null) {
      return IntegerLiteral.builder()
          .setRawValue(ctx.L_INT().getText())
          .setValue(parseInteger(ctx.L_INT().getText()))
          .build();
    } else if (ctx.L_STRING() != null) {
      return StringLiteral.builder().setValue(ctx.L_STRING().getText()).build();
    } else if (ctx.K_NONE() != null) {
      return ObjectLiteral.builder().setValue(ObjectLiteral.Reference.NONE).build();
    } else if (ctx.K_SELF() != null) {
      return ObjectLiteral.builder().setValue(ObjectLiteral.Reference.SELF).build();
    } else if (ctx.K_PARENT() != null) {
      return ObjectLiteral.builder().setValue(ObjectLiteral.Reference.PARENT).build();
    } else {
      throw new AssertionError();
    }
  }

  private static int parseInteger(String raw) {
    String lower = raw.toLowerCase(Locale.US);
    if (lower.contains("0x")) {
      return Integer.parseInt(lower.replace("0x", ""), 16);
    }
    return Integer.parseInt(lower);
  }

  private static Type create(TypeContext ctx) {
    Type.Builder type = Type.builder();
    boolean isArray = ctx.S_LBRAKET() != null;
    if (ctx.K_BOOL() != null) {
      type.setDataType(isArray ? DataType.BOOL_ARRAY : DataType.BOOL);
    }
    if (ctx.K_INT() != null) {
      type.setDataType(isArray ? DataType.INT_ARRAY : DataType.INT);
    }
    if (ctx.K_FLOAT() != null) {
      type.setDataType(isArray ? DataType.FLOAT_ARRAY : DataType.FLOAT);
    }
    if (ctx.K_STRING() != null) {
      type.setDataType(isArray ? DataType.STRING_ARRAY : DataType.STRING);
    }
    if (ctx.ID() != null) {
      type.setDataType(isArray ? DataType.OBJECT_ARRAY : DataType.OBJECT)
          .setIdentifier(create(ctx.ID().getSymbol()));
    }
    return type.build();
  }

  private static Identifier create(Token token) {
    return Identifier.builder().setValue(token.getText()).build();
  }
}
