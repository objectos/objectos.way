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

import java.util.Arrays;
import java.util.Objects;
import javax.lang.model.SourceVersion;
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
import objectos.code.JavaModel.ExpressionElement;
import objectos.code.JavaModel.ExpressionName;
import objectos.code.JavaModel.ExtendsMany;
import objectos.code.JavaModel.ExtendsSingle;
import objectos.code.JavaModel.FieldAccessExpression;
import objectos.code.JavaModel.FieldDeclaration;
import objectos.code.JavaModel.FieldDeclarationElement;
import objectos.code.JavaModel.FinalModifier;
import objectos.code.JavaModel.FormalParameter;
import objectos.code.JavaModel.Identifier;
import objectos.code.JavaModel.ImplementsKeyword;
import objectos.code.JavaModel.Include;
import objectos.code.JavaModel.IntegerLiteral;
import objectos.code.JavaModel.InterfaceDeclaration;
import objectos.code.JavaModel.InterfaceDeclarationElement;
import objectos.code.JavaModel.LeftHandSide;
import objectos.code.JavaModel.LocalVariableDeclarationStatement;
import objectos.code.JavaModel.Markable;
import objectos.code.JavaModel.MarkerApi;
import objectos.code.JavaModel.MethodDeclaration;
import objectos.code.JavaModel.MethodDeclarationElement;
import objectos.code.JavaModel.MethodInvocationElement;
import objectos.code.JavaModel.NewLine;
import objectos.code.JavaModel.ParameterElement;
import objectos.code.JavaModel.ParameterizedType;
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
import objectos.code.JavaTemplate.IncludeTarget;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class InternalApi extends InternalState implements MarkerApi {

  private static final int LOCAL = -1;
  private static final int EXT = -2;
  private static final int LAMBDA = -3;

  int[][] levelArray = new int[2][];

  int[] levelIndex = new int[2];

  int level;

  boolean v2;

  public final AbstractModifier _abstract() {
    modifier(Keyword.ABSTRACT);

    return JavaModel.REF;
  }

  public final PrimitiveType _boolean() {
    primitive(Keyword.BOOLEAN);

    return JavaModel.REF;
  }

  public final ClassDeclaration _class(ClassDeclarationElement[] elements) {
    markStart();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.CLASS_DECLARATION);

    return JavaModel.REF;
  }

  public final PrimitiveType _double() {
    primitive(Keyword.DOUBLE);

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

  public final ExtendsSingle _extends(ClassType superclass) {
    markStart();

    superclass.mark(this);

    element(ByteProto.EXTENDS_SINGLE);

    return JavaModel.REF;
  }

  public final ExtendsMany _extends(ClassType[] interfaces) {
    markStart();

    for (var iface : interfaces) {
      iface.mark(this);
    }

    element(ByteProto.EXTENDS_MANY);

    return JavaModel.REF;
  }

  public final FinalModifier _final() {
    modifier(Keyword.FINAL);

    return JavaModel.REF;
  }

  public final ImplementsKeyword _implements(ClassType... interfaces) {
    markStart();

    for (var iface : interfaces) {
      iface.mark(this);
    }

    element(ByteProto.IMPLEMENTS);

    return JavaModel.REF;
  }

  public final PrimitiveType _int() {
    primitive(Keyword.INT);

    return JavaModel.REF;
  }

  public final InterfaceDeclaration _interface(InterfaceDeclarationElement[] elements) {
    markStart();

    for (var element : elements) {
      element.mark(this);
    }

    element(ByteProto.INTERFACE_DECLARATION);

    return JavaModel.REF;
  }

  public final ClassInstanceCreationExpression _new(
      ClassType type, Expression[] arguments) {
    markStart();

    type.mark(this);

    for (var arg : arguments) {
      arg.mark(this);
    }

    element(ByteProto.CLASS_INSTANCE_CREATION);

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
    modifier(Keyword.PRIVATE);

    return JavaModel.REF;
  }

  public final ProtectedModifier _protected() {
    modifier(Keyword.PROTECTED);

    return JavaModel.REF;
  }

  public final PublicModifier _public() {
    modifier(Keyword.PUBLIC);

    return JavaModel.REF;
  }

  public final ReturnKeyword _return(Expression expression) {
    markStart();

    expression.mark(this);

    element(ByteProto.RETURN_STATEMENT);

    return JavaModel.REF;
  }

  public final StaticModifier _static() {
    modifier(Keyword.STATIC);

    return JavaModel.REF;
  }

  public final ExplicitConstructorInvocation _super(ExpressionElement[] arguments) {
    markStart();

    for (var arg : arguments) {
      arg.mark(this);
    }

    element(ByteProto.SUPER_INVOCATION);

    return JavaModel.REF;
  }

  public final ThisKeyword _this() {
    elementAdd(protoIndex);

    protoAdd(ByteProto.THIS, ByteProto.OBJECT_END);

    return JavaModel.REF;
  }

  public final VoidKeyword _void() {
    elementAdd(protoIndex);

    protoAdd(ByteProto.NO_TYPE, ByteProto.OBJECT_END);

    return JavaModel.REF;
  }

  public final ArrayInitializer a(ArrayInitializerElement[] elements) {
    markStart();

    for (var element : elements) { // implicit null-check
      element.mark(this);
    }

    element(ByteProto.ARRAY_INITIALIZER);

    return JavaModel.REF;
  }

  public final ArrayAccessExpression aget(ExpressionName reference, Expression[] expressions) {
    markStart();

    reference.mark(this);

    for (var expression : expressions) {
      expression.mark(this);
    }

    element(ByteProto.ARRAY_ACCESS_EXPRESSION);

    return JavaModel.REF;
  }

  public final AnnotationInvocation annotation(ClassType annotationType) {
    markStart();

    annotationType.mark(this);

    element(ByteProto.ANNOTATION);

    return JavaModel.REF;
  }

  public final AnnotationInvocation annotation(
      ClassType annotationType, AnnotationElementValue value) {
    markStart();

    annotationType.mark(this);

    value.mark(this);

    element(ByteProto.ANNOTATION);

    return JavaModel.REF;
  }

  public final AssignmentExpression assign(LeftHandSide leftHandSide, Expression expression) {
    return assign(Operator2.ASSIGNMENT, leftHandSide, expression);
  }

  public final AssignmentExpression assign(
      Operator2 operator, LeftHandSide leftHandSide, Expression expression) {
    Objects.requireNonNull(operator, "operator == null");

    operator(operator);

    markStart();

    markReference();

    leftHandSide.mark(this);

    expression.mark(this);

    element(ByteProto.ASSIGNMENT_EXPRESSION);

    return JavaModel.REF;
  }

  public final void autoImports() {
    autoImports.enable();

    elementAdd(protoIndex);

    protoAdd(ByteProto.AUTO_IMPORTS, ByteProto.OBJECT_END);
  }

  public final Block block(BlockElement[] elements) {
    markStart();

    for (var element : elements) {
      element.mark(this);
    }

    element(ByteProto.BLOCK);

    return JavaModel.REF;
  }

  public final ChainedMethodInvocation chain(
      ChainedMethodInvocationHead first, ChainedMethodInvocationElement[] more) {
    markStart();

    first.mark(this);

    for (var element : more) {
      element.mark(this);
    }

    element(ByteProto.CHAINED_METHOD_INVOCATION);

    return JavaModel.REF;
  }

  public final ClassType classType(Class<?> type) {
    var last = objectIndex;

    while (true) {
      var simpleName = type.getSimpleName(); // implicit null-check

      object(simpleName);

      var outer = type.getEnclosingClass();

      if (outer == null) {
        break;
      } else {
        type = outer;
      }
    }

    var first = objectIndex - 1;

    var names = objectIndex - last;

    var packageName = type.getPackageName();

    leveladd(LOCAL, protoIndex);

    protoadd(ByteProto.CLASS_TYPE, object(packageName), names);

    for (var index = first; index >= last; index--) {
      var simpleName = objectArray[index];

      protoadd(object(simpleName));
    }

    return itemret();
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

    element(ByteProto.ARRAY_DIMENSION);

    return JavaModel.REF;
  }

  public final JavaModel._Elem elem(int proto) {
    elempre();
    elemcnt(proto);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1) {
    elempre();
    elempre(e1);
    elemcnt(proto);
    elemcntx(e1);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2) {
    elempre();
    elempre(e1);
    elempre(e2);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2, Object e3) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    elemcntx(e3);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2, Object e3, Object e4) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    elemcntx(e3);
    elemcntx(e4);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    elemcntx(e3);
    elemcntx(e4);
    elemcntx(e5);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    elemcntx(e3);
    elemcntx(e4);
    elemcntx(e5);
    elemcntx(e6);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elempre(e7);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    elemcntx(e3);
    elemcntx(e4);
    elemcntx(e5);
    elemcntx(e6);
    elemcntx(e7);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elempre(e7);
    elempre(e8);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    elemcntx(e3);
    elemcntx(e4);
    elemcntx(e5);
    elemcntx(e6);
    elemcntx(e7);
    elemcntx(e8);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8, Object e9) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elempre(e7);
    elempre(e8);
    elempre(e9);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    elemcntx(e3);
    elemcntx(e4);
    elemcntx(e5);
    elemcntx(e6);
    elemcntx(e7);
    elemcntx(e8);
    elemcntx(e9);
    return elemret();
  }

  public final JavaModel._Elem elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8, Object e9, Object e10) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elempre(e7);
    elempre(e8);
    elempre(e9);
    elempre(e10);
    elemcnt(proto);
    elemcntx(e1);
    elemcntx(e2);
    elemcntx(e3);
    elemcntx(e4);
    elemcntx(e5);
    elemcntx(e6);
    elemcntx(e7);
    elemcntx(e8);
    elemcntx(e9);
    elemcntx(e10);
    return elemret();
  }

  public final JavaModel._Elem elemmany(int proto, Object first, Object[] elements) {
    elempre();

    elempre(first);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      elempre(element);
    }

    elemcnt(proto);

    elemcntx(first);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemcntx(element);
    }

    return elemret();
  }

  public final JavaModel._Elem elemmany(int proto, Object[] elements) {
    elempre();

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      elempre(element);
    }

    elemcnt(proto);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemcntx(element);
    }

    return elemret();
  }

  public final Ellipsis ellipsis() {
    elementAdd(protoIndex);

    protoAdd(ByteProto.ELLIPSIS, ByteProto.OBJECT_END);

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

  public final IntegerLiteral i(int value) {
    var s = Integer.toString(value);

    object(ByteProto.PRIMITIVE_LITERAL, s);

    return JavaModel.REF;
  }

  public final Identifier id(String name) {
    identifier$(name);

    return JavaModel.REF;
  }

  public final void identifierext(String value) {
    leveladd(EXT, protoIndex);

    protoadd(ByteProto.IDENTIFIER, object(value));
  }

  public final Include include(IncludeTarget target) {
    lambdaStart();

    target.execute();

    lambdaEnd();

    return JavaModel.INCLUDE;
  }

  public final QualifiedMethodInvocation invoke(
      Markable subject, String methodName, MethodInvocationElement[] elements) {
    invokeMethodName(methodName);

    markStart();

    subject.mark(this);

    markReference();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.METHOD_INVOCATION_QUALIFIED);

    return JavaModel.REF;
  }

  public final UnqualifiedMethodInvocation invoke(
      String methodName, MethodInvocationElement[] elements) {
    invokeMethodName(methodName);

    markStart();

    markReference();

    for (var element : elements) { // implicit elements null check
      element.mark(this);
    }

    element(ByteProto.METHOD_INVOCATION);

    return JavaModel.REF;
  }

  public final JavaModel._Item item(int v0) {
    leveladd(LOCAL, protoIndex);

    protoadd(v0);

    return itemret();
  }

  public final JavaModel._Item item(int v0, int v1) {
    leveladd(LOCAL, protoIndex);

    protoadd(v0, v1);

    return itemret();
  }

  public final JavaModel._Item item(int v0, int v1, int v2, int v3) {
    leveladd(LOCAL, protoIndex);

    protoadd(v0, v1, v2, v3);

    return itemret();
  }

  public final JavaModel._Item item(int v0, int v1, int v2, int v3, int v4) {
    leveladd(LOCAL, protoIndex);

    protoadd(v0, v1, v2, v3, v4);

    return itemret();
  }

  public final void lambdaend() {
    level--;
  }

  public final void lambdastart() {
    leveladd(LAMBDA, level + 1);
    level++;
    levelIndex = IntArrays.growIfNecessary(levelIndex, level);
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

  public final ExpressionName n(ClassType type, String[] identifiers) {
    Objects.requireNonNull(type, "type == null");

    for (var identifier : identifiers) {
      identifier$(identifier);
    }

    markStart();

    markReference();

    for (int i = 0; i < identifiers.length; i++) {
      markReference();
    }

    element(ByteProto.EXPRESSION_NAME);

    return JavaModel.REF;
  }

  public final ExpressionName n(String[] identifiers) {
    for (var identifier : identifiers) {
      identifier$(identifier);
    }

    markStart();

    for (int i = 0; i < identifiers.length; i++) {
      markReference();
    }

    element(ByteProto.EXPRESSION_NAME);

    return JavaModel.REF;
  }

  public final FieldAccessExpression n(ThisKeyword keyword, String identifier) {
    identifier$(identifier);

    markStart();

    markReference();

    markReference();

    element(ByteProto.FIELD_ACCESS_EXPRESSION0);

    return JavaModel.REF;
  }

  public final NewLine nl() {
    elementAdd(protoIndex);

    protoAdd(ByteProto.NEW_LINE, ByteProto.OBJECT_END);

    return JavaModel.REF;
  }

  public final int object(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  public final FormalParameter param(ParameterElement[] elements) {
    markStart();

    for (var element : elements) {
      element.mark(this);
    }

    element(ByteProto.FORMAL_PARAMETER);

    return JavaModel.REF;
  }

  public StringLiteral s(String value) {
    Check.notNull(value, "value == null");

    object(ByteProto.STRING_LITERAL, value);

    return JavaModel.REF;
  }

  public final ArrayType t(ArrayTypeComponent type, ArrayTypeElement[] elements) {
    markStart();

    type.mark(this);

    for (var element : elements) {
      element.mark(this);
    }

    element(ByteProto.ARRAY_TYPE);

    return JavaModel.REF;
  }

  public final ClassType t(Class<?> type) {
    var last = objectIndex;

    while (true) {
      var simpleName = type.getSimpleName();

      objectAdd(simpleName);

      var outer = type.getEnclosingClass(); // implicit null-check

      if (outer == null) {
        break;
      } else {
        type = outer;
      }
    }

    var index = objectIndex - 1;

    var packageName = type.getPackageName();

    var outer = t(packageName, (String) objectArray[index]);

    for (index = index - 1; index >= last; index--) {
      outer = t(outer, (String) objectArray[index]);
    }

    return outer;
  }

  public final ParameterizedType t(ClassType rawType, AnyType[] arguments) {
    markStart();

    rawType.mark(this);

    for (var arg : arguments) {
      arg.mark(this);
    }

    element(ByteProto.PARAMETERIZED_TYPE);

    return JavaModel.REF;
  }

  public final ClassType t(ClassType outer, String simpleName) {
    JavaModel.checkSimpleName(simpleName.toString()); // implicit null check

    object(ByteProto.SIMPLE_NAME, simpleName);

    markStart();

    outer.mark(this);

    markReference();

    element(ByteProto.CLASS_TYPE);

    return JavaModel.REF;
  }

  public final ClassType t(String packageName, String simpleName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName.toString()); // implicit null check

    object(ByteProto.PACKAGE_NAME, packageName);

    object(ByteProto.SIMPLE_NAME, simpleName);

    markStart();

    markReference();

    markReference();

    element(ByteProto.CLASS_TYPE);

    return JavaModel.REF;
  }

  public final ClassType t(String packageName, String[] simpleNames) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check

    object(ByteProto.PACKAGE_NAME, packageName);

    for (var simpleName : simpleNames) {
      JavaModel.checkSimpleName(simpleName.toString()); // implicit null check

      object(ByteProto.SIMPLE_NAME, simpleName);
    }

    markStart();

    markReference();

    markIncrement(simpleNames.length);

    element(ByteProto.CLASS_TYPE);

    return JavaModel.REF;
  }

  public final TypeParameter tparam(String name, TypeParameterBound[] bounds) {
    Objects.requireNonNull(name, "name == null");

    object(ByteProto.INVOKE_METHOD_NAME, name);

    markStart();

    markReference();

    for (var bound : bounds) { // implicit null check
      bound.mark(this);
    }

    element(ByteProto.TYPE_PARAMETER);

    return JavaModel.REF;
  }

  public final TypeVariable tvar(String name) {
    Objects.requireNonNull(name, "name == null");

    object(ByteProto.TYPE_VARIABLE, name);

    return JavaModel.REF;
  }

  public final LocalVariableDeclarationStatement var(String name, Expression expression) {
    varName(name);

    markStart();

    markReference();

    expression.mark(this);

    element(ByteProto.LOCAL_VARIABLE);

    return JavaModel.REF;
  }

  final void accept(JavaTemplate template) {
    autoImports.clear();

    stackIndex = -1;

    codeIndex = protoIndex = level = localIndex = objectIndex = 0;

    Arrays.fill(levelIndex, 0);

    template.execute(this);

    assert level == 0;

    int self = protoIndex;

    int[] array = levelArray[level];

    int length = levelIndex[level];

    int count = length / 2;

    protoadd(count);

    for (int i = 0; i < length;) {
      int code = array[i++];

      if (code == LOCAL) {
        protoadd(array[i++]);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: code=" + code
        );
      }
    }

    protoIndex = self;
  }

  final void pass0(JavaTemplate template) {
    pass0Start();

    template.execute(this);

    pass0End();
  }

  private void elemcnt(int value) {
    int itemCount = stackpop(),
        levelStart = levelIndex[level] - (itemCount * 2),
        localIndex = levelStart,
        extIndex = levelStart,
        includeIndex = levelStart;

    stackpush(
      /*4*/protoIndex,
      /*3*/levelStart,
      /*2*/includeIndex,
      /*1*/extIndex,
      /*0*/localIndex
    );

    protoadd(value, 0);
  }

  private void elemcntx(Object obj) {
    if (obj == JavaModel.ITEM || obj == JavaModel.ELEM) {
      int localIndex = stackpeek(0);

      localIndex = levelsearch(localIndex, LOCAL);

      localIndex++;

      int value = levelget(localIndex);

      protoadd(value);

      stackset(0, localIndex);
    } else if (obj == JavaModel.EXT) {
      int extIndex = stackpeek(1);

      extIndex = levelsearch(extIndex, EXT);

      extIndex++;

      int value = levelget(extIndex);

      protoadd(value);

      stackset(1, extIndex);
    } else if (obj == JavaModel.INCLUDE) {
      int codeIndex = stackpeek(2);

      codeIndex = levelsearch(codeIndex, LAMBDA);

      codeIndex++;

      int level = levelget(codeIndex);

      elemcntx0lambda(level);

      stackset(2, codeIndex);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: obj=" + obj);
    }
  }

  private void elemcntx0lambda(int level) {
    int[] array = levelArray[level];

    int length = levelIndex[level];

    for (int i = 0; i < length;) {
      int code = array[i++];
      int value = array[i++];

      if (code == LOCAL) {
        protoadd(value);
      } else if (code == LAMBDA) {
        elemcntx0lambda(value);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: code=" + code);
      }
    }

    levelIndex[level] = 0;
  }

  private void elempre() {
    stackpush(0);
  }

  private void elempre(Object obj) {
    stackinc(0);
  }

  private JavaModel._Elem elemret() {
    /*localIndex = */stackpop();
    /*extIndex = */stackpop();
    /*includeIndex = */stackpop();

    int levelStart = stackpop(),
        self = stackpop();

    levelIndex[level] = levelStart;

    int itemStart = self + 2;

    int count = protoIndex - itemStart;

    protoArray[self + 1] = count;

    leveladd(LOCAL, self);

    return JavaModel.ELEM;
  }

  private void identifier$(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    object(ByteProto.IDENTIFIER, name);
  }

  private void invokeMethodName(String methodName) {
    JavaModel.checkMethodName(methodName);

    object(ByteProto.INVOKE_METHOD_NAME, methodName);
  }

  private JavaModel._Item itemret() {
    return JavaModel.ITEM;
  }

  private void leveladd(int v0, int v1) {
    levelArray = ObjectArrays.growIfNecessary(levelArray, level);

    if (levelArray[level] == null) {
      levelArray[level] = new int[64];
    }

    levelArray[level] = IntArrays.growIfNecessary(levelArray[level], levelIndex[level] + 1);
    levelArray[level][levelIndex[level]++] = v0;
    levelArray[level][levelIndex[level]++] = v1;
  }

  private int levelget(int index) { return levelArray[level][index]; }

  private int levelsearch(int index, int condition) {
    int[] array = levelArray[level];
    int length = levelIndex[level];

    for (int i = index; i < length; i++) {
      int value = array[i];

      if (value == condition) {
        // assuming codeArray was properly assembled
        // there will always be a i+1 index
        return i;
      }
    }

    throw new UnsupportedOperationException(
      "Implement me :: could not find code (index=%d; condition=%d)"
          .formatted(index, condition)
    );
  }

  private void modifier(Keyword value) {
    elementAdd(protoIndex);

    protoAdd(ByteProto.MODIFIER, value.ordinal(), ByteProto.OBJECT_END);
  }

  private void operator(Operator2 operator) {
    elementAdd(protoIndex);

    protoAdd(ByteProto.ASSIGNMENT_OPERATOR, operator.ordinal(), ByteProto.OBJECT_END);
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

    localIndex = -1;

    protoIndex = 0;

    protoAdd(ByteProto.JMP, ByteProto.NULL, ByteProto.EOF);
  }

  private void primitive(Keyword value) {
    elementAdd(protoIndex);

    protoAdd(ByteProto.PRIMITIVE_TYPE, value.ordinal(), ByteProto.OBJECT_END);
  }

  private void protoadd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 0);
    protoArray[protoIndex++] = v0;
  }

  private void protoadd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  private void protoadd(int v0, int v1, int v2) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 2);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
  }

  private void protoadd(int v0, int v1, int v2, int v3) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 3);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
  }

  private void protoadd(int v0, int v1, int v2, int v3, int v4) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 4);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
    protoArray[protoIndex++] = v4;
  }

  private void stackinc(int offset) { stackArray[stackIndex - offset]++; }

  private int stackpeek(int offset) { return stackArray[stackIndex - offset]; }

  private int stackpop() { return stackArray[stackIndex--]; }

  private void stackpush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);

    stackArray[++stackIndex] = v0;
  }

  private void stackpush(int v0, int v1, int v2, int v3, int v4) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 5);

    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
    stackArray[++stackIndex] = v2;
    stackArray[++stackIndex] = v3;
    stackArray[++stackIndex] = v4;
  }

  private void stackset(int offset, int value) { stackArray[stackIndex - offset] = value; }

  private void varName(String name) {
    JavaModel.checkVarName(name);

    object(ByteProto.VAR_NAME, name);
  }

}