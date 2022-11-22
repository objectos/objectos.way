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

import java.lang.annotation.Annotation;
import objectos.code.JavaModel.AnnotationElementValue;
import objectos.code.JavaModel.AnnotationInvocation;
import objectos.code.JavaModel.ArrayAccessExpression;
import objectos.code.JavaModel.ArrayDimension;
import objectos.code.JavaModel.ArrayTypeElement;
import objectos.code.JavaModel.ArrayTypeInvocation;
import objectos.code.JavaModel.AssignmentExpression;
import objectos.code.JavaModel.ClassDeclaration;
import objectos.code.JavaModel.ClassDeclarationElement;
import objectos.code.JavaModel.ClassInstanceCreationExpression;
import objectos.code.JavaModel.ClassNameInvocation;
import objectos.code.JavaModel.ConstructorDeclaration;
import objectos.code.JavaModel.ConstructorDeclarationElement;
import objectos.code.JavaModel.EnumConstant;
import objectos.code.JavaModel.EnumConstantElement;
import objectos.code.JavaModel.EnumDeclaration;
import objectos.code.JavaModel.EnumDeclarationElement;
import objectos.code.JavaModel.Expression;
import objectos.code.JavaModel.ExpressionName;
import objectos.code.JavaModel.ExtendsRef;
import objectos.code.JavaModel.FieldAccessExpression;
import objectos.code.JavaModel.FieldDeclaration;
import objectos.code.JavaModel.FieldDeclarationElement;
import objectos.code.JavaModel.FinalModifier;
import objectos.code.JavaModel.FormalParameter;
import objectos.code.JavaModel.FormalParameterType;
import objectos.code.JavaModel.IdentifierRef;
import objectos.code.JavaModel.Implements;
import objectos.code.JavaModel.IncludeRef;
import objectos.code.JavaModel.IntPrimitiveType;
import objectos.code.JavaModel.LeftHandSide;
import objectos.code.JavaModel.LocalVariableDeclarationRef;
import objectos.code.JavaModel.MethodDeclaration;
import objectos.code.JavaModel.MethodDeclarationElement;
import objectos.code.JavaModel.MethodInvocation;
import objectos.code.JavaModel.MethodInvocationElement;
import objectos.code.JavaModel.MethodInvocationSubject;
import objectos.code.JavaModel.NewLineRef;
import objectos.code.JavaModel.PrivateModifier;
import objectos.code.JavaModel.ProtectedModifier;
import objectos.code.JavaModel.PublicModifier;
import objectos.code.JavaModel.ReturnStatement;
import objectos.code.JavaModel.StaticModifier;
import objectos.code.JavaModel.StringLiteral;
import objectos.code.JavaModel.ThisKeyword;
import objectos.code.JavaModel.VoidInvocation;
import objectos.lang.Check;

public abstract class JavaTemplate {

  @FunctionalInterface
  protected interface IncludeTarget {
    void execute();
  }

  private TempInternalApi api;

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

  protected final ClassDeclaration _class(ClassDeclarationElement... elements) {
    return api()._class(elements);
  }

  protected final EnumDeclaration _enum(EnumDeclarationElement... elements) {
    return api()._enum(elements);
  }

  protected final ExtendsRef _extends(ClassName superclass) {
    return api()._extends(superclass);
  }

  protected final FinalModifier _final() {
    return api()._final();
  }

  protected final Implements _implements(ClassName... interfaces) {
    return api()._implements(interfaces);
  }

  protected final IntPrimitiveType _int() {
    return api()._int();
  }

  protected final ClassInstanceCreationExpression _new(
      ClassNameInvocation type, Expression... arguments) {
    return api()._new(type, arguments);
  }

  protected final void _package(String packageName) {
    api()._package(packageName);
  }

  protected final PrivateModifier _private() {
    return api()._private();
  }

  protected final ProtectedModifier _protected() {
    return api()._protected();
  }

  protected final PublicModifier _public() {
    return api()._public();
  }

  protected final ReturnStatement _return(Expression expression) {
    return api()._return(expression);
  }

  protected final StaticModifier _static() {
    return api()._static();
  }

  protected final ThisKeyword _this() {
    return api()._this();
  }

  protected final VoidInvocation _void() {
    return api()._void();
  }

  protected final ArrayAccessExpression a(ExpressionName reference, Expression... expressions) {
    return api().a(reference, expressions);
  }

  protected final AnnotationInvocation annotation(Class<? extends Annotation> annotationType) {
    return api().annotation(annotationType);
  }

  protected final AnnotationInvocation annotation(ClassName annotationType,
      AnnotationElementValue value) {
    return api().annotation(annotationType, value);
  }

  protected final AssignmentExpression assign(LeftHandSide leftHandSide, Expression expression) {
    return api().assign(leftHandSide, expression);
  }

  protected final void autoImports() {
    api().autoImports();
  }

  protected final ConstructorDeclaration constructor(ConstructorDeclarationElement... elements) {
    return api().constructor(elements);
  }

  protected abstract void definition();

  protected final ArrayDimension dim() {
    return api().dim();
  }

  protected final EnumConstant enumConstant(EnumConstantElement... elements) {
    return api().enumConstant(elements);
  }

  protected final FieldDeclaration field(FieldDeclarationElement... elements) {
    return api().field(elements);
  }

  protected final IdentifierRef id(String name) {
    return api().id(name);
  }

  protected final IncludeRef include(IncludeTarget target) {
    return api().include(target);
  }

  protected final MethodInvocation invoke(
      MethodInvocationSubject subject, String methodName, MethodInvocationElement... elements) {
    return api().invoke(subject, methodName, elements);
  }

  protected final MethodInvocation invoke(
      String methodName, MethodInvocationElement... elements) {
    return api().invoke(methodName, elements);
  }

  protected final MethodDeclaration method(MethodDeclarationElement... elements) {
    return api().method(elements);
  }

  protected final ExpressionName n(ClassName name, String identifier) {
    return api().n(name, identifier);
  }

  protected final ExpressionName n(String value) {
    return api().n(value);
  }

  protected final FieldAccessExpression n(ThisKeyword keyword, String identifier) {
    return api().n(keyword, identifier);
  }

  protected final NewLineRef nl() {
    return api().nl();
  }

  protected final FormalParameter param(FormalParameterType type, IdentifierRef name) {
    return api().param(type, name);
  }

  protected final StringLiteral s(String value) {
    return api().s(value);
  }

  protected final ClassNameInvocation t(Class<?> value) {
    return api().t(value);
  }

  protected final ArrayTypeInvocation t(Class<?> type, ArrayTypeElement... elements) {
    return api().t(type, elements);
  }

  protected final LocalVariableDeclarationRef var(String name, Expression expression) {
    return api().var(name, expression);
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

  private TempInternalApi api() {
    Check.state(api != null, """
    An InternalApi instance was not set.

    Are you trying to execute the method directly?
    Please not that this method should only be invoked inside a definition() method.
    """);

    return api;
  }

}