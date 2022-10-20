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
import javax.lang.model.SourceVersion;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

final class Pass0 {

  private static final int NULL = Integer.MIN_VALUE;

  static final int EOF = -1;
  static final int JMP = -2;
  static final int AUTO_IMPORTS = -3;

  static final int COMPILATION_UNIT = -4;
  static final int PACKAGE = -5;
  static final int CLASS = -6;
  static final int EXTENDS = -7;

  static final int IDENTIFIER = -8;
  static final int NAME = -9;

  private int[] code = new int[10];

  private int codeIndex;

  private int[] element = new int[10];

  private int elementIndex;

  private Object[] object = new Object[10];

  private int objectIndex;

  Pass0() {}

  public final void _class(int length) {
    element(CLASS, length);
  }

  public final void _extends(ClassName superclass) {
    Check.notNull(superclass, "superclass == null");

    markElement(codeIndex);

    add(EXTENDS, store(superclass), JMP, NULL);
  }

  public final void _package(String packageName) {
    Check.notNull(packageName, "packageName == null");
    Check.argument(
      SourceVersion.isName(packageName),
      packageName, " is not a valid package name"
    );

    name0(packageName);

    element(PACKAGE, 1);
  }

  public final void autoImports() {
    markElement(codeIndex);

    add(AUTO_IMPORTS, JMP, NULL);
  }

  public final void id(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    markElement(codeIndex);

    add(IDENTIFIER, store(name), JMP, NULL);
  }

  public final void templateEnd() {
    element(COMPILATION_UNIT, elementIndex);

    if (elementIndex != 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    code[1] = element[0];

    code[codeIndex - 1] = codeIndex;

    add(EOF);
  }

  public final void templateStart() {
    codeIndex = 0;

    elementIndex = 0;

    objectIndex = 0;

    add(JMP, NULL);
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

  private void add(int v0, int v1, int v2) {
    code = IntArrays.growIfNecessary(code, codeIndex + 2);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
  }

  private void add(int v0, int v1, int v2, int v3) {
    code = IntArrays.growIfNecessary(code, codeIndex + 3);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
  }

  private void element(int type, int length) {
    var start = elementIndex - length;

    var mark = codeIndex;

    add(type, length);

    for (int i = start; i < elementIndex; i++) {
      int currentElementIndex = element[i];

      add(JMP, currentElementIndex);

      var ret = codeIndex;

      int c = code[currentElementIndex];

      int offset = switch (c) {
        case PACKAGE, CLASS -> {
          int children = code[currentElementIndex + 1];

          int skip = 1; // length;

          skip += children * 2;

          skip += 2; // JMP

          yield skip;
        }

        case AUTO_IMPORTS -> 2;

        case EXTENDS, IDENTIFIER, NAME -> 3;

        default -> throw new UnsupportedOperationException("Implement me :: code=" + c);
      };

      code[currentElementIndex + offset] = ret;
    }

    add(JMP, NULL);

    elementIndex = start;

    markElement(mark);
  }

  private void markElement(int value) {
    element = IntArrays.growIfNecessary(element, elementIndex);

    element[elementIndex++] = value;
  }

  private void name0(Object name) {
    markElement(codeIndex);

    add(NAME, store(name), JMP, NULL);
  }

  private int store(Object value) {
    int result = objectIndex;

    object = ObjectArrays.growIfNecessary(object, objectIndex);

    object[objectIndex++] = value;

    return result;
  }

}