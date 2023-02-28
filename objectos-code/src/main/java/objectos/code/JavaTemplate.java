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
import objectos.code.internal.External;
import objectos.code.internal.InternalApi;
import objectos.code.internal.InternalJavaTemplate;
import objectos.code.internal.JavaModel;
import objectos.code.internal.Keyword;
import objectos.code.internal.Symbol;
import objectos.code.tmpl.AnnotationInstruction;
import objectos.code.tmpl.ArgsPart;
import objectos.code.tmpl.ArrayTypeComponent;
import objectos.code.tmpl.BlockElement;
import objectos.code.tmpl.BodyElement;
import objectos.code.tmpl.ClassDeclarationInstruction;
import objectos.code.tmpl.ClassOrInterfaceDeclarationInstruction;
import objectos.code.tmpl.ClassOrParameterizedTypeName;
import objectos.code.tmpl.EnumDeclarationInstruction;
import objectos.code.tmpl.ExpressionPart;
import objectos.code.tmpl.FieldDeclarationInstruction;
import objectos.code.tmpl.IncludeTarget;
import objectos.code.tmpl.Instruction;
import objectos.code.tmpl.InterfaceDeclarationInstruction;
import objectos.code.tmpl.MethodDeclarationInstruction;
import objectos.code.tmpl.StatementPart;
import objectos.code.tmpl.TypeName;
import objectos.code.tmpl.VariableInitializer;
import objectos.lang.Check;

/**
 * The {@code JavaTemplate} class provides a pure Java API for generating Java
 * source code.
 *
 * @since 0.4
 */
public non-sealed abstract class JavaTemplate extends InternalJavaTemplate {

  /**
   * An {@link Instruction} that can be used with the
   * {@link JavaTemplate#constructor(ConstructorDeclarationInstruction...)}
   * method.
   *
   * @since 0.4.4
   */
  protected interface ConstructorDeclarationInstruction extends Instruction {}

  /**
   * The ellipsis ({@code ...}) separator. It is used to indicate that the last
   * formal parameter of a constructor or method as being a variable arity
   * parameter.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * method(
   *   PUBLIC, VOID, name("varargs"),
   *   param(INT, ELLIPSIS, values)
   * )</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * public void varargs(int... values) {}</pre>
   *
   * @since 0.4.3.1
   */
  protected static final class Ellipsis extends External {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.ELLIPSIS, ByteProto.NOOP);
    }
  }

  /**
   * Represents a modifier of the Java language.
   *
   * @since 0.4.2
   */
  protected static final class Modifier extends External
      implements
      ClassDeclarationInstruction,
      ConstructorDeclarationInstruction,
      EnumDeclarationInstruction,
      FieldDeclarationInstruction,
      InterfaceDeclarationInstruction,
      MethodDeclarationInstruction {
    final int value;

    private Modifier(Keyword keyword) { this.value = keyword.ordinal(); }

    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.MODIFIER, value);
    }
  }

  /**
   * An {@link Instruction} that can be used with constructs that can declare
   * formal parameters.
   *
   * @see JavaTemplate#constructor(ParameterElement...)
   * @see JavaTemplate#method(String, ParameterElement...)
   */
  protected interface ParameterElement extends Instruction {}

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
      ExtendsClause,
      ExtendsKeyword,
      FieldDeclaration,
      FieldDeclarationInstruction,
      FinalModifier,
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
      ModifiersElement,
      MethodInvocation,
      OldNewKeyword,
      OldNewLine,
      OldNullLiteral,
      OldClassTypeInstruction,
      PackageKeyword,
      Parameter,
      ParameterizedType,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      OldReturnKeyword,
      ReturnType,
      OldSimpleAssigmentOperator,
      Statement,
      OldStatement,
      StaticModifier,
      StringLiteral,
      OldSuperKeyword,
      OldThisKeyword,
      OldThrowKeyword,
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
      InterfaceDeclarationInstruction,
      MethodDeclarationInstruction {}

  interface AnyType extends BodyElement, BlockElement, ParameterElement {}

  interface Argument extends ExpressionPart {}

  interface ArrayAccess extends ExpressionPart {}

  interface ArrayDimension extends ArrayTypeElement {}

  interface ArrayInitializer extends BodyElement, VariableInitializer {}

  interface ArrayInitializerValue extends AnnotationInstruction, VariableInitializer {}

  interface ArrayType extends BodyElement, ReferenceType {}

  interface ArrayTypeElement {}

  interface At extends BodyElement {}

  interface AtElement extends Instruction {}

  interface AutoImports extends Instruction {}

  interface Block extends BlockElement, BodyElement, StatementPart {}

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

  interface DeclarationName
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      FieldDeclarationInstruction,
      InterfaceDeclarationInstruction,
      MethodDeclarationInstruction,
      StatementPart {}

  interface End extends ArgsPart, BlockElement {}

  interface EnumConstant extends BodyElement, EnumDeclarationInstruction {}

  /**
   * @since 0.4.4
   */
  interface EnumDeclaration extends ClassDeclarationInstruction {}

  @Deprecated
  interface EnumKeyword extends BodyElement {}

  interface ExplicitConstructorInvocation extends BlockElement {}

  interface ExpressionName extends ExpressionPart {}

  /**
   * @since 0.4.4
   */
  interface ExtendsClause
      extends ClassDeclarationInstruction, InterfaceDeclarationInstruction {}

  @Deprecated
  interface ExtendsKeyword extends BodyElement {}

  interface FieldDeclaration
      extends
      BodyElement,
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      InterfaceDeclarationInstruction {}

  interface FinalModifier extends BodyElement {}

  interface Identifier extends BlockElement, BodyElement, ParameterElement {}

  interface IfCondition extends BlockElement {}

  /**
   * @since 0.4.4
   */
  interface ImplementsClause
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction {}

  @Deprecated
  interface ImplementsKeyword extends BodyElement {}

  interface Include
      extends
      ArgsPart, BlockElement, BodyElement, ExpressionPart, ParameterElement, VariableInitializer {}

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

  interface ModifiersElement
      extends ConstructorDeclarationInstruction, MethodDeclarationInstruction {}

  @Deprecated
  interface OldClassTypeInstruction
      extends ArgsPart, OldClassOrParameterizedType, ReferenceType, TypeParameterBound {}

  @Deprecated
  interface OldEllipsis extends ParameterElement {}

  @Deprecated
  interface OldElseKeyword extends BlockElement {}

  @Deprecated
  interface OldEqualityOperator extends ExpressionPart {}

  @Deprecated
  interface OldNewKeyword extends BlockElement {}

  @Deprecated
  interface OldNewLine extends ArgsPart, BlockElement {}

  @Deprecated
  interface OldNullLiteral extends ExpressionPart {}

  @Deprecated
  interface OldReturnKeyword extends BlockElement {}

  @Deprecated
  interface OldSimpleAssigmentOperator extends ExpressionPart {}

  interface OldStatement extends BlockElement {}

  @Deprecated
  interface OldSuperKeyword extends BlockElement {}

  @Deprecated
  interface OldThisKeyword extends PrimaryNoNewArray {}

  @Deprecated
  interface OldThrowKeyword extends BlockElement {}

  @Deprecated
  interface OldVarKeyword extends BlockElement {}

  @Deprecated
  interface OldVoidKeyword extends BodyElement {}

  @Deprecated
  interface PackageKeyword extends Instruction {}

  interface Parameter
      extends ConstructorDeclarationInstruction, MethodDeclarationInstruction {}

  interface ParameterizedType extends OldClassOrParameterizedType, ReferenceType {}

  @Deprecated
  interface OldClassOrParameterizedType extends Instruction {}

  interface PrimitiveType extends AnyType, ArrayTypeComponent, BodyElement {}

  interface PrivateModifier extends AccessModifier {}

  interface ProtectedModifier extends AccessModifier {}

  interface PublicModifier extends AccessModifier {}

  interface ReferenceType extends AnyType, ArrayTypeComponent {}

  interface ReturnType extends MethodDeclarationInstruction {}

  interface Statement
      extends BlockElement, ConstructorDeclarationInstruction, MethodDeclarationInstruction {}

  interface StaticModifier extends BodyElement {}

  interface StringLiteral extends Literal, PrimaryNoNewArray {}

  interface TypeParameter extends MethodDeclarationInstruction {}

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

  /**
   * @since 0.4.3.1
   */
  private static final class NewLine extends External implements ExpressionPart {
    @Override
    public final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.NEW_LINE, ByteProto.NOOP);
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
  protected static final Ellipsis ELLIPSIS = new Ellipsis();

  /**
   * The new line instruction.
   *
   * @since 0.4.3.1
   */
  protected static final ExpressionPart NL = new NewLine();

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
  protected static final Modifier PUBLIC = new Modifier(Keyword.PUBLIC);

  /**
   * The {@code protected} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier PROTECTED = new Modifier(Keyword.PROTECTED);

  /**
   * The {@code private} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier PRIVATE = new Modifier(Keyword.PRIVATE);

  /**
   * The {@code abstract} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier ABSTRACT = new Modifier(Keyword.ABSTRACT);

  /**
   * The {@code static} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier STATIC = new Modifier(Keyword.STATIC);

  /**
   * The {@code final} modifier.
   *
   * @since 0.4.2
   */
  protected static final Modifier FINAL = new Modifier(Keyword.FINAL);

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

  public static final _Ext EXT = _Ext.INSTANCE;

  public static final _Include INCLUDE = _Include.INSTANCE;

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
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final EnumKeyword _enum(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.ENUM, api.object(name));
  }

  /**
   * TODO
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
  protected final FinalModifier _final() {
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
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final InterfaceKeyword _interface(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.INTERFACE, api.object(name));
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ClassInstanceCreationExpression _new(OldClassOrParameterizedType type) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self()
    );
  }

  /**
   * TODO
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
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final PackageKeyword _package(String packageName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    var api = api();
    api.autoImports.packageName(packageName);
    return api.itemAdd(ByteProto.PACKAGE, api.object(packageName));
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
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final OldSuperKeyword _super() {
    return api().itemAdd(ByteProto.SUPER, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExplicitConstructorInvocation _super(ArgsPart e1) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2,
      ArgsPart e3) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self(),
      e3.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2,
      ArgsPart e3, ArgsPart e4) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self(),
      e3.self(), e4.self());
  }

  /**
   * TODO
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
   */
  protected final ArrayInitializer ainit() {
    return api().elem(ByteProto.ARRAY_INITIALIZER);
  }

  /**
   * TODO
   */
  protected final ArrayInitializer ainit(
      VariableInitializer e1) {
    return api().elem(ByteProto.ARRAY_INITIALIZER, e1);
  }

  /**
   * TODO
   */
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
   *   at(Override.class),
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
   * TODO
   *
   * @since 0.4.4
   */
  protected final AnyDeclarationInstruction annotation(
      ClassTypeName annotationType, AnnotationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.ANNOTATION, annotationType.self(), many);
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final AnnotationInstruction annotationValue(ExpressionPart... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.ANNOTATION_VALUE, many);
  }

  /**
   * TODO
   *
   * @since 0.4.3.1
   */
  protected final Argument arg(ExpressionPart... parts) {
    Object[] many = Objects.requireNonNull(parts, "parts == null");
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
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block() {
    return api().elem(ByteProto.BLOCK);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block(BlockElement e1) {
    return api().elem(ByteProto.BLOCK, e1.self());
  }

  /**
   * TODO
   *
   * @since 0.4.3.1
   */
  protected final Block block(BlockElement... statements) {
    Object[] many = Objects.requireNonNull(statements, "statements == null");
    return api().elemMany(ByteProto.BLOCK, many);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block(BlockElement e1, BlockElement e2) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6, BlockElement e7) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self(), e7.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6, BlockElement e7, BlockElement e8) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self(), e7.self(), e8.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Body body() {
    return api().elem(ByteProto.BODY);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Body body(BodyElement e1) {
    return api().elem(ByteProto.BODY, e1.self());
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
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Body body(BodyElement e1, BodyElement e2) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4,
      BodyElement e5) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self());
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final ClassDeclaration classDeclaration(ClassDeclarationInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.CLASS_DECLARATION, many);
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final ClassDeclaration classDeclaration(IncludeTarget target) {
    return api().elem(ByteProto.CLASS_DECLARATION, include(target));
  }

  /**
   * TODO
   */
  protected final void code(Instruction... elements) {
    // no-op
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor() {
    return api().elem(ByteProto.CONSTRUCTOR);
  }

  /**
   * TODO
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
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement... elements) {
    Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.CONSTRUCTOR, elements);
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4, ParameterElement e5) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self(), e4.self(), e5.self());
  }

  /**
   * TODO
   */
  @Deprecated(forRemoval = true, since = "0.4.3.1")
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4, ParameterElement e5, ParameterElement e6) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(),
      e3.self(), e4.self(), e5.self(), e6.self());
  }

  /**
   * TODO
   *
   * @since 0.4.4
   */
  protected final void consume(Instruction instruction) {
    if (instruction instanceof External external) {
      var api = api();
      external.execute(api);
      api.externalToLocal();
    }
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
   */
  protected final EnumConstant enumConstant(String name) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    return api().elem(ByteProto.ENUM_CONSTANT, name(name));
  }

  /**
   * TODO
   */
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
  protected final ExtendsClause extendsClause(ClassOrParameterizedTypeName... types) {
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
   * TODO
   *
   * @since 0.4.4
   */
  protected final FieldDeclaration field(IncludeTarget target) {
    return api().elem(ByteProto.FIELD_DECLARATION, include(target));
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
  protected final ImplementsClause implementsClause(ClassOrParameterizedTypeName... types) {
    Object[] many = Objects.requireNonNull(types, "types == null");
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
  protected final InterfaceDeclaration interfaceDeclaration(IncludeTarget target) {
    return api().elem(ByteProto.INTERFACE_DECLARATION, include(target));
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
   * Adds a method declaration with the contents given by the specified
   * {@code target}.
   *
   * <p>
   * This method is typically invoked with a method reference:
   *
   * <pre>
   * method(this::methodContents)</pre>
   *
   * <p>
   * Where {@code methodContents} is a private method in your template class:
   *
   * <pre>
   * private void methodContents() {
   *   modifiers(PUBLIC);
   *   if (shouldBeFinal()) {
   *     modifiers(FINAL);
   *   }
   *   returnType(VOID);
   *   name("maybeFinal");
   * }</pre>
   *
   * @param target
   *        code to be executed containing the contents of this method
   *        declaration
   *
   * @return a method declaration
   *
   * @since 0.4.3.1
   */
  protected final MethodDeclaration method(IncludeTarget target) {
    return api().elem(ByteProto.METHOD_DECLARATION, include(target));
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
   * Adds the specified {@code modifiers} to a declaration. Modifiers are
   * included in the order they are declared. A particular modifier will be
   * emitted as many times as it was declared; in other words, the method
   * <em>does not</em> filter out duplicates.
   *
   * <p>
   * The following Objectos Code method declaration:
   *
   * <pre>
   * method(
   *   modifiers(PUBLIC, STATIC),
   *   returnType(INT),
   *   name("value")
   * )</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * public static int value() {}</pre>
   *
   * @param modifiers
   *        the modifiers to be added to the enclosing declaration
   *
   * @return an element that adds the specified modifiers to the enclosing
   *         declaration
   *
   * @since 0.4.2
   */
  protected final ModifiersElement modifiers(Modifier... modifiers) {
    var api = api();
    api.localStart();
    api.protoAdd(ByteProto.MODIFIERS, modifiers.length); // implicit null-check
    for (var modifier : modifiers) {
      api.protoAdd(modifier.value);// implicit null-check
    }
    return api.itemEnd();
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
  protected final ClassOrInterfaceDeclarationInstruction name(ClassTypeName name) {
    var simpleName = name.simpleName(); // implicit null-check
    var api = api();
    return api.itemAdd(ByteProto.DECLARATION_NAME, api.object(simpleName));
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
  protected final Statement p(StatementPart... parts) {
    Object[] many = Objects.requireNonNull(parts, "parts == null");
    return api().elemMany(ByteProto.STATEMENT, many);
  }

  /**
   * Adds a variable arity formal parameter declaration.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * parameter(int.class, ELLIPSIS, "values")</pre>
   *
   * <p>
   * Generates the following Java parameter declaration:
   *
   * <pre>
   * int... values</pre>
   *
   * @param type
   *        the type of this parameter
   * @param ellipsis
   *        the ellipsis separator. Must be the {@link JavaTemplate#ELLIPSIS}
   *        constant
   * @param name
   *        the name of this parameter
   *
   * @return a formal parameter declaration
   *
   * @since 0.4.3.1
   */
  protected final Parameter parameter(Class<?> type, Ellipsis ellipsis, String name) {
    JavaModel.checkIdentifier(name.toString());
    Object typeName = typeName(type);
    return api().elem(ByteProto.PARAMETER_SHORT, typeName, ellipsis.self(), name);
  }

  /**
   * Adds a formal parameter declaration with the specified {@code type} and
   * {@code name}.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * parameter(String.class, "name")</pre>
   *
   * <p>
   * Generates the following Java parameter declaration:
   *
   * <pre>
   * java.lang.String name</pre>
   *
   * @param type
   *        the type of this parameter
   * @param name
   *        the name of this parameter
   *
   * @return a formal parameter declaration
   *
   * @since 0.4.2
   */
  protected final Parameter parameter(Class<?> type, String name) {
    JavaModel.checkIdentifier(name.toString());
    Object typeName = typeName(type);
    return api().elem(ByteProto.PARAMETER_SHORT, typeName, name);
  }

  /**
   * TODO
   *
   * @since 0.4.3.1
   */
  protected final Parameter parameter(TypeName type, Ellipsis ellipsis, String name) {
    JavaModel.checkIdentifier(name.toString());
    return api().elem(ByteProto.PARAMETER_SHORT, type.self(), ellipsis.self(), name);
  }

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected final Parameter parameter(TypeName type, String name) {
    JavaModel.checkIdentifier(name.toString());
    return api().elem(ByteProto.PARAMETER_SHORT, type, name);
  }

  /**
   * Sets the specified {@code type} as the return type of the receiving method
   * declaration.
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * // class or interface
   * method(returnType(Integer.class), name("a"))
   *
   * // array type
   * method(returnType(String[].class), name("b"))
   *
   * // primitive type
   * method(returnType(int.class), name("c"))
   *
   * // void
   * method(returnType(void.class), name("d"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * java.lang.Integer a() {}
   *
   * java.lang.String[] b() {}
   *
   * int c() {}
   *
   * void d() {}</pre>
   *
   * <p>
   * Use the {@link #returnType(TypeName)} method if the return type cannot be
   * represented by a class literal.
   *
   * @param type
   *        the value to be set as the return type
   *
   * @return the return type instruction
   *
   * @see #returnType(TypeName)
   *
   * @since 0.4.2
   */
  protected final ReturnType returnType(Class<?> type) {
    Objects.requireNonNull(type, "type == null");
    var api = api();
    return api.elem(ByteProto.RETURN_TYPE, typeName(type));
  }

  /**
   * Sets the specified {@code type} as the return type of the receiving method
   * declaration.
   *
   * <p>
   * This method must be used when the return type cannot be represented by a
   * class literal:
   *
   * <ul>
   * <li>the return type is a parameterized type;</li>
   * <li>the return type is a type variable;</li>
   * <li>the return type is a generated type; or</li>
   * <li>the return type will be only available to the generated code but is not
   * available to the code responsible for generating the code.</li>
   * </ul>
   *
   * <p>
   * The following Objectos Code:
   *
   * <pre>
   * // parameterized type
   * method(
   *   returnType(
   *     parameterizedType(
   *       classType(List.class),
   *       classType(String.class)
   *     )
   *   ), name("a"))
   *
   * // type variable
   * method(returnType(typeVariable("E")), name("b"))
   *
   * // generated type
   * method(returnType(classType("com.example", "Generated")), name("c"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * java.util.List&lt;java.lang.String&gt; a() {}
   *
   * E b() {}
   *
   * com.example.Generated c() {}</pre>
   *
   * @param type
   *        the value to be set as the return type
   *
   * @return the return type instruction
   *
   * @since 0.4.2
   */
  protected final ReturnType returnType(TypeName type) {
    return api().elem(ByteProto.RETURN_TYPE, type.self());
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

  private Object arrayTypeName(Class<?> type) {
    int dimCount = 1;

    Class<?> componentType = type.getComponentType();

    for (;;) {
      Class<?> next = componentType.getComponentType();

      if (next == null) {
        break;
      }

      dimCount++;

      componentType = next;
    }

    t(componentType);
    for (int i = 0; i < dimCount; i++) {
      dim();
    }
    return api().arrayTypeName(dimCount);
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

  private Object typeName(Class<?> type) {
    if (type == void.class) {
      return _void();
    } else if (type == boolean.class) {
      return _boolean();
    } else if (type == double.class) {
      return _double();
    } else if (type == int.class) {
      return _int();
    } else if (type.isPrimitive()) {
      throw new UnsupportedOperationException("Implement me");
    } else if (type.isArray()) {
      return arrayTypeName(type);
    } else {
      return t(type);
    }
  }

}