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
import objectos.util.IntArrays;

final class Pass1 {

  interface Source {

    int codeAt(int index);

  }

  private static final int _COMPILATION_UNIT = 1;

  private static final int _CLASS_DECL = 2;

  static final int KEYWORD = -1;

  static final int IDENTIFIER = -2;

  static final int BLOCK_START = -3;

  static final int BLOCK_END = -4;

  static final int EOF = -5;

  private int[] code = new int[32];

  private int codeIndex;

  private Source source;

  private int sourceIndex;

  private int[] stack = new int[16];

  private int stackIndex;

  public final void execute(Source source) {
    this.source = source;

    codeIndex = 0;

    sourceIndex = 0;

    stackIndex = -1;

    execute0();
  }

  final int[] toArray() {
    return Arrays.copyOf(code, codeIndex);
  }

  private void add(int c0) {
    code = IntArrays.growIfNecessary(code, codeIndex);

    code[codeIndex++] = c0;
  }

  private void add(int c0, int c1) {
    code = IntArrays.growIfNecessary(code, codeIndex + 1);

    code[codeIndex++] = c0;
    code[codeIndex++] = c1;
  }

  private void contextDec() {
    assert stackIndex > 0;

    stack[stackIndex]--;
  }

  private int contextPeek() {
    assert stackIndex > 1;

    return stack[stackIndex - 1];
  }

  private void execute0() {
    while (true) {
      var code = source.codeAt(sourceIndex++);

      switch (code) {
        case Pass0.JMP -> {
          sourceIndex = source.codeAt(sourceIndex);
        }

        case Pass0.CLASS -> {
          contextDec();

          var len = source.codeAt(sourceIndex++);

          push(_CLASS_DECL, len);
        }

        case Pass0.COMPILATION_UNIT -> {
          assert stackIndex == -1;

          var len = source.codeAt(sourceIndex++);

          push(_COMPILATION_UNIT, len);
        }

        case Pass0.IDENTIFIER -> {
          contextDec();

          var ctx = contextPeek();

          switch (ctx) {
            case _CLASS_DECL -> add(Pass1.KEYWORD, Keyword.CLASS.ordinal());

            default -> throw new UnsupportedOperationException("Implement me :: ctx=" + ctx);
          }

          var idx = source.codeAt(sourceIndex++);

          add(Pass1.IDENTIFIER, idx);
        }

        case Pass0.EOF -> {
          var idx = stackIndex;

          while (idx > 0) {
            var len = stack[idx--];

            assert len == 0;

            var ctx = stack[idx--];

            switch (ctx) {
              case _CLASS_DECL -> add(Pass1.BLOCK_START, Pass1.BLOCK_END);

              case _COMPILATION_UNIT -> add(Pass1.EOF);

              default -> throw new UnsupportedOperationException("Implement me :: ctx=" + ctx);
            }
          }

          return;
        }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
  }

  private void push(int c0, int c1) {
    stack = IntArrays.growIfNecessary(stack, stackIndex + 2);

    stack[++stackIndex] = c0;
    stack[++stackIndex] = c1;
  }

}