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
import objectos.code.JavaModel.MarkerApi;
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
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class InternalApi extends InternalState implements MarkerApi {

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

  public final Implements _implements(ClassType... interfaces) {
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

    element(ByteProto.CLASS_INSTANCE_CREATION0);

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

  public final ReturnStatement _return(Expression expression) {
    markStart();

    expression.mark(this);

    element(ByteProto.RETURN_STATEMENT);

    return JavaModel.REF;
  }

  public final StaticModifier _static() {
    modifier(Keyword.STATIC);

    return JavaModel.REF;
  }

  public final ExplicitConstructorInvocation _super(Expression[] arguments) {
    markStart();

    for (var arg : arguments) {
      arg.mark(this);
    }

    element(ByteProto.SUPER_INVOCATION);

    return JavaModel.REF;
  }

  public final ThisKeyword _this() {
    elementAdd(itemIndex);

    protoAdd(ByteProto.THIS, ByteProto.OBJECT_END);

    return JavaModel.REF;
  }

  public final VoidInvocation _void() {
    elementAdd(itemIndex);

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

    elementAdd(itemIndex);

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

    rootadd(itemIndex);

    itemadd(ByteProto.CLASS_TYPE, object(packageName), names);

    for (var index = first; index >= last; index--) {
      var simpleName = objectArray[index];

      itemadd(object(simpleName));
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

    element(ByteProto.DIM);

    return JavaModel.REF;
  }

  public final JavaModel._Elem elem(int proto, int count) {
    int self = itemIndex;

    itemadd(proto, count);

    rootcopy(count);

    rootadd(self);

    return elemret();
  }

  public final void elemcnt(Object obj) {
    if (obj == JavaModel.ITEM || obj == JavaModel.ELEM) {
      codeArray[1]++; // total count
      codeArray[2]++; // root count
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  public final JavaModel._Elem elemret() {
    int totalCount = codeArray[1];

    itemadd(totalCount);

    rootcopy(totalCount);

    rootadd(codeArray[0]);

    return JavaModel.ELEM;
  }

  public final void elemstart(int value) {
    codeArray[0] = itemIndex;
    codeArray[1] = 0; // total count
    codeArray[2] = 0; // root count

    itemadd(value);
  }

  public final Ellipsis ellipsis() {
    elementAdd(itemIndex);

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
    rootadd(itemIndex);

    itemadd(v0);

    return itemret();
  }

  public final JavaModel._Item item(int v0, int v1) {
    rootadd(itemIndex);

    itemadd(v0, v1);

    return itemret();
  }

  public final JavaModel._Item item(int v0, int v1, int v2, int v3) {
    rootadd(itemIndex);

    itemadd(v0, v1, v2, v3);

    return itemret();
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
      identifier(identifier);
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
      identifier(identifier);
    }

    markStart();

    for (int i = 0; i < identifiers.length; i++) {
      markReference();
    }

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
    elementAdd(itemIndex);

    protoAdd(ByteProto.NEW_LINE, ByteProto.OBJECT_END);

    return JavaModel.REF;
  }

  public final int object(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  public final FormalParameter param(FormalParameterElement[] elements) {
    markStart();

    for (var element : elements) {
      element.mark(this);
    }

    element(ByteProto.FORMAL_PARAMETER);

    return JavaModel.REF;
  }

  public final StringLiteral s(String value) {
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

  public final ParameterizedClassType t(ClassType rawType, AnyType[] arguments) {
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

  public final LocalVariableDeclarationRef var(String name, Expression expression) {
    varName(name);

    markStart();

    markReference();

    expression.mark(this);

    element(ByteProto.LOCAL_VARIABLE);

    return JavaModel.REF;
  }

  final void accept(JavaTemplate template) {
    autoImports.clear();

    codeIndex = -1;

    itemIndex = objectIndex = rootIndex = 0;

    template.execute(this);

    int self = itemIndex;

    int count = rootIndex;

    itemadd(count);

    rootcopy(count);

    itemIndex = self;
  }

  final void pass0(JavaTemplate template) {
    pass0Start();

    template.execute(this);

    pass0End();
  }

  private void identifier(String name) {
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

  private void itemadd(int v0) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 0);

    itemArray[itemIndex++] = v0;
  }

  private void itemadd(int v0, int v1) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 1);

    itemArray[itemIndex++] = v0;
    itemArray[itemIndex++] = v1;
  }

  private void itemadd(int v0, int v1, int v2) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 2);

    itemArray[itemIndex++] = v0;
    itemArray[itemIndex++] = v1;
    itemArray[itemIndex++] = v2;
  }

  private void itemadd(int v0, int v1, int v2, int v3) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 3);

    itemArray[itemIndex++] = v0;
    itemArray[itemIndex++] = v1;
    itemArray[itemIndex++] = v2;
    itemArray[itemIndex++] = v3;
  }

  private JavaModel._Item itemret() {
    return JavaModel.ITEM;
  }

  private void modifier(Keyword value) {
    elementAdd(itemIndex);

    protoAdd(ByteProto.MODIFIER, value.ordinal(), ByteProto.OBJECT_END);
  }

  private void operator(Operator2 operator) {
    elementAdd(itemIndex);

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

    itemArray[1] = codeArray[0];
  }

  private void pass0Start() {
    autoImports.clear();

    codeIndex = 0;

    objectIndex = 0;

    stackIndex = -1;

    rootIndex = -1;

    itemIndex = 0;

    protoAdd(ByteProto.JMP, ByteProto.NULL, ByteProto.EOF);
  }

  private void primitive(Keyword value) {
    elementAdd(itemIndex);

    protoAdd(ByteProto.PRIMITIVE_TYPE, value.ordinal(), ByteProto.OBJECT_END);
  }

  private void rootadd(int value) {
    rootArray = IntArrays.growIfNecessary(rootArray, rootIndex + 0);

    rootArray[rootIndex++] = value;
  }

  private void rootcopy(int count) {
    if (count > 0) {
      rootIndex -= count;
      int itemMax = itemIndex + count - 1;
      itemArray = IntArrays.growIfNecessary(itemArray, itemMax);
      System.arraycopy(rootArray, rootIndex, itemArray, itemIndex, count);
      itemIndex += count;
    }
  }

  private void varName(String name) {
    JavaModel.checkVarName(name);

    object(ByteProto.VAR_NAME, name);
  }

}