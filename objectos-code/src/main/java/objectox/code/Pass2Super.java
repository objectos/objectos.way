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

import objectos.code.JavaTemplate.Renderer;
import objectos.util.IntArrays;

abstract class Pass2Super {

  int[] codes;

  int cursor;

  Object[] objects;

  ImportSet importSet;

  Renderer processor;

  int[] stack = new int[16];

  int stackCursor;

  int code;

  final void codeadv() {
    code = codes[cursor++];
  }

  final void codeass(int value) {
    assert code == value : value;
  }

  final void codejmp() {
    codeadv();

    codeass(Pass1Super.JMP);

    codeadv();

    codepsh();
  }

  final boolean codenop() {
    return code == Pass1Super.NOP;
  }

  final boolean codenxt() {
    codeadv();

    return code != Pass1Super.NOP;
  }

  final Object codeobj() {
    return objects[code];
  }

  final int codepek() {
    return codes[code];
  }

  final void codepop() {
    cursor = stack[--stackCursor];

    code = codes[cursor - 1];
  }

  final void codepsh() {
    stack = IntArrays.growIfNecessary(stack, stackCursor);

    stack[stackCursor++] = cursor;

    cursor = code;

    codeadv();
  }

  final UnsupportedOperationException codeuoe() {
    return new UnsupportedOperationException("Implement me :: code=" + code);
  }

  final boolean largs(boolean comma) {
    var result = false;

    var nl = 0;

    while (lnext()) {
      if (code != Pass1Super.NEW_LINE) {
        result = true;

        break;
      }

      nl++;
    }

    if (result && comma) {
      processor.comma();
    }

    for (int i = 0; i < nl; i++) {
      processor.newLine();
    }

    return result;
  }

  final boolean lnext() {
    if (code == Pass1Super.LHEAD) {
      codeadv();

      codepsh();

      return true;
    }

    if (code == Pass1Super.LNEXT) {
      throw new UnsupportedOperationException("Implement me");
    }

    if (code == Pass1Super.LNULL) {
      throw new UnsupportedOperationException("Implement me");
    }

    codepop();

    codeadv();

    if (code == Pass1Super.LNULL) {
      return false;
    }

    cursor = code;

    codeadv();

    codeass(Pass1Super.LNEXT);

    codeadv();

    codepsh();

    return true;
  }

}
