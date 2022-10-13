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

public final class JavaGeneratorImpl {

  private int[] code = new int[10];

  private int codeIndex;

  private int[] element = new int[10];

  private int elementIndex;

  private final List<String> strings = new GrowableList<>();

  JavaGeneratorImpl() {}

  final void _class(int length) {
    element(Code.CLASS, length);
  }

  final int[] codes() { return Arrays.copyOf(code, codeIndex); }

  final void id(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    markElement();

    code(Code.IDENTIFIER, string(name), Code.JMP, Integer.MIN_VALUE);
  }

  final void templateEnd() {
    element(Code.COMPILATION_UNIT, elementIndex);
  }

  private void child(int el, int ret) {
    int c = code[el];

    int offset = switch (c) {
      case Code.IDENTIFIER -> 3;

      default -> throw new UnsupportedOperationException("Implement me :: code=" + c);
    };

    code[el + offset] = ret;
  }

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

  private void element(int code, int length) {
    var ret = codeIndex;

    code(code);

    var start = elementIndex - length;

    for (int i = start; i < elementIndex; i++) {
      int el = element[i];

      child(el, ret);
    }

    code(Code.JMP, Integer.MIN_VALUE);

    elementIndex = start;
  }

  private void markElement() {
    element = IntArrays.growIfNecessary(element, elementIndex);

    element[elementIndex++] = codeIndex;
  }

  private int string(String value) {
    int index = strings.size();

    strings.add(value);

    return index;
  }

}