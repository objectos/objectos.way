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

  private interface Class {
    int NAME = 3;
    int NEXT = 9;
  }

  private interface CompilationUnit {
    int CLASS_INTERFACE = 3;
    int MODULE = 4;
  }

  private static final int NULL = Integer.MAX_VALUE;

  static final int NOP = -1;

  static final int COMPILATION_UNIT = -2;

  static final int CLASS = -3;

  static final int EOF = -4;

  private int[] code = new int[32];

  private int codeIndex;

  private int[] source;

  private int sourceIndex;

  private int[] stack = new int[16];

  private int stackIndex;

  public final void execute(int[] source) {
    this.source = source;

    codeIndex = 0;

    sourceIndex = 0;

    stackIndex = -1;

    execute0();
  }

  final int[] toArray() {
    return Arrays.copyOf(code, codeIndex);
  }

  private void add(int v0, int v1, int v2, int v3, int v4, int v5) {
    code = IntArrays.growIfNecessary(code, codeIndex + 5);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
    code[codeIndex++] = v4;
    code[codeIndex++] = v5;
  }

  private void add(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8, int v9) {
    code = IntArrays.growIfNecessary(code, codeIndex + 9);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
    code[codeIndex++] = v4;
    code[codeIndex++] = v5;
    code[codeIndex++] = v6;
    code[codeIndex++] = v7;
    code[codeIndex++] = v8;
    code[codeIndex++] = v9;
  }

  private void execute0() {
    while (true) {
      var code = source[sourceIndex++];

      switch (code) {
        case Pass0.JMP -> sourceIndex = source[sourceIndex];

        case Pass0.CLASS -> executeClass();

        case Pass0.COMPILATION_UNIT -> executeCompilationUnit();

        case Pass0.IDENTIFIER -> executeIdentifier();

        case Pass0.EOF -> {
          executeEof();

          return;
        }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
  }

  private void executeClass() {
    // parent update

    parentDecrement();

    var parentIndex = stack[stackIndex - 1];

    var parent = code[parentIndex];

    switch (parent) {
      case COMPILATION_UNIT -> {
        var moduleIndex = parentIndex + CompilationUnit.MODULE;

        var module = code[moduleIndex];

        if (module != NULL) {
          throw new UnsupportedOperationException(
            "Implement me :: invalid class decl in module");
        }

        var classIfaceIndex = parentIndex + CompilationUnit.CLASS_INTERFACE;

        var classIface = code[classIfaceIndex];

        if (classIface != NULL) {
          throw new UnsupportedOperationException(
            "Implement me :: sibling class?");
        }

        code[classIfaceIndex] = codeIndex;
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: parent=" + parent);
    }

    // class

    var len = source[sourceIndex++];

    push(codeIndex, len);

    add(
      CLASS,
      NULL, // annotations
      NULL, // mods
      NULL, // name
      NULL, // type args
      NULL, // super
      NULL, // implements
      NULL, // permits
      NULL, // body
      NULL // NEXT
    );
  }

  private void executeClassEnd(int count, int selfIndex) {
    if (count > 0) {
      return;
    }

    pop();

    // self update

    for (int i = 1; i < 9; i++) {
      var idx = selfIndex + i;

      if (code[idx] == NULL) {
        code[idx] = NOP;
      }
    }
  }

  private void executeCompilationUnit() {
    var len = source[sourceIndex++];

    push(codeIndex, len);

    add(
      COMPILATION_UNIT,
      NULL, // package
      NULL, // import
      NULL, // class/interface
      NULL, // module
      NULL // EOF
    );
  }

  private void executeEof() {
    assert stackIndex == 1;
    assert stack[stackIndex] == 0;

    var index = stack[stackIndex - 1];

    if (index != 0) {
      throw new UnsupportedOperationException("Implement me :: expecting compilation unit");
    }

    index++;

    // package
    if (code[index] == NULL) {
      code[index] = NOP;
    }

    index++;

    // imports
    if (code[index] == NULL) {
      code[index] = NOP;
    }

    index++;

    var classIface = code[index];

    if (classIface == NULL) {
      code[index] = NOP;
    } else {
      executeEofClassIface(classIface, 5);
    }

    index++;

    var mod = code[index];

    if (mod == NULL) {
      code[index] = NOP;
    } else {
      throw new UnsupportedOperationException("module-info.java not supported yet");
    }

    index++;

    code[index] = EOF;
  }

  private void executeEofClassIface(int startIndex, int value) {
    var index = startIndex;

    while (true) {
      var type = code[index];

      switch (type) {
        case CLASS -> {
          var next = code[index + Class.NEXT];

          if (next == NULL) {
            code[index + Class.NEXT] = value;

            return;
          } else {
            index = next;
          }
        }

        default -> throw new UnsupportedOperationException("Implement me :: type=" + type);
      }
    }
  }

  private void executeIdentifier() {
    var objectIndex = source[sourceIndex++];

    int count = parentDecrement();

    var parentIndex = stack[stackIndex - 1];

    var parent = code[parentIndex];

    switch (code[parentIndex]) {
      case CLASS -> {
        var index = parentIndex + Class.NAME;

        var current = code[index];

        if (current != NULL) {
          throw new UnsupportedOperationException(
            "Implement me :: replace name?");
        }

        code[index] = objectIndex;

        executeClassEnd(count, parentIndex);
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: parent=" + parent);
    }
  }

  private int parentDecrement() {
    assert stackIndex >= 1;

    return --stack[stackIndex];
  }

  private void pop() {
    stackIndex -= 2;
  }

  private void push(int v0, int v1) {
    stack = IntArrays.growIfNecessary(stack, stackIndex + 2);

    stack[++stackIndex] = v0;
    stack[++stackIndex] = v1;
  }

}