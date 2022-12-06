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
import objectos.code.JavaModel.ExtendsMany;
import objectos.code.JavaModel.ExtendsSingle;
import objectos.code.JavaModel.FieldAccessExpression;
import objectos.code.JavaModel.FieldDeclaration;
import objectos.code.JavaModel.FieldDeclarationElement;
import objectos.code.JavaModel.FinalModifier;
import objectos.code.JavaModel.FormalParameter;
import objectos.code.JavaModel.FormalParameterElement;
import objectos.code.JavaModel.IdentifierRef;
import objectos.code.JavaModel.Implements;
import objectos.code.JavaModel.IncludeRef;
import objectos.code.JavaModel.IntegerLiteral;
import objectos.code.JavaModel.InterfaceDeclaration;
import objectos.code.JavaModel.InterfaceDeclarationElement;
import objectos.code.JavaModel.LeftHandSide;
import objectos.code.JavaModel.LocalVariableDeclarationRef;
import objectos.code.JavaModel.Markable;
import objectos.code.JavaModel.MethodDeclaration;
import objectos.code.JavaModel.MethodDeclarationElement;
import objectos.code.JavaModel.MethodInvocationElement;
import objectos.code.JavaModel.NewLineRef;
import objectos.code.JavaModel.ParameterizedClassType;
import objectos.code.JavaModel.PrimitiveType;
import objectos.code.JavaModel.PrivateModifier;
import objectos.code.JavaModel.ProtectedModifier;
import objectos.code.JavaModel.PublicModifier;
import objectos.code.JavaModel.QualifiedMethodInvocation;
import objectos.code.JavaModel.ReturnStatement;
import objectos.code.JavaModel.StaticModifier;
import objectos.code.JavaModel.StringLiteral;
import objectos.code.JavaModel.ThisKeyword;
import objectos.code.JavaModel.TypeParameter;
import objectos.code.JavaModel.TypeParameterBound;
import objectos.code.JavaModel.TypeVariable;
import objectos.code.JavaModel.UnqualifiedMethodInvocation;
import objectos.code.JavaModel.VoidInvocation;
import objectos.code.JavaTemplate.IncludeTarget;

interface TempInternalApi {

  AbstractModifier _abstract();

  PrimitiveType _boolean();

  ClassDeclaration _class(ClassDeclarationElement[] elements);

  PrimitiveType _double();

  EnumDeclaration _enum(EnumDeclarationElement[] elements);

  ExtendsSingle _extends(ClassType value);

  ExtendsMany _extends(ClassType[] interfaces);

  FinalModifier _final();

  Implements _implements(ClassType[] interfaces);

  PrimitiveType _int();

  InterfaceDeclaration _interface(InterfaceDeclarationElement[] elements);

  ClassInstanceCreationExpression _new(ClassType type, Expression[] arguments);

  void _package(String packageName);

  PrivateModifier _private();

  ProtectedModifier _protected();

  PublicModifier _public();

  ReturnStatement _return(Expression expression);

  StaticModifier _static();

  ExplicitConstructorInvocation _super(Expression[] arguments);

  ThisKeyword _this();

  VoidInvocation _void();

  ArrayInitializer a(ArrayInitializerElement[] elements);

  ArrayAccessExpression aget(ExpressionName reference, Expression[] expressions);

  AnnotationInvocation annotation(ClassType annotationType);

  AnnotationInvocation annotation(ClassType annotationType, AnnotationElementValue value);

  AssignmentExpression assign(LeftHandSide leftHandSide, Expression expression);

  void autoImports();

  Block block(BlockElement[] elements);

  ChainedMethodInvocation chain(ChainedMethodInvocationHead first,
      ChainedMethodInvocationElement[] more);

  ConstructorDeclaration constructor(ConstructorDeclarationElement[] elements);

  ArrayDimension dim();

  Ellipsis ellipsis();

  EnumConstant enumConstant(EnumConstantElement[] elements);

  FieldDeclaration field(FieldDeclarationElement[] elements);

  IntegerLiteral i(int value);

  IdentifierRef id(String name);

  IncludeRef include(IncludeTarget target);

  QualifiedMethodInvocation invoke(Markable subject, String methodName,
      MethodInvocationElement[] elements);

  UnqualifiedMethodInvocation invoke(String methodName, MethodInvocationElement[] elements);

  MethodDeclaration method(MethodDeclarationElement[] elements);

  ExpressionName n(ClassType type, String[] identifiers);

  ExpressionName n(String[] identifiers);

  FieldAccessExpression n(ThisKeyword keyword, String identifier);

  NewLineRef nl();

  FormalParameter param(FormalParameterElement[] elements);

  StringLiteral s(String value);

  ArrayType t(ArrayTypeComponent type, ArrayTypeElement[] elements);

  ClassType t(Class<?> value);

  ParameterizedClassType t(ClassType rawType, AnyType[] arguments);

  ClassType t(ClassType outer, String name);

  ClassType t(String packageName, String simpleName);

  TypeParameter tparam(String name, TypeParameterBound[] bounds);

  TypeVariable tvar(String name);

  LocalVariableDeclarationRef var(String name, Expression expression);

}