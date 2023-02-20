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
import java.util.Arrays;
import java.util.Objects;
import objectos.lang.Check;

/**
 * The {@code JavaTemplate} class provides a pure Java API for generating Java
 * source code.
 *
 * @since 0.4
 */
public abstract class JavaTemplate {

  /**
   * An {@link Instruction} that can be used with the instructions that take
   * arguments.
   *
   * @see JavaTemplate#invoke(String, ArgsPart...)
   */
  protected sealed interface ArgsPart extends Instruction {}

  /**
   * An {@link Instruction} that can be used with the
   * {@link JavaTemplate#block(BlockElement...)} method.
   */
  protected sealed interface BlockElement extends Instruction {}

  /**
   * An {@link Instruction} that can be used with the
   * {@link JavaTemplate#body(BodyElement...)} method.
   */
  protected sealed interface BodyElement extends Instruction {}

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected static abstract sealed class ClassTypeName extends ReferenceTypeName {

    private static final class OfClass extends ClassTypeName {
      private final Class<?> value;

      public OfClass(Class<?> value) {
        this.value = value;
      }

      @Override
      final void execute(InternalApi api) {
        api.classType(value);
        api.localToExternal();
      }
    }

    private static final class OfNames extends ClassTypeName {
      private final String packageName;

      private final String simpleName;

      private final String[] nested;

      OfNames(String packageName, String simpleName, String[] nested) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.nested = nested;
      }

      @Override
      final void execute(InternalApi api) {
        int count = 1; // simple name
        count += nested.length;

        api.extStart();
        api.protoAdd(ByteProto.CLASS_TYPE, api.object(packageName));
        api.protoAdd(count, api.object(simpleName));
        for (var n : nested) {
          api.protoAdd(api.object(n));
        }
      }
    }

    ClassTypeName() {}

    static ClassTypeName of(Class<?> type) {
      Check.argument(!type.isPrimitive(), """
      A `ClassTypeName` cannot be used to represent a primitive type.

      Use a `PrimitiveTypeName` instead.
      """);

      Check.argument(type != void.class, """
      A `ClassTypeName` cannot be used to represent the no-type (void).
      """);

      Check.argument(!type.isArray(), """
      A `ClassTypeName` cannot be used to represent array types.

      Use a `ArrayTypeName` instead.
      """);

      return new OfClass(type);
    }

    static ClassTypeName of(String packageName, String simpleName, String... nested) {
      JavaModel.checkPackageName(packageName.toString());
      JavaModel.checkSimpleName(simpleName.toString());

      for (int i = 0; i < nested.length; i++) {
        var nestedName = nested[i];

        if (nestedName == null) {
          throw new NullPointerException("nested[" + i + "] == null");
        }

        JavaModel.checkSimpleName(nestedName);
      }

      return new OfNames(packageName, simpleName, Arrays.copyOf(nested, nested.length));
    }

  }

  /**
   * Represents a partial template to be included as part of the enclosing
   * template.
   */
  @FunctionalInterface
  protected interface IncludeTarget {

    /**
     * Includes all instructions from this partial template into the including
     * template.
     */
    void execute();

  }

  /**
   * Represents an instruction that generates part of the output of a template.
   *
   * <p>
   * Unless noted references to a particular instruction MUST NOT be reused.
   */
  protected sealed interface Instruction {

    /**
     * Returns itself.
     *
     * <p>
     * Its sole purpose is to trigger an implicit null check.
     *
     * @return this instance
     */
    default Object self() { return this; }

  }

  /**
   * An {@link Instruction} that can be used with the
   * {@link JavaTemplate#method(MethodDeclarationInstruction...)} method.
   */
  protected sealed interface MethodDeclarationInstruction extends Instruction {}

  /**
   * Represents a modifier of the Java language.
   *
   * @since 0.4.2
   */
  protected static final class Modifier extends External implements MethodDeclarationInstruction {
    final int value;

    private Modifier(Keyword keyword) { this.value = keyword.ordinal(); }

    @Override
    final void execute(InternalApi api) {
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
  protected sealed interface ParameterElement extends Instruction {}

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected static final class ParameterizedTypeName extends ReferenceTypeName {
    private final ClassTypeName raw;

    private final ReferenceTypeName first;

    private final ReferenceTypeName[] rest;

    ParameterizedTypeName(ClassTypeName raw,
                          ReferenceTypeName first,
                          ReferenceTypeName[] rest) {
      this.raw = raw;
      this.first = first;
      this.rest = rest;
    }

    static ParameterizedTypeName of(
        ClassTypeName raw, ReferenceTypeName first, ReferenceTypeName... rest) {
      Objects.requireNonNull(raw, "raw == null");
      Objects.requireNonNull(first, "first == null");

      for (int i = 0; i < rest.length; i++) {
        var e = rest[i];

        if (e == null) {
          throw new NullPointerException("rest[" + i + "] == null");
        }
      }

      return new ParameterizedTypeName(raw, first, Arrays.copyOf(rest, rest.length));
    }

    @Override
    final void execute(InternalApi api) {
      Object[] many = rest;
      api.elemMany(ByteProto.PARAMETERIZED_TYPE, raw, first, many);
      api.localToExternal();
    }
  }

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected static final class PrimitiveTypeName extends TypeName {
    /**
     * The {@code boolean} primitive type.
     */
    public static final PrimitiveTypeName BOOLEAN = new PrimitiveTypeName(Keyword.BOOLEAN);

    /**
     * The {@code double} primitive type.
     */
    public static final PrimitiveTypeName DOUBLE = new PrimitiveTypeName(Keyword.DOUBLE);

    /**
     * The {@code int} primitive type.
     */
    public static final PrimitiveTypeName INT = new PrimitiveTypeName(Keyword.INT);

    private final int value;

    private PrimitiveTypeName(Keyword keyword) {
      value = keyword.ordinal();
    }

    @Override
    final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.PRIMITIVE_TYPE, value);
    }
  }

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected abstract static sealed class ReferenceTypeName extends TypeName {
    ReferenceTypeName() {}
  }

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected abstract static sealed class TypeName extends External
      implements
      MethodDeclarationInstruction,
      TypeParameterInstruction {
    TypeName() {}
  }

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected static final class TypeVariableName extends ReferenceTypeName {
    private final String name;

    TypeVariableName(String name) {
      this.name = name;
    }

    static TypeVariableName of(String name) {
      JavaModel.checkVarName(name.toString());
      return new TypeVariableName(name);
    }

    @Override
    final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.TYPE_VARIABLE, api.object(name));
    }
  }

  enum _Ext {
    INSTANCE;
  }

  enum _Include implements Include {
    INSTANCE;
  }

  static final class _Item implements
      AbstractModifier,
      AnnotationInst,
      ArrayAccess,
      ArrayDimension,
      ArrayInitializer,
      ArrayType,
      At,
      AutoImports,
      Block,
      Body,
      ClassInstanceCreationExpression,
      ClassKeyword,
      ClassTypeInstruction,
      ConstructorDeclaration,
      DeclarationName,
      Ellipsis,
      ElseKeyword,
      End,
      EnumConstant,
      EnumKeyword,
      EqualityOperator,
      ExplicitConstructorInvocation,
      ExpressionName,
      ExtendsKeyword,
      FinalModifier,
      Identifier,
      IfCondition,
      ImplementsKeyword,
      IntegerLiteral,
      InterfaceKeyword,
      MethodDeclaration,
      MethodDeclarationInstruction,
      MethodDeclarator,
      ModifiersElement,
      MethodInvocation,
      NewKeyword,
      NewLine,
      NullLiteral,
      PackageKeyword,
      Parameter,
      ParameterizedType,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      ReturnKeyword,
      ReturnType,
      SimpleAssigmentOperator,
      Statement,
      StaticModifier,
      StringLiteral,
      SuperKeyword,
      ThisKeyword,
      ThrowKeyword,
      TypeParameter,
      TypeParameterOld,
      TypeVariable,
      VarKeyword,
      OldVoidKeyword {

    private final InternalApi api;

    _Item(InternalApi api) { this.api = api; }

    @Override
    public final ArrayAccess dim(ExpressionPart e1) {
      api.elem(ByteProto.ARRAY_ACCESS, e1.self());
      return api.joinWith(ByteProto.DIM);
    }

    @Override
    public final MethodInvocation invoke(String methodName, ArgsPart... arguments) {
      JavaModel.checkMethodName(methodName.toString()); // implicit null check
      api.identifierext(methodName);
      Object[] many = Objects.requireNonNull(arguments, "arguments == null");
      api.elemMany(ByteProto.INVOKE, EXT, many);
      return api.joinWith(ByteProto.DOT);
    }

    @Override
    public final ExpressionName n(String name) {
      JavaModel.checkSimpleName(name.toString());
      api.itemAdd(ByteProto.EXPRESSION_NAME, api.object(name));
      return api.joinWith(ByteProto.DOT);
    }

  }

  sealed interface AbstractModifier extends BodyElement {}

  sealed interface AnnotationInst extends MethodDeclarationInstruction {}

  sealed interface AnyType extends BodyElement, BlockElement, ParameterElement {}

  sealed interface ArrayAccess extends PrimaryNoNewArray {}

  sealed interface ArrayDimension extends ArrayTypeElement {}

  sealed interface ArrayInitializer extends BodyElement, VariableInitializer {}

  sealed interface ArrayType extends BodyElement, ReferenceType {}

  sealed interface ArrayTypeComponent {}

  sealed interface ArrayTypeElement {}

  sealed interface At extends BodyElement {}

  sealed interface AtElement extends Instruction {}

  sealed interface AutoImports extends Instruction {}

  sealed interface Block extends BlockElement, BodyElement {}

  sealed interface Body extends BodyElement {}

  sealed interface ClassInstanceCreationExpression extends PrimaryNoNewArray {}

  sealed interface ClassKeyword extends BodyElement {}

  sealed interface ClassOrParameterizedType extends Instruction {}

  sealed interface ClassTypeInstruction
      extends ArgsPart, ClassOrParameterizedType, ReferenceType, TypeParameterBound {
    ExpressionName n(String name);
  }

  sealed interface ConstructorDeclaration extends BodyElement {}

  sealed interface DeclarationName extends MethodDeclarationInstruction {}

  sealed interface Ellipsis extends ParameterElement {}

  sealed interface ElseKeyword extends BlockElement {}

  sealed interface End extends ArgsPart, BlockElement {}

  sealed interface EnumConstant extends BodyElement {}

  sealed interface EnumKeyword extends BodyElement {}

  sealed interface EqualityOperator extends ExpressionPart {}

  sealed interface ExplicitConstructorInvocation extends BlockElement {}

  sealed interface ExpressionName extends CanArrayAccess, CanInvoke, ExpressionPart {
    ExpressionName n(String name);
  }

  sealed interface ExpressionPart
      extends ArgsPart, BlockElement, BodyElement, VariableInitializer {}

  sealed interface ExtendsKeyword extends BodyElement {}

  abstract static sealed class External {
    External() {}

    abstract void execute(InternalApi api);
  }

  sealed interface FinalModifier extends BodyElement {}

  sealed interface Identifier extends BlockElement, BodyElement, ParameterElement {}

  sealed interface IfCondition extends BlockElement {}

  sealed interface ImplementsKeyword extends BodyElement {}

  sealed interface Include
      extends
      ArgsPart, BlockElement, BodyElement, ExpressionPart, ParameterElement, VariableInitializer {}

  sealed interface IntegerLiteral extends Literal {}

  sealed interface InterfaceKeyword extends BodyElement {}

  sealed interface Literal extends AtElement, ExpressionPart {}

  sealed interface MethodDeclaration extends BodyElement {}

  sealed interface MethodDeclarator extends BodyElement {}

  sealed interface MethodInvocation extends PrimaryNoNewArray {}

  sealed interface ModifiersElement extends MethodDeclarationInstruction {}

  sealed interface NewKeyword extends BlockElement {}

  sealed interface NewLine extends ArgsPart, BlockElement {}

  sealed interface NullLiteral extends ExpressionPart {}

  @Deprecated
  sealed interface OldVoidKeyword extends BodyElement {}

  sealed interface PackageKeyword extends Instruction {}

  sealed interface Parameter extends MethodDeclarationInstruction {}

  sealed interface ParameterizedType extends ClassOrParameterizedType, ReferenceType {}

  sealed interface PrimitiveType extends AnyType, ArrayTypeComponent, BodyElement {}

  sealed interface PrivateModifier extends AccessModifier {}

  sealed interface ProtectedModifier extends AccessModifier {}

  sealed interface PublicModifier extends AccessModifier {}

  sealed interface ReferenceType extends AnyType, ArrayTypeComponent {}

  sealed interface ReturnKeyword extends BlockElement {}

  sealed interface ReturnType extends MethodDeclarationInstruction {}

  sealed interface SimpleAssigmentOperator extends ExpressionPart {}

  sealed interface Statement extends BlockElement {}

  sealed interface StaticModifier extends BodyElement {}

  sealed interface StringLiteral extends Literal, PrimaryNoNewArray {}

  sealed interface SuperKeyword extends BlockElement {}

  sealed interface ThisKeyword extends PrimaryNoNewArray {}

  sealed interface ThrowKeyword extends BlockElement {}

  sealed interface TypeParameter extends MethodDeclarationInstruction {}

  @Deprecated
  sealed interface TypeParameterBound extends Instruction {}

  sealed interface TypeParameterInstruction extends Instruction {}

  @Deprecated
  sealed interface TypeParameterOld extends BodyElement {}

  sealed interface TypeVariable extends ReferenceType {}

  sealed interface VariableInitializer {}

  sealed interface VarKeyword extends BlockElement {}

  private sealed interface AccessModifier extends BodyElement {}

  private sealed interface CanArrayAccess {
    ArrayAccess dim(ExpressionPart e1);
  }

  private sealed interface CanInvoke {
    MethodInvocation invoke(String methodName, ArgsPart... arguments);
  }

  private sealed interface Primary extends CanInvoke, ExpressionPart, MethodDeclarationInstruction {
    ExpressionName n(String name);
  }

  private sealed interface PrimaryNoNewArray extends CanArrayAccess, Primary {}

  private static final class VoidKeyword extends External implements MethodDeclarationInstruction {
    @Override
    final void execute(InternalApi api) {
      api.extStart();
      api.protoAdd(ByteProto.VOID, ByteProto.NOOP);
    }
  }

  protected static final MethodDeclarationInstruction VOID = new VoidKeyword();

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

  static final _Ext EXT = _Ext.INSTANCE;

  static final _Include INCLUDE = _Include.INSTANCE;

  private InternalApi api;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected static ClassTypeName classType(Class<?> type) {
    return ClassTypeName.of(type);
  }

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected static ClassTypeName classType(
      String packageName, String simpleName, String... nested) {
    return ClassTypeName.of(packageName, simpleName, nested);
  }

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected static ParameterizedTypeName parameterizedType(
      ClassTypeName raw, ReferenceTypeName first, ReferenceTypeName... rest) {
    return ParameterizedTypeName.of(raw, first, rest);
  }

  /**
   * TODO
   *
   * @since 0.4.2
   */
  protected static TypeVariableName typeVariable(String name) {
    return TypeVariableName.of(name);
  }

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
  protected final AbstractModifier _abstract() {
    return modifier(Keyword.ABSTRACT);
  }

  /**
   * The {@code boolean} primitive type.
   *
   * @return the {@code boolean} primitive type
   */
  protected final PrimitiveType _boolean() {
    return primitiveType(Keyword.BOOLEAN);
  }

  /**
   * TODO
   */
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
  protected final ElseKeyword _else() {
    return api().itemAdd(ByteProto.ELSE, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  protected final EnumKeyword _enum(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.ENUM, api.object(name));
  }

  /**
   * TODO
   */
  protected final ExtendsKeyword _extends() {
    return api().itemAdd(ByteProto.EXTENDS, ByteProto.NOOP);
  }

  /**
   * The {@code final} modifier.
   *
   * @return the {@code final} modifier
   */
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
  protected final IfCondition _if(ExpressionPart e1, ExpressionPart e2, ExpressionPart e3,
      ExpressionPart e4, ExpressionPart e5, ExpressionPart e6) {
    return api().elem(ByteProto.IF_CONDITION, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self(), e6.self());
  }

  /**
   * TODO
   */
  protected final ImplementsKeyword _implements() {
    return api().itemAdd(ByteProto.IMPLEMENTS, ByteProto.NOOP);
  }

  /**
   * The {@code int} primitive type.
   *
   * @return the {@code int} primitive type
   */
  protected final PrimitiveType _int() {
    return primitiveType(Keyword.INT);
  }

  /**
   * TODO
   */
  protected final InterfaceKeyword _interface(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.INTERFACE, api.object(name));
  }

  /**
   * TODO
   */
  protected final ClassInstanceCreationExpression _new(ClassOrParameterizedType type) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self()
    );
  }

  /**
   * TODO
   */
  protected final ClassInstanceCreationExpression _new(ClassOrParameterizedType type,
      ArgsPart arg1) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self(),
      arg1.self()
    );
  }

  /**
   * TODO
   */
  protected final ClassInstanceCreationExpression _new(ClassOrParameterizedType type,
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
  protected final NullLiteral _null() {
    return api().itemAdd(ByteProto.NULL_LITERAL, ByteProto.NOOP);
  }

  /**
   * TODO
   */
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
  protected final PrivateModifier _private() {
    return modifier(Keyword.PRIVATE);
  }

  /**
   * The {@code protected} modifier.
   *
   * @return the {@code protected} modifier
   */
  protected final ProtectedModifier _protected() {
    return modifier(Keyword.PROTECTED);
  }

  /**
   * The {@code public} modifier.
   *
   * @return the {@code public} modifier
   */
  protected final PublicModifier _public() {
    return modifier(Keyword.PUBLIC);
  }

  /**
   * TODO
   */
  protected final ReturnKeyword _return() {
    return api().itemAdd(ByteProto.RETURN, ByteProto.NOOP);
  }

  /**
   * The {@code static} modifier.
   *
   * @return the {@code static} modifier
   */
  protected final StaticModifier _static() {
    return modifier(Keyword.STATIC);
  }

  /**
   * TODO
   */
  protected final SuperKeyword _super() {
    return api().itemAdd(ByteProto.SUPER, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  protected final ExplicitConstructorInvocation _super(ArgsPart e1) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self());
  }

  /**
   * TODO
   */
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self());
  }

  /**
   * TODO
   */
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2,
      ArgsPart e3) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self(),
      e3.self());
  }

  /**
   * TODO
   */
  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2,
      ArgsPart e3, ArgsPart e4) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self(),
      e3.self(), e4.self());
  }

  /**
   * TODO
   */
  protected final ThisKeyword _this() {
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
  protected final ThrowKeyword _throw() {
    return api().itemAdd(ByteProto.THROW, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  protected final VarKeyword _var() {
    return api().itemAdd(ByteProto.VAR, ByteProto.NOOP);
  }

  /**
   * TODO
   */
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
  protected final AnnotationInst annotation(Class<? extends Annotation> annotationType) {
    Objects.requireNonNull(annotationType, "annotationType == null");
    var api = api();
    return api.elem(ByteProto.ANNOTATION, api.classType(annotationType));
  }

  /**
   * TODO
   */
  protected final At at(ClassTypeInstruction annotationType) {
    return api().elem(ByteProto.ANNOTATION, annotationType.self());
  }

  /**
   * TODO
   */
  protected final At at(ClassTypeInstruction annotationType, AtElement e1) {
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
  protected final Block block() {
    return api().elem(ByteProto.BLOCK);
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement e1) {
    return api().elem(ByteProto.BLOCK, e1.self());
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement... elements) {
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.BLOCK, many);
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement e1, BlockElement e2) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self());
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self());
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6, BlockElement e7) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self(), e7.self());
  }

  /**
   * TODO
   */
  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6, BlockElement e7, BlockElement e8) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self(), e7.self(), e8.self());
  }

  /**
   * TODO
   */
  protected final Body body() {
    return api().elem(ByteProto.BODY);
  }

  /**
   * TODO
   */
  protected final Body body(BodyElement e1) {
    return api().elem(ByteProto.BODY, e1.self());
  }

  /**
   * TODO
   */
  protected final Body body(BodyElement... elements) {
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.BODY, many);
  }

  /**
   * TODO
   */
  protected final Body body(BodyElement e1, BodyElement e2) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   */
  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   */
  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4,
      BodyElement e5) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self());
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
  protected final ConstructorDeclaration constructor() {
    return api().elem(ByteProto.CONSTRUCTOR);
  }

  /**
   * TODO
   */
  protected final ConstructorDeclaration constructor(ParameterElement e1) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self());
  }

  /**
   * TODO
   */
  protected final ConstructorDeclaration constructor(ParameterElement... elements) {
    Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.CONSTRUCTOR, elements);
  }

  /**
   * TODO
   */
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self());
  }

  /**
   * TODO
   */
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self());
  }

  /**
   * TODO
   */
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self(), e4.self());
  }

  /**
   * TODO
   */
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4, ParameterElement e5) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self(), e4.self(), e5.self());
  }

  /**
   * TODO
   */
  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4, ParameterElement e5, ParameterElement e6) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(),
      e3.self(), e4.self(), e5.self(), e6.self());
  }

  /**
   * TODO
   */
  protected abstract void definition();

  /**
   * TODO
   */
  protected final ArrayDimension dim() {
    return api().itemAdd(ByteProto.ARRAY_DIMENSION, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  protected final ArrayAccess dim(ExpressionPart e1) {
    return api().elem(ByteProto.ARRAY_ACCESS, e1.self());
  }

  /**
   * TODO
   */
  protected final Ellipsis ellipsis() {
    return api().itemAdd(ByteProto.ELLIPSIS, ByteProto.NOOP);
  }

  /**
   * TODO
   */
  protected final End end() {
    return stop();
  }

  /**
   * TODO
   */
  protected final EnumConstant enumConstant(String name) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT);
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
   * The {@code ==} (equal to) operator.
   *
   * @return the {@code ==} (equal to) operator
   *
   * @since 0.4.1
   */
  protected final EqualityOperator equalTo() {
    return api().itemAdd(ByteProto.EQUALITY_OPERATOR, Symbol.EQUAL_TO.ordinal());
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
  protected final SimpleAssigmentOperator gets() {
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
   */
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
   *   RETURN, s("Objectos Code")
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
   * static final ClassTypeName String$ = classType(String.class);
   *
   * method(
   *   annotation(Override.class),
   *   PUBLIC, FINAL, String$, name("toString"),
   *   RETURN, s("Objectos Code")
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
  protected final MethodDeclarator method(String methodName) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD, EXT);
  }

  /**
   * TODO
   */
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
   * Sets the {@code name} of a declaration. If the declaration is a method
   * then this instruction sets the method's name. If the declaration is a field
   * then this instruction sets the field's name. And so on.
   *
   * <p>
   * The following Objectos Code method declaration:
   *
   * <pre>
   * method(name("example"))</pre>
   *
   * <p>
   * Generates the following Java code:
   *
   * <pre>
   * void example() {}</pre>
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
  protected final NewLine nl() {
    return api().itemAdd(ByteProto.NEW_LINE, ByteProto.NOOP);
  }

  /**
   * The {@code !=} (not equal to) operator.
   *
   * @return the {@code !=} (not equal to) operator
   *
   * @since 0.4.1
   */
  protected final EqualityOperator notEqualTo() {
    return api().itemAdd(ByteProto.EQUALITY_OPERATOR, Symbol.NOT_EQUAL_TO.ordinal());
  }

  /**
   * TODO
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
    return api.elem(ByteProto.RETURN_TYPE, type.self());
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
  protected final ClassTypeInstruction t(Class<?> type) {
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
      ClassTypeInstruction rawType,
      ReferenceType arg1) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1);
  }

  /**
   * TODO
   */
  protected final ParameterizedType t(
      ClassTypeInstruction rawType,
      ReferenceType arg1, ReferenceType arg2) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1, arg2);
  }

  /**
   * TODO
   */
  protected final ParameterizedType t(
      ClassTypeInstruction rawType,
      ReferenceType arg1, ReferenceType arg2, ReferenceType arg3) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1, arg2, arg3);
  }

  /**
   * TODO
   */
  protected final ClassTypeInstruction t(String packageName, String simpleName) {
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
  protected final ClassTypeInstruction t(String packageName, String simpleName1,
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
  protected final TypeParameter typeParameter(String name, TypeParameterInstruction... bounds) {
    JavaModel.checkVarName(name.toString());
    Object[] many = Objects.requireNonNull(bounds, "bounds == null");
    return api().elemMany(ByteProto.TYPE_PARAMETER, name, many);
  }

  /**
   * TODO
   *
   * @since 0.4.3.1
   */
  protected final MethodInvocation v(String methodName, ArgsPart... arguments) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    Object[] many = Objects.requireNonNull(arguments, "arguments == null");
    return api.elemMany(ByteProto.METHOD_INVOCATION, methodName, many);
  }

  InternalApi api() {
    Check.state(api != null, """
    An InternalApi instance was not set.

    Are you trying to execute the method directly?
    Please not that this method should only be invoked inside a definition() method.
    """);

    return api;
  }

  final void execute(InternalApi api) {
    Check.state(this.api == null, """
    Another evaluation is already in progress.
    """);

    this.api = api;

    try {
      definition();
    } finally {
      this.api = null;
    }
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
    return api.arrayTypeName(dimCount);
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