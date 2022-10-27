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
import objectos.code.tmpl.InternalApi;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

public final class Pass0 implements InternalApi {

  private static final int NULL = Integer.MIN_VALUE;

  static final int EOF = -1;
  static final int JMP = -2;

  static final int COMPILATION_UNIT = -3;
  static final int PACKAGE = -4;
  static final int AUTO_IMPORTS = -5;
  static final int ANNOTATION = -6;
  static final int MODIFIER = -7;
  static final int CLASS = -8;
  static final int EXTENDS = -9;
  static final int METHOD = -10;

  static final int IDENTIFIER = -11;
  static final int NAME = -12;
  static final int STRING_LITERAL = -13;

  static final int LOCAL_VARIABLE = -14;
  static final int METHOD_INVOCATION = -15;

  int[] code = new int[10];

  private int codeIndex;

  private int[] element = new int[10];

  private int elementIndex;

  Object[] object = new Object[10];

  private int objectIndex;

  @Override
  public final void _extends(ClassName superclass) {
    Check.notNull(superclass, "superclass == null");

    addObject(EXTENDS, superclass);
  }

  @Override
  public final void _final() {
    addObject(MODIFIER, Modifier.FINAL);
  }

  @Override
  public final void annotation(int length) {
    element(ANNOTATION, length);
  }

  @Override
  public final void autoImports() {
    markElement(codeIndex);

    add(AUTO_IMPORTS);
  }

  @Override
  public final void classDeclaration(int length) {
    element(CLASS, length);
  }

  @Override
  public final void className(ClassName name) {
    Check.notNull(name, "name == null");

    addObject(NAME, name);
  }

  public final void compilationUnitEnd() {
    element(COMPILATION_UNIT, elementIndex);

    if (elementIndex != 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    code[1] = element[0];
  }

  public final void compilationUnitStart() {
    codeIndex = 0;

    elementIndex = 0;

    objectIndex = 0;

    add(JMP, NULL);
  }

  @Override
  public final void identifier(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    addObject(IDENTIFIER, name);
  }

  @Override
  public final void localVariable(int length) {
    element(LOCAL_VARIABLE, length);
  }

  @Override
  public final void methodDeclaration(int length) {
    element(METHOD, length);
  }

  @Override
  public final void methodInvocation(int length) {
    element(METHOD_INVOCATION, length);
  }

  @Override
  public final void name(String value) {
    Check.argument(
      SourceVersion.isIdentifier(value), // implicit null-check
      value, " is not a valid identifier"
    );

    addObject(NAME, value);
  }

  @Override
  public final void packageDeclaration(String packageName) {
    Check.notNull(packageName, "packageName == null");
    Check.argument(
      SourceVersion.isName(packageName),
      packageName, " is not a valid package name"
    );

    addObject(NAME, packageName);

    element(PACKAGE, 1);
  }

  @Override
  public void stringLiteral(String value) {
    Check.notNull(value, "value == null");

    addObject(STRING_LITERAL, value);
  }

  final int[] toCodes() { return Arrays.copyOf(code, codeIndex); }

  final Object[] toObjects() {
    return Arrays.copyOf(object, objectIndex);
  }

  private void add(int v0) {
    code = IntArrays.growIfNecessary(code, codeIndex);

    code[codeIndex++] = v0;
  }

  private void add(int v0, int v1) {
    code = IntArrays.growIfNecessary(code, codeIndex + 1);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
  }

  private void addObject(int type, Object value) {
    markElement(codeIndex);

    add(type, store(value));
  }

  private void element(int type, int length) {
    var start = elementIndex - length;

    var mark = codeIndex;

    add(type, length);

    for (int i = start; i < elementIndex; i++) {
      add(element[i]);
    }

    elementIndex = start;

    markElement(mark);
  }

  private void markElement(int value) {
    element = IntArrays.growIfNecessary(element, elementIndex);

    element[elementIndex++] = value;
  }

  private int store(Object value) {
    int result = objectIndex;

    object = ObjectArrays.growIfNecessary(object, objectIndex);

    object[objectIndex++] = value;

    return result;
  }

}