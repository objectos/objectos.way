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

import objectos.code.JavaModel.AbstractModifier;
import objectos.code.JavaModel.AnnotationElementValue;
import objectos.code.JavaModel.AnnotationInvocation;
import objectos.code.JavaModel.AnyType;
import objectos.code.JavaModel.ArrayAccessExpression;
import objectos.code.JavaModel.ArrayDimension;
import objectos.code.JavaModel.ArrayInitializer;
import objectos.code.JavaModel.ArrayInitializerElement;
import objectos.code.JavaModel.ArrayType;
import objectos.code.JavaModel.ArrayTypeComponent;
import objectos.code.JavaModel.ArrayTypeElement;
import objectos.code.JavaModel.AssignmentExpression;
import objectos.code.JavaModel.AutoImports;
import objectos.code.JavaModel.Block;
import objectos.code.JavaModel.BlockElement;
import objectos.code.JavaModel.ChainedMethodInvocation;
import objectos.code.JavaModel.ChainedMethodInvocationElement;
import objectos.code.JavaModel.ChainedMethodInvocationHead;
import objectos.code.JavaModel.ClassDeclaration;
import objectos.code.JavaModel.ClassDeclarationElement;
import objectos.code.JavaModel.ClassInstanceCreationExpression;
import objectos.code.JavaModel.ClassType;
import objectos.code.JavaModel.ConstructorDeclaration;
import objectos.code.JavaModel.ConstructorDeclarationElement;
import objectos.code.JavaModel.Ellipsis;
import objectos.code.JavaModel.EnumConstant;
import objectos.code.JavaModel.EnumConstantElement;
import objectos.code.JavaModel.EnumDeclaration;
import objectos.code.JavaModel.EnumDeclarationElement;
import objectos.code.JavaModel.ExplicitConstructorInvocation;
import objectos.code.JavaModel.Expression;
import objectos.code.JavaModel.ExpressionName;
import objectos.code.JavaModel.ExtendsKeyword;
import objectos.code.JavaModel.ExtendsMany;
import objectos.code.JavaModel.ExtendsSingle;
import objectos.code.JavaModel.FieldAccessExpression;
import objectos.code.JavaModel.FieldDeclaration;
import objectos.code.JavaModel.FieldDeclarationElement;
import objectos.code.JavaModel.FinalModifier;
import objectos.code.JavaModel.FormalParameter;
import objectos.code.JavaModel.FormalParameterElement;
import objectos.code.JavaModel.Identifier;
import objectos.code.JavaModel.ImplementsKeyword;
import objectos.code.JavaModel.Include;
import objectos.code.JavaModel.IntegerLiteral;
import objectos.code.JavaModel.InterfaceDeclaration;
import objectos.code.JavaModel.InterfaceDeclarationElement;
import objectos.code.JavaModel.LeftHandSide;
import objectos.code.JavaModel.LocalVariableDeclarationRef;
import objectos.code.JavaModel.MethodDeclaration;
import objectos.code.JavaModel.MethodDeclarationElement;
import objectos.code.JavaModel.MethodInvocationElement;
import objectos.code.JavaModel.NewLine;
import objectos.code.JavaModel.PackageKeyword;
import objectos.code.JavaModel.ParameterizedType;
import objectos.code.JavaModel.PrimaryExpression;
import objectos.code.JavaModel.PrimitiveType;
import objectos.code.JavaModel.PrivateModifier;
import objectos.code.JavaModel.ProtectedModifier;
import objectos.code.JavaModel.PublicModifier;
import objectos.code.JavaModel.QualifiedMethodInvocation;
import objectos.code.JavaModel.ReturnKeyword;
import objectos.code.JavaModel.StaticModifier;
import objectos.code.JavaModel.StringLiteral;
import objectos.code.JavaModel.ThisKeyword;
import objectos.code.JavaModel.TypeParameter;
import objectos.code.JavaModel.TypeParameterBound;
import objectos.code.JavaModel.TypeVariable;
import objectos.code.JavaModel.UnqualifiedMethodInvocation;
import objectos.code.JavaModel.VoidKeyword;
import objectos.lang.Check;

public abstract class JavaTemplate {

  @FunctionalInterface
  protected interface IncludeTarget {
    void execute();
  }

  private InternalApi api;

  boolean v2;

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

  protected AbstractModifier _abstract() {
    return api()._abstract();
  }

  protected final PrimitiveType _boolean() {
    return api()._boolean();
  }

  protected final ClassDeclaration _class(ClassDeclarationElement... elements) {
    return api()._class(elements);
  }

  protected final PrimitiveType _double() {
    return api()._double();
  }

  protected final EnumDeclaration _enum(EnumDeclarationElement... elements) {
    return api()._enum(elements);
  }

  protected ExtendsKeyword _extends() {
    throw new UnsupportedOperationException();
  }

  protected final ExtendsMany _extends(ClassType... interfaces) {
    return api()._extends(interfaces);
  }

  protected final ExtendsSingle _extends(ClassType value) {
    return api()._extends(value);
  }

  protected FinalModifier _final() {
    return api()._final();
  }

  protected ImplementsKeyword _implements() {
    throw new UnsupportedOperationException();
  }

  protected final ImplementsKeyword _implements(ClassType type) {
    return api()._implements(type);
  }

  protected final ImplementsKeyword _implements(ClassType... interfaces) {
    return api()._implements(interfaces);
  }

  protected final ImplementsKeyword _implements(ClassType type1, ClassType type2) {
    return api()._implements(type1, type2);
  }

  protected PrimitiveType _int() {
    return api()._int();
  }

  protected final InterfaceDeclaration _interface(InterfaceDeclarationElement... elements) {
    return api()._interface(elements);
  }

  protected final ClassInstanceCreationExpression _new(
      ClassType type, Expression... arguments) {
    return api()._new(type, arguments);
  }

  protected PackageKeyword _package(String packageName) {
    api()._package(packageName);
    return null;
  }

  protected PrivateModifier _private() {
    return api()._private();
  }

  protected ProtectedModifier _protected() {
    return api()._protected();
  }

  protected PublicModifier _public() {
    return api()._public();
  }

  protected final ReturnKeyword _return(Expression expression) {
    return api()._return(expression);
  }

  protected StaticModifier _static() {
    return api()._static();
  }

  protected final ExplicitConstructorInvocation _super(Expression... arguments) {
    return api()._super(arguments);
  }

  protected final ThisKeyword _this() {
    return api()._this();
  }

  protected VoidKeyword _void() {
    return api()._void();
  }

  protected ArrayInitializer a(ArrayInitializerElement... elements) {
    return api().a(elements);
  }

  protected final ArrayAccessExpression aget(ExpressionName reference, Expression... expressions) {
    return api().aget(reference, expressions);
  }

  protected final AnnotationInvocation annotation(ClassType annotationType) {
    return api().annotation(annotationType);
  }

  protected final AnnotationInvocation annotation(ClassType annotationType,
      AnnotationElementValue value) {
    return api().annotation(annotationType, value);
  }

  protected final AssignmentExpression assign(LeftHandSide leftHandSide, Expression expression) {
    return api().assign(leftHandSide, expression);
  }

  protected AutoImports autoImports() {
    api().autoImports();
    return null;
  }

  protected Block block(BlockElement... elements) {
    return api().block(elements);
  }

  @Deprecated
  protected final ChainedMethodInvocation chain(ChainedMethodInvocationHead invalid0) {
    throw new UnsupportedOperationException();
  }

  protected final ChainedMethodInvocation chain(
      ChainedMethodInvocationHead first, ChainedMethodInvocationElement... more) {
    return api().chain(first, more);
  }

  protected final ConstructorDeclaration constructor(ConstructorDeclarationElement... elements) {
    return api().constructor(elements);
  }

  protected abstract void definition();

  protected ArrayDimension dim() {
    return api().dim();
  }

  protected final Ellipsis ellipsis() {
    return api().ellipsis();
  }

  protected final EnumConstant enumConstant(EnumConstantElement... elements) {
    return api().enumConstant(elements);
  }

  protected final FieldDeclaration field(FieldDeclarationElement... elements) {
    return api().field(elements);
  }

  protected IntegerLiteral i(int value) {
    return api().i(value);
  }

  protected Identifier id(String name) {
    return api().id(name);
  }

  protected Include include(IncludeTarget target) {
    return api().include(target);
  }

  protected final QualifiedMethodInvocation invoke(
      ClassType typeName, String methodName, MethodInvocationElement... elements) {
    return api().invoke(typeName, methodName, elements);
  }

  protected final QualifiedMethodInvocation invoke(
      ExpressionName expressionName, String methodName, MethodInvocationElement... elements) {
    return api().invoke(expressionName, methodName, elements);
  }

  protected final QualifiedMethodInvocation invoke(
      PrimaryExpression primary, String methodName, MethodInvocationElement... elements) {
    return api().invoke(primary, methodName, elements);
  }

  protected final UnqualifiedMethodInvocation invoke(
      String methodName, MethodInvocationElement... elements) {
    return api().invoke(methodName, elements);
  }

  protected final MethodDeclaration method(MethodDeclarationElement... elements) {
    return api().method(elements);
  }

  @Deprecated
  protected final ExpressionName n() {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  protected final ExpressionName n(ClassType type) {
    throw new UnsupportedOperationException();
  }

  protected ExpressionName n(ClassType type, String... identifiers) {
    return api().n(type, identifiers);
  }

  protected final ExpressionName n(String... identifiers) {
    return api().n(identifiers);
  }

  protected final FieldAccessExpression n(ThisKeyword keyword, String identifier) {
    return api().n(keyword, identifier);
  }

  protected NewLine nl() {
    return api().nl();
  }

  @Deprecated
  protected final FormalParameter param() {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  protected final FormalParameter param(FormalParameterElement e1) {
    throw new UnsupportedOperationException();
  }

  protected final FormalParameter param(FormalParameterElement... elements) {
    return api().param(elements);
  }

  protected StringLiteral s(String value) {
    return api().s(value);
  }

  protected ArrayType t(ArrayTypeComponent type, ArrayTypeElement... elements) {
    return api().t(type, elements);
  }

  protected ClassType t(Class<?> value) {
    return api().t(value);
  }

  protected ParameterizedType t(
      ClassType rawType, AnyType... arguments) {
    return api().t(rawType, arguments);
  }

  protected ClassType t(String packageName, String simpleName) {
    return api().t(packageName, simpleName);
  }

  protected final ClassType t(String packageName, String... simpleNames) {
    return api().t(packageName, simpleNames);
  }

  protected final TypeParameter tparam(String name, TypeParameterBound... bounds) {
    return api().tparam(name, bounds);
  }

  protected final TypeVariable tvar(String name) {
    return api().tvar(name);
  }

  protected final LocalVariableDeclarationRef var(String name, Expression expression) {
    return api().var(name, expression);
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

  void onEval() {}

}