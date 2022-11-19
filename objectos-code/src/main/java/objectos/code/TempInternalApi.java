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
import objectos.code.JavaTemplate.IncludeTarget;

interface TempInternalApi {

  ClassDeclaration _class(ClassDeclarationElement[] elements);

  EnumDeclaration _enum(EnumDeclarationElement[] elements);

  ExtendsRef _extends(ClassName superclass);

  FinalModifier _final();

  Implements _implements(ClassName[] interfaces);

  IntPrimitiveType _int();

  ClassInstanceCreationExpression _new(ClassNameInvocation type, Expression[] arguments);

  void _package(String packageName);

  PrivateModifier _private();

  ProtectedModifier _protected();

  PublicModifier _public();

  ReturnStatement _return(Expression expression);

  StaticModifier _static();

  ThisKeyword _this();

  VoidInvocation _void();

  ArrayAccessExpression a(ExpressionName reference, Expression[] expressions);

  AnnotationInvocation annotation(Class<? extends Annotation> annotationType);

  AnnotationInvocation annotation(ClassName annotationType, AnnotationElementValue value);

  AssignmentExpression assign(LeftHandSide leftHandSide, Expression expression);

  void autoImports();

  ConstructorDeclaration constructor(ConstructorDeclarationElement[] elements);

  ArrayDimension dim();

  EnumConstant enumConstant(EnumConstantElement[] elements);

  FieldDeclaration field(FieldDeclarationElement[] elements);

  IdentifierRef id(String name);

  IncludeRef include(IncludeTarget target);

  MethodInvocation invoke(MethodInvocationSubject subject, String methodName,
      MethodInvocationElement[] elements);

  MethodInvocation invoke(String methodName, MethodInvocationElement[] elements);

  MethodDeclaration method(MethodDeclarationElement[] elements);

  ExpressionName n(ClassName name, String identifier);

  ExpressionName n(String value);

  FieldAccessExpression n(ThisKeyword keyword, String identifier);

  NewLineRef nl();

  FormalParameter param(FormalParameterType type, IdentifierRef name);

  StringLiteral s(String value);

  ClassNameInvocation t(Class<?> value);

  ArrayTypeInvocation t(Class<?> type, ArrayTypeElement[] elements);

  LocalVariableDeclarationRef var(String name, Expression expression);

}