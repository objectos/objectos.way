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
import objectos.code.JavaModel.AbstractModifier;
import objectos.code.JavaModel.ArrayAccess;
import objectos.code.JavaModel.ArrayDimension;
import objectos.code.JavaModel.ArrayInitializer;
import objectos.code.JavaModel.ArrayInitializerElement;
import objectos.code.JavaModel.ArrayType;
import objectos.code.JavaModel.ArrayTypeComponent;
import objectos.code.JavaModel.ArrayTypeElement;
import objectos.code.JavaModel.AssignmentOperator;
import objectos.code.JavaModel.At;
import objectos.code.JavaModel.AtElement;
import objectos.code.JavaModel.AutoImports;
import objectos.code.JavaModel.Block;
import objectos.code.JavaModel.BlockElement;
import objectos.code.JavaModel.Body;
import objectos.code.JavaModel.BodyElement;
import objectos.code.JavaModel.ClassInstanceCreationExpression;
import objectos.code.JavaModel.ClassKeyword;
import objectos.code.JavaModel.ClassType;
import objectos.code.JavaModel.ConstructorDeclaration;
import objectos.code.JavaModel.Element;
import objectos.code.JavaModel.Ellipsis;
import objectos.code.JavaModel.End;
import objectos.code.JavaModel.EnumConstant;
import objectos.code.JavaModel.EnumKeyword;
import objectos.code.JavaModel.ExplicitConstructorInvocation;
import objectos.code.JavaModel.Expression;
import objectos.code.JavaModel.ExpressionElement;
import objectos.code.JavaModel.ExtendsKeyword;
import objectos.code.JavaModel.FieldName;
import objectos.code.JavaModel.FinalModifier;
import objectos.code.JavaModel.Identifier;
import objectos.code.JavaModel.ImplementsKeyword;
import objectos.code.JavaModel.Include;
import objectos.code.JavaModel.IntegerLiteral;
import objectos.code.JavaModel.InterfaceKeyword;
import objectos.code.JavaModel.MethodDeclaration;
import objectos.code.JavaModel.MethodInvocationElement;
import objectos.code.JavaModel.NewLine;
import objectos.code.JavaModel.PackageKeyword;
import objectos.code.JavaModel.ParameterElement;
import objectos.code.JavaModel.ParameterizedType;
import objectos.code.JavaModel.PrimitiveType;
import objectos.code.JavaModel.PrivateModifier;
import objectos.code.JavaModel.ProtectedModifier;
import objectos.code.JavaModel.PublicModifier;
import objectos.code.JavaModel.ReferenceType;
import objectos.code.JavaModel.ReturnKeyword;
import objectos.code.JavaModel.SomeName;
import objectos.code.JavaModel.StaticModifier;
import objectos.code.JavaModel.StringLiteral;
import objectos.code.JavaModel.SuperKeyword;
import objectos.code.JavaModel.ThisKeyword;
import objectos.code.JavaModel.TypeParameter;
import objectos.code.JavaModel.TypeParameterBound;
import objectos.code.JavaModel.TypeVariable;
import objectos.code.JavaModel.UnqualifiedMethodInvocation;
import objectos.code.JavaModel.VarKeyword;
import objectos.code.JavaModel.VoidKeyword;
import objectos.code.JavaModel._Item;
import objectos.lang.Check;

public abstract class JavaTemplate {

  @FunctionalInterface
  protected interface IncludeTarget {
    void execute();
  }

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

    return api.item(ByteProto.CLASS, api.object(name));
  }

  protected final PrimitiveType _double() {
    return primitiveType(Keyword.DOUBLE);
  }

  protected final EnumKeyword _enum(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.item(ByteProto.ENUM, api.object(name));
  }

  protected final ExtendsKeyword _extends() {
    return api().item(ByteProto.EXTENDS);
  }

  protected final FinalModifier _final() {
    return api().item(ByteProto.MODIFIER, Keyword.FINAL.ordinal());
  }

  protected final ImplementsKeyword _implements() {
    return api().item(ByteProto.IMPLEMENTS);
  }

  protected final PrimitiveType _int() {
    return primitiveType(Keyword.INT);
  }

  protected final InterfaceKeyword _interface(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.item(ByteProto.INTERFACE, api.object(name));
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

    return api.item(ByteProto.PACKAGE, api.object(packageName));
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
    return api().item(ByteProto.RETURN);
  }

  protected final StaticModifier _static() {
    return modifier(Keyword.STATIC);
  }

  protected final SuperKeyword _super() {
    return api().item(ByteProto.SUPER);
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
    return api().item(ByteProto.THIS);
  }

  protected final VarKeyword _var() {
    return api().item(ByteProto.VAR);
  }

  protected final VoidKeyword _void() {
    return api().item(ByteProto.VOID);
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

  protected final At at(ClassType annotationType) {
    return api().elem(ByteProto.ANNOTATION, annotationType.self());
  }

  protected final At at(ClassType annotationType, AtElement e1) {
    return api().elem(ByteProto.ANNOTATION, annotationType.self(), e1.self());
  }

  protected final AutoImports autoImports() {
    var api = api();

    api.autoImports.enable();

    return api.item(ByteProto.AUTO_IMPORTS);
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
    return api().item(ByteProto.ARRAY_DIMENSION);
  }

  protected final ArrayAccess dim(ExpressionElement e1) {
    return api().elem(ByteProto.ARRAY_ACCESS, e1.self());
  }

  protected final ArrayAccess dim(ExpressionElement e1, ExpressionElement e2) {
    return api().elem(ByteProto.ARRAY_ACCESS, e1.self(), e2.self());
  }

  protected final Ellipsis ellipsis() {
    return api().item(ByteProto.ELLIPSIS);
  }

  protected final End end() {
    return api().item(ByteProto.END);
  }

  protected final EnumConstant enumConstant(String name) {
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, JavaModel.EXT);
  }

  protected final EnumConstant enumConstant(String name,
      MethodInvocationElement e1) {
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, JavaModel.EXT, e1.self());
  }

  protected final EnumConstant enumConstant(String name,
      MethodInvocationElement e1, MethodInvocationElement e2) {
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, JavaModel.EXT, e1.self(), e2.self());
  }

  protected final EnumConstant enumConstant(String name,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3) {
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.ENUM_CONSTANT, JavaModel.EXT, e1.self(), e2.self(), e3.self());
  }

  protected final FieldName field(String name) {
    JavaModel.checkIdentifier(name.toString()); // force implicit null-check
    var api = api();
    return api.item(ByteProto.FIELD_NAME, api.object(name));
  }

  protected final AssignmentOperator gets() {
    return api().item(ByteProto.GETS);
  }

  protected final IntegerLiteral i(int value) {
    var s = Integer.toString(value);

    var api = api();

    return api.item(ByteProto.PRIMITIVE_LITERAL, api.object(s));
  }

  protected final Identifier id(String name) {
    JavaModel.checkIdentifier(name);
    var api = api();
    return api.item(ByteProto.IDENTIFIER, api.object(name));
  }

  protected final Include include(IncludeTarget target) {
    var api = api();
    api.lambdastart();
    target.execute(); // implicit null-check
    api.lambdaend();
    return JavaModel.INCLUDE;
  }

  protected final Include include(JavaTemplate template) {
    var api = api();
    api.lambdastart();
    template.execute(api);
    api.lambdaend();
    return JavaModel.INCLUDE;
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check

    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT);
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check

    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self());
  }

  protected final UnqualifiedMethodInvocation invoke(String methodName,
      MethodInvocationElement... elements) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api.elemmany(ByteProto.METHOD_INVOCATION, JavaModel.EXT, many);
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self(), e2.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self(), e2.self(), e3.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self(), e2.self(), e3.self(),
      e4.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4, MethodInvocationElement e5) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self());
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName,
      MethodInvocationElement e1, MethodInvocationElement e2, MethodInvocationElement e3,
      MethodInvocationElement e4, MethodInvocationElement e5, MethodInvocationElement e6) {
    JavaModel.checkMethodName(methodName.toString()); // implicit null check
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self(), e2.self(), e3.self(),
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
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self(), e2.self(), e3.self(),
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
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self(), e2.self(), e3.self(),
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
    return api.elem(ByteProto.METHOD_INVOCATION, JavaModel.EXT, e1.self(), e2.self(), e3.self(),
      e4.self(), e5.self(), e6.self(), e7.self(), e8.self(), e9.self());
  }

  protected final MethodDeclaration method(String methodName) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_DECLARATION, JavaModel.EXT);
  }

  protected final MethodDeclaration method(String methodName,
      ParameterElement e1) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_DECLARATION, JavaModel.EXT, e1.self());
  }

  protected final MethodDeclaration method(String methodName,
      ParameterElement... elements) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    Object[] many = Objects.requireNonNull(elements, "elements == null");
    return api.elemmany(ByteProto.METHOD_DECLARATION, JavaModel.EXT, many);
  }

  protected final MethodDeclaration method(String methodName,
      ParameterElement e1, ParameterElement e2) {
    JavaModel.checkMethodName(methodName);
    var api = api();
    api.identifierext(methodName);
    return api.elem(ByteProto.METHOD_DECLARATION, JavaModel.EXT, e1.self(), e2.self());
  }

  protected final SomeName n(String name) {
    JavaModel.checkSimpleName(name.toString());
    var api = api();
    return api.item(ByteProto.NAME, api.object(name));
  }

  protected final NewLine nl() {
    return api().item(ByteProto.NEW_LINE);
  }

  protected final StringLiteral s(String string) {
    Objects.requireNonNull(string, "string == null");

    var api = api();

    return api.item(ByteProto.STRING_LITERAL, api.object(string));
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

    return api.item(
      ByteProto.CLASS_TYPE, api.object(packageName),
      1, api.object(simpleName)
    );
  }

  protected final ClassType t(String packageName, String simpleName1, String simpleName2) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName1.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName2.toString()); // implicit null check

    var api = api();

    return api.item(
      ByteProto.CLASS_TYPE, api.object(packageName),
      2, api.object(simpleName1), api.object(simpleName2)
    );
  }

  protected final TypeParameter tparam(String name) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, JavaModel.EXT);
  }

  protected final TypeParameter tparam(String name, TypeParameterBound bound1) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, JavaModel.EXT, bound1.self());
  }

  protected final TypeParameter tparam(String name, TypeParameterBound... bounds) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    Object[] many = Objects.requireNonNull(bounds, "bounds == null");
    return api.elemmany(ByteProto.TYPE_PARAMETER, JavaModel.EXT, many);
  }

  protected final TypeParameter tparam(String name,
      TypeParameterBound bound1, TypeParameterBound bound2) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, JavaModel.EXT,
      bound1.self(), bound2.self());
  }

  protected final TypeParameter tparam(String name,
      TypeParameterBound bound1, TypeParameterBound bound2, TypeParameterBound bound3) {
    JavaModel.checkVarName(name);
    var api = api();
    api.identifierext(name);
    return api.elem(ByteProto.TYPE_PARAMETER, JavaModel.EXT,
      bound1.self(), bound2.self(), bound3.self());
  }

  protected final TypeVariable tvar(String name) {
    Objects.requireNonNull(name, "name == null");
    var api = api();
    return api.item(ByteProto.TYPE_VARIABLE, api.object(name));
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

  private JavaModel._Item modifier(Keyword value) {
    return api().item(ByteProto.MODIFIER, value.ordinal());
  }

  private _Item primitiveType(Keyword value) {
    return api().item(ByteProto.PRIMITIVE_TYPE, value.ordinal());
  }

}