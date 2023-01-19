/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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

  protected sealed interface AnnotationElementValue {}

  protected sealed interface AnyType extends BodyElement, ParameterElement {}

  protected sealed interface ArrayInitializerElement {}

  protected sealed interface ArrayTypeElement {}

  protected sealed interface BlockElement extends Element {}

  protected sealed interface BlockStatement extends BlockElement {}

  protected sealed interface BodyElement extends Element {}

  protected sealed interface ClassType
      extends BlockElement, BodyElement, MethodInvocationElement, ReferenceType,
      /* to remove */
      AnyType,
      TypeParameterBound {}

  protected sealed interface Element {
    /**
     * Triggers implicit null check.
     */
    default Object self() { return this; }
  }

  protected sealed interface EnumConstant extends BodyElement {}

  protected sealed interface EnumConstantElement {}

  protected sealed interface Expression extends ExpressionElement, BlockElement {}

  protected sealed interface ExpressionElement extends
      ArrayInitializerElement, BlockElement, BodyElement, DimElement, MethodInvocationElement {}

  protected sealed interface ExpressionStatement extends Statement {}

  @FunctionalInterface
  protected interface IncludeTarget {
    void execute();
  }

  protected sealed interface LeftHandSide {}

  protected sealed interface MethodInvocationElement extends Element {}

  protected sealed interface ParameterElement extends Element {}

  protected sealed interface Statement extends BlockStatement {}

  protected sealed interface TypeParameterBound extends Element {}

  protected sealed interface UnqualifiedMethodInvocation extends
      MethodInvocation {}

  final class _Elem extends _ElemOrItem implements
      ArrayAccessExpression,
      ArrayInitializer,
      ArrayType,
      At,
      Block,
      Body,
      ClassInstanceCreationExpression,
      ConstructorDeclaration,
      EnumConstant,
      ExplicitConstructorInvocation,
      MethodDeclaration,
      UnqualifiedMethodInvocation,
      ParameterizedType,
      TypeParameter {}

  enum _Ext {
    INSTANCE;
  }

  enum _Include implements Include {
    INSTANCE;
  }

  final class _Item extends _ElemOrItem implements
      AbstractModifier,
      ArrayDimension,
      AssignmentOperator,
      AutoImports,
      ClassKeyword,
      ClassType,
      Ellipsis,
      End,
      EnumKeyword,
      ExpressionName,
      ExtendsKeyword,
      FieldName,
      FinalModifier,
      Identifier,
      ImplementsKeyword,
      IntegerLiteral,
      InterfaceKeyword,
      NewKeyword,
      NewLine,
      PackageKeyword,
      PrimitiveType,
      PrivateModifier,
      ProtectedModifier,
      PublicModifier,
      ReturnKeyword,
      StaticModifier,
      StringLiteral,
      SuperKeyword,
      ThisKeyword,
      TypeVariable,
      VarKeyword,
      VoidKeyword {}

  sealed interface AbstractModifier extends BodyElement {}

  sealed interface ArrayAccessExpression extends ArrayReferenceExpression {}

  sealed interface ArrayDimension extends ArrayTypeElement {}

  sealed interface ArrayInitializer extends ArrayInitializerElement {}

  sealed interface ArrayReferenceExpression extends Expression {}

  sealed interface ArrayType extends BodyElement,
      AnyType,
      ReferenceType {}

  sealed interface ArrayTypeComponent {}

  sealed interface AssignmentOperator extends BlockElement {}

  sealed interface At extends BodyElement {}

  sealed interface AtElement extends Element {}

  sealed interface AutoImports extends Element {}

  sealed interface Block extends BodyElement,
      /* to remove */
      Statement {}

  sealed interface Body extends BodyElement {}

  sealed interface ClassInstanceCreationExpression extends ExpressionStatement, PrimaryExpression {}

  sealed interface ClassKeyword extends BodyElement {}

  sealed interface ConstructorDeclaration extends BodyElement {}

  sealed interface DimElement {}

  sealed interface Ellipsis extends ParameterElement {}

  sealed interface End extends BlockElement, Element, MethodInvocationElement {}

  sealed interface EnumKeyword extends BodyElement {}

  sealed interface ExplicitConstructorInvocation extends BlockElement {}

  sealed interface ExpressionName extends
      ArrayReferenceExpression, ExpressionNamePart, LeftHandSide {}

  sealed interface ExpressionNamePart {
    ExpressionName n(String name);
  }

  sealed interface ExtendsKeyword extends BodyElement {}

  sealed interface FieldName extends BodyElement {}

  sealed interface FinalModifier extends BodyElement {}

  sealed interface Identifier extends BlockElement, ParameterElement,
      /* to remove */
      EnumConstantElement {}

  sealed interface ImplementsKeyword extends BodyElement {}

  sealed interface Include extends BlockElement, BodyElement, MethodInvocationElement {}

  sealed interface IntegerLiteral extends Literal {}

  sealed interface InterfaceKeyword extends BodyElement {}

  sealed interface Literal extends AtElement, PrimaryExpression {}

  sealed interface MethodDeclaration extends BodyElement {}

  sealed interface MethodInvocation extends Expression, ExpressionStatement {}

  sealed interface NewKeyword extends BlockElement {}

  sealed interface NewLine extends BlockElement,
      /* to remove */
      MethodInvocationElement {}

  sealed interface PackageKeyword extends Element {}

  sealed interface ParameterizedType extends ReferenceType,
      /* to remove */
      AnyType {}

  sealed interface PrimaryExpression extends Expression {}

  sealed interface PrimitiveType extends AnyType, BodyElement,
      /* to remove */
      ArrayTypeComponent {}

  sealed interface PrivateModifier extends AccessModifier {}

  sealed interface ProtectedModifier extends AccessModifier {}

  sealed interface PublicModifier extends AccessModifier {}

  sealed interface ReferenceType extends ArrayTypeComponent,
      /* to remove */
      AnyType {}

  sealed interface ReturnKeyword extends BlockElement,
      /* to remove */
      Statement {}

  sealed interface StaticModifier extends BodyElement {}

  sealed interface StringLiteral extends Literal,
      /* to remove */
      AnnotationElementValue {}

  sealed interface SuperKeyword extends BlockElement {}

  sealed interface ThisKeyword extends PrimaryExpression {}

  sealed interface TypeParameter extends BodyElement {}

  sealed interface TypeVariable extends ReferenceType {}

  sealed interface VarKeyword extends BlockElement {}

  sealed interface VoidKeyword extends BodyElement {}

  private abstract class _ElemOrItem {
    public final ExpressionName n(String name) {
      return api().dot(JavaTemplate.this.n(name));
    }
  }

  private sealed interface AccessModifier extends BodyElement {}

  static final _Ext EXT = _Ext.INSTANCE;

  static final _Include INCLUDE = _Include.INSTANCE;

  final _Elem elem = new _Elem();

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

    return api.itemadd(ByteProto.CLASS, api.object(name));
  }

  protected final PrimitiveType _double() {
    return primitiveType(Keyword.DOUBLE);
  }

  protected final EnumKeyword _enum(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.itemadd(ByteProto.ENUM, api.object(name));
  }

  protected final ExtendsKeyword _extends() {
    return api().itemadd(ByteProto.EXTENDS);
  }

  protected final FinalModifier _final() {
    return api().itemadd(ByteProto.MODIFIER, Keyword.FINAL.ordinal());
  }

  protected final ImplementsKeyword _implements() {
    return api().itemadd(ByteProto.IMPLEMENTS);
  }

  protected final PrimitiveType _int() {
    return primitiveType(Keyword.INT);
  }

  protected final InterfaceKeyword _interface(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.itemadd(ByteProto.INTERFACE, api.object(name));
  }

  protected final ClassInstanceCreationExpression _new(ClassType type) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self()
    );
  }

  protected final ClassInstanceCreationExpression _new(ClassType type,
      Expression arg1) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self(),
      arg1.self()
    );
  }

  protected final ClassInstanceCreationExpression _new(ClassType type,
      Expression arg1, Expression arg2) {
    return api().elem(
      ByteProto.CLASS_INSTANCE_CREATION, type.self(),
      arg1.self(), arg2.self()
    );
  }

  protected final PackageKeyword _package(String packageName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check

    var api = api();

    api.autoImports.packageName(packageName);

    return api.itemadd(ByteProto.PACKAGE, api.object(packageName));
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
    return api().itemadd(ByteProto.RETURN);
  }

  protected final StaticModifier _static() {
    return modifier(Keyword.STATIC);
  }

  protected final SuperKeyword _super() {
    return api().itemadd(ByteProto.SUPER);
  }

  protected final ExplicitConstructorInvocation _super(ExpressionElement e1) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self());
  }

  protected final ExplicitConstructorInvocation _super(ExpressionElement... elements) {
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api().elemmany(ByteProto.SUPER_INVOCATION, many);
  }

  protected final ExplicitConstructorInvocation _super(ExpressionElement e1, ExpressionElement e2) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self());
  }

  protected final ExplicitConstructorInvocation _super(ExpressionElement e1, ExpressionElement e2,
      ExpressionElement e3) {
    return api().elem(ByteProto.SUPER_INVOCATION, e1.self(), e1.self(), e3.self());
  }

  protected final ThisKeyword _this() {
    return api().itemadd(ByteProto.THIS);
  }

  protected final VarKeyword _var() {
    return api().itemadd(ByteProto.VAR);
  }

  protected final VoidKeyword _void() {
    return api().itemadd(ByteProto.VOID);
  }

  protected final ArrayInitializer a() {
    return api().elem(ByteProto.ARRAY_INITIALIZER);
  }

  protected final ArrayInitializer a(
      ArrayInitializerElement e1) {
    return api().elem(ByteProto.ARRAY_INITIALIZER, e1);
  }

  protected final ArrayInitializer a(
      ArrayInitializerElement e1, ArrayInitializerElement e2) {
    return api().elem(ByteProto.ARRAY_INITIALIZER, e1, e2);
  }

  protected final ArrayAccessExpression aaccess(ArrayReferenceExpression reference,
      Expression e1) {
    return api().elem(ByteProto.ARRAY_ACCESS_EXPRESSION, reference.self(),
      e1.self());
  }

  protected final ArrayAccessExpression aaccess(ArrayReferenceExpression reference,
      Expression e1, Expression e2) {
    return api().elem(ByteProto.ARRAY_ACCESS_EXPRESSION, reference.self(),
      e1.self(), e2.self());
  }

  protected final ArrayAccessExpression aaccess(ArrayReferenceExpression reference,
      Expression e1, Expression e2, Expression e3) {
    return api().elem(ByteProto.ARRAY_ACCESS_EXPRESSION, reference.self(),
      e1.self(), e2.self(), e3.self());
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

    return api.itemadd(ByteProto.AUTO_IMPORTS);
  }

  protected final Block block() {
    return api().elem(ByteProto.BLOCK);
  }

  protected final Block block(BlockElement e1) {
    return api().elem(ByteProto.BLOCK, e1.self());
  }

  protected final Block block(BlockElement... elements) {
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api().elemmany(ByteProto.BLOCK, many);
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
    return api().elemmany(ByteProto.BODY, many);
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
    return api().elemmany(ByteProto.CONSTRUCTOR, elements);
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

  protected void definition() {}

  protected final ArrayDimension dim() {
    return api().itemadd(ByteProto.ARRAY_DIMENSION);
  }

  protected final Ellipsis ellipsis() {
    return api().itemadd(ByteProto.ELLIPSIS);
  }

  protected final EnumConstant enumConstant(String name) {
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT);
  }

  protected final EnumConstant enumConstant(String name,
      MethodInvocationElement e1) {
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT, e1.self());
  }

  protected final EnumConstant enumConstant(String name,
      MethodInvocationElement e1, MethodInvocationElement e2) {
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT, e1.self(), e2.self());
  }

  protected final EnumConstant enumConstant(String name,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3) {
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, EXT, e1.self(), e2.self(), e3.self());
  }

  protected final FieldName field(String name) {
    JavaModel.checkIdentifier(name.toString()); // force implicit null-check
    var api = api();
    return api.itemadd(ByteProto.FIELD_NAME, api.object(name));
  }

  protected final AssignmentOperator gets() {
    return api().itemadd(ByteProto.GETS);
  }

  protected final IntegerLiteral i(int value) {
    var s = Integer.toString(value);

    var api = api();

    return api.itemadd(ByteProto.PRIMITIVE_LITERAL, api.object(s));
  }

  protected final Identifier id(String name) {
    JavaModel.checkIdentifier(name);
    var api = api();
    return api.itemadd(ByteProto.IDENTIFIER, api.object(name));
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

  protected final UnqualifiedMethodInvocation invoke(
      String methodName) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check

    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT);
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check

    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self());
  }

  protected final UnqualifiedMethodInvocation invoke(String methodName,
      MethodInvocationElement... elements) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api.elemmany(ByteProto.METHOD_INVOCATION, EXT, many);
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self(), e2.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self(), e2.self(), e3.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self(), e2.self(), e3.self(),
      e4.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4, MethodInvocationElement e5) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4, MethodInvocationElement e5, MethodInvocationElement e6) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self(), e6.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4, MethodInvocationElement e5, MethodInvocationElement e6,
      MethodInvocationElement e7) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self(), e6.self(), e7.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4, MethodInvocationElement e5, MethodInvocationElement e6,
      MethodInvocationElement e7, MethodInvocationElement e8) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self(), e6.self(), e7.self(), e8.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4, MethodInvocationElement e5, MethodInvocationElement e6,
      MethodInvocationElement e7, MethodInvocationElement e8, MethodInvocationElement e9) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, EXT, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self(), e6.self(), e7.self(), e8.self(), e9.self());
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
    return api.elemmany(ByteProto.METHOD, EXT, many);
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
    return api.itemadd(ByteProto.EXPRESSION_NAME, api.object(name));
  }

  protected final NewLine nl() {
    return api().itemadd(ByteProto.NEW_LINE);
  }

  protected final StringLiteral s(String string) {
    Objects.requireNonNull(string, "string == null");

    var api = api();

    return api.itemadd(ByteProto.STRING_LITERAL, api.object(string));
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

    return api.itemadd(
      ByteProto.CLASS_TYPE, api.object(packageName),
      1, api.object(simpleName)
    );
  }

  protected final ClassType t(String packageName, String simpleName1, String simpleName2) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName1.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName2.toString()); // implicit null check

    var api = api();

    return api.itemadd(
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
    return api.elemmany(ByteProto.TYPE_PARAMETER, EXT, many);
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
    return api.itemadd(ByteProto.TYPE_VARIABLE, api.object(name));
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
    return api().itemadd(ByteProto.MODIFIER, value.ordinal());
  }

  private _Item primitiveType(Keyword value) {
    return api().itemadd(ByteProto.PRIMITIVE_TYPE, value.ordinal());
  }

}