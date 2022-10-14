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
import java.util.List;
import javax.lang.model.SourceVersion;
import objectos.lang.Check;
import objectos.util.GrowableList;
import objectos.util.IntArrays;

final class Pass0 {

  static final int EOF = -1;
  static final int JMP = -2;

  static final int COMPILATION_UNIT = -3;
  static final int CLASS = -4;

  static final int IDENTIFIER = -5;

  private int[] code = new int[10];

  private int codeIndex;

  private int[] element = new int[10];

  private int elementIndex;

  private final List<String> strings = new GrowableList<>();

  Pass0() {}

  public final void _class(int length) {
    element(CLASS, length);
  }

  public final void id(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    markElement(codeIndex);

    code(IDENTIFIER, string(name), JMP, Integer.MIN_VALUE);
  }

  public final void templateEnd() {
    element(COMPILATION_UNIT, elementIndex);

    if (elementIndex != 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    code[1] = element[0];

    code[codeIndex - 1] = codeIndex;

    code(EOF);
  }

  public final void templateStart() {
    codeIndex = 0;

    elementIndex = 0;

    strings.clear();

    code(JMP, Integer.MIN_VALUE);
  }

  final int[] toCodes() { return Arrays.copyOf(code, codeIndex); }

  private void code(int c0) {
    code = IntArrays.growIfNecessary(code, codeIndex);

    code[codeIndex++] = c0;
  }

  private void code(int c0, int c1) {
    code = IntArrays.growIfNecessary(code, codeIndex + 1);

    code[codeIndex++] = c0;
    code[codeIndex++] = c1;
  }

  private void code(int c0, int c1, int c2, int c3) {
    code = IntArrays.growIfNecessary(code, codeIndex + 3);

    code[codeIndex++] = c0;
    code[codeIndex++] = c1;
    code[codeIndex++] = c2;
    code[codeIndex++] = c3;
  }

  private void element(int type, int length) {
    var start = elementIndex - length;

    var mark = codeIndex;

    code(type, length);

    for (int i = start; i < elementIndex; i++) {
      int currentElementIndex = element[i];

      code(JMP, currentElementIndex);

      var ret = codeIndex;

      int c = code[currentElementIndex];

      int offset = switch (c) {
        case CLASS -> {
          int children = code[currentElementIndex + 1];

          int skip = 1; // length;

          skip += children * 2;

          skip += 2; // JMP

          yield skip;
        }

        case IDENTIFIER -> 3;

        default -> throw new UnsupportedOperationException("Implement me :: code=" + c);
      };

      code[currentElementIndex + offset] = ret;
    }

    code(JMP, Integer.MIN_VALUE);

    elementIndex = start;

    markElement(mark);
  }

  private void markElement(int value) {
    element = IntArrays.growIfNecessary(element, elementIndex);

    element[elementIndex++] = value;
  }

  private int string(String value) {
    int index = strings.size();

    strings.add(value);

    return index;
  }

}