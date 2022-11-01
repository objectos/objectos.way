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
package objectox.code;

import java.util.Arrays;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import objectos.code.ClassName;
import objectos.code.TypeName;
import objectos.code.tmpl.InternalApi;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

public final class Pass0 implements InternalApi {

  public static final Ref REF = new Ref();

  int[] protoArray = new int[10];

  private int protoIndex;

  private int[] element = new int[10];

  private int elementIndex;

  Object[] objectArray = new Object[10];

  private int objectIndex;

  @Override
  public final void _extends(ClassName superclass) {
    Check.notNull(superclass, "superclass == null");

    addObject(ByteProto.EXTENDS, superclass);
  }

  @Override
  public final void _final() {
    addObject(ByteProto.MODIFIER, Modifier.FINAL);
  }

  @Override
  public final void annotation(int length) {
    element(ByteProto.ANNOTATION, length);
  }

  @Override
  public final void autoImports() {
    elemMark(protoIndex);

    protoAdd(ByteProto.AUTO_IMPORTS);
  }

  @Override
  public final void classDeclaration(int length) {
    element(ByteProto.CLASS_DECLARATION, length);
  }

  @Override
  public final void className(ClassName name) {
    Check.notNull(name, "name == null");

    addObject(ByteProto.CLASS_NAME, name);
  }

  public final void compilationUnitEnd() {
    element(ByteProto.COMPILATION_UNIT, elementIndex);

    if (elementIndex != 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    protoArray[1] = element[0];
  }

  public final void compilationUnitStart() {
    protoIndex = 0;

    elementIndex = 0;

    objectIndex = 0;

    protoAdd(ByteProto.JMP, ByteProto.NULL);
  }

  @Override
  public final void expressionName(int length) {
    element(ByteProto.EXPRESSION_NAME, length);
  }

  @Override
  public final void identifier(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    addObject(ByteProto.IDENTIFIER, name);
  }

  @Override
  public final void localVariable(int length) {
    element(ByteProto.LOCAL_VARIABLE, length);
  }

  @Override
  public final void methodDeclaration(int length) {
    element(ByteProto.METHOD_DECLARATION, length);
  }

  @Override
  public final void methodInvocation(int length) {
    element(ByteProto.METHOD_INVOCATION, length);
  }

  @Override
  public final void newLine() {
    element(ByteProto.NEW_LINE, 0);
  }

  @Override
  public final void packageDeclaration(int length) {
    element(ByteProto.PACKAGE_DECLARATION, length);
  }

  @Override
  public final void packageName(String packageName) {
    Check.argument(
      SourceVersion.isName(packageName),
      packageName, " is not a valid package name"
    );

    addObject(ByteProto.PACKAGE_NAME, packageName);
  }

  @Override
  public void stringLiteral(String value) {
    Check.notNull(value, "value == null");

    addObject(ByteProto.STRING_LITERAL, value);
  }

  @Override
  public final void typeName(TypeName typeName) {
    Check.notNull(typeName, "typeName == null");

    addObject(ByteProto.TYPE_NAME, typeName);
  }

  final int[] toCodes() { return Arrays.copyOf(protoArray, protoIndex); }

  final Object[] toObjects() {
    return Arrays.copyOf(objectArray, objectIndex);
  }

  private void addObject(int type, Object value) {
    elemMark(protoIndex);

    protoAdd(type, objectAdd(value));
  }

  private void element(int type, int length) {
    var start = elementIndex - length;

    var mark = protoIndex;

    protoAdd(type);

    for (int i = start; i < elementIndex; i++) {
      protoAdd(ByteProto.JMP, element[i]);
    }

    protoAdd(ByteProto.BREAK);

    elementIndex = start;

    elemMark(mark);
  }

  private void elemMark(int value) {
    element = IntArrays.growIfNecessary(element, elementIndex);

    element[elementIndex++] = value;
  }

  private int objectAdd(Object value) {
    int result = objectIndex;

    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    objectArray[objectIndex++] = value;

    return result;
  }

  private void protoAdd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex);

    protoArray[protoIndex++] = v0;
  }

  private void protoAdd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

}