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
import java.util.Objects;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import objectos.code.JavaModel.AnnotationElementValue;
import objectos.code.JavaModel.AnnotationInvocation;
import objectos.code.JavaModel.ArrayAccessExpression;
import objectos.code.JavaModel.ArrayDimension;
import objectos.code.JavaModel.ArrayTypeElement;
import objectos.code.JavaModel.ArrayTypeInvocation;
import objectos.code.JavaModel.AssignmentExpression;
import objectos.code.JavaModel.ClassDeclaration;
import objectos.code.JavaModel.ClassDeclarationElement;
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
import objectos.code.JavaModel.MarkerApi;
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
import objectos.code.JavaTemplate.IncludeTarget;
import objectos.lang.Check;

class InternalApi extends State implements MarkerApi {

  public final ClassDeclaration _class(ClassDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.CLASS_DECLARATION);

    return JavaModel.REF;
  }

  public final EnumDeclaration _enum(EnumDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.ENUM_DECLARATION);

    return JavaModel.REF;
  }

  public final ExtendsRef _extends(ClassName superclass) {
    object(ByteProto.EXTENDS, superclass);

    return JavaModel.REF;
  }

  public final FinalModifier _final() {
    object(ByteProto.MODIFIER, Modifier.FINAL);

    return JavaModel.REF;
  }

  public final Implements _implements(ClassName[] interfaces) {
    for (var iface : interfaces) {
      object(ByteProto.TYPE_NAME, iface);
    }

    markStart();

    markIncrement(interfaces.length);

    element(ByteProto.IMPLEMENTS);

    return JavaModel.REF;
  }

  public final IntPrimitiveType _int() {
    object(ByteProto.PRIMITIVE_TYPE, PrimitiveType.INT);

    return JavaModel.REF;
  }

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

  public final PrivateModifier _private() {
    object(ByteProto.MODIFIER, Modifier.PRIVATE);

    return JavaModel.REF;
  }

  public final ProtectedModifier _protected() {
    object(ByteProto.MODIFIER, Modifier.PROTECTED);

    return JavaModel.REF;
  }

  public final PublicModifier _public() {
    object(ByteProto.MODIFIER, Modifier.PUBLIC);

    return JavaModel.REF;
  }

  public final ReturnStatement _return(Expression expression) {
    markStart();

    expression.mark(this);

    element(ByteProto.RETURN_STATEMENT);

    return JavaModel.REF;
  }

  public final StaticModifier _static() {
    object(ByteProto.MODIFIER, Modifier.STATIC);

    return JavaModel.REF;
  }

  public final ThisKeyword _this() {
    markStart();

    element(ByteProto.THIS);

    return JavaModel.REF;
  }

  public final VoidInvocation _void() {
    object(ByteProto.TYPE_NAME, TypeName.VOID);

    return JavaModel.REF;
  }

  public final ArrayAccessExpression a(ExpressionName reference, Expression[] expressions) {
    markStart();

    reference.mark(this);

    for (var expression : expressions) {
      expression.mark(this);
    }

    element(ByteProto.ARRAY_ACCESS_EXPRESSION);

    return JavaModel.REF;
  }

  public final AnnotationInvocation annotation(Class<? extends Annotation> annotationType) {
    var name = ClassName.of(annotationType); // implicit null-check

    className(name);

    markStart();

    markReference();

    element(ByteProto.ANNOTATION);

    return JavaModel.REF;
  }

  public final AnnotationInvocation annotation(ClassName annotationType,
      AnnotationElementValue value) {
    className(annotationType);

    markStart();

    markReference();

    value.mark(this);

    element(ByteProto.ANNOTATION);

    return JavaModel.REF;
  }

  public final AssignmentExpression assign(
      AssignmentOperator operator, LeftHandSide leftHandSide, Expression expression) {
    Objects.requireNonNull(operator, "operator == null");

    object(ByteProto.ASSIGNMENT_OPERATOR, operator);

    markStart();

    markReference();

    leftHandSide.mark(this);

    expression.mark(this);

    element(ByteProto.ASSIGNMENT_EXPRESSION);

    return JavaModel.REF;
  }

  public final AssignmentExpression assign(LeftHandSide leftHandSide, Expression expression) {
    return assign(AssignmentOperator.SIMPLE, leftHandSide, expression);
  }

  public final void autoImports() {
    autoImports.enable();
  }

  public final ConstructorDeclaration constructor(ConstructorDeclarationElement[] elements) {
    markStart();

    for (var element : elements) {
      element.mark(this);
    }

    element(ByteProto.CONSTRUCTOR_DECLARATION);

    return JavaModel.REF;
  }

  public final ArrayDimension dim() {
    markStart();

    element(ByteProto.DIM);

    return JavaModel.REF;
  }

  public final EnumConstant enumConstant(EnumConstantElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.ENUM_CONSTANT);

    return JavaModel.REF;
  }

  public FieldDeclaration field(FieldDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.FIELD_DECLARATION);

    return JavaModel.REF;
  }

  public final IdentifierRef id(String name) {
    identifier(name);

    return JavaModel.REF;
  }

  public final IncludeRef include(IncludeTarget target) {
    lambdaStart();

    target.execute();

    lambdaEnd();

    return JavaModel.INCLUDE;
  }

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

    return JavaModel.REF;
  }

  public final MethodInvocation invoke(
      String methodName, MethodInvocationElement[] elements) {
    identifier(methodName);

    markStart();

    markReference();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    methodInvocation();

    return JavaModel.REF;
  }

  @Override
  public final void markLambda() {
    lambdaCount();
  }

  @Override
  public final void markReference() {
    markIncrement();
  }

  public final MethodDeclaration method(MethodDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.METHOD_DECLARATION);

    return JavaModel.REF;
  }

  public final ExpressionName n(ClassName name, String identifier) {
    className(name);

    identifier(identifier);

    markStart();

    markReference();

    markReference();

    element(ByteProto.EXPRESSION_NAME);

    return JavaModel.REF;
  }

  public final ExpressionName n(String value) {
    identifier(value);

    markStart();

    markReference();

    element(ByteProto.EXPRESSION_NAME);

    return JavaModel.REF;
  }

  public final FieldAccessExpression n(ThisKeyword keyword, String identifier) {
    identifier(identifier);

    markStart();

    markReference();

    markReference();

    element(ByteProto.FIELD_ACCESS_EXPRESSION0);

    return JavaModel.REF;
  }

  public final NewLineRef nl() {
    markStart();

    element(ByteProto.NEW_LINE);

    return JavaModel.REF;
  }

  public final FormalParameter param(FormalParameterType type, IdentifierRef name) {
    markStart();

    type.mark(this);

    name.mark(this);

    element(ByteProto.FORMAL_PARAMETER);

    return JavaModel.REF;
  }

  public final StringLiteral s(String value) {
    Check.notNull(value, "value == null");

    object(ByteProto.STRING_LITERAL, value);

    return JavaModel.REF;
  }

  public final ClassNameInvocation t(Class<?> type) {
    var cn = ClassName.of(type);

    return typeName(cn);
  }

  public final ArrayTypeInvocation t(Class<?> type, ArrayTypeElement[] elements) {
    var t = t(type);

    markStart();

    t.mark(this);

    for (var element : elements) {
      element.mark(this);
    }

    element(ByteProto.ARRAY_TYPE);

    return JavaModel.REF;
  }

  public final LocalVariableDeclarationRef var(String name, Expression expression) {
    identifier(name);

    markStart();

    markReference();

    expression.mark(this);

    element(ByteProto.LOCAL_VARIABLE);

    return JavaModel.REF;
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

    return JavaModel.REF;
  }

}