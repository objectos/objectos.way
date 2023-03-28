/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.code;

import java.lang.annotation.Annotation;
import java.util.Objects;
import objectos.code.internal.ByteProto;
import objectos.code.internal.ClassTypeNameImpl;
import objectos.code.internal.External;
import objectos.code.internal.InternalApi;
import objectos.code.internal.InternalJavaTemplate;
import objectos.code.internal.JavaModel;
import objectos.code.internal.Keyword;
import objectos.code.internal.ModifierImpl;
import objectos.code.internal.NewLineImpl;
import objectos.code.internal.ParameterInstructionImpl;
import objectos.code.internal.Symbol;
import objectos.code.tmpl.AnnotationInstruction;
import objectos.code.tmpl.ArgsPart;
import objectos.code.tmpl.Argument;
import objectos.code.tmpl.ArrayTypeComponent;
import objectos.code.tmpl.BlockInstruction;
import objectos.code.tmpl.BodyElement;
import objectos.code.tmpl.ClassDeclarationInstruction;
import objectos.code.tmpl.ClassOrInterfaceDeclarationInstruction;
import objectos.code.tmpl.ClassOrParameterizedTypeName;
import objectos.code.tmpl.ConstructorDeclarationInstruction;
import objectos.code.tmpl.DeclarationName;
import objectos.code.tmpl.EnumConstantInstruction;
import objectos.code.tmpl.EnumDeclarationInstruction;
import objectos.code.tmpl.ExecutableDeclarationInstruction;
import objectos.code.tmpl.ExpressionPart;
import objectos.code.tmpl.FieldDeclarationInstruction;
import objectos.code.tmpl.ImplementsClause;
import objectos.code.tmpl.ImplementsClauseInstruction;
import objectos.code.tmpl.Include;
import objectos.code.tmpl.IncludeTarget;
import objectos.code.tmpl.Instruction;
import objectos.code.tmpl.InterfaceDeclarationInstruction;
import objectos.code.tmpl.MethodDeclarationInstruction;
import objectos.code.tmpl.Modifier;
import objectos.code.tmpl.NewLine;
import objectos.code.tmpl.ParameterElement;
import objectos.code.tmpl.ParameterInstruction;
import objectos.code.tmpl.StatementPart;
import objectos.code.tmpl.TypeDeclarationInstruction;
import objectos.code.tmpl.TypeName;
import objectos.code.tmpl.TypeParameter;
import objectos.code.tmpl.VariableInitializer;
import objectos.lang.Check;

/**
 * The {@code JavaTemplate} class provides a pure Java API for generating Java
 * source code.
 *
 * @since 0.4
 */
@SuppressWarnings("deprecation")
public non-sealed abstract class JavaTemplate extends InternalJavaTemplate {

  public enum _Ext {
    INSTANCE;
  }

  public enum _Include implements Include {
    INSTANCE;
  }

  public enum _Item
      implements
      AbstractModifier,
      AnyDeclarationInstruction,
      Argument,
      ArrayAccess,
      ArrayDimension,
      ArrayInitializer,
      ArrayInitializerValue,
      ArrayType,
      At,
      AutoImports,
      Block,
      Body,
      ClassDeclaration,
      ClassInstanceCreationExpression,
      ClassKeyword,
      ClassOrInterfaceDeclarationInstruction,
      ClassTypeInstruction,
      ClassTypeWithArgs,
      ConstructorDeclaration,
      ConstructorDeclarationInstruction,
      DeclarationName,
      OldEllipsis,
      OldElseKeyword,
      End,
      EnumConstant,
      EnumDeclaration,
      EnumKeyword,
      OldEqualityOperator,
      ExplicitConstructorInvocation,
      ExpressionName,
      ExtendsKeyword,
      FieldDeclaration,
      FieldDeclarationInstruction,
      OldFinalModifier,
      Identifier,
      IfCondition,
      ImplementsClause,
      ImplementsKeyword,
      IntegerLiteral,
      InterfaceDeclaration,
      InterfaceKeyword,
      MethodDeclaration,
      MethodDeclarationInstruction,
      MethodDeclarator,
      MethodInvocation,
      OldNewKeyword,
      OldNewLine,
      OldNullLiteral,
      OldClassTypeInstruction,
      PackageKeyword,
      ExecutableDeclarationInstruction,
      ParameterInstruction,
      ParameterizedType,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      OldReturnKeyword,
      OldSimpleAssigmentOperator,
      OldStatement,
      StaticModifier,
      StringLiteral,
      OldSuperKeyword,
      OldThisKeyword,
      OldThrowKeyword,
      TypeDeclarationInstruction,
      TypeParameter,
      TypeParameterOld,
      TypeVariable,
      OldVarKeyword,
      OldVoidKeyword {

    INSTANCE;

  }

  interface AbstractModifier extends BodyElement {}

  /**
   * @since 0.4.4
   */
  interface AnyDeclarationInstruction
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      FieldDeclarationInstruction,
      InterfaceDeclarationInstruction,
      MethodDeclarationInstruction {}

  interface AnyType extends BodyElement, BlockInstruction, ParameterElement {}

  interface ArrayAccess extends ExpressionPart {}

  interface ArrayDimension extends ArrayTypeElement {}

  interface ArrayInitializer extends BodyElement, VariableInitializer {}

  interface ArrayInitializerValue extends AnnotationInstruction, VariableInitializer {}

  interface ArrayType extends BodyElement, ReferenceType {}

  interface ArrayTypeElement {}

  interface At extends BodyElement {}

  interface AtElement extends Instruction {}

  interface AutoImports extends Instruction {}

  interface Block extends BlockInstruction, BodyElement, StatementPart {}

  interface Body extends BodyElement {}

  /**
   * @since 0.4.4
   */
  interface ClassDeclaration extends ClassDeclarationInstruction {}

  interface ClassInstanceCreationExpression extends PrimaryNoNewArray {}

  interface ClassKeyword extends BodyElement {}

  interface ClassTypeInstruction extends MethodDeclarationInstruction {}

  interface ClassTypeWithArgs extends ClassTypeInstruction {}

  interface ConstructorDeclaration
      extends
      BodyElement,
      ClassDeclarationInstruction,
      EnumDeclarationInstruction {}

  interface End extends ArgsPart, BlockInstruction {}

  interface EnumConstant extends BodyElement, EnumDeclarationInstruction {}

  /**
   * @since 0.4.4
   */
  interface EnumDeclaration extends ClassDeclarationInstruction {}

  @Deprecated
  interface EnumKeyword extends BodyElement {}

  interface ExplicitConstructorInvocation extends BlockInstruction {}

  interface ExpressionName extends ExpressionPart {}

  @Deprecated
  interface ExtendsKeyword extends BodyElement {}

  interface FieldDeclaration
      extends
      BodyElement,
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      InterfaceDeclarationInstruction {}

  interface Identifier extends BlockInstruction, BodyElement, ParameterElement {}

  interface IfCondition extends BlockInstruction {}

  @Deprecated
  interface ImplementsKeyword extends BodyElement {}

  interface IntegerLiteral extends Literal {}

  /**
   * @since 0.4.4
   */
  interface InterfaceDeclaration extends ClassDeclarationInstruction {}

  @Deprecated
  interface InterfaceKeyword extends BodyElement {}

  interface Literal extends AtElement, ExpressionPart {}

  interface MethodDeclaration
      extends
      BodyElement,
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      InterfaceDeclarationInstruction {}

  interface MethodDeclarator extends BodyElement {}

  interface MethodInvocation extends PrimaryNoNewArray {}

  @Deprecated
  interface OldClassOrParameterizedType extends Instruction {}

  @Deprecated
  interface OldClassTypeInstruction
      extends ArgsPart, OldClassOrParameterizedType, ReferenceType, TypeParameterBound {}

  @Deprecated
  interface OldEllipsis extends ParameterElement {}

  @Deprecated
  interface OldElseKeyword extends BlockInstruction {}

  @Deprecated
  interface OldEqualityOperator extends ExpressionPart {}

  @Deprecated
  interface OldFinalModifier extends BodyElement {}

  @Deprecated
  interface OldNewKeyword extends BlockInstruction {}

  @Deprecated
  interface OldNewLine extends ArgsPart, BlockInstruction {}

  @Deprecated
  interface OldNullLiteral extends ExpressionPart {}

  @Deprecated
  interface OldReturnKeyword extends BlockInstruction {}

  @Deprecated
  interface OldSimpleAssigmentOperator extends ExpressionPart {}

  interface OldStatement extends BlockInstruction {}

  @Deprecated
  interface OldSuperKeyword extends BlockInstruction {}

  @Deprecated
  interface OldThisKeyword extends PrimaryNoNewArray {}

  @Deprecated
  interface OldThrowKeyword extends BlockInstruction {}

  @Deprecated
  interface OldVarKeyword extends BlockInstruction {}

  @Deprecated
  interface OldVoidKeyword extends BodyElement {}

  @Deprecated
  interface PackageKeyword extends Instruction {}

  interface ParameterizedType extends OldClassOrParameterizedType, ReferenceType {}

  interface PrimitiveType extends AnyType, ArrayTypeComponent, BodyElement {}

  interface PrivateModifier extends AccessModifier {}

  interface ProtectedModifier extends AccessModifier {}

  interface PublicModifier extends AccessModifier {}

  interface ReferenceType extends AnyType, ArrayTypeComponent {}

  interface StaticModifier extends BodyElement {}

  interface StringLiteral extends Literal, PrimaryNoNewArray {}

  @Deprecated
  interface TypeParameterBound extends Instruction {}

  @Deprecated
  interface TypeParameterOld extends BodyElement {}

  interface TypeVariable extends ReferenceType {}

  private interface AccessModifier extends BodyElement {}

  /**
   * @since 0.4.3.1
   */
  private static final class AssignmentOperator extends External implements ExpressionPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.ASSIGNMENT_OPERATOR, Symbol.ASSIGNMENT.ordinal());
    }
  }

  /**
   * @since 0.4.3.1
   */
  private static final class ElseKeyword extends External implements StatementPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.ELSE, ByteProto.NOOP);
    }
  }

  /**
   * @since 0.4.3.1
   */
  private static final class EqualityOperator extends External implements ExpressionPart {
    private final int value;

    EqualityOperator(Symbol symbol) {
      value = symbol.ordinal();
    }

    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.EQUALITY_OPERATOR, value);
    }
  }

  /**
   * @since 0.4.3.1
   */
  private static final class IfKeyword extends External implements StatementPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.IF, ByteProto.NOOP);
    }
  }

  /**
   * @since 0.4.3.1
   */
  private static final class NewKeyword extends External implements ExpressionPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.NEW, ByteProto.NOOP);
    }
  }

  private static final class NullLiteral extends External implements ExpressionPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.NULL_LITERAL, ByteProto.NOOP);
    }
  }

  private interface Primary extends ExpressionPart {}

  private interface PrimaryNoNewArray extends Primary {}

  /**
   * @since 0.4.3.1
   */
  private static final class ReturnKeyword extends External implements StatementPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.RETURN, ByteProto.NOOP);
    }
  }

  /**
   * @since 0.4.4
   */
  private static final class SuperKeyword extends External implements ExpressionPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.SUPER, ByteProto.NOOP);
    }
  }

  /**
   * @since 0.4.3.1
   */
  private static final class ThisKeyword extends External implements ExpressionPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.THIS, ByteProto.NOOP);
    }
  }

  /**
   * @since 0.4.3.1
   */
  private static final class ThrowKeyword extends External implements StatementPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.THROW, ByteProto.NOOP);
    }
  }

  /**
   * @since 0.4.3.1
   */
  private static final class VarKeyword extends External implements StatementPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.VAR, ByteProto.NOOP);
    }
  }

  /**
   * @since 0.4.3.1
   */
  private static final class VoidKeyword extends External implements MethodDeclarationInstruction {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.VOID, ByteProto.NOOP);
    }
  }

  /**
   * The ellipsis instruction.
   *
   * @since 0.4.3.1
   */
  protected static final ParameterInstruction ELLIPSIS = ParameterInstructionImpl.ELLIPSIS;

  /**
   * The new line instruction.
   *
   * @since 0.4.3.1
   */
  protected static final NewLine NL = new NewLineImpl();

  /**
   * The {@code else} keyword. Use it as part of a `if-then-else` Java
   * statement.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * p(IF, arg(n("condition")), block(
   *   p(v("whenTrue"))
   * ), ELSE, block(
   *   p(v("whenFalse"))
   * ))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (condition) {
   *   whenTrue();
   * } else {
   *   whenFalse();
   * }</pre>
   *
   * @since 0.4.3.1
   */
  protected static final StatementPart ELSE = new ElseKeyword();

  /**
   * The {@code if} keyword. It is used to start a Java `if` statement.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * p(IF, arg(n("condition")), block(
   *   p(v("whenTrue"))
   * ))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (condition) {
   *   whenTrue();
   * }</pre>
   *
   * @since 0.4.3.1
   */
  protected static final StatementPart IF = new IfKeyword();

  /**
   * The {@code new} keyword.
   *
   * @since 0.4.3.1
   */
  protected static final ExpressionPart NEW = new NewKeyword();

  /**
   * The {@code return} keyword.
   *
   * @since 0.4.3.1
   */
  protected static final StatementPart RETURN = new ReturnKeyword();

  /**
   * The {@code super} keyword.
   *
   * @since 0.4.4
   */
  protected static final ExpressionPart SUPER = new SuperKeyword();

  /**
   * The {@code this} keyword.
   *
   * @since 0.4.3.1
   */
  protected static final ExpressionPart THIS = new ThisKeyword();

  /**
   * The {@code throw} keyword.
   *
   * @since 0.4.3.1
   */
  protected static final StatementPart THROW = new ThrowKeyword();

  /**
   * The {@code var} keyword.
   *
   * @since 0.4.3.1
   */
  protected static final StatementPart VAR = new VarKeyword();

  /**
   * The {@code void} keyword.
   *
   * @since 0.4.3.1
   */
  protected static final MethodDeclarationInstruction VOID = new VoidKeyword();

  /**
   * The {@code null} literal.
   *
   * @since 0.4.3.1
   */
  protected static final ExpressionPart NULL = new NullLiteral();

  /**
   * The {@code public} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier PUBLIC = new ModifierImpl(Keyword.PUBLIC);

  /**
   * The {@code protected} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier PROTECTED = new ModifierImpl(Keyword.PROTECTED);

  /**
   * The {@code private} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier PRIVATE = new ModifierImpl(Keyword.PRIVATE);

  /**
   * The {@code abstract} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier ABSTRACT = new ModifierImpl(Keyword.ABSTRACT);

  /**
   * The {@code static} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier STATIC = new ModifierImpl(Keyword.STATIC);

  /**
   * The {@code final} modifier.
   *
   * @since 0.4.2
   */
  protected static final objectos.code.tmpl.FinalModifier FINAL = new ModifierImpl(Keyword.FINAL);

  /**
   * The {@code sealed} modifier.
   *
   * @since 0.5.3
   */
  protected static final ClassOrInterfaceDeclarationInstruction SEALED
      = new ModifierImpl(Keyword.SEALED);

  /**
   * The {@code non-sealed} modifier.
   *
   * @since 0.5.3
   */
  protected static final ClassOrInterfaceDeclarationInstruction NON_SEALED
      = new ModifierImpl(Keyword.NON_SEALED);

  /**
   * The simple assignment operator {@code =}.
   *
   * <p>
   * The following Objectos Code example:
   *
   * <pre>
   * p(THIS, n("x"), IS, n("x"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * this.x = x;</pre>
   *
   * @since 0.4.3.1
   */
  protected static final ExpressionPart IS = new AssignmentOperator();

  /**
   * The {@code ==} (equal to) operator.
   *
   * @since 0.4.3.1
   */
  protected static final ExpressionPart EQ = new EqualityOperator(Symbol.EQUAL_TO);

  /**
   * The {@code !=} (not equal to) operator.
   *
   * @since 0.4.3.1
   */
  protected static final ExpressionPart NE = new EqualityOperator(Symbol.NOT_EQUAL_TO);

  /**
   * The {@code boolean} primitive type.
   *
   * @since 0.4.2
   */
  protected static final PrimitiveTypeName BOOLEAN = PrimitiveTypeName.BOOLEAN;

  /**
   * The {@code double} primitive type.
   *
   * @since 0.4.2
   */
  protected static final PrimitiveTypeName DOUBLE = PrimitiveTypeName.DOUBLE;

  /**
   * The {@code int} primitive type.
   *
   * @since 0.4.2
   */
  protected static final PrimitiveTypeName INT = PrimitiveTypeName.INT;

  private static final _Ext EXT = _Ext.INSTANCE;

  private static final _Include INCLUDE = _Include.INSTANCE;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  /**
   * Returns the Java source code generated by this template using the default
   * formatting.
   *
   * @return the Java source code
   */
  @Override
  public String toString() {
    var out = new StringBuilder();

    var sink = JavaSink.ofStringBuilder(out);

    sink.eval(this);

    return out.toString();
  }

  /**
   * The {@code abstract} modifier.
   *
   * @return the {@code abstract} modifier
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final AbstractModifier _abstract() {
    return modifier(Keyword.ABSTRACT);
  }

  /**
   * The {@code boolean} primitive type.
   *
   * @return the {@code boolean} primitive type
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final PrimitiveType _boolean() {
    return primitiveType(Keyword.BOOLEAN);
  }

  /**
   * TODO
   *
   * @param name Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ClassKeyword _class(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.CLASS, api.object(name));
  }

  /**
   * The {@code double} primitive type.
   *
   * @return the {@code double} primitive type
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final PrimitiveType _double() {
    return primitiveType(Keyword.DOUBLE);
  }

  /**
   * Begins the {@code else} clause of an {@code if-then-else} statement.
   *
   * <p>
   * The following Objectos code:
   *
   * <pre>
   * _if(n("size"), equalTo(), i(0)), block(
   *   invoke("whenEmpty")
   * ), _else(), block(
   *   invoke("whenNotEmpty")
   * )</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (size == 0) {
   *   whenEmpty();
   * } else {
   *   whenNotEmpty();
   * }</pre>
   *
   * @return the {@code else} keyword
   *
   * @since 0.4.2
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldElseKeyword _else() {
    return api().itemAdd(ByteProto.ELSE, ByteProto.NOOP);
  }

  /**
   * TODO
   *
   * @param name Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final EnumKeyword _enum(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.ENUM, api.object(name));
  }

  /**
   * TODO
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExtendsKeyword _extends() {
    return api().itemAdd(ByteProto.EXTENDS, ByteProto.NOOP);
  }

  /**
   * The {@code final} modifier.
   *
   * @return the {@code final} modifier
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldFinalModifier _final() {
    return api().itemAdd(ByteProto.MODIFIER, Keyword.FINAL.ordinal());
  }

  /**
   * The {@code if ( Expression )} clause with an expression made of 1 part.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * _if(n("condition"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (condition);</pre>
   *
   * @param e1 the first part of the expression
   *
   * @return the {@code if ( Expression )} clause
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final IfCondition _if(ExpressionPart e1) {
    return api().elem(ByteProto.IF_CONDITION, e1.self());
  }

  /**
   * The {@code if ( Expression )} clause with an expression made of 2 parts.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * _if(n("x"), n("valid"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (x.valid);</pre>
   *
   * @param e1 the first part of the expression
   * @param e2 the second part of the expression
   *
   * @return the {@code if ( Expression )} clause
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final IfCondition _if(ExpressionPart e1, ExpressionPart e2) {
    return api().elem(ByteProto.IF_CONDITION, e1.self(), e2.self());
  }

  /**
   * The {@code if ( Expression )} clause with an expression made of 3 parts.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * _if(n("x"), equalTo(), n("y"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (x == y);</pre>
   *
   * @param e1 the first part of the expression
   * @param e2 the second part of the expression
   * @param e3 the third part of the expression
   *
   * @return the {@code if ( Expression )} clause
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final IfCondition _if(ExpressionPart e1, ExpressionPart e2, ExpressionPart e3) {
    return api().elem(ByteProto.IF_CONDITION, e1.self(), e2.self(), e3.self());
  }

  /**
   * The {@code if ( Expression )} clause with an expression made of 4 parts.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * _if(n("x"), n("y"), notEqualTo(), _null())</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (x.y != null);</pre>
   *
   * @param e1 the first part of the expression
   * @param e2 the second part of the expression
   * @param e3 the third part of the expression
   * @param e4 the fourth part of the expression
   *
   * @return the {@code if ( Expression )} clause
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final IfCondition _if(ExpressionPart e1, ExpressionPart e2, ExpressionPart e3,
      ExpressionPart e4) {
    return api().elem(ByteProto.IF_CONDITION, e1.self(), e2.self(), e3.self(),
      e4.self());
  }

  /**
   * The {@code if ( Expression )} clause with an expression made of 5 parts.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * _if(n("a"), n("b"), equalTo(), n("c"), n("d"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (a.b == c.d);</pre>
   *
   * @param e1 the first part of the expression
   * @param e2 the second part of the expression
   * @param e3 the third part of the expression
   * @param e4 the fourth part of the expression
   * @param e5 the fifth part of the expression
   *
   * @return the {@code if ( Expression )} clause
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final IfCondition _if(ExpressionPart e1, ExpressionPart e2, ExpressionPart e3,
      ExpressionPart e4, ExpressionPart e5) {
    return api().elem(ByteProto.IF_CONDITION, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self());
  }

  /**
   * The {@code if ( Expression )} clause with an expression made of 6 parts.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * _if(n("a"), dim(i(0)), dim(i(1)), dim(i(3)), equalTo(), n("x"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * if (a[0][1][3] == x);</pre>
   *
   * @param e1 the first part of the expression
   * @param e2 the second part of the expression
   * @param e3 the third part of the expression
   * @param e4 the fourth part of the expression
   * @param e5 the fifth part of the expression
   * @param e6 the sixth part of the expression
   *
   * @return the {@code if ( Expression )} clause
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final IfCondition _if(ExpressionPart e1, ExpressionPart e2, ExpressionPart e3,
      ExpressionPart e4, ExpressionPart e5, ExpressionPart e6) {
    return api().elem(ByteProto.IF_CONDITION, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self(), e6.self());
  }

  /**
   * TODO
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ImplementsKeyword _implements() {
    return api().itemAdd(ByteProto.IMPLEMENTS, ByteProto.NOOP);
  }

  /**
   * The {@code int} primitive type.
   *
   * @return the {@code int} primitive type
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final PrimitiveType _int() {
    return primitiveType(Keyword.INT);
  }

  /**
   * TODO
   *
   * @param name Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final InterfaceKeyword _interface(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.INTERFACE, api.object(name));
  }

  /**
   * TODO
   *
   * @param type Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ClassInstanceCreationExpression _new(OldClassOrParameterizedType type) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self()
    );
  }

  /**
   * TODO
   *
   * @param type Do not use!
   * @param arg1 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ClassInstanceCreationExpression _new(OldClassOrParameterizedType type,
      ArgsPart arg1) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self(),
      arg1.self()
    );
  }

  /**
   * TODO
   *
   * @param type Do not use!
   * @param arg1 Do not use!
   * @param arg2 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ClassInstanceCreationExpression _new(OldClassOrParameterizedType type,
      ArgsPart arg1, ArgsPart arg2) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self(),
      arg1.self(), arg2.self()
    );
  }

  /**
   * The {@code null} literal.
   *
   * <p>
   * Typical uses are:
   *
   * <pre>
   * // each of the following Objectos Code:
   * _if(n("x"), equalTo(), _null());
   * _return(); _null();
   *
   * // generates the following Java code:
   * if (x == null);
   * return null;</pre>
   *
   * @return the {@code null} literal
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldNullLiteral _null() {
    return api().itemAdd(ByteProto.NULL_LITERAL, ByteProto.NOOP);
  }

  /**
   * TODO
   *
   * @param packageName Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final PackageKeyword _package(String packageName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    var api = api();
    api.autoImports.packageName(packageName);
    return api.itemAdd(ByteProto.PACKAGE, api.object(packageName));
  }

  /**
   * The {@code private} modifier.
   *
   * @return the {@code private} modifier
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final PrivateModifier _private() {
    return modifier(Keyword.PRIVATE);
  }

  /**
   * The {@code protected} modifier.
   *
   * @return the {@code protected} modifier
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ProtectedModifier _protected() {
    return modifier(Keyword.PROTECTED);
  }

  /**
   * The {@code public} modifier.
   *
   * @return the {@code public} modifier
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final PublicModifier _public() {
    return modifier(Keyword.PUBLIC);
  }

  /**
   * TODO
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldReturnKeyword _return() {
    return api().itemAdd(ByteProto.RETURN, ByteProto.NOOP);
  }

  /**
   * The {@code static} modifier.
   *
   * @return the {@code static} modifier
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final StaticModifier _static() {
    return modifier(Keyword.STATIC);
  }

  /**
   * TODO
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldSuperKeyword _super() {
    return api().itemAdd(ByteProto.SUPER, ByteProto.NOOP);
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExplicitConstructorInvocation _super(ArgsPart e1) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self());
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   * @param e2 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self());
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   * @param e2 Do not use!
   * @param e3 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2,
      ArgsPart e3) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self(),
      e3.self());
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   * @param e2 Do not use!
   * @param e3 Do not use!
   * @param e4 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2,
      ArgsPart e3, ArgsPart e4) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self(),
      e3.self(), e4.self());
  }

  /**
   * TODO
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldThisKeyword _this() {
    return api().itemAdd(ByteProto.THIS, ByteProto.NOOP);
  }

  /**
   * Begins a {@code throw} statement by emitting the {@code throw} keyword.
   *
   * <p>
   * A typical use is:
   *
   * <pre>
   * _throw(); _new(t(IOException.class));</pre>
   *
   * <p>
   * Which generates the following Java code:
   *
   * <pre>
   * throw new java.io.IOException();</pre>
   *
   * @return the {@code throw} keyword
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldThrowKeyword _throw() {
    return api().itemAdd(ByteProto.THROW, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldVarKeyword _var() {
    return api().itemAdd(ByteProto.VAR, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldVoidKeyword _void() {
    return api().itemAdd(ByteProto.VOID, ByteProto.NOOP);
  }

  /**
   * TODO
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.4")
  protected final ArrayInitializer ainit() {
    return api().elem(ByteProto.ARRAY_INITIALIZER);
  }

  /**
   * TODO
   *
   * @param e1 do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.4")
  protected final ArrayInitializer ainit(
      VariableInitializer e1) {
    return api().elem(ByteProto.ARRAY_INITIALIZER, e1);
  }

  /**
   * TODO
   *
   * @param e1 do not use!
   * @param e2 do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.4")
  protected final ArrayInitializer ainit(
      VariableInitializer e1, VariableInitializer e2) {
    return api().elem(ByteProto.ARRAY_INITIALIZER, e1, e2);
  }

  /**
   * Adds an annotation to a declaration or to a type usage.
   *
   * <p>
   * The following Objectos Code method declaration:
   *
   * <pre>
   * method(
   *   annotation(Override.class),
   *   PUBLIC, INT, name("hashCode")
   * )</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * &#064;java.lang.Override
   * public int hashCode() {}</pre>
   *
   * @param annotationType
   *        the type of the annotation
   * @param contents
   *        the element-value pairs of this annotation
   *
   * @return the annotation instruction
   *
   * @since 0.4.2
   */
  protected final AnyDeclarationInstruction annotation(
      Class<? extends Annotation> annotationType, AnnotationInstruction... contents) {
    Objects.requireNonNull(annotationType, "annotationType == null");
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    var api = api();
    return api.elemMany(ByteProto.ANNOTATION, api.classType(annotationType), many);
  }

  /**
   * Adds an annotation to a declaration or to a type usage.
   *
   * <p>
   * The following Objectos Code method declaration:
   *
   * <pre>
   * static final ClassTypeName OVERRIDE
   *     = ClassTypeName.of(Override.class);
   *
   * method(
   *   annotation(OVERRIDE),
   *   PUBLIC, INT, name("hashCode")
   * )</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * &#064;java.lang.Override
   * public int hashCode() {}</pre>
   *
   * @param annotationType
   *        the type of the annotation
   * @param contents
   *        the element-value pairs of this annotation
   *
   * @return the annotation instruction
   *
   * @since 0.4.4
   */
  protected final AnyDeclarationInstruction annotation(
      ClassTypeName annotationType, AnnotationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.ANNOTATION, annotationType.self(), many);
  }

  /**
   * Adds a single element-value or element-value pair to an annotation.
   *
   * <p>
   * The following Objectos Code annotation:
   *
   * <pre>
   * static final ClassTypeName DESCRIPTION
   *     = ClassTypeName.of("com.example", "Description");
   *
   * annotation(
   *   DESCRIPTION,
   *   annotationValue(s("The `annotationValue` instruction"))
   * )</pre>
   *
   * <p>
   * Generates the following Java annotation:
   *
   * <pre>
   * &#064;com.example.Description("The `annotationValue` instruction")</pre>
   *
   * @param contents
   *        the parts of this element-value or element-value pair
   *
   * @return the element-value instruction
   *
   * @since 0.4.4
   */
  protected final AnnotationInstruction annotationValue(ExpressionPart... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.ANNOTATION_VALUE, many);
  }

  /**
   * Adds an expression as the argument to a constructor or method invocation.
   *
   * <p>
   * The following Objectos Code method expression statement:
   *
   * <pre>
   * p(v("put"), argument(i(1)), argument(s("One")))</pre>
   *
   * <p>
   * Generates:
   *
   * <pre>
   * put(1, "One");</pre>
   *
   * @param contents
   *        the parts of the expression
   *
   * @return an argument instruction
   *
   * @since 0.4.3.1
   */
  protected final Argument argument(ExpressionPart... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.ARGUMENT, many);
  }

  /**
   * @since 0.4.4
   */
  protected final ArrayInitializer arrayInitializer() {
    return api().itemAdd(ByteProto.ARRAY_INITIALIZER, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final At at(OldClassTypeInstruction annotationType) {
    return api().elem(ByteProto.ANNOTATION, annotationType.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final At at(OldClassTypeInstruction annotationType, AtElement e1) {
    return api().elem(ByteProto.ANNOTATION, annotationType.self(), e1.self());
  }

  /**
   * Instructs this template to automatically add import declarations for the
   * types declared.
   *
   * <p>
   * The imports declarations will be inserted at the relative
   * location this instruction is invoked. In other words, this instruction must
   * be called <em>after</em> the package declaration instruction (if any) and
   * <em>before</em> any top level declaration (if any).
   *
   * @return the {@code autoImports} instruction
   */
  protected final AutoImports autoImports() {
    var api = api();
    api.autoImports.enable();
    return api.itemAdd(ByteProto.AUTO_IMPORTS, ByteProto.NOOP);
  }

  /**
   * TODO
   *
   * @since 0.4.3.1
   */
  protected final Block block(BlockInstruction... instructions) {
    Object[] many = Objects.requireNonNull(instructions, "statements == null");
    return api().elemMany(ByteProto.BLOCK, many);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Body body(BodyElement... elements) {
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.BODY, many);
  }

  /**
   * Adds a class declaration to a compilation unit or as a member of an
   * enclosing type.
   *
   * @param contents
   *        the contents of this class declaration
   *
   * @return a class declaration instruction
   *
   * @since 0.4.4
   */
  protected final ClassDeclaration classDeclaration(ClassDeclarationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.CLASS_DECLARATION, many);
  }

  /**
   * Consumes the provided {@code instructions} if necessary.
   *
   * @param instructions
   *        the instructions to be consumed
   */
  protected final void code(Instruction... instructions) {
    for (var instruction : instructions) {
      if (instruction instanceof External external) {
        var api = api();
        external.execute(api);
        api.externalToLocal();
      }
    }
  }

  /**
   * Adds the parenthesized expression that is part of a {@code if},
   * {@code while} and {@code do-while} statement.
   *
   * <p>
   * The following Objectos Code statement:
   *
   * <pre>
   * p(IF, condition(n("active")), block(
   *   p(v("execute"))
   * ))</pre>
   *
   * <p>
   * Generates:
   *
   * <pre>
   * if (active) {
   *   execute();
   * }</pre>
   *
   * @param contents
   *        the parts of the expression
   *
   * @return a condition instruction
   *
   * @since 0.4.4
   */
  protected final StatementPart condition(ExpressionPart... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.CONDITION, many);
  }

  /**
   * TODO
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor() {
    return api().elem(ByteProto.CONSTRUCTOR);
  }

  /**
   * Adds a constructor declaration.
   *
   * @param contents
   *        the contents of this constructor
   *
   * @return a constructor declaration instruction
   *
   * @since 0.4.4
   */
  protected final ConstructorDeclaration constructor(
      ConstructorDeclarationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.CONSTRUCTOR_DECLARATION, many);
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self());
  }

  /**
   * TODO
   *
   * @param elements Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement... elements) {
    Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.CONSTRUCTOR, elements);
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   * @param e2 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self());
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   * @param e2 Do not use!
   * @param e3 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   * @param e2 Do not use!
   * @param e3 Do not use!
   * @param e4 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   * @param e2 Do not use!
   * @param e3 Do not use!
   * @param e4 Do not use!
   * @param e5 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4, ParameterElement e5) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self(), e4.self(), e5.self());
  }

  /**
   * TODO
   *
   * @param e1 Do not use!
   * @param e2 Do not use!
   * @param e3 Do not use!
   * @param e4 Do not use!
   * @param e5 Do not use!
   * @param e6 Do not use!
   *
   * @return Do not use!
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4, ParameterElement e5, ParameterElement e6) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(),
      e3.self(), e4.self(), e5.self(), e6.self());
  }

  /**
   * TODO
   */
  protected final ArrayDimension dim() {
    return api().itemAdd(ByteProto.ARRAY_DIMENSION, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  protected final ArrayAccess dim(ExpressionPart... parts) {
    Object[] many = Objects.requireNonNull(parts, "parts == null");
    return api().elemMany(ByteProto.ARRAY_ACCESS, many);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldEllipsis ellipsis() {
    return api().itemAdd(ByteProto.ELLIPSIS, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final End end() {
    return stop();
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final EnumConstant enumConstant(EnumConstantInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.ENUM_CONSTANT, many);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.4")
  protected final EnumConstant enumConstant(String name) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    return api().elem(ByteProto.ENUM_CONSTANT, name(name));
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.4")
  protected final EnumConstant enumConstant(String name,
      ArgsPart e1) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT, e1.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.4")
  protected final EnumConstant enumConstant(String name,
      ArgsPart e1, ArgsPart e2) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.4")
  protected final EnumConstant enumConstant(String name,
      ArgsPart e1, ArgsPart e2, ArgsPart e3) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.4")
  protected final EnumConstant enumConstant(String name,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final EnumDeclaration enumDeclaration(EnumDeclarationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.ENUM_DECLARATION, many);
  }

  /**
   * The {@code ==} (equal to) operator.
   *
   * @return the {@code ==} (equal to) operator
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldEqualityOperator equalTo() {
    return api().itemAdd(ByteProto.EQUALITY_OPERATOR, Symbol.EQUAL_TO.ordinal());
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final ClassOrInterfaceDeclarationInstruction
      extendsClause(ClassOrParameterizedTypeName type) {
    return api().elem(ByteProto.EXTENDS_CLAUSE, type.self());
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final InterfaceDeclarationInstruction
      extendsClause(ClassOrParameterizedTypeName... types) {
    Object[] many = Objects.requireNonNull(types, "types == null");
    return api().elemMany(ByteProto.EXTENDS_CLAUSE, many);
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final FieldDeclaration field(FieldDeclarationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.FIELD_DECLARATION, many);
  }

  /**
   * The simple assignment operator {@code =}.
   *
   * <p>
   * The following Objectos Code example:
   *
   * <pre>
   * _this(), n("x"), gets(), n("x")</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * this.x = x;</pre>
   *
   * @return the simple assignment operator {@code =}
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldSimpleAssigmentOperator gets() {
    return api().itemAdd(ByteProto.ASSIGNMENT_OPERATOR, Symbol.ASSIGNMENT.ordinal());
  }

  /**
   * TODO
   */
  protected final IntegerLiteral i(int value) {
    var s = Integer.toString(value);
    var api = api();
    return api.itemAdd(ByteProto.PRIMITIVE_LITERAL, api.object(s));
  }

  /**
   * TODO
   */
  protected final Identifier id(String name) {
    JavaModel.checkIdentifier(name);
    var api = api();
    return api.itemAdd(ByteProto.IDENTIFIER, api.object(name));
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final ImplementsClause implementsClause(ImplementsClauseInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.IMPLEMENTS_CLAUSE, many);
  }

  /**
   * TODO
   */
  protected final Include include(IncludeTarget target) {
    var api = api();
    api.lambdastart();
    target.execute(); // implicit null-check
    api.lambdaend();
    return INCLUDE;
  }

  /**
   * TODO
   */
  protected final Include include(JavaTemplate template) {
    var api = api();
    api.lambdastart();
    template.execute(api);
    api.lambdaend();
    return INCLUDE;
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final InterfaceDeclaration interfaceDeclaration(
      InterfaceDeclarationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.INTERFACE_DECLARATION, many);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart... elements) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api.elemMany(ByteProto.INVOKE, EXT, many);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5,
      ArgsPart e6) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self(),
      e6.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5,
      ArgsPart e6, ArgsPart e7) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self(),
      e6.self(), e7.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5,
      ArgsPart e6, ArgsPart e7, ArgsPart e8) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self(),
      e6.self(), e7.self(), e8.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodInvocation invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5,
      ArgsPart e6, ArgsPart e7, ArgsPart e8, ArgsPart e9) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self(),
      e6.self(), e7.self(), e8.self(), e9.self());
  }

  /**
   * Adds a method declaration to a class or interface declaration.
   *
   * <p>
   * The following Objectos Code method declaration:
   *
   * <pre>
   * method(
   *   annotation(Override.class),
   *   modifiers(PUBLIC, FINAL),
   *   returnType(String.class),
   *   name("toString"),
   *
   *   p(RETURN, s("Objectos Code"))
   * )</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * &#064;java.lang.Override
   * public final java.lang.String toString() {
   *   return "Objectos Code";
   * }</pre>
   *
   * <p>
   * This method also accepts a shorthand form.
   * The following produces the same result as the previous form:
   *
   * <pre>
   * static final ClassTypeName STRING = classType(String.class);
   *
   * method(
   *   annotation(Override.class),
   *   PUBLIC, FINAL, STRING, name("toString"),
   *   p(RETURN, s("Objectos Code"))
   * )</pre>
   *
   * <p>
   * If the method's return type is not explicitly defined, then it is assumed
   * that the method does not return a value. In other other words, the keyword
   * {@code void} will be added.
   *
   * <p>
   * If the method's name is not explicitly defined, then the method will be
   * named {@code unnamed}.
   *
   * <p>
   * Therefore, if this method is invoked without arguments, like so:
   *
   * <pre>
   * method()</pre>
   *
   * <p>
   * Then the following Java code will be generated:
   *
   * <pre>
   * void unnamed() {}</pre>
   *
   * @param contents
   *        the contents of this method declaration
   *
   * @return a method declaration
   *
   * @since 0.4.2
   */
  protected final MethodDeclaration method(MethodDeclarationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.METHOD_DECLARATION, many);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodDeclarator method(String methodName) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD, EXT);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodDeclarator method(String methodName,
      ParameterElement e1) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD, EXT, e1.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodDeclarator method(String methodName,
      ParameterElement... elements) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api.elemMany(ByteProto.METHOD, EXT, many);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final MethodDeclarator method(String methodName,
      ParameterElement e1, ParameterElement e2) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD, EXT, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  protected final ExpressionName n(String name) {
    JavaModel.checkSimpleName(name.toString());
    var api = api();
    return api.itemAdd(ByteProto.EXPRESSION_NAME, api.object(name));
  }

  /**
   * Sets the name of a class or an interface declaration.
   * More specifically, this instruction will use the rightmost name of the
   * specified fully qualified name as the simple name of a class or interface
   * declaration. All other names, i.e the package name and any enclosing name,
   * are ignored.
   *
   * <p>
   * The following Objectos Code class declaration:
   *
   * <pre>
   * static final ClassTypeName INNER =
   *     ClassTypeName.of("com.example", "Outer", "Inner");
   *
   * class(
   *   name(INNER)
   * )</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * class Inner {}</pre>
   *
   * @param name
   *        the {@code ClassTypeName} whose simple name will be used
   *
   * @return the instruction to set the name of a class or interface declaration
   *
   * @since 0.4.4
   */
  protected final TypeDeclarationInstruction name(ClassTypeName name) {
    // cast is safe: ClassTypeName is sealed
    var impl = (ClassTypeNameImpl) name;
    // `impl` implicit null-check
    var simpleName = impl.simpleName();
    var api = api();
    return api.itemAdd(ByteProto.DECLARATION_NAME, api.object(simpleName));
  }

  /**
   * Sets the {@code name} of a declaration. This instruction can set the name
   * of the following declarations:
   *
   * <ul>
   * <li>class declaration;</li>
   * <li>enum declaration;</li>
   * <li>field declaration;</li>
   * <li>interface declaration;</li>
   * <li>method declaration; and</li>
   * <li>local variable declaration.</li>
   * </ul>
   *
   * <p>
   * The following Objectos Code method declaration:
   *
   * <pre>
   * static final ClassTypeName STRING =
   *     ClassTypeName.of(String.class);
   *
   * method(
   *   name("example"),
   *   p(VAR, name("i"), i(0)),
   *   p(STRING, name("s"), s("hello"))
   * )</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * void example() {
   *   var i = 0;
   *   java.lang.String s = "hello";
   * }</pre>
   *
   * @param name
   *        the value to be used as the declaration name
   *
   * @return the declaration name
   *
   * @throws IllegalArgumentException
   *         if {@code name} contains characters that are not allowed to be part
   *         of an identifier
   *
   * @since 0.4.2
   */
  protected final DeclarationName name(String name) {
    JavaModel.checkIdentifier(name);
    var api = api();
    return api.itemAdd(ByteProto.DECLARATION_NAME, api.object(name));
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldNewLine nl() {
    return api().itemAdd(ByteProto.NEW_LINE, ByteProto.NOOP);
  }

  /**
   * The {@code !=} (not equal to) operator.
   *
   * @return the {@code !=} (not equal to) operator
   *
   * @since 0.4.1
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldEqualityOperator notEqualTo() {
    return api().itemAdd(ByteProto.EQUALITY_OPERATOR, Symbol.NOT_EQUAL_TO.ordinal());
  }

  /**
   * TODO
   *
   * @since 0.4.3.1
   */
  protected final BlockInstruction p(StatementPart... parts) {
    Object[] many = Objects.requireNonNull(parts, "parts == null");
    return api().elemMany(ByteProto.STATEMENT, many);
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final void packageDeclaration(String packageName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    var api = api();
    api.autoImports.packageName(packageName);
    api.elem(ByteProto.PACKAGE_DECLARATION, packageName);
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final ExecutableDeclarationInstruction parameter(ParameterInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.PARAMETER_DECLARATION, many);
  }

  /**
   * TODO
   *
   * @since 0.5.3
   */
  protected final ClassOrInterfaceDeclarationInstruction permitsClause(ClassTypeName... types) {
    Object[] many = Objects.requireNonNull(types, "types == null");
    return api().elemMany(ByteProto.PERMITS_CLAUSE, many);
  }

  /**
   * TODO
   */
  protected final StringLiteral s(String string) {
    Objects.requireNonNull(string, "string == null");

    var api = api();

    return api.itemAdd(ByteProto.STRING_LITERAL, api.object(string));
  }

  /**
   * TODO
   */
  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1) {
    return api().elem(ByteProto.ARRAY_TYPE, type, e1);
  }

  /**
   * TODO
   */
  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2) {
    return api().elem(ByteProto.ARRAY_TYPE, type, e1, e2);
  }

  /**
   * TODO
   */
  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2, ArrayTypeElement e3) {
    return api().elem(ByteProto.ARRAY_TYPE, type, e1, e2, e3);
  }

  /**
   * TODO
   */
  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2, ArrayTypeElement e3, ArrayTypeElement e4) {
    return api().elem(ByteProto.ARRAY_TYPE, type, e1, e2, e3, e4);
  }

  /**
   * TODO
   */
  protected final OldClassTypeInstruction t(Class<?> type) {
    Check.argument(!type.isPrimitive(), """
    The `t(Class<?>)` instruction must not be used represent a primitive type.

    Use one of the following methods instead:

    - `_boolean()`
    - `_double()`
    - `_int()`
    """);

    Check.argument(type != void.class, """
    The `t(Class<?>)` instruction must not be used represent the no-type (void).

    Use the `_void()` method instead.
    """);

    Check.argument(!type.isArray(), """
    The `t(Class<?>)` instruction must not be used represent array types.

    Use the `t(ArraTypeComponent, ArrayTypeElement...)` method instead.
    """);

    return api().classType(type);
  }

  /**
   * TODO
   */
  protected final ParameterizedType t(
      OldClassTypeInstruction rawType,
      ReferenceType arg1) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1);
  }

  /**
   * TODO
   */
  protected final ParameterizedType t(
      OldClassTypeInstruction rawType,
      ReferenceType arg1, ReferenceType arg2) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1, arg2);
  }

  /**
   * TODO
   */
  protected final ParameterizedType t(
      OldClassTypeInstruction rawType,
      ReferenceType arg1, ReferenceType arg2, ReferenceType arg3) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1, arg2, arg3);
  }

  /**
   * TODO
   */
  protected final OldClassTypeInstruction t(String packageName, String simpleName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName.toString()); // implicit null check
    var api = api();
    return api.itemAdd(
      ByteProto.CLASS_TYPE, api.object(packageName),
      1, api.object(simpleName)
    );
  }

  /**
   * TODO
   */
  protected final OldClassTypeInstruction t(String packageName, String simpleName1,
      String simpleName2) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName1.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName2.toString()); // implicit null check
    var api = api();
    return api.itemAdd(
      ByteProto.CLASS_TYPE, api.object(packageName),
      2, api.object(simpleName1), api.object(simpleName2)
    );
  }

  /**
   * TODO
   */
  @Deprecated(since = "0.4.3.1", forRemoval = true)
  protected final TypeParameterOld tparam(String name) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, EXT);
  }

  /**
   * TODO
   */
  @Deprecated(since = "0.4.3.1", forRemoval = true)
  protected final TypeParameterOld tparam(String name, TypeParameterBound bound1) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, EXT, bound1.self());
  }

  /**
   * TODO
   */
  @Deprecated(since = "0.4.3.1", forRemoval = true)
  protected final TypeParameterOld tparam(String name, TypeParameterBound... bounds) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    Object[] many = Objects.requireNonNull(bounds, "bounds == null");
    return api.elemMany(ByteProto.TYPE_PARAMETER, EXT, many);
  }

  /**
   * TODO
   */
  @Deprecated(since = "0.4.3.1", forRemoval = true)
  protected final TypeParameterOld tparam(String name,
      TypeParameterBound bound1, TypeParameterBound bound2) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, EXT,
      bound1.self(), bound2.self());
  }

  /**
   * TODO
   */
  @Deprecated(since = "0.4.3.1", forRemoval = true)
  protected final TypeParameterOld tparam(String name,
      TypeParameterBound bound1, TypeParameterBound bound2, TypeParameterBound bound3) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, EXT,
      bound1.self(), bound2.self(), bound3.self());
  }

  /**
   * TODO
   */
  protected final TypeVariable tvar(String name) {
    JavaModel.checkVarName(name.toString());
    var api = api();
    return api.itemAdd(ByteProto.TYPE_VARIABLE, api.object(name));
  }

  /**
   * Adds a single type parameter to a declaration.
   *
   * <p>
   * The following Objectos Code method declaration:
   *
   * <pre>
   * static final ClassTypeName OBJECT = classType(Object.class);
   *
   * static final ClassTypeName SERIALIZABLE = classType(Serializable.class);
   *
   * method(
   *   PUBLIC, typeParameter("T", OBJECT, SERIALIZABLE), name("example")
   * )</pre>
   *
   * <p>
   * Generates the following Java method declaration:
   *
   * <pre>
   * public &lt;T extends java.lang.Object &amp; java.io.Serializable&gt; void example() {}</pre>
   *
   * @param name
   *        the type parameter's name
   * @param bounds
   *        the bounds of this type parameter
   *
   * @return a type parameter instruction
   *
   * @throws IllegalArgumentException
   *         if {@code name} contains characters that are not allowed to be part
   *         of an identifier
   *
   * @since 0.4.3.1
   */
  protected final TypeParameter typeParameter(String name, TypeName... bounds) {
    JavaModel.checkVarName(name.toString());
    Object[] many = Objects.requireNonNull(bounds, "bounds == null");
    return api().elemMany(ByteProto.TYPE_PARAMETER, name, many);
  }

  /**
   * TODO
   *
   * @since 0.4.3.1
   */
  protected final MethodInvocation v(String methodName) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.V, api.object(methodName));
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final ArrayInitializerValue value(ExpressionPart... parts) {
    Object[] many = Objects.requireNonNull(parts, "parts == null");
    return api().elemMany(ByteProto.VALUE, many);
  }

  private _Item modifier(Keyword value) {
    return api().itemAdd(ByteProto.MODIFIER, value.ordinal());
  }

  private _Item primitiveType(Keyword value) {
    return api().itemAdd(ByteProto.PRIMITIVE_TYPE, value.ordinal());
  }

  private _Item stop() {
    return api().itemAdd(ByteProto.STOP, ByteProto.NOOP);
  }

}