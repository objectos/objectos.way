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
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import objectos.code.tmpl.IncludeTarget;
import objectos.code.tmpl.InternalApi;
import objectos.code.tmpl.InternalApi.AnnotationElementValue;
import objectos.code.tmpl.InternalApi.AtRef;
import objectos.code.tmpl.InternalApi.ClassDeclaration;
import objectos.code.tmpl.InternalApi.ClassDeclarationElement;
import objectos.code.tmpl.InternalApi.ClassNameInvocation;
import objectos.code.tmpl.InternalApi.EnumConstant;
import objectos.code.tmpl.InternalApi.EnumConstantElement;
import objectos.code.tmpl.InternalApi.EnumDeclaration;
import objectos.code.tmpl.InternalApi.EnumDeclarationElement;
import objectos.code.tmpl.InternalApi.Expression;
import objectos.code.tmpl.InternalApi.ExpressionNameRef;
import objectos.code.tmpl.InternalApi.ExtendsRef;
import objectos.code.tmpl.InternalApi.FieldDeclaration;
import objectos.code.tmpl.InternalApi.FieldDeclarationElement;
import objectos.code.tmpl.InternalApi.FinalModifier;
import objectos.code.tmpl.InternalApi.IdentifierRef;
import objectos.code.tmpl.InternalApi.Implements;
import objectos.code.tmpl.InternalApi.IncludeRef;
import objectos.code.tmpl.InternalApi.LocalVariableDeclarationRef;
import objectos.code.tmpl.InternalApi.MethodDeclaration;
import objectos.code.tmpl.InternalApi.MethodDeclarationElement;
import objectos.code.tmpl.InternalApi.MethodInvocation;
import objectos.code.tmpl.InternalApi.MethodInvocationElement;
import objectos.code.tmpl.InternalApi.MethodInvocationSubject;
import objectos.code.tmpl.InternalApi.NewLineRef;
import objectos.code.tmpl.InternalApi.PrivateModifier;
import objectos.code.tmpl.InternalApi.PublicModifier;
import objectos.code.tmpl.InternalApi.StaticModifier;
import objectos.code.tmpl.InternalApi.StringLiteral;
import objectos.code.tmpl.InternalApi.VoidRef;
import objectos.code.tmpl.TemplateApi;
import objectos.lang.Check;

class Pass0 extends State implements TemplateApi {

  @Override
  public final ClassDeclaration _class(ClassDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.CLASS_DECLARATION);

    return InternalApi.REF;
  }

  @Override
  public final EnumDeclaration _enum(EnumDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.ENUM_DECLARATION);

    return InternalApi.REF;
  }

  @Override
  public final ExtendsRef _extends(ClassName superclass) {
    object(ByteProto.EXTENDS, superclass);

    return InternalApi.REF;
  }

  @Override
  public final FinalModifier _final() {
    object(ByteProto.MODIFIER, Modifier.FINAL);

    return InternalApi.REF;
  }

  @Override
  public final Implements _implements(ClassName[] interfaces) {
    for (var iface : interfaces) {
      object(ByteProto.TYPE_NAME, iface);
    }

    markStart();

    markIncrement(interfaces.length);

    element(ByteProto.IMPLEMENTS);

    return InternalApi.REF;
  }

  @Override
  public final void _package(String packageName) {
    Check.argument(
      SourceVersion.isName(packageName),
      packageName, " is not a valid package name"
    );

    autoImports.packageName(packageName);

    object(ByteProto.PACKAGE_NAME, packageName);

    markStart();

    markReference();

    element(ByteProto.PACKAGE_DECLARATION);
  }

  @Override
  public final PrivateModifier _private() {
    object(ByteProto.MODIFIER, Modifier.PRIVATE);

    return InternalApi.REF;
  }

  @Override
  public final PublicModifier _public() {
    object(ByteProto.MODIFIER, Modifier.PUBLIC);

    return InternalApi.REF;
  }

  @Override
  public final StaticModifier _static() {
    object(ByteProto.MODIFIER, Modifier.STATIC);

    return InternalApi.REF;
  }

  @Override
  public final VoidRef _void() {
    object(ByteProto.TYPE_NAME, TypeName.VOID);

    return InternalApi.REF;
  }

  @Override
  public final AtRef annotation(Class<? extends Annotation> annotationType) {
    var name = ClassName.of(annotationType); // implicit null-check

    className(name);

    markStart();

    markReference();

    element(ByteProto.ANNOTATION);

    return InternalApi.REF;
  }

  @Override
  public final AtRef annotation(ClassName annotationType, AnnotationElementValue value) {
    className(annotationType);

    markStart();

    markReference();

    value.mark(this);

    element(ByteProto.ANNOTATION);

    return InternalApi.REF;
  }

  @Override
  public final void autoImports() {
    autoImports.enable();
  }

  @Override
  public final EnumConstant enumConstant(EnumConstantElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.ENUM_CONSTANT);

    return InternalApi.REF;
  }

  @Override
  public FieldDeclaration field(FieldDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.FIELD_DECLARATION);

    return InternalApi.REF;
  }

  @Override
  public final IdentifierRef id(String name) {
    identifier(name);

    return InternalApi.REF;
  }

  @Override
  public final IncludeRef include(IncludeTarget target) {
    lambdaStart();

    target.execute();

    lambdaEnd();

    return InternalApi.INCLUDE;
  }

  @Override
  public final MethodInvocation invoke(
      MethodInvocationSubject subject, String methodName, MethodInvocationElement[] elements) {
    identifier(methodName);

    markStart();

    subject.mark(this);

    markReference();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    methodInvocation();

    return InternalApi.REF;
  }

  @Override
  public final MethodInvocation invoke(
      String methodName, MethodInvocationElement[] elements) {
    identifier(methodName);

    markStart();

    markReference();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    methodInvocation();

    return InternalApi.REF;
  }

  @Override
  public final void markLambda() {
    lambdaCount();
  }

  @Override
  public final void markReference() {
    markIncrement();
  }

  @Override
  public final MethodDeclaration method(MethodDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.METHOD_DECLARATION);

    return InternalApi.REF;
  }

  @Override
  public final ExpressionNameRef n(ClassName name, String identifier) {
    className(name);

    identifier(identifier);

    markStart();

    markReference();

    markReference();

    element(ByteProto.EXPRESSION_NAME);

    return InternalApi.REF;
  }

  @Override
  public final ExpressionNameRef n(String value) {
    identifier(value);

    markStart();

    markReference();

    element(ByteProto.EXPRESSION_NAME);

    return InternalApi.REF;
  }

  @Override
  public final NewLineRef nl() {
    markStart();

    element(ByteProto.NEW_LINE);

    return InternalApi.REF;
  }

  @Override
  public final StringLiteral s(String value) {
    Check.notNull(value, "value == null");

    object(ByteProto.STRING_LITERAL, value);

    return InternalApi.REF;
  }

  @Override
  public final ClassNameInvocation t(Class<?> type) {
    var cn = ClassName.of(type);

    return typeName(cn);
  }

  @Override
  public final LocalVariableDeclarationRef var(String name, Expression expression) {
    identifier(name);

    markStart();

    markReference();

    expression.mark(this);

    element(ByteProto.LOCAL_VARIABLE);

    return InternalApi.REF;
  }

  final void pass0(JavaTemplate template) {
    pass0Start();

    template.execute(this);

    pass0End();
  }

  private void className(ClassName name) {
    object(ByteProto.CLASS_NAME, name);
  }

  private void identifier(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    object(ByteProto.IDENTIFIER, name);
  }

  private void lambdaEnd() {
    lambdaPop();
  }

  private void lambdaStart() {
    lambdaPush();
  }

  private void markStart() {
    markPush();
  }

  private void methodInvocation() {
    element(ByteProto.METHOD_INVOCATION);
  }

  private void pass0End() {
    markStart();

    for (int i = 0; i < codeIndex; i++) {
      markReference();
    }

    element(ByteProto.COMPILATION_UNIT);

    if (codeIndex != 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    protoArray[1] = codeArray[0];
  }

  private void pass0Start() {
    autoImports.clear();

    codeIndex = 0;

    objectIndex = 0;

    stackIndex = -1;

    markIndex = -1;

    protoIndex = 0;

    protoAdd(ByteProto.JMP, ByteProto.NULL);
  }

  private ClassNameInvocation typeName(TypeName value) {
    object(ByteProto.TYPE_NAME, value);

    return InternalApi.REF;
  }

}