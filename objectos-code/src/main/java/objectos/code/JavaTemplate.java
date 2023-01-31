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

import java.util.Objects;
import objectos.lang.Check;

public abstract class JavaTemplate {

  protected sealed interface BlockElement extends Element {}

  protected sealed interface BodyElement extends Element {}

  protected sealed interface Element {
    /**
     * Triggers implicit null check.
     */
    default Object self() { return this; }
  }

  protected sealed interface EnumConstant extends BodyElement {}

  @FunctionalInterface
  protected interface IncludeTarget {
    void execute();
  }

  protected sealed interface TypeParameterBound extends Element {}

  protected sealed interface VariableInitializer {}

  enum _Ext {
    INSTANCE;
  }

  enum _Include implements Include {
    INSTANCE;
  }

  final class _Item implements
      AbstractModifier,
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
      ClassType,
      ConstructorDeclaration,
      Ellipsis,
      End,
      EnumConstant,
      EnumKeyword,
      ExplicitConstructorInvocation,
      ExpressionName,
      ExtendsKeyword,
      FinalModifier,
      Identifier,
      ImplementsKeyword,
      IntegerLiteral,
      InterfaceKeyword,
      MethodDeclaration,
      Invoke,
      NewKeyword,
      NewLine,
      PackageKeyword,
      ParameterizedType,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      ReturnKeyword,
      SimpleAssigmentOperator,
      StaticModifier,
      StringLiteral,
      SuperKeyword,
      ThisKeyword,
      TypeParameter,
      TypeVariable,
      VarKeyword,
      VoidKeyword {}

  sealed interface AbstractModifier extends BodyElement {}

  sealed interface AnyType extends BodyElement, ParameterElement {}

  sealed interface ArgsPart extends Element {}

  sealed interface ArrayAccess extends ExpressionPart {}

  sealed interface ArrayDimension extends ArrayTypeElement {}

  sealed interface ArrayInitializer extends BodyElement, VariableInitializer {}

  sealed interface ArrayType extends BodyElement, ReferenceType {}

  sealed interface ArrayTypeComponent {}

  sealed interface ArrayTypeElement {}

  sealed interface At extends BodyElement {}

  sealed interface AtElement extends Element {}

  sealed interface AutoImports extends Element {}

  sealed interface Block extends BodyElement, StatementWithoutTrailingSubstatement {}

  sealed interface Body extends BodyElement {}

  sealed interface ClassInstanceCreationExpression extends ExpressionPart {}

  sealed interface ClassKeyword extends BodyElement {}

  sealed interface ClassType extends ReferenceType, TypeParameterBound {}

  sealed interface ConstructorDeclaration extends BodyElement {}

  sealed interface Ellipsis extends ParameterElement {}

  sealed interface End extends ArgsPart, BlockElement {}

  sealed interface EnumKeyword extends BodyElement {}

  sealed interface ExplicitConstructorInvocation extends BlockElement {}

  sealed interface ExpressionName extends ExpressionPart {}

  sealed interface ExpressionPart
      extends ArgsPart, BlockElement, BodyElement, VariableInitializer {}

  sealed interface ExtendsKeyword extends BodyElement {}

  sealed interface FinalModifier extends BodyElement {}

  sealed interface Identifier extends BodyElement, ParameterElement {}

  sealed interface ImplementsKeyword extends BodyElement {}

  sealed interface Include extends BlockElement, BodyElement {}

  sealed interface IntegerLiteral extends Literal {}

  sealed interface InterfaceKeyword extends BodyElement {}

  sealed interface Invoke extends ExpressionPart {}

  sealed interface Literal extends AtElement, ExpressionPart {}

  sealed interface MethodDeclaration extends BodyElement {}

  sealed interface NewKeyword extends BlockElement {}

  sealed interface NewLine extends ArgsPart, BlockElement {}

  sealed interface PackageKeyword extends Element {}

  sealed interface ParameterElement extends Element {}

  sealed interface ParameterizedType extends ReferenceType {}

  sealed interface PrimitiveType extends AnyType, BodyElement,
      /* to remove */
      ArrayTypeComponent {}

  sealed interface PrivateModifier extends AccessModifier {}

  sealed interface ProtectedModifier extends AccessModifier {}

  sealed interface PublicModifier extends AccessModifier {}

  sealed interface ReferenceType extends AnyType, ArrayTypeComponent {}

  sealed interface ReturnKeyword extends BlockElement {}

  sealed interface SimpleAssigmentOperator extends ExpressionPart {}

  sealed interface Statement extends Element {}

  sealed interface StatementWithoutTrailingSubstatement extends Statement {}

  sealed interface StaticModifier extends BodyElement {}

  sealed interface StringLiteral extends Literal {}

  sealed interface SuperKeyword extends BlockElement {}

  sealed interface ThisKeyword extends ExpressionPart {}

  sealed interface TypeParameter extends BodyElement {}

  sealed interface TypeVariable extends ReferenceType {}

  sealed interface VarKeyword extends BlockElement {}

  sealed interface VoidKeyword extends BodyElement {}

  private sealed interface AccessModifier extends BodyElement {}

  static final _Ext EXT = _Ext.INSTANCE;

  static final _Include INCLUDE = _Include.INSTANCE;

  final _Item item = new _Item();

  private InternalApi api;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  @Override
  public String toString() {
    var out = new StringBuilder();

    var sink = JavaSink.ofStringBuilder(out);

    sink.eval(this);

    return out.toString();
  }

  protected final AbstractModifier _abstract() {
    return modifier(Keyword.ABSTRACT);
  }

  protected final PrimitiveType _boolean() {
    return primitiveType(Keyword.BOOLEAN);
  }

  protected final ClassKeyword _class(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check
    var api = api();
    return api.itemAdd(ByteProto.CLASS, api.object(name));
  }

  protected final PrimitiveType _double() {
    return primitiveType(Keyword.DOUBLE);
  }

  protected final EnumKeyword _enum(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.itemAdd(ByteProto.ENUM, api.object(name));
  }

  protected final ExtendsKeyword _extends() {
    return api().itemAdd(ByteProto.EXTENDS, ByteProto.NOOP);
  }

  protected final FinalModifier _final() {
    return api().itemAdd(ByteProto.MODIFIER, Keyword.FINAL.ordinal());
  }

  protected final ImplementsKeyword _implements() {
    return api().itemAdd(ByteProto.IMPLEMENTS, ByteProto.NOOP);
  }

  protected final PrimitiveType _int() {
    return primitiveType(Keyword.INT);
  }

  protected final InterfaceKeyword _interface(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.itemAdd(ByteProto.INTERFACE, api.object(name));
  }

  protected final ClassInstanceCreationExpression _new(ClassType type) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self()
    );
  }

  protected final ClassInstanceCreationExpression _new(ClassType type,
      ArgsPart arg1) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self(),
      arg1.self()
    );
  }

  protected final ClassInstanceCreationExpression _new(ClassType type,
      ArgsPart arg1, ArgsPart arg2) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self(),
      arg1.self(), arg2.self()
    );
  }

  protected final PackageKeyword _package(String packageName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check

    var api = api();

    api.autoImports.packageName(packageName);

    return api.itemAdd(ByteProto.PACKAGE, api.object(packageName));
  }

  protected final PrivateModifier _private() {
    return modifier(Keyword.PRIVATE);
  }

  protected final ProtectedModifier _protected() {
    return modifier(Keyword.PROTECTED);
  }

  protected final PublicModifier _public() {
    return modifier(Keyword.PUBLIC);
  }

  protected final ReturnKeyword _return() {
    return api().itemAdd(ByteProto.RETURN, ByteProto.NOOP);
  }

  protected final StaticModifier _static() {
    return modifier(Keyword.STATIC);
  }

  protected final SuperKeyword _super() {
    return api().itemAdd(ByteProto.SUPER, ByteProto.NOOP);
  }

  protected final ExplicitConstructorInvocation _super(ArgsPart e1) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self());
  }

  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self());
  }

  protected final ExplicitConstructorInvocation _super(ArgsPart e1, ArgsPart e2,
      ArgsPart e3) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self(), e3.self());
  }

  protected final ThisKeyword _this() {
    return api().itemAdd(ByteProto.THIS, ByteProto.NOOP);
  }

  protected final VarKeyword _var() {
    return api().itemAdd(ByteProto.VAR);
  }

  protected final VoidKeyword _void() {
    return api().itemAdd(ByteProto.VOID, ByteProto.NOOP);
  }

  protected final ArrayInitializer ainit() {
    return api().elem(ByteProto.ARRAY_INITIALIZER);
  }

  protected final ArrayInitializer ainit(
      VariableInitializer e1) {
    return api().elem(ByteProto.ARRAY_INITIALIZER, e1);
  }

  protected final ArrayInitializer ainit(
      VariableInitializer e1, VariableInitializer e2) {
    return api().elem(ByteProto.ARRAY_INITIALIZER, e1, e2);
  }

  protected final At at(ClassType annotationType) {
    return api().elem(ByteProto.ANNOTATION, annotationType.self());
  }

  protected final At at(ClassType annotationType, AtElement e1) {
    return api().elem(ByteProto.ANNOTATION, annotationType.self(), e1.self());
  }

  protected final AutoImports autoImports() {
    var api = api();
    api.autoImports.enable();
    return api.itemAdd(ByteProto.AUTO_IMPORTS, ByteProto.NOOP);
  }

  protected final Block block() {
    return api().elem(ByteProto.BLOCK);
  }

  protected final Block block(BlockElement e1) {
    return api().elem(ByteProto.BLOCK, e1.self());
  }

  protected final Block block(BlockElement... elements) {
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.BLOCK, many);
  }

  protected final Block block(BlockElement e1, BlockElement e2) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self());
  }

  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self());
  }

  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self());
  }

  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self());
  }

  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self());
  }

  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6, BlockElement e7) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self(), e7.self());
  }

  protected final Block block(BlockElement e1, BlockElement e2, BlockElement e3, BlockElement e4,
      BlockElement e5, BlockElement e6, BlockElement e7, BlockElement e8) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self(), e6.self(), e7.self(), e8.self());
  }

  protected final Body body() {
    return api().elem(ByteProto.BODY);
  }

  protected final Body body(BodyElement e1) {
    return api().elem(ByteProto.BODY, e1.self());
  }

  protected final Body body(BodyElement... elements) {
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.BODY, many);
  }

  protected final Body body(BodyElement e1, BodyElement e2) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self());
  }

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self());
  }

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self(), e4.self());
  }

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4,
      BodyElement e5) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self());
  }

  protected final void code(Element... elements) {
    // no-op
  }

  protected final ConstructorDeclaration constructor() {
    return api().elem(ByteProto.CONSTRUCTOR);
  }

  protected final ConstructorDeclaration constructor(ParameterElement e1) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self());
  }

  protected final ConstructorDeclaration constructor(ParameterElement... elements) {
    Objects.requireNonNull(elements, "elements == null");
    return api().elemMany(ByteProto.CONSTRUCTOR, elements);
  }

  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self());
  }

  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self());
  }

  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self(), e4.self());
  }

  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4, ParameterElement e5) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(), e3.self(), e4.self(), e5.self());
  }

  protected final ConstructorDeclaration constructor(ParameterElement e1, ParameterElement e2,
      ParameterElement e3, ParameterElement e4, ParameterElement e5, ParameterElement e6) {
    return api().elem(ByteProto.CONSTRUCTOR, e1.self(), e2.self(),
      e3.self(), e4.self(), e5.self(), e6.self());
  }

  protected abstract void definition();

  protected final ArrayDimension dim() {
    return api().itemAdd(ByteProto.ARRAY_DIMENSION, ByteProto.NOOP);
  }

  protected final ArrayAccess dim(ExpressionPart e1) {
    return api().elem(ByteProto.ARRAY_ACCESS, e1.self());
  }

  protected final Ellipsis ellipsis() {
    return api().itemAdd(ByteProto.ELLIPSIS, ByteProto.NOOP);
  }

  protected final End end() {
    return stop();
  }

  protected final EnumConstant enumConstant(String name) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    //    int id = api.identifier(name);
    //    api.elemstart(ByteProto.ENUM_CONSTANT);
    //    api.elemproto(id);
    //    api.elemargs();
    return api.elemend();
  }

  protected final EnumConstant enumConstant(String name,
      ArgsPart e1) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    //    int id = api.identifier(name);
    //    api.elemstart(ByteProto.ENUM_CONSTANT,
    //      e1.self());
    //    api.elemproto(id);
    //    api.elemargs(e1);
    return api.elemend();
  }

  protected final EnumConstant enumConstant(String name,
      ArgsPart e1, ArgsPart e2) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    //    int id = api.identifier(name);
    //    api.elemstart(ByteProto.ENUM_CONSTANT,
    //      e1.self(), e2.self());
    //    api.elemproto(id);
    //    api.elemargs(e1, e2);
    return api.elemend();
  }

  protected final EnumConstant enumConstant(String name,
      ArgsPart e1, ArgsPart e2, ArgsPart e3) {
    JavaModel.checkIdentifier(name.toString()); // implicit null check
    var api = api();
    //    int id = api.identifier(name);
    //    api.elemstart(ByteProto.ENUM_CONSTANT,
    //      e1.self(), e2.self(), e3.self());
    //    api.elemproto(id);
    //    api.elemargs(e1, e2, e3);
    return api.elemend();
  }

  protected final SimpleAssigmentOperator gets() {
    return api().itemAdd(ByteProto.ASSIGNMENT_OPERATOR, Symbol.ASSIGNMENT.ordinal());
  }

  protected final IntegerLiteral i(int value) {
    var s = Integer.toString(value);

    var api = api();

    return api.itemAdd(ByteProto.PRIMITIVE_LITERAL, api.object(s));
  }

  protected final Identifier id(String name) {
    JavaModel.checkIdentifier(name);
    var api = api();
    return api.itemAdd(ByteProto.IDENTIFIER, api.object(name));
  }

  protected final Include include(IncludeTarget target) {
    var api = api();
    api.lambdastart();
    target.execute(); // implicit null-check
    api.lambdaend();
    return INCLUDE;
  }

  protected final Include include(JavaTemplate template) {
    var api = api();
    api.lambdastart();
    template.execute(api);
    api.lambdaend();
    return INCLUDE;
  }

  protected final Invoke invoke(
      String methodName) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT);
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self());
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart... elements) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api.elemMany(ByteProto.INVOKE, EXT, many);
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self());
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self());
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self());
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self());
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5,
      ArgsPart e6) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self(),
      e6.self());
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5,
      ArgsPart e6, ArgsPart e7) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self(),
      e6.self(), e7.self());
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5,
      ArgsPart e6, ArgsPart e7, ArgsPart e8) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self(),
      e6.self(), e7.self(), e8.self());
  }

  protected final Invoke invoke(
      String methodName,
      ArgsPart e1, ArgsPart e2, ArgsPart e3, ArgsPart e4, ArgsPart e5,
      ArgsPart e6, ArgsPart e7, ArgsPart e8, ArgsPart e9) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.INVOKE, EXT, e1.self(), e2.self(), e3.self(), e4.self(), e5.self(),
      e6.self(), e7.self(), e8.self(), e9.self());
  }

  protected final MethodDeclaration method(String methodName) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD, EXT);
  }

  protected final MethodDeclaration method(String methodName,
      ParameterElement e1) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD, EXT, e1.self());
  }

  protected final MethodDeclaration method(String methodName,
      ParameterElement... elements) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api.elemMany(ByteProto.METHOD, EXT, many);
  }

  protected final MethodDeclaration method(String methodName,
      ParameterElement e1, ParameterElement e2) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD, EXT, e1.self(), e2.self());
  }

  protected final ExpressionName n(String name) {
    JavaModel.checkSimpleName(name.toString());
    var api = api();
    return api.itemAdd(ByteProto.EXPRESSION_NAME, api.object(name));
  }

  protected final NewLine nl() {
    return api().itemAdd(ByteProto.NEW_LINE, ByteProto.NOOP);
  }

  protected final StringLiteral s(String string) {
    Objects.requireNonNull(string, "string == null");

    var api = api();

    return api.itemAdd(ByteProto.STRING_LITERAL, api.object(string));
  }

  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1) {
    return api().elem(ByteProto.ARRAY_TYPE, type, e1);
  }

  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2) {
    return api().elem(ByteProto.ARRAY_TYPE, type, e1, e2);
  }

  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2, ArrayTypeElement e3) {
    return api().elem(ByteProto.ARRAY_TYPE, type, e1, e2, e3);
  }

  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2, ArrayTypeElement e3, ArrayTypeElement e4) {
    return api().elem(ByteProto.ARRAY_TYPE, type, e1, e2, e3, e4);
  }

  protected final ClassType t(Class<?> type) {
    return api().classType(type);
  }

  protected final ParameterizedType t(
      ClassType rawType,
      ReferenceType arg1) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1);
  }

  protected final ParameterizedType t(
      ClassType rawType,
      ReferenceType arg1, ReferenceType arg2) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1, arg2);
  }

  protected final ParameterizedType t(
      ClassType rawType,
      ReferenceType arg1, ReferenceType arg2, ReferenceType arg3) {
    return api().elem(ByteProto.PARAMETERIZED_TYPE, rawType, arg1, arg2, arg3);
  }

  protected final ClassType t(String packageName, String simpleName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName.toString()); // implicit null check

    var api = api();

    return api.itemAdd(
      ByteProto.CLASS_TYPE, api.object(packageName),
      1, api.object(simpleName)
    );
  }

  protected final ClassType t(String packageName, String simpleName1, String simpleName2) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName1.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName2.toString()); // implicit null check

    var api = api();

    return api.itemAdd(
      ByteProto.CLASS_TYPE, api.object(packageName),
      2, api.object(simpleName1), api.object(simpleName2)
    );
  }

  protected final TypeParameter tparam(String name) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, EXT);
  }

  protected final TypeParameter tparam(String name, TypeParameterBound bound1) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, EXT, bound1.self());
  }

  protected final TypeParameter tparam(String name, TypeParameterBound... bounds) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    Object[] many = Objects.requireNonNull(bounds, "bounds == null");
    return api.elemMany(ByteProto.TYPE_PARAMETER, EXT, many);
  }

  protected final TypeParameter tparam(String name,
      TypeParameterBound bound1, TypeParameterBound bound2) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, EXT,
      bound1.self(), bound2.self());
  }

  protected final TypeParameter tparam(String name,
      TypeParameterBound bound1, TypeParameterBound bound2, TypeParameterBound bound3) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, EXT,
      bound1.self(), bound2.self(), bound3.self());
  }

  protected final TypeVariable tvar(String name) {
    Objects.requireNonNull(name, "name == null");
    var api = api();
    return api.itemAdd(ByteProto.TYPE_VARIABLE, api.object(name));
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