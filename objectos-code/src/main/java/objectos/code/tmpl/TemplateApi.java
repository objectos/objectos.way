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
package objectos.code.tmpl;

import java.lang.annotation.Annotation;
import objectos.code.AssignmentOperator;
import objectos.code.ClassName;
import objectos.code.tmpl.InternalApi.AnnotationElementValue;
import objectos.code.tmpl.InternalApi.AnnotationInvocation;
import objectos.code.tmpl.InternalApi.ArrayAccessExpression;
import objectos.code.tmpl.InternalApi.ArrayDimension;
import objectos.code.tmpl.InternalApi.ArrayTypeElement;
import objectos.code.tmpl.InternalApi.ArrayTypeInvocation;
import objectos.code.tmpl.InternalApi.AssignmentExpression;
import objectos.code.tmpl.InternalApi.ClassDeclaration;
import objectos.code.tmpl.InternalApi.ClassDeclarationElement;
import objectos.code.tmpl.InternalApi.ClassNameInvocation;
import objectos.code.tmpl.InternalApi.EnumConstant;
import objectos.code.tmpl.InternalApi.EnumConstantElement;
import objectos.code.tmpl.InternalApi.EnumDeclaration;
import objectos.code.tmpl.InternalApi.EnumDeclarationElement;
import objectos.code.tmpl.InternalApi.Expression;
import objectos.code.tmpl.InternalApi.ExpressionName;
import objectos.code.tmpl.InternalApi.ExtendsRef;
import objectos.code.tmpl.InternalApi.FieldDeclaration;
import objectos.code.tmpl.InternalApi.FieldDeclarationElement;
import objectos.code.tmpl.InternalApi.FinalModifier;
import objectos.code.tmpl.InternalApi.FormalParameter;
import objectos.code.tmpl.InternalApi.FormalParameterType;
import objectos.code.tmpl.InternalApi.IdentifierRef;
import objectos.code.tmpl.InternalApi.Implements;
import objectos.code.tmpl.InternalApi.IncludeRef;
import objectos.code.tmpl.InternalApi.IntPrimitiveType;
import objectos.code.tmpl.InternalApi.LeftHandSide;
import objectos.code.tmpl.InternalApi.LocalVariableDeclarationRef;
import objectos.code.tmpl.InternalApi.MethodDeclaration;
import objectos.code.tmpl.InternalApi.MethodDeclarationElement;
import objectos.code.tmpl.InternalApi.MethodInvocation;
import objectos.code.tmpl.InternalApi.MethodInvocationElement;
import objectos.code.tmpl.InternalApi.MethodInvocationSubject;
import objectos.code.tmpl.InternalApi.NewLineRef;
import objectos.code.tmpl.InternalApi.PrivateModifier;
import objectos.code.tmpl.InternalApi.PublicModifier;
import objectos.code.tmpl.InternalApi.ReturnStatement;
import objectos.code.tmpl.InternalApi.StaticModifier;
import objectos.code.tmpl.InternalApi.StringLiteral;
import objectos.code.tmpl.InternalApi.VoidInvocation;

public interface TemplateApi extends MarkerApi {

  ClassDeclaration _class(ClassDeclarationElement[] elements);

  EnumDeclaration _enum(EnumDeclarationElement[] elements);

  ExtendsRef _extends(ClassName superclass);

  FinalModifier _final();

  Implements _implements(ClassName[] interfaces);

  IntPrimitiveType _int();

  void _package(String packageName);

  PrivateModifier _private();

  PublicModifier _public();

  ReturnStatement _return(Expression expression);

  StaticModifier _static();

  VoidInvocation _void();

  ArrayAccessExpression a(ExpressionName reference, Expression[] expressions);

  AnnotationInvocation annotation(Class<? extends Annotation> annotationType);

  AnnotationInvocation annotation(ClassName annotationType, AnnotationElementValue value);

  AssignmentExpression assign(
      AssignmentOperator operator, LeftHandSide leftHandSide, Expression expression);

  AssignmentExpression assign(LeftHandSide leftHandSide, Expression expression);

  void autoImports();

  ArrayDimension dim();

  EnumConstant enumConstant(EnumConstantElement[] elements);

  FieldDeclaration field(FieldDeclarationElement[] elements);

  IdentifierRef id(String name);

  IncludeRef include(IncludeTarget target);

  MethodInvocation invoke(
      MethodInvocationSubject subject, String methodName, MethodInvocationElement[] elements);

  MethodInvocation invoke(String methodName, MethodInvocationElement[] elements);

  MethodDeclaration method(MethodDeclarationElement[] elements);

  ExpressionName n(ClassName name, String identifier);

  ExpressionName n(String value);

  NewLineRef nl();

  FormalParameter param(FormalParameterType type, IdentifierRef name);

  StringLiteral s(String value);

  ClassNameInvocation t(Class<?> type);

  ArrayTypeInvocation t(Class<?> type, ArrayTypeElement[] elements);

  LocalVariableDeclarationRef var(String name, Expression expression);

}