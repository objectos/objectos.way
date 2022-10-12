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
package br.com.objectos.code.java;

import br.com.objectos.code.java.declaration.AbstractModifier;
import br.com.objectos.code.java.declaration.AnnotationCode;
import br.com.objectos.code.java.declaration.AnnotationCodeElement;
import br.com.objectos.code.java.declaration.AnnotationCodeValue;
import br.com.objectos.code.java.declaration.AnnotationCodeValuePair;
import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.declaration.ClassCodeElement;
import br.com.objectos.code.java.declaration.ConstructorCode;
import br.com.objectos.code.java.declaration.ConstructorCodeElement;
import br.com.objectos.code.java.declaration.DefaultModifier;
import br.com.objectos.code.java.declaration.EnumCode;
import br.com.objectos.code.java.declaration.EnumCodeElement;
import br.com.objectos.code.java.declaration.EnumConstantCode;
import br.com.objectos.code.java.declaration.EnumConstantCodeElement;
import br.com.objectos.code.java.declaration.EnumConstantList;
import br.com.objectos.code.java.declaration.ExtendsMany;
import br.com.objectos.code.java.declaration.ExtendsOne;
import br.com.objectos.code.java.declaration.FieldCode;
import br.com.objectos.code.java.declaration.FieldCodeDeclarator;
import br.com.objectos.code.java.declaration.FieldCodeElement;
import br.com.objectos.code.java.declaration.FieldsShorthand;
import br.com.objectos.code.java.declaration.FinalModifier;
import br.com.objectos.code.java.declaration.Implements;
import br.com.objectos.code.java.declaration.InterfaceCode;
import br.com.objectos.code.java.declaration.InterfaceCodeElement;
import br.com.objectos.code.java.declaration.MethodCode;
import br.com.objectos.code.java.declaration.MethodCode.OverridingProcessingMethod;
import br.com.objectos.code.java.declaration.MethodCode.SignatureOfProcessingMethod;
import br.com.objectos.code.java.declaration.MethodCodeElement;
import br.com.objectos.code.java.declaration.MethodsShorthand;
import br.com.objectos.code.java.declaration.Modifier;
import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.java.declaration.NativeModifier;
import br.com.objectos.code.java.declaration.NonSealedModifier;
import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.declaration.ParameterCode;
import br.com.objectos.code.java.declaration.ParameterCode.Builder;
import br.com.objectos.code.java.declaration.ParameterCode.ParamsShorthand;
import br.com.objectos.code.java.declaration.ParameterTypeName;
import br.com.objectos.code.java.declaration.PrivateModifier;
import br.com.objectos.code.java.declaration.ProtectedModifier;
import br.com.objectos.code.java.declaration.PublicModifier;
import br.com.objectos.code.java.declaration.SealedModifier;
import br.com.objectos.code.java.declaration.StaticModifier;
import br.com.objectos.code.java.declaration.StrictfpModifier;
import br.com.objectos.code.java.declaration.SuperConstructorInvocation;
import br.com.objectos.code.java.declaration.SynchronizedModifier;
import br.com.objectos.code.java.declaration.ThisConstructorInvocation;
import br.com.objectos.code.java.declaration.ThrowsElement;
import br.com.objectos.code.java.declaration.ThrowsShorthand;
import br.com.objectos.code.java.declaration.TransientModifier;
import br.com.objectos.code.java.declaration.TypeCode;
import br.com.objectos.code.java.declaration.TypesShorthand;
import br.com.objectos.code.java.declaration.VolatileModifier;
import br.com.objectos.code.java.element.BreakKeyword;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.ContinueKeyword;
import br.com.objectos.code.java.element.ElseKeyword;
import br.com.objectos.code.java.element.FinallyKeyword;
import br.com.objectos.code.java.element.Keyword;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.element.NewLine;
import br.com.objectos.code.java.element.NoopCodeElement;
import br.com.objectos.code.java.element.ReturnKeyword;
import br.com.objectos.code.java.element.SuperKeyword;
import br.com.objectos.code.java.element.ThisKeyword;
import br.com.objectos.code.java.expression.AdditiveExpression;
import br.com.objectos.code.java.expression.AndExpression;
import br.com.objectos.code.java.expression.Argument;
import br.com.objectos.code.java.expression.Arguments;
import br.com.objectos.code.java.expression.ArgumentsElement;
import br.com.objectos.code.java.expression.ArrayAccess;
import br.com.objectos.code.java.expression.ArrayCreationExpression;
import br.com.objectos.code.java.expression.ArrayInitializer;
import br.com.objectos.code.java.expression.ArrayReferenceExpression;
import br.com.objectos.code.java.expression.Assignment;
import br.com.objectos.code.java.expression.AssignmentOperator;
import br.com.objectos.code.java.expression.Callee;
import br.com.objectos.code.java.expression.CastExpression;
import br.com.objectos.code.java.expression.ConditionalAndExpression;
import br.com.objectos.code.java.expression.ConditionalExpression;
import br.com.objectos.code.java.expression.ConditionalOrExpression;
import br.com.objectos.code.java.expression.EmptyExpression;
import br.com.objectos.code.java.expression.EqualityExpression;
import br.com.objectos.code.java.expression.ExclusiveOrExpression;
import br.com.objectos.code.java.expression.Expression;
import br.com.objectos.code.java.expression.ExpressionName;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.FieldAccess;
import br.com.objectos.code.java.expression.FieldAccessReferenceExpression;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.expression.IdentifierBuilder;
import br.com.objectos.code.java.expression.InclusiveOrExpression;
import br.com.objectos.code.java.expression.LambdaBody;
import br.com.objectos.code.java.expression.LambdaExpression;
import br.com.objectos.code.java.expression.LambdaParameter;
import br.com.objectos.code.java.expression.LeftHandSide;
import br.com.objectos.code.java.expression.Literal;
import br.com.objectos.code.java.expression.MethodInvocation;
import br.com.objectos.code.java.expression.MethodInvocation.Unqualified;
import br.com.objectos.code.java.expression.MethodInvocationChain;
import br.com.objectos.code.java.expression.MethodInvocationChainElement;
import br.com.objectos.code.java.expression.MethodReference;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MultiplicativeExpression;
import br.com.objectos.code.java.expression.NewClass;
import br.com.objectos.code.java.expression.ParenthesizedExpression;
import br.com.objectos.code.java.expression.PostDecrementExpression;
import br.com.objectos.code.java.expression.PostIncrementExpression;
import br.com.objectos.code.java.expression.PostfixExpression;
import br.com.objectos.code.java.expression.PreDecrementExpression;
import br.com.objectos.code.java.expression.PreIncrementExpression;
import br.com.objectos.code.java.expression.RelationalExpression;
import br.com.objectos.code.java.expression.ShiftExpression;
import br.com.objectos.code.java.expression.ThrowableExpression;
import br.com.objectos.code.java.expression.TypeWitness;
import br.com.objectos.code.java.expression.UnaryExpression;
import br.com.objectos.code.java.expression.UnaryExpressionNotPlusMinus;
import br.com.objectos.code.java.io.JavaFile;
import br.com.objectos.code.java.statement.AssertStatement;
import br.com.objectos.code.java.statement.Block;
import br.com.objectos.code.java.statement.BlockElement;
import br.com.objectos.code.java.statement.BlockStatement;
import br.com.objectos.code.java.statement.BreakStatement;
import br.com.objectos.code.java.statement.CaseSwitchElement;
import br.com.objectos.code.java.statement.CatchElement;
import br.com.objectos.code.java.statement.ContinueStatement;
import br.com.objectos.code.java.statement.DefaultSwitchElement;
import br.com.objectos.code.java.statement.DoStatement;
import br.com.objectos.code.java.statement.DoStatement.WhileElement;
import br.com.objectos.code.java.statement.ForConditionElement;
import br.com.objectos.code.java.statement.ForInitElement;
import br.com.objectos.code.java.statement.ForStatement;
import br.com.objectos.code.java.statement.ForStatementElement;
import br.com.objectos.code.java.statement.ForUpdateElement;
import br.com.objectos.code.java.statement.IfStatement;
import br.com.objectos.code.java.statement.IfStatementElement;
import br.com.objectos.code.java.statement.ResourceElement;
import br.com.objectos.code.java.statement.ReturnStatement;
import br.com.objectos.code.java.statement.SimpleLocalVariableDeclaration;
import br.com.objectos.code.java.statement.Statement;
import br.com.objectos.code.java.statement.Statements;
import br.com.objectos.code.java.statement.StatementsShorthand;
import br.com.objectos.code.java.statement.SwitchElement;
import br.com.objectos.code.java.statement.SwitchStatement;
import br.com.objectos.code.java.statement.SynchronizedStatement;
import br.com.objectos.code.java.statement.ThrowStatement;
import br.com.objectos.code.java.statement.TryStatement;
import br.com.objectos.code.java.statement.TryStatementElement;
import br.com.objectos.code.java.statement.VariableInitializer;
import br.com.objectos.code.java.statement.WhileStatement;
import br.com.objectos.code.java.statement.WithInitLocalVariableDeclaration;
import br.com.objectos.code.java.type.NamedArray;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedClassOrParameterized;
import br.com.objectos.code.java.type.NamedParameterized;
import br.com.objectos.code.java.type.NamedPrimitive;
import br.com.objectos.code.java.type.NamedReferenceType;
import br.com.objectos.code.java.type.NamedSingleDimensionArrayComponent;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedTypeParameter;
import br.com.objectos.code.java.type.NamedTypeVariable;
import br.com.objectos.code.java.type.NamedTypes;
import br.com.objectos.code.java.type.NamedVoid;
import br.com.objectos.code.java.type.NamedWildcard;
import br.com.objectos.code.model.element.ProcessingMethod;
import java.lang.annotation.Annotation;

public final class Java {

  public static final AbstractModifier ABSTRACT = Modifiers.ABSTRACT;

  public static final DefaultModifier DEFAULT = Modifiers.DEFAULT;

  public static final FinalModifier FINAL = Modifiers.FINAL;

  public static final NativeModifier NATIVE = Modifiers.NATIVE;

  public static final NonSealedModifier NON_SEALED = Modifiers.NON_SEALED;

  public static final PrivateModifier PRIVATE = Modifiers.PRIVATE;

  public static final ProtectedModifier PROTECTED = Modifiers.PROTECTED;

  public static final PublicModifier PUBLIC = Modifiers.PUBLIC;

  public static final SealedModifier SEALED = Modifiers.SEALED;

  public static final StaticModifier STATIC = Modifiers.STATIC;

  public static final StrictfpModifier STRICTFP = Modifiers.STRICTFP;

  public static final SynchronizedModifier SYNCHRONIZED = Modifiers.SYNCHRONIZED;

  public static final TransientModifier TRANSIENT = Modifiers.TRANSIENT;

  public static final VolatileModifier VOLATILE = Modifiers.VOLATILE;

  private Java() {}

  public static AnnotationCode annotation(AnnotationCodeElement... elements) {
    return AnnotationCode.annotation(elements);
  }

  public static AnnotationCode annotation(AnnotationCodeElement v1, AnnotationCodeElement v2) {
    return AnnotationCode.annotation(v1, v2);
  }

  public static AnnotationCode annotation(AnnotationCodeElement v1, AnnotationCodeElement v2, AnnotationCodeElement v3) {
    return AnnotationCode.annotation(v1, v2, v3);
  }

  public static AnnotationCode annotation(AnnotationCodeElement v1, AnnotationCodeElement v2, AnnotationCodeElement v3, AnnotationCodeElement v4) {
    return AnnotationCode.annotation(v1, v2, v3, v4);
  }

  public static AnnotationCode annotation(AnnotationCodeElement v1, AnnotationCodeElement v2, AnnotationCodeElement v3, AnnotationCodeElement v4, AnnotationCodeElement v5) {
    return AnnotationCode.annotation(v1, v2, v3, v4, v5);
  }

  public static AnnotationCode annotation(AnnotationCodeElement v1, AnnotationCodeElement v2, AnnotationCodeElement v3, AnnotationCodeElement v4, AnnotationCodeElement v5, AnnotationCodeElement v6) {
    return AnnotationCode.annotation(v1, v2, v3, v4, v5, v6);
  }

  public static AnnotationCode annotation(AnnotationCodeElement v1, AnnotationCodeElement v2, AnnotationCodeElement v3, AnnotationCodeElement v4, AnnotationCodeElement v5, AnnotationCodeElement v6, AnnotationCodeElement v7) {
    return AnnotationCode.annotation(v1, v2, v3, v4, v5, v6, v7);
  }

  public static AnnotationCode annotation(AnnotationCodeElement v1, AnnotationCodeElement v2, AnnotationCodeElement v3, AnnotationCodeElement v4, AnnotationCodeElement v5, AnnotationCodeElement v6, AnnotationCodeElement v7, AnnotationCodeElement v8) {
    return AnnotationCode.annotation(v1, v2, v3, v4, v5, v6, v7, v8);
  }

  public static AnnotationCode annotation(AnnotationCodeElement v1, AnnotationCodeElement v2, AnnotationCodeElement v3, AnnotationCodeElement v4, AnnotationCodeElement v5, AnnotationCodeElement v6, AnnotationCodeElement v7, AnnotationCodeElement v8, AnnotationCodeElement v9) {
    return AnnotationCode.annotation(v1, v2, v3, v4, v5, v6, v7, v8, v9);
  }

  public static AnnotationCode annotation(Class<? extends Annotation> annotationType) {
    return AnnotationCode.annotation(annotationType);
  }

  public static AnnotationCode annotation(Class<? extends Annotation> annotationType, AnnotationCodeValue value) {
    return AnnotationCode.annotation(annotationType, value);
  }

  public static AnnotationCode annotation(NamedClass className) {
    return AnnotationCode.annotation(className);
  }

  public static AnnotationCode annotation(NamedClass className, AnnotationCodeValue value) {
    return AnnotationCode.annotation(className, value);
  }

  public static AnnotationCodeValuePair value(String name, AnnotationCodeValue value) {
    return AnnotationCodeValuePair.value(name, value);
  }

  public static ClassCode _class(ClassCodeElement e1) {
    return ClassCode._class(e1);
  }

  public static ClassCode _class(ClassCodeElement... elements) {
    return ClassCode._class(elements);
  }

  public static ClassCode _class(ClassCodeElement e1, ClassCodeElement e2) {
    return ClassCode._class(e1, e2);
  }

  public static ClassCode _class(ClassCodeElement e1, ClassCodeElement e2, ClassCodeElement e3) {
    return ClassCode._class(e1, e2, e3);
  }

  public static ClassCode _class(ClassCodeElement e1, ClassCodeElement e2, ClassCodeElement e3, ClassCodeElement e4) {
    return ClassCode._class(e1, e2, e3, e4);
  }

  public static ClassCode _class(ClassCodeElement e1, ClassCodeElement e2, ClassCodeElement e3, ClassCodeElement e4, ClassCodeElement e5) {
    return ClassCode._class(e1, e2, e3, e4, e5);
  }

  public static ClassCode _class(ClassCodeElement e1, ClassCodeElement e2, ClassCodeElement e3, ClassCodeElement e4, ClassCodeElement e5, ClassCodeElement e6) {
    return ClassCode._class(e1, e2, e3, e4, e5, e6);
  }

  public static ClassCode _class(ClassCodeElement e1, ClassCodeElement e2, ClassCodeElement e3, ClassCodeElement e4, ClassCodeElement e5, ClassCodeElement e6, ClassCodeElement e7) {
    return ClassCode._class(e1, e2, e3, e4, e5, e6, e7);
  }

  public static ClassCode _class(ClassCodeElement e1, ClassCodeElement e2, ClassCodeElement e3, ClassCodeElement e4, ClassCodeElement e5, ClassCodeElement e6, ClassCodeElement e7, ClassCodeElement e8) {
    return ClassCode._class(e1, e2, e3, e4, e5, e6, e7, e8);
  }

  public static ClassCode _class(ClassCodeElement e1, ClassCodeElement e2, ClassCodeElement e3, ClassCodeElement e4, ClassCodeElement e5, ClassCodeElement e6, ClassCodeElement e7, ClassCodeElement e8, ClassCodeElement e9) {
    return ClassCode._class(e1, e2, e3, e4, e5, e6, e7, e8, e9);
  }

  public static ConstructorCode constructor() {
    return ConstructorCode.constructor();
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1) {
    return ConstructorCode.constructor(e1);
  }

  public static ConstructorCode constructor(ConstructorCodeElement... elements) {
    return ConstructorCode.constructor(elements);
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1, ConstructorCodeElement e2) {
    return ConstructorCode.constructor(e1, e2);
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1, ConstructorCodeElement e2, ConstructorCodeElement e3) {
    return ConstructorCode.constructor(e1, e2, e3);
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1, ConstructorCodeElement e2, ConstructorCodeElement e3, ConstructorCodeElement e4) {
    return ConstructorCode.constructor(e1, e2, e3, e4);
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1, ConstructorCodeElement e2, ConstructorCodeElement e3, ConstructorCodeElement e4, ConstructorCodeElement e5) {
    return ConstructorCode.constructor(e1, e2, e3, e4, e5);
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1, ConstructorCodeElement e2, ConstructorCodeElement e3, ConstructorCodeElement e4, ConstructorCodeElement e5, ConstructorCodeElement e6) {
    return ConstructorCode.constructor(e1, e2, e3, e4, e5, e6);
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1, ConstructorCodeElement e2, ConstructorCodeElement e3, ConstructorCodeElement e4, ConstructorCodeElement e5, ConstructorCodeElement e6, ConstructorCodeElement e7) {
    return ConstructorCode.constructor(e1, e2, e3, e4, e5, e6, e7);
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1, ConstructorCodeElement e2, ConstructorCodeElement e3, ConstructorCodeElement e4, ConstructorCodeElement e5, ConstructorCodeElement e6, ConstructorCodeElement e7, ConstructorCodeElement e8) {
    return ConstructorCode.constructor(e1, e2, e3, e4, e5, e6, e7, e8);
  }

  public static ConstructorCode constructor(ConstructorCodeElement e1, ConstructorCodeElement e2, ConstructorCodeElement e3, ConstructorCodeElement e4, ConstructorCodeElement e5, ConstructorCodeElement e6, ConstructorCodeElement e7, ConstructorCodeElement e8, ConstructorCodeElement e9) {
    return ConstructorCode.constructor(e1, e2, e3, e4, e5, e6, e7, e8, e9);
  }

  public static ConstructorCode privateConstructor() {
    return ConstructorCode.privateConstructor();
  }

  public static ConstructorCode protectedConstructor() {
    return ConstructorCode.protectedConstructor();
  }

  public static ConstructorCode publicConstructor() {
    return ConstructorCode.publicConstructor();
  }

  public static EnumCode _enum(EnumCodeElement e1) {
    return EnumCode._enum(e1);
  }

  public static EnumCode _enum(EnumCodeElement... elements) {
    return EnumCode._enum(elements);
  }

  public static EnumCode _enum(EnumCodeElement e1, EnumCodeElement e2) {
    return EnumCode._enum(e1, e2);
  }

  public static EnumCode _enum(EnumCodeElement e1, EnumCodeElement e2, EnumCodeElement e3) {
    return EnumCode._enum(e1, e2, e3);
  }

  public static EnumCode _enum(EnumCodeElement e1, EnumCodeElement e2, EnumCodeElement e3, EnumCodeElement e4) {
    return EnumCode._enum(e1, e2, e3, e4);
  }

  public static EnumCode _enum(EnumCodeElement e1, EnumCodeElement e2, EnumCodeElement e3, EnumCodeElement e4, EnumCodeElement e5) {
    return EnumCode._enum(e1, e2, e3, e4, e5);
  }

  public static EnumCode _enum(EnumCodeElement e1, EnumCodeElement e2, EnumCodeElement e3, EnumCodeElement e4, EnumCodeElement e5, EnumCodeElement e6) {
    return EnumCode._enum(e1, e2, e3, e4, e5, e6);
  }

  public static EnumCode _enum(EnumCodeElement e1, EnumCodeElement e2, EnumCodeElement e3, EnumCodeElement e4, EnumCodeElement e5, EnumCodeElement e6, EnumCodeElement e7) {
    return EnumCode._enum(e1, e2, e3, e4, e5, e6, e7);
  }

  public static EnumCode _enum(EnumCodeElement e1, EnumCodeElement e2, EnumCodeElement e3, EnumCodeElement e4, EnumCodeElement e5, EnumCodeElement e6, EnumCodeElement e7, EnumCodeElement e8) {
    return EnumCode._enum(e1, e2, e3, e4, e5, e6, e7, e8);
  }

  public static EnumCode _enum(EnumCodeElement e1, EnumCodeElement e2, EnumCodeElement e3, EnumCodeElement e4, EnumCodeElement e5, EnumCodeElement e6, EnumCodeElement e7, EnumCodeElement e8, EnumCodeElement e9) {
    return EnumCode._enum(e1, e2, e3, e4, e5, e6, e7, e8, e9);
  }

  public static EnumConstantCode enumConstant(EnumConstantCodeElement e1) {
    return EnumConstantCode.enumConstant(e1);
  }

  public static EnumConstantCode enumConstant(EnumConstantCodeElement e1, EnumConstantCodeElement e2) {
    return EnumConstantCode.enumConstant(e1, e2);
  }

  public static EnumConstantList enumConstants(Iterable<? extends EnumConstantCode> constants) {
    return EnumConstantList.enumConstants(constants);
  }

  public static ExtendsMany _extends(Iterable<? extends NamedClassOrParameterized> interfaces) {
    return ExtendsMany._extends(interfaces);
  }

  public static ExtendsMany _extends(NamedClassOrParameterized... interfaces) {
    return ExtendsMany._extends(interfaces);
  }

  public static ExtendsMany _extends(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2) {
    return ExtendsMany._extends(iface1, iface2);
  }

  public static ExtendsMany _extends(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3) {
    return ExtendsMany._extends(iface1, iface2, iface3);
  }

  public static ExtendsMany _extends(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3, NamedClassOrParameterized iface4) {
    return ExtendsMany._extends(iface1, iface2, iface3, iface4);
  }

  public static ExtendsOne _extends(NamedClassOrParameterized type) {
    return ExtendsOne._extends(type);
  }

  public static FieldCode field(FieldCodeElement e1, FieldCodeElement e2) {
    return FieldCode.field(e1, e2);
  }

  public static FieldCode field(FieldCodeElement e1, FieldCodeElement e2, FieldCodeElement e3) {
    return FieldCode.field(e1, e2, e3);
  }

  public static FieldCode field(FieldCodeElement e1, FieldCodeElement e2, FieldCodeElement e3, FieldCodeElement e4) {
    return FieldCode.field(e1, e2, e3, e4);
  }

  public static FieldCode field(FieldCodeElement e1, FieldCodeElement e2, FieldCodeElement e3, FieldCodeElement e4, FieldCodeElement e5) {
    return FieldCode.field(e1, e2, e3, e4, e5);
  }

  public static FieldCodeDeclarator init(Identifier name, VariableInitializer initializer) {
    return FieldCode.init(name, initializer);
  }

  public static FieldsShorthand fields(Iterable<FieldCode> fields) {
    return FieldsShorthand.fields(fields);
  }

  public static Implements _implements(Iterable<? extends NamedClassOrParameterized> interfaces) {
    return Implements._implements(interfaces);
  }

  public static Implements _implements(NamedClassOrParameterized iface1) {
    return Implements._implements(iface1);
  }

  public static Implements _implements(NamedClassOrParameterized... interfaces) {
    return Implements._implements(interfaces);
  }

  public static Implements _implements(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2) {
    return Implements._implements(iface1, iface2);
  }

  public static Implements _implements(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3) {
    return Implements._implements(iface1, iface2, iface3);
  }

  public static Implements _implements(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3, NamedClassOrParameterized iface4) {
    return Implements._implements(iface1, iface2, iface3, iface4);
  }

  public static Implements _implements(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3, NamedClassOrParameterized iface4, NamedClassOrParameterized iface5) {
    return Implements._implements(iface1, iface2, iface3, iface4, iface5);
  }

  public static Implements _implements(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3, NamedClassOrParameterized iface4, NamedClassOrParameterized iface5, NamedClassOrParameterized iface6) {
    return Implements._implements(iface1, iface2, iface3, iface4, iface5, iface6);
  }

  public static Implements _implements(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3, NamedClassOrParameterized iface4, NamedClassOrParameterized iface5, NamedClassOrParameterized iface6, NamedClassOrParameterized iface7) {
    return Implements._implements(iface1, iface2, iface3, iface4, iface5, iface6, iface7);
  }

  public static Implements _implements(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3, NamedClassOrParameterized iface4, NamedClassOrParameterized iface5, NamedClassOrParameterized iface6, NamedClassOrParameterized iface7, NamedClassOrParameterized iface8) {
    return Implements._implements(iface1, iface2, iface3, iface4, iface5, iface6, iface7, iface8);
  }

  public static Implements _implements(NamedClassOrParameterized iface1, NamedClassOrParameterized iface2, NamedClassOrParameterized iface3, NamedClassOrParameterized iface4, NamedClassOrParameterized iface5, NamedClassOrParameterized iface6, NamedClassOrParameterized iface7, NamedClassOrParameterized iface8, NamedClassOrParameterized iface9) {
    return Implements._implements(iface1, iface2, iface3, iface4, iface5, iface6, iface7, iface8, iface9);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1) {
    return InterfaceCode._interface(e1);
  }

  public static InterfaceCode _interface(InterfaceCodeElement... elements) {
    return InterfaceCode._interface(elements);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1, InterfaceCodeElement e2) {
    return InterfaceCode._interface(e1, e2);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1, InterfaceCodeElement e2, InterfaceCodeElement e3) {
    return InterfaceCode._interface(e1, e2, e3);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1, InterfaceCodeElement e2, InterfaceCodeElement e3, InterfaceCodeElement e4) {
    return InterfaceCode._interface(e1, e2, e3, e4);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1, InterfaceCodeElement e2, InterfaceCodeElement e3, InterfaceCodeElement e4, InterfaceCodeElement e5) {
    return InterfaceCode._interface(e1, e2, e3, e4, e5);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1, InterfaceCodeElement e2, InterfaceCodeElement e3, InterfaceCodeElement e4, InterfaceCodeElement e5, InterfaceCodeElement e6) {
    return InterfaceCode._interface(e1, e2, e3, e4, e5, e6);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1, InterfaceCodeElement e2, InterfaceCodeElement e3, InterfaceCodeElement e4, InterfaceCodeElement e5, InterfaceCodeElement e6, InterfaceCodeElement e7) {
    return InterfaceCode._interface(e1, e2, e3, e4, e5, e6, e7);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1, InterfaceCodeElement e2, InterfaceCodeElement e3, InterfaceCodeElement e4, InterfaceCodeElement e5, InterfaceCodeElement e6, InterfaceCodeElement e7, InterfaceCodeElement e8) {
    return InterfaceCode._interface(e1, e2, e3, e4, e5, e6, e7, e8);
  }

  public static InterfaceCode _interface(InterfaceCodeElement e1, InterfaceCodeElement e2, InterfaceCodeElement e3, InterfaceCodeElement e4, InterfaceCodeElement e5, InterfaceCodeElement e6, InterfaceCodeElement e7, InterfaceCodeElement e8, InterfaceCodeElement e9) {
    return InterfaceCode._interface(e1, e2, e3, e4, e5, e6, e7, e8, e9);
  }

  public static MethodCode method(MethodCodeElement e1) {
    return MethodCode.method(e1);
  }

  public static MethodCode method(MethodCodeElement... elements) {
    return MethodCode.method(elements);
  }

  public static MethodCode method(MethodCodeElement e1, MethodCodeElement e2) {
    return MethodCode.method(e1, e2);
  }

  public static MethodCode method(MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3) {
    return MethodCode.method(e1, e2, e3);
  }

  public static MethodCode method(MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3, MethodCodeElement e4) {
    return MethodCode.method(e1, e2, e3, e4);
  }

  public static MethodCode method(MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3, MethodCodeElement e4, MethodCodeElement e5) {
    return MethodCode.method(e1, e2, e3, e4, e5);
  }

  public static MethodCode method(MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3, MethodCodeElement e4, MethodCodeElement e5, MethodCodeElement e6) {
    return MethodCode.method(e1, e2, e3, e4, e5, e6);
  }

  public static MethodCode method(MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3, MethodCodeElement e4, MethodCodeElement e5, MethodCodeElement e6, MethodCodeElement e7) {
    return MethodCode.method(e1, e2, e3, e4, e5, e6, e7);
  }

  public static MethodCode method(MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3, MethodCodeElement e4, MethodCodeElement e5, MethodCodeElement e6, MethodCodeElement e7, MethodCodeElement e8) {
    return MethodCode.method(e1, e2, e3, e4, e5, e6, e7, e8);
  }

  public static MethodCode method(MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3, MethodCodeElement e4, MethodCodeElement e5, MethodCodeElement e6, MethodCodeElement e7, MethodCodeElement e8, MethodCodeElement e9) {
    return MethodCode.method(e1, e2, e3, e4, e5, e6, e7, e8, e9);
  }

  public static OverridingProcessingMethod overriding(ProcessingMethod method) {
    return MethodCode.overriding(method);
  }

  public static SignatureOfProcessingMethod signatureOf(ProcessingMethod method) {
    return MethodCode.signatureOf(method);
  }

  public static MethodsShorthand methods(Iterable<MethodCode> methods) {
    return MethodsShorthand.methods(methods);
  }

  public static AbstractModifier _abstract() {
    return Modifiers._abstract();
  }

  public static FinalModifier _final() {
    return Modifiers._final();
  }

  public static NativeModifier _native() {
    return Modifiers._native();
  }

  public static NonSealedModifier _nonSealed() {
    return Modifiers._nonSealed();
  }

  public static PrivateModifier _private() {
    return Modifiers._private();
  }

  public static ProtectedModifier _protected() {
    return Modifiers._protected();
  }

  public static PublicModifier _public() {
    return Modifiers._public();
  }

  public static SealedModifier _sealed() {
    return Modifiers._sealed();
  }

  public static StaticModifier _static() {
    return Modifiers._static();
  }

  public static StrictfpModifier _strictfp() {
    return Modifiers._strictfp();
  }

  public static SynchronizedModifier _synchronized() {
    return Modifiers._synchronized();
  }

  public static TransientModifier _transient() {
    return Modifiers._transient();
  }

  public static VolatileModifier _volatile() {
    return Modifiers._volatile();
  }

  public static Modifier of(javax.lang.model.element.Modifier javaxModifier) {
    return Modifiers.of(javaxModifier);
  }

  public static PackageName _package(PackageName parent, String child) {
    return PackageName._package(parent, child);
  }

  public static PackageName _package(String packageName) {
    return PackageName._package(packageName);
  }

  public static PackageName pn(PackageName parent, String child) {
    return PackageName.pn(parent, child);
  }

  public static PackageName pn(String packageName) {
    return PackageName.pn(packageName);
  }

  public static Builder builder() {
    return ParameterCode.builder();
  }

  public static ParameterCode param(ParameterTypeName type, Identifier name) {
    return ParameterCode.param(type, name);
  }

  public static ParamsShorthand params(Iterable<ParameterCode> parameters) {
    return ParameterCode.params(parameters);
  }

  public static SuperConstructorInvocation _super(Argument arg1) {
    return SuperConstructorInvocation._super(arg1);
  }

  public static SuperConstructorInvocation _super(Argument... arguments) {
    return SuperConstructorInvocation._super(arguments);
  }

  public static SuperConstructorInvocation _super(Argument arg1, Argument arg2) {
    return SuperConstructorInvocation._super(arg1, arg2);
  }

  public static SuperConstructorInvocation _super(Argument arg1, Argument arg2, Argument arg3) {
    return SuperConstructorInvocation._super(arg1, arg2, arg3);
  }

  public static ThisConstructorInvocation _this(Argument arg1) {
    return ThisConstructorInvocation._this(arg1);
  }

  public static ThisConstructorInvocation _this(Argument... arguments) {
    return ThisConstructorInvocation._this(arguments);
  }

  public static ThisConstructorInvocation _this(Argument arg1, Argument arg2) {
    return ThisConstructorInvocation._this(arg1, arg2);
  }

  public static ThisConstructorInvocation _this(Argument arg1, Argument arg2, Argument arg3) {
    return ThisConstructorInvocation._this(arg1, arg2, arg3);
  }

  public static ThrowsShorthand _throws(Class<? extends Throwable> throwable) {
    return ThrowsShorthand._throws(throwable);
  }

  public static ThrowsShorthand _throws(Iterable<? extends ThrowsElement> elements) {
    return ThrowsShorthand._throws(elements);
  }

  public static ThrowsShorthand _throws(ThrowsElement throwable) {
    return ThrowsShorthand._throws(throwable);
  }

  public static ThrowsShorthand _throws(ThrowsElement t1, ThrowsElement t2) {
    return ThrowsShorthand._throws(t1, t2);
  }

  public static ThrowsShorthand _throws(ThrowsElement t1, ThrowsElement t2, ThrowsElement t3) {
    return ThrowsShorthand._throws(t1, t2, t3);
  }

  public static TypesShorthand types(Iterable<? extends TypeCode> types) {
    return TypesShorthand.types(types);
  }

  public static Keyword _assert() {
    return Keywords._assert();
  }

  public static BreakKeyword _break() {
    return Keywords._break();
  }

  public static Keyword _case() {
    return Keywords._case();
  }

  public static Keyword _catch() {
    return Keywords._catch();
  }

  public static Keyword _class() {
    return Keywords._class();
  }

  public static ContinueKeyword _continue() {
    return Keywords._continue();
  }

  public static Keyword _do() {
    return Keywords._do();
  }

  public static ElseKeyword _else() {
    return Keywords._else();
  }

  public static Keyword _enum() {
    return Keywords._enum();
  }

  public static Keyword _extends() {
    return Keywords._extends();
  }

  public static FinallyKeyword _finally() {
    return Keywords._finally();
  }

  public static Keyword _for() {
    return Keywords._for();
  }

  public static Keyword _if() {
    return Keywords._if();
  }

  public static Keyword _implements() {
    return Keywords._implements();
  }

  public static Keyword _interface() {
    return Keywords._interface();
  }

  public static Keyword _new() {
    return Keywords._new();
  }

  public static ReturnKeyword _return() {
    return Keywords._return();
  }

  public static SuperKeyword _super() {
    return Keywords._super();
  }

  public static Keyword _switch() {
    return Keywords._switch();
  }

  public static ThisKeyword _this() {
    return Keywords._this();
  }

  public static Keyword _throw() {
    return Keywords._throw();
  }

  public static Keyword _throws() {
    return Keywords._throws();
  }

  public static Keyword _try() {
    return Keywords._try();
  }

  public static Keyword _while() {
    return Keywords._while();
  }

  public static CodeElement nextLine() {
    return NewLine.nextLine();
  }

  public static NewLine nl() {
    return NewLine.nl();
  }

  public static NoopCodeElement noop() {
    return NoopCodeElement.noop();
  }

  public static Arguments args() {
    return Arguments.args();
  }

  public static Arguments args(ArgumentsElement a1) {
    return Arguments.args(a1);
  }

  public static Arguments args(ArgumentsElement... args) {
    return Arguments.args(args);
  }

  public static Arguments args(ArgumentsElement a1, ArgumentsElement a2) {
    return Arguments.args(a1, a2);
  }

  public static Arguments args(ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3) {
    return Arguments.args(a1, a2, a3);
  }

  public static Arguments args(ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4) {
    return Arguments.args(a1, a2, a3, a4);
  }

  public static Arguments args(ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5) {
    return Arguments.args(a1, a2, a3, a4, a5);
  }

  public static Arguments args(ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6) {
    return Arguments.args(a1, a2, a3, a4, a5, a6);
  }

  public static Arguments args(ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7) {
    return Arguments.args(a1, a2, a3, a4, a5, a6, a7);
  }

  public static Arguments args(ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7, ArgumentsElement a8) {
    return Arguments.args(a1, a2, a3, a4, a5, a6, a7, a8);
  }

  public static Arguments args(ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7, ArgumentsElement a8, ArgumentsElement a9) {
    return Arguments.args(a1, a2, a3, a4, a5, a6, a7, a8, a9);
  }

  public static Arguments args(Iterable<? extends ArgumentsElement> args) {
    return Arguments.args(args);
  }

  public static AdditiveExpression add(AdditiveExpression lhs, MultiplicativeExpression rhs) {
    return Expressions.add(lhs, rhs);
  }

  public static AdditiveExpression subtract(AdditiveExpression lhs, MultiplicativeExpression rhs) {
    return Expressions.subtract(lhs, rhs);
  }

  public static AndExpression bitwiseAnd(AndExpression lhs, EqualityExpression rhs) {
    return Expressions.bitwiseAnd(lhs, rhs);
  }

  public static ArrayAccess aget(ArrayReferenceExpression ref, Expression e0) {
    return Expressions.aget(ref, e0);
  }

  public static ArrayAccess aget(ArrayReferenceExpression ref, Expression e0, Expression e1) {
    return Expressions.aget(ref, e0, e1);
  }

  public static ArrayAccess aget(ArrayReferenceExpression ref, Expression e0, Expression e1, Expression e2) {
    return Expressions.aget(ref, e0, e1, e2);
  }

  public static ArrayAccess aget(ArrayReferenceExpression ref, Expression e0, Expression e1, Expression e2, Expression e3) {
    return Expressions.aget(ref, e0, e1, e2, e3);
  }

  public static ArrayAccess aget(ArrayReferenceExpression ref, Iterable<? extends Expression> expressions) {
    return Expressions.aget(ref, expressions);
  }

  public static ArrayAccess arrayAccess(ArrayReferenceExpression ref, Expression e0) {
    return Expressions.arrayAccess(ref, e0);
  }

  public static ArrayAccess arrayAccess(ArrayReferenceExpression ref, Expression e0, Expression e1) {
    return Expressions.arrayAccess(ref, e0, e1);
  }

  public static ArrayAccess arrayAccess(ArrayReferenceExpression ref, Expression e0, Expression e1, Expression e2) {
    return Expressions.arrayAccess(ref, e0, e1, e2);
  }

  public static ArrayAccess arrayAccess(ArrayReferenceExpression ref, Expression e0, Expression e1, Expression e2, Expression e3) {
    return Expressions.arrayAccess(ref, e0, e1, e2, e3);
  }

  public static ArrayAccess arrayAccess(ArrayReferenceExpression ref, Iterable<? extends Expression> expressions) {
    return Expressions.arrayAccess(ref, expressions);
  }

  public static ArrayCreationExpression _new(NamedArray type, ArrayInitializer initializer) {
    return Expressions._new(type, initializer);
  }

  public static ArrayCreationExpression _new(NamedArray type, Expression dim0) {
    return Expressions._new(type, dim0);
  }

  public static ArrayCreationExpression _new(NamedArray type, Expression dim0, Expression dim1) {
    return Expressions._new(type, dim0, dim1);
  }

  public static ArrayCreationExpression _new(NamedArray type, Expression dim0, Expression dim1, Expression dim2) {
    return Expressions._new(type, dim0, dim1, dim2);
  }

  public static ArrayCreationExpression _new(NamedArray type, Expression dim0, Expression dim1, Expression dim2, Expression dim3) {
    return Expressions._new(type, dim0, dim1, dim2, dim3);
  }

  public static ArrayCreationExpression _new(NamedArray type, Iterable<? extends Expression> dims) {
    return Expressions._new(type, dims);
  }

  public static ArrayInitializer a() {
    return Expressions.a();
  }

  public static ArrayInitializer a(VariableInitializer v0) {
    return Expressions.a(v0);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1) {
    return Expressions.a(v0, v1);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1, VariableInitializer v2) {
    return Expressions.a(v0, v1, v2);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1, VariableInitializer v2, VariableInitializer v3) {
    return Expressions.a(v0, v1, v2, v3);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1, VariableInitializer v2, VariableInitializer v3, VariableInitializer v4) {
    return Expressions.a(v0, v1, v2, v3, v4);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1, VariableInitializer v2, VariableInitializer v3, VariableInitializer v4, VariableInitializer v5) {
    return Expressions.a(v0, v1, v2, v3, v4, v5);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1, VariableInitializer v2, VariableInitializer v3, VariableInitializer v4, VariableInitializer v5, VariableInitializer v6) {
    return Expressions.a(v0, v1, v2, v3, v4, v5, v6);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1, VariableInitializer v2, VariableInitializer v3, VariableInitializer v4, VariableInitializer v5, VariableInitializer v6, VariableInitializer v7) {
    return Expressions.a(v0, v1, v2, v3, v4, v5, v6, v7);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1, VariableInitializer v2, VariableInitializer v3, VariableInitializer v4, VariableInitializer v5, VariableInitializer v6, VariableInitializer v7, VariableInitializer v8) {
    return Expressions.a(v0, v1, v2, v3, v4, v5, v6, v7, v8);
  }

  public static ArrayInitializer a(VariableInitializer v0, VariableInitializer v1, VariableInitializer v2, VariableInitializer v3, VariableInitializer v4, VariableInitializer v5, VariableInitializer v6, VariableInitializer v7, VariableInitializer v8, VariableInitializer v9) {
    return Expressions.a(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9);
  }

  public static ArrayInitializer a(VariableInitializer... elements) {
    return Expressions.a(elements);
  }

  public static ArrayInitializer a(Iterable<? extends VariableInitializer> elements) {
    return Expressions.a(elements);
  }

  public static Assignment assign(LeftHandSide lhs, Expression rhs) {
    return Expressions.assign(lhs, rhs);
  }

  public static Assignment assign(AssignmentOperator operator, LeftHandSide lhs, Expression rhs) {
    return Expressions.assign(operator, lhs, rhs);
  }

  public static CastExpression cast(NamedReferenceType type, UnaryExpressionNotPlusMinus expression) {
    return Expressions.cast(type, expression);
  }

  public static CastExpression cast(NamedPrimitive type, UnaryExpression expression) {
    return Expressions.cast(type, expression);
  }

  public static ConditionalAndExpression and(ConditionalAndExpression lhs, InclusiveOrExpression rhs) {
    return Expressions.and(lhs, rhs);
  }

  public static ConditionalExpression ternary(ConditionalOrExpression condition, Expression trueExpression, ConditionalExpression falseExpression) {
    return Expressions.ternary(condition, trueExpression, falseExpression);
  }

  public static ConditionalExpression ternary(ConditionalOrExpression condition, Expression trueExpression, LambdaExpression falseExpression) {
    return Expressions.ternary(condition, trueExpression, falseExpression);
  }

  public static ConditionalOrExpression or(ConditionalOrExpression lhs, ConditionalAndExpression rhs) {
    return Expressions.or(lhs, rhs);
  }

  public static ExclusiveOrExpression bitwiseXor(ExclusiveOrExpression lhs, AndExpression rhs) {
    return Expressions.bitwiseXor(lhs, rhs);
  }

  public static EqualityExpression eq(EqualityExpression lhs, RelationalExpression rhs) {
    return Expressions.eq(lhs, rhs);
  }

  public static EqualityExpression ne(EqualityExpression lhs, RelationalExpression rhs) {
    return Expressions.ne(lhs, rhs);
  }

  public static EmptyExpression empty() {
    return Expressions.empty();
  }

  public static ExpressionName expressionName(Class<?> type, String id) {
    return Expressions.expressionName(type, id);
  }

  public static ExpressionName expressionName(NamedClass className, Identifier id) {
    return Expressions.expressionName(className, id);
  }

  public static ExpressionName expressionName(NamedClass className, String id) {
    return Expressions.expressionName(className, id);
  }

  public static ExpressionName expressionName(ExpressionName name, String id) {
    return Expressions.expressionName(name, id);
  }

  public static ExpressionName expressionName(ExpressionName name, Identifier id) {
    return Expressions.expressionName(name, id);
  }

  public static FieldAccess fieldAccess(FieldAccessReferenceExpression ref, String id) {
    return Expressions.fieldAccess(ref, id);
  }

  public static FieldAccess fieldAccess(FieldAccessReferenceExpression ref, Identifier id) {
    return Expressions.fieldAccess(ref, id);
  }

  public static Identifier id(String name) {
    return Expressions.id(name);
  }

  public static IdentifierBuilder idBuilder() {
    return Expressions.idBuilder();
  }

  public static InclusiveOrExpression bitwiseOr(InclusiveOrExpression lhs, ExclusiveOrExpression rhs) {
    return Expressions.bitwiseOr(lhs, rhs);
  }

  public static LambdaExpression lambda(LambdaBody body) {
    return Expressions.lambda(body);
  }

  public static LambdaExpression lambda(Identifier p1, LambdaBody body) {
    return Expressions.lambda(p1, body);
  }

  public static LambdaExpression lambda(ParameterCode p1, LambdaBody body) {
    return Expressions.lambda(p1, body);
  }

  public static LambdaExpression lambda(LambdaParameter p1, LambdaParameter p2, LambdaBody body) {
    return Expressions.lambda(p1, p2, body);
  }

  public static LambdaExpression lambda(LambdaParameter p1, LambdaParameter p2, LambdaParameter p3, LambdaBody body) {
    return Expressions.lambda(p1, p2, p3, body);
  }

  public static LambdaExpression lambda(LambdaParameter p1, LambdaParameter p2, LambdaParameter p3, LambdaParameter p4, LambdaBody body) {
    return Expressions.lambda(p1, p2, p3, p4, body);
  }

  public static LambdaExpression lambda(Iterable<? extends LambdaParameter> params, LambdaBody body) {
    return Expressions.lambda(params, body);
  }

  public static Literal _null() {
    return Expressions._null();
  }

  public static Literal _true() {
    return Expressions._true();
  }

  public static Literal _false() {
    return Expressions._false();
  }

  public static Literal l(boolean value) {
    return Expressions.l(value);
  }

  public static Literal l(char value) {
    return Expressions.l(value);
  }

  public static Literal l(double value) {
    return Expressions.l(value);
  }

  public static Literal l(int value) {
    return Expressions.l(value);
  }

  public static Literal l(float value) {
    return Expressions.l(value);
  }

  public static Literal l(long value) {
    return Expressions.l(value);
  }

  public static Literal l(Class<?> type) {
    return Expressions.l(type);
  }

  public static Literal l(NamedClass className) {
    return Expressions.l(className);
  }

  public static Literal l(String s) {
    return Expressions.l(s);
  }

  public static MethodReference ref(MethodReferenceReferenceExpression expression, String methodName) {
    return Expressions.ref(expression, methodName);
  }

  public static MethodReference ref(MethodReferenceReferenceExpression expression, TypeWitness witness, String methodName) {
    return Expressions.ref(expression, witness, methodName);
  }

  public static MethodReference ref(NamedReferenceType typeName, String methodName) {
    return Expressions.ref(typeName, methodName);
  }

  public static MethodReference ref(NamedReferenceType typeName, TypeWitness witness, String methodName) {
    return Expressions.ref(typeName, witness, methodName);
  }

  public static MethodInvocation invoke(Callee callee, String methodName) {
    return Expressions.invoke(callee, methodName);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1) {
    return Expressions.invoke(callee, methodName, a1);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1, ArgumentsElement a2) {
    return Expressions.invoke(callee, methodName, a1, a2);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3) {
    return Expressions.invoke(callee, methodName, a1, a2, a3);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4) {
    return Expressions.invoke(callee, methodName, a1, a2, a3, a4);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5) {
    return Expressions.invoke(callee, methodName, a1, a2, a3, a4, a5);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6) {
    return Expressions.invoke(callee, methodName, a1, a2, a3, a4, a5, a6);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7) {
    return Expressions.invoke(callee, methodName, a1, a2, a3, a4, a5, a6, a7);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7, ArgumentsElement a8) {
    return Expressions.invoke(callee, methodName, a1, a2, a3, a4, a5, a6, a7, a8);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7, ArgumentsElement a8, ArgumentsElement a9) {
    return Expressions.invoke(callee, methodName, a1, a2, a3, a4, a5, a6, a7, a8, a9);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, ArgumentsElement... args) {
    return Expressions.invoke(callee, methodName, args);
  }

  public static MethodInvocation invoke(Callee callee, String methodName, Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(callee, methodName, args);
  }

  public static MethodInvocation invoke(Callee callee, TypeWitness witness, String methodName) {
    return Expressions.invoke(callee, witness, methodName);
  }

  public static MethodInvocation invoke(Callee callee, TypeWitness witness, String methodName, ArgumentsElement a1) {
    return Expressions.invoke(callee, witness, methodName, a1);
  }

  public static MethodInvocation invoke(Callee callee, TypeWitness witness, String methodName, ArgumentsElement a1, ArgumentsElement a2) {
    return Expressions.invoke(callee, witness, methodName, a1, a2);
  }

  public static MethodInvocation invoke(Callee callee, TypeWitness witness, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3) {
    return Expressions.invoke(callee, witness, methodName, a1, a2, a3);
  }

  public static MethodInvocation invoke(Callee callee, TypeWitness witness, String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4) {
    return Expressions.invoke(callee, witness, methodName, a1, a2, a3, a4);
  }

  public static MethodInvocation invoke(Callee callee, TypeWitness witness, String methodName, Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(callee, witness, methodName, args);
  }

  public static Unqualified invoke(String methodName) {
    return Expressions.invoke(methodName);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1) {
    return Expressions.invoke(methodName, a1);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1, ArgumentsElement a2) {
    return Expressions.invoke(methodName, a1, a2);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3) {
    return Expressions.invoke(methodName, a1, a2, a3);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4) {
    return Expressions.invoke(methodName, a1, a2, a3, a4);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5) {
    return Expressions.invoke(methodName, a1, a2, a3, a4, a5);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6) {
    return Expressions.invoke(methodName, a1, a2, a3, a4, a5, a6);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7) {
    return Expressions.invoke(methodName, a1, a2, a3, a4, a5, a6, a7);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7, ArgumentsElement a8) {
    return Expressions.invoke(methodName, a1, a2, a3, a4, a5, a6, a7, a8);
  }

  public static Unqualified invoke(String methodName, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4, ArgumentsElement a5, ArgumentsElement a6, ArgumentsElement a7, ArgumentsElement a8, ArgumentsElement a9) {
    return Expressions.invoke(methodName, a1, a2, a3, a4, a5, a6, a7, a8, a9);
  }

  public static Unqualified invoke(String methodName, Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(methodName, args);
  }

  public static MultiplicativeExpression divide(MultiplicativeExpression lhs, UnaryExpression rhs) {
    return Expressions.divide(lhs, rhs);
  }

  public static MultiplicativeExpression multiply(MultiplicativeExpression lhs, UnaryExpression rhs) {
    return Expressions.multiply(lhs, rhs);
  }

  public static MultiplicativeExpression remainder(MultiplicativeExpression lhs, UnaryExpression rhs) {
    return Expressions.remainder(lhs, rhs);
  }

  public static ParenthesizedExpression parens(Expression expression) {
    return Expressions.parens(expression);
  }

  public static PostDecrementExpression postDec(PostfixExpression expression) {
    return Expressions.postDec(expression);
  }

  public static PostIncrementExpression postInc(PostfixExpression expression) {
    return Expressions.postInc(expression);
  }

  public static PreDecrementExpression preDec(UnaryExpression expression) {
    return Expressions.preDec(expression);
  }

  public static PreIncrementExpression preInc(UnaryExpression expression) {
    return Expressions.preInc(expression);
  }

  public static RelationalExpression lt(RelationalExpression lhs, ShiftExpression rhs) {
    return Expressions.lt(lhs, rhs);
  }

  public static RelationalExpression gt(RelationalExpression lhs, ShiftExpression rhs) {
    return Expressions.gt(lhs, rhs);
  }

  public static RelationalExpression le(RelationalExpression lhs, ShiftExpression rhs) {
    return Expressions.le(lhs, rhs);
  }

  public static RelationalExpression ge(RelationalExpression lhs, ShiftExpression rhs) {
    return Expressions.ge(lhs, rhs);
  }

  public static RelationalExpression instanceOf(RelationalExpression subject, NamedReferenceType test) {
    return Expressions.instanceOf(subject, test);
  }

  public static ShiftExpression leftShift(ShiftExpression lhs, AdditiveExpression rhs) {
    return Expressions.leftShift(lhs, rhs);
  }

  public static ShiftExpression rightShift(ShiftExpression lhs, AdditiveExpression rhs) {
    return Expressions.rightShift(lhs, rhs);
  }

  public static ShiftExpression unsignedRightShift(ShiftExpression lhs, AdditiveExpression rhs) {
    return Expressions.unsignedRightShift(lhs, rhs);
  }

  public static TypeWitness hint() {
    return Expressions.hint();
  }

  public static TypeWitness hint(NamedType t1) {
    return Expressions.hint(t1);
  }

  public static TypeWitness hint(NamedType t1, NamedType t2) {
    return Expressions.hint(t1, t2);
  }

  public static TypeWitness hint(NamedType t1, NamedType t2, NamedType t3) {
    return Expressions.hint(t1, t2, t3);
  }

  public static TypeWitness hint(Iterable<? extends NamedType> types) {
    return Expressions.hint(types);
  }

  public static TypeWitness witness() {
    return Expressions.witness();
  }

  public static TypeWitness witness(NamedType t1) {
    return Expressions.witness(t1);
  }

  public static TypeWitness witness(NamedType t1, NamedType t2) {
    return Expressions.witness(t1, t2);
  }

  public static TypeWitness witness(NamedType t1, NamedType t2, NamedType t3) {
    return Expressions.witness(t1, t2, t3);
  }

  public static TypeWitness witness(Iterable<? extends NamedType> types) {
    return Expressions.witness(types);
  }

  public static UnaryExpression unaryMinus(UnaryExpression expression) {
    return Expressions.unaryMinus(expression);
  }

  public static UnaryExpression unaryPlus(UnaryExpression expression) {
    return Expressions.unaryPlus(expression);
  }

  public static UnaryExpressionNotPlusMinus not(UnaryExpression expression) {
    return Expressions.not(expression);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1) {
    return MethodInvocationChain.chain(caller, e1);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement... elements) {
    return MethodInvocationChain.chain(caller, elements);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1, MethodInvocationChainElement e2) {
    return MethodInvocationChain.chain(caller, e1, e2);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1, MethodInvocationChainElement e2, MethodInvocationChainElement e3) {
    return MethodInvocationChain.chain(caller, e1, e2, e3);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1, MethodInvocationChainElement e2, MethodInvocationChainElement e3, MethodInvocationChainElement e4) {
    return MethodInvocationChain.chain(caller, e1, e2, e3, e4);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1, MethodInvocationChainElement e2, MethodInvocationChainElement e3, MethodInvocationChainElement e4, MethodInvocationChainElement e5) {
    return MethodInvocationChain.chain(caller, e1, e2, e3, e4, e5);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1, MethodInvocationChainElement e2, MethodInvocationChainElement e3, MethodInvocationChainElement e4, MethodInvocationChainElement e5, MethodInvocationChainElement e6) {
    return MethodInvocationChain.chain(caller, e1, e2, e3, e4, e5, e6);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1, MethodInvocationChainElement e2, MethodInvocationChainElement e3, MethodInvocationChainElement e4, MethodInvocationChainElement e5, MethodInvocationChainElement e6, MethodInvocationChainElement e7) {
    return MethodInvocationChain.chain(caller, e1, e2, e3, e4, e5, e6, e7);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1, MethodInvocationChainElement e2, MethodInvocationChainElement e3, MethodInvocationChainElement e4, MethodInvocationChainElement e5, MethodInvocationChainElement e6, MethodInvocationChainElement e7, MethodInvocationChainElement e8) {
    return MethodInvocationChain.chain(caller, e1, e2, e3, e4, e5, e6, e7, e8);
  }

  public static MethodInvocationChain chain(Callee caller, MethodInvocationChainElement e1, MethodInvocationChainElement e2, MethodInvocationChainElement e3, MethodInvocationChainElement e4, MethodInvocationChainElement e5, MethodInvocationChainElement e6, MethodInvocationChainElement e7, MethodInvocationChainElement e8, MethodInvocationChainElement e9) {
    return MethodInvocationChain.chain(caller, e1, e2, e3, e4, e5, e6, e7, e8, e9);
  }

  public static NewClass _new(NamedClass className) {
    return NewClass._new(className);
  }

  public static NewClass _new(NamedClass className, ArgumentsElement a1) {
    return NewClass._new(className, a1);
  }

  public static NewClass _new(NamedClass className, ArgumentsElement a1, ArgumentsElement a2) {
    return NewClass._new(className, a1, a2);
  }

  public static NewClass _new(NamedClass className, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3) {
    return NewClass._new(className, a1, a2, a3);
  }

  public static NewClass _new(NamedClass className, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4) {
    return NewClass._new(className, a1, a2, a3, a4);
  }

  public static NewClass _new(NamedClass className, Arguments args) {
    return NewClass._new(className, args);
  }

  public static NewClass _new(NamedClass className, Iterable<? extends ArgumentsElement> args) {
    return NewClass._new(className, args);
  }

  public static NewClass _new(NamedClass className, TypeWitness witness) {
    return NewClass._new(className, witness);
  }

  public static NewClass _new(NamedClass className, TypeWitness witness, ArgumentsElement a1) {
    return NewClass._new(className, witness, a1);
  }

  public static NewClass _new(NamedClass className, TypeWitness witness, ArgumentsElement a1, ArgumentsElement a2) {
    return NewClass._new(className, witness, a1, a2);
  }

  public static NewClass _new(NamedClass className, TypeWitness witness, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3) {
    return NewClass._new(className, witness, a1, a2, a3);
  }

  public static NewClass _new(NamedClass className, TypeWitness witness, ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4) {
    return NewClass._new(className, witness, a1, a2, a3, a4);
  }

  public static NewClass _new(NamedClass className, TypeWitness witness, Arguments args) {
    return NewClass._new(className, witness, args);
  }

  public static NewClass _new(NamedClass className, TypeWitness witness, Iterable<? extends ArgumentsElement> args) {
    return NewClass._new(className, witness, args);
  }

  public static JavaFile javaFile(PackageName packageName, TypeCode type) {
    return JavaFile.javaFile(packageName, type);
  }

  public static AssertStatement _assert(Expression expression) {
    return AssertStatement._assert(expression);
  }

  public static AssertStatement _assert(Expression expression, Expression detailMessage) {
    return AssertStatement._assert(expression, detailMessage);
  }

  public static Block block() {
    return Block.block();
  }

  public static Block block(BlockElement... elements) {
    return Block.block(elements);
  }

  public static Block block(Iterable<? extends BlockElement> elements) {
    return Block.block(elements);
  }

  public static BreakStatement _break(Identifier id) {
    return BreakStatement._break(id);
  }

  public static BreakStatement _break(String name) {
    return BreakStatement._break(name);
  }

  public static CaseSwitchElement _case(Identifier value, BlockStatement... statements) {
    return CaseSwitchElement._case(value, statements);
  }

  public static CaseSwitchElement _case(int value, BlockStatement... statements) {
    return CaseSwitchElement._case(value, statements);
  }

  public static CaseSwitchElement _case(String value, BlockStatement... statements) {
    return CaseSwitchElement._case(value, statements);
  }

  public static CatchElement _catch(Class<? extends Throwable> type1, Class<? extends Throwable> type2, Identifier id) {
    return CatchElement._catch(type1, type2, id);
  }

  public static CatchElement _catch(Class<? extends Throwable> type, Identifier id) {
    return CatchElement._catch(type, id);
  }

  public static ContinueStatement _continue(Identifier id) {
    return ContinueStatement._continue(id);
  }

  public static ContinueStatement _continue(String id) {
    return ContinueStatement._continue(id);
  }

  public static DefaultSwitchElement _default() {
    return DefaultSwitchElement._default();
  }

  public static DefaultSwitchElement _default(BlockStatement... statements) {
    return DefaultSwitchElement._default(statements);
  }

  public static DoStatement _do(Statement body, WhileElement condition) {
    return DoStatement._do(body, condition);
  }

  public static WhileElement _while(Expression condition) {
    return DoStatement._while(condition);
  }

  public static ForStatement _for(Class<?> type, Identifier id, Expression expression, Statement body) {
    return ForStatement._for(type, id, expression, body);
  }

  public static ForStatement _for(ForInitElement init, ForConditionElement condition, ForUpdateElement update, ForStatementElement e1) {
    return ForStatement._for(init, condition, update, e1);
  }

  public static ForStatement _for(ForInitElement init, ForConditionElement condition, ForUpdateElement update, ForStatementElement e1, ForStatementElement e2) {
    return ForStatement._for(init, condition, update, e1, e2);
  }

  public static ForStatement _for(NamedType typeName, Identifier id, Expression expression, Statement body) {
    return ForStatement._for(typeName, id, expression, body);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1) {
    return IfStatement._if(condition, e1);
  }

  public static IfStatement _if(Expression condition, IfStatementElement... elements) {
    return IfStatement._if(condition, elements);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1, IfStatementElement e2) {
    return IfStatement._if(condition, e1, e2);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1, IfStatementElement e2, IfStatementElement e3) {
    return IfStatement._if(condition, e1, e2, e3);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1, IfStatementElement e2, IfStatementElement e3, IfStatementElement e4) {
    return IfStatement._if(condition, e1, e2, e3, e4);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1, IfStatementElement e2, IfStatementElement e3, IfStatementElement e4, IfStatementElement e5) {
    return IfStatement._if(condition, e1, e2, e3, e4, e5);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1, IfStatementElement e2, IfStatementElement e3, IfStatementElement e4, IfStatementElement e5, IfStatementElement e6) {
    return IfStatement._if(condition, e1, e2, e3, e4, e5, e6);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1, IfStatementElement e2, IfStatementElement e3, IfStatementElement e4, IfStatementElement e5, IfStatementElement e6, IfStatementElement e7) {
    return IfStatement._if(condition, e1, e2, e3, e4, e5, e6, e7);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1, IfStatementElement e2, IfStatementElement e3, IfStatementElement e4, IfStatementElement e5, IfStatementElement e6, IfStatementElement e7, IfStatementElement e8) {
    return IfStatement._if(condition, e1, e2, e3, e4, e5, e6, e7, e8);
  }

  public static IfStatement _if(Expression condition, IfStatementElement e1, IfStatementElement e2, IfStatementElement e3, IfStatementElement e4, IfStatementElement e5, IfStatementElement e6, IfStatementElement e7, IfStatementElement e8, IfStatementElement e9) {
    return IfStatement._if(condition, e1, e2, e3, e4, e5, e6, e7, e8, e9);
  }

  public static ResourceElement resource(Class<?> type, Identifier id, VariableInitializer init) {
    return ResourceElement.resource(type, id, init);
  }

  public static ReturnStatement _return(String id) {
    return ReturnStatement._return(id);
  }

  public static ReturnStatement _return(Expression expression) {
    return ReturnStatement._return(expression);
  }

  public static SimpleLocalVariableDeclaration _var(Class<?> type, Identifier id) {
    return Statements._var(type, id);
  }

  public static WithInitLocalVariableDeclaration _var(Class<?> type, Identifier id, VariableInitializer init) {
    return Statements._var(type, id, init);
  }

  public static SimpleLocalVariableDeclaration _var(Class<?> type, String name) {
    return Statements._var(type, name);
  }

  public static WithInitLocalVariableDeclaration _var(Class<?> type, String name, VariableInitializer init) {
    return Statements._var(type, name, init);
  }

  public static SimpleLocalVariableDeclaration _var(NamedType typeName, Identifier id) {
    return Statements._var(typeName, id);
  }

  public static WithInitLocalVariableDeclaration _var(NamedType typeName, Identifier id, VariableInitializer init) {
    return Statements._var(typeName, id, init);
  }

  public static SimpleLocalVariableDeclaration _var(NamedType typeName, String name) {
    return Statements._var(typeName, name);
  }

  public static WithInitLocalVariableDeclaration _var(NamedType typeName, String name, VariableInitializer init) {
    return Statements._var(typeName, name, init);
  }

  public static StatementsShorthand statements(Iterable<? extends BlockStatement> statements) {
    return Statements.statements(statements);
  }

  public static String toString(Statement statement) {
    return Statements.toString(statement);
  }

  public static SwitchStatement _switch(Expression expression, SwitchElement... elements) {
    return SwitchStatement._switch(expression, elements);
  }

  public static SynchronizedStatement _synchronized(Expression lock, Block body) {
    return SynchronizedStatement._synchronized(lock, body);
  }

  public static ThrowStatement _throw(ThrowableExpression expression) {
    return ThrowStatement._throw(expression);
  }

  public static TryStatement _try(TryStatementElement e1, TryStatementElement e2) {
    return TryStatement._try(e1, e2);
  }

  public static TryStatement _try(TryStatementElement e1, TryStatementElement e2, TryStatementElement e3) {
    return TryStatement._try(e1, e2, e3);
  }

  public static TryStatement _try(TryStatementElement e1, TryStatementElement e2, TryStatementElement e3, TryStatementElement e4) {
    return TryStatement._try(e1, e2, e3, e4);
  }

  public static TryStatement _try(TryStatementElement e1, TryStatementElement e2, TryStatementElement e3, TryStatementElement e4, TryStatementElement e5) {
    return TryStatement._try(e1, e2, e3, e4, e5);
  }

  public static WhileStatement _while(Expression expression, Statement body) {
    return WhileStatement._while(expression, body);
  }

  public static NamedPrimitive _boolean() {
    return NamedTypes._boolean();
  }

  public static NamedPrimitive _byte() {
    return NamedTypes._byte();
  }

  public static NamedPrimitive _char() {
    return NamedTypes._char();
  }

  public static NamedPrimitive _double() {
    return NamedTypes._double();
  }

  public static NamedPrimitive _float() {
    return NamedTypes._float();
  }

  public static NamedPrimitive _int() {
    return NamedTypes._int();
  }

  public static NamedPrimitive _long() {
    return NamedTypes._long();
  }

  public static NamedPrimitive _short() {
    return NamedTypes._short();
  }

  public static NamedVoid _void() {
    return NamedTypes._void();
  }

  public static NamedArray a(NamedArray type) {
    return NamedTypes.a(type);
  }

  public static NamedArray a(NamedSingleDimensionArrayComponent type) {
    return NamedTypes.a(type);
  }

  public static NamedClass t(Class<?> type) {
    return NamedTypes.t(type);
  }

  public static NamedClassOrParameterized t(NamedClass raw, Iterable<? extends NamedType> arguments) {
    return NamedTypes.t(raw, arguments);
  }

  public static NamedClassOrParameterized t(NamedClass raw, NamedType... arguments) {
    return NamedTypes.t(raw, arguments);
  }

  public static NamedParameterized t(NamedClass raw, NamedType arg) {
    return NamedTypes.t(raw, arg);
  }

  public static NamedParameterized t(NamedClass raw, NamedType arg1, NamedType arg2) {
    return NamedTypes.t(raw, arg1, arg2);
  }

  public static NamedParameterized t(NamedClass raw, NamedType arg1, NamedType arg2, NamedType arg3) {
    return NamedTypes.t(raw, arg1, arg2, arg3);
  }

  public static NamedClass t(NamedClass className, String simpleName) {
    return NamedTypes.t(className, simpleName);
  }

  public static NamedClass t(PackageName packageName, String simpleName) {
    return NamedTypes.t(packageName, simpleName);
  }

  public static NamedTypeVariable tvar(String name) {
    return NamedTypes.tvar(name);
  }

  public static NamedWildcard wildcard() {
    return NamedTypes.wildcard();
  }

  public static NamedWildcard wildcardExtends(NamedReferenceType bound) {
    return NamedTypes.wildcardExtends(bound);
  }

  public static NamedWildcard wildcardSuper(NamedReferenceType bound) {
    return NamedTypes.wildcardSuper(bound);
  }

  public static NamedTypeParameter typeParam(String name) {
    return NamedTypeParameter.typeParam(name);
  }

  public static NamedTypeParameter typeParam(String name, NamedClass bound) {
    return NamedTypeParameter.typeParam(name, bound);
  }

  public static NamedTypeParameter typeParam(String name, NamedClass bound1, NamedClass bound2) {
    return NamedTypeParameter.typeParam(name, bound1, bound2);
  }

  public static NamedTypeParameter typeParam(String name, NamedClass bound1, NamedClass bound2, NamedClass bound3) {
    return NamedTypeParameter.typeParam(name, bound1, bound2, bound3);
  }

}