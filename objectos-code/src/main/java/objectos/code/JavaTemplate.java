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
import objectos.code.tmpl.Argument;
import objectos.code.tmpl.ArrayTypeComponent;
import objectos.code.tmpl.BlockInstruction;
import objectos.code.tmpl.ClassDeclarationInstruction;
import objectos.code.tmpl.ClassOrInterfaceDeclarationInstruction;
import objectos.code.tmpl.ClassOrParameterizedTypeName;
import objectos.code.tmpl.ConstructorDeclarationInstruction;
import objectos.code.tmpl.DeclarationName;
import objectos.code.tmpl.EnumConstantInstruction;
import objectos.code.tmpl.EnumDeclarationInstruction;
import objectos.code.tmpl.ExecutableDeclarationInstruction;
import objectos.code.tmpl.ExpressionPart;
import objectos.code.tmpl.ExtendsClauseInstruction;
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
import objectos.code.tmpl.ParameterInstruction;
import objectos.code.tmpl.StatementPart;
import objectos.code.tmpl.TypeDeclarationInstruction;
import objectos.code.tmpl.TypeName;
import objectos.code.tmpl.TypeParameter;
import objectos.code.tmpl.VariableInitializer;

/**
 * The {@code JavaTemplate} class provides a pure Java API for generating Java
 * source code.
 *
 * @since 0.4
 */
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
      AutoImports,
      Block,
      ClassDeclaration,
      ClassInstanceCreationExpression,
      ClassKeyword,
      ClassOrInterfaceDeclarationInstruction,
      ClassTypeInstruction,
      ClassTypeWithArgs,
      ConstructorDeclaration,
      ConstructorDeclarationInstruction,
      DeclarationName,
      EnumConstant,
      EnumDeclaration,
      ExplicitConstructorInvocation,
      ExpressionName,
      FieldDeclaration,
      FieldDeclarationInstruction,
      Identifier,
      IfCondition,
      ImplementsClause,
      IntegerLiteral,
      InterfaceDeclaration,
      MethodDeclaration,
      MethodDeclarationInstruction,
      MethodInvocation,
      ExecutableDeclarationInstruction,
      ParameterInstruction,
      ParameterizedType,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      OldStatement,
      StaticModifier,
      StringLiteral,
      TypeDeclarationInstruction,
      TypeParameter,
      TypeVariable {

    INSTANCE;

  }

  interface AbstractModifier {}

  /**
   * @since 0.4.4
   */
  interface AnyDeclarationInstruction
      extends
      ClassDeclarationInstruction,
      ConstructorDeclarationInstruction,
      EnumDeclarationInstruction,
      FieldDeclarationInstruction,
      InterfaceDeclarationInstruction,
      MethodDeclarationInstruction {}

  interface AnyType extends BlockInstruction {}

  interface ArrayAccess extends ExpressionPart {}

  interface ArrayDimension extends ArrayTypeElement {}

  interface ArrayInitializer extends VariableInitializer {}

  interface ArrayInitializerValue extends AnnotationInstruction, VariableInitializer {}

  interface ArrayType extends ReferenceType {}

  interface ArrayTypeElement {}

  interface AutoImports extends Instruction {}

  interface Block extends BlockInstruction, StatementPart {}

  /**
   * @since 0.4.4
   */
  interface ClassDeclaration
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      InterfaceDeclarationInstruction {}

  interface ClassInstanceCreationExpression extends PrimaryNoNewArray {}

  interface ClassKeyword {}

  interface ClassTypeInstruction extends MethodDeclarationInstruction {}

  interface ClassTypeWithArgs extends ClassTypeInstruction {}

  interface ConstructorDeclaration
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction {}

  interface EnumConstant extends EnumDeclarationInstruction {}

  /**
   * @since 0.4.4
   */
  interface EnumDeclaration
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      InterfaceDeclarationInstruction {}

  interface ExplicitConstructorInvocation extends BlockInstruction {}

  interface ExpressionName extends ExpressionPart {}

  interface FieldDeclaration
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      InterfaceDeclarationInstruction {}

  interface Identifier extends BlockInstruction {}

  interface IfCondition extends BlockInstruction {}

  interface IntegerLiteral extends Literal {}

  /**
   * @since 0.4.4
   */
  interface InterfaceDeclaration
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      InterfaceDeclarationInstruction {}

  interface Literal extends ExpressionPart {}

  interface MethodDeclaration
      extends
      ClassDeclarationInstruction,
      EnumDeclarationInstruction,
      InterfaceDeclarationInstruction {}

  interface MethodInvocation extends PrimaryNoNewArray {}

  interface OldStatement extends BlockInstruction {}

  interface ParameterizedType extends ReferenceType {}

  interface PrimitiveType extends AnyType, ArrayTypeComponent {}

  interface PrivateModifier extends AccessModifier {}

  interface ProtectedModifier extends AccessModifier {}

  interface PublicModifier extends AccessModifier {}

  interface ReferenceType extends AnyType, ArrayTypeComponent {}

  interface StaticModifier {}

  interface StringLiteral extends Literal, PrimaryNoNewArray {}

  interface TypeVariable extends ReferenceType {}

  private interface AccessModifier {}

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
   *
   * @since 0.4.4
   */
  protected final EnumConstant enumConstant(EnumConstantInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
    return api().elemMany(ByteProto.ENUM_CONSTANT, many);
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
      extendsClause(ExtendsClauseInstruction... contents) {
    Object[] many = Objects.requireNonNull(contents, "contents == null");
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

}