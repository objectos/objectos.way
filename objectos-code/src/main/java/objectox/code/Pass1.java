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
import objectos.code.ClassName;
import objectos.util.IntArrays;

public final class Pass1 {

  static final int NOP = -1;

  static final int COMPILATION_UNIT = -2;

  static final int IMPORT = -3;

  static final int IMPORT_ON_DEMAND = -4;

  static final int PACKAGE = -5;

  static final int CLASS = -6;

  static final int MODIFIER = -7;

  static final int EOF = -8;

  private final ImportSet importSet = new ImportSet();

  private int[] code = new int[32];

  private int codeIndex;

  private Object[] object;

  private int[] source;

  private int instruction;

  public final void execute(int[] source, Object[] object) {
    this.source = source;
    this.object = object;

    importSet.clear();

    codeIndex = 0;

    execute();
  }

  final int[] toArray() {
    return Arrays.copyOf(code, codeIndex);
  }

  private void add(int v0) {
    code = IntArrays.growIfNecessary(code, codeIndex + 0);

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

  private void execute() {
    var start = source[0];

    assert start == Pass0.JMP : start;

    var jmp = source[1];

    var inst = source[jmp];

    assert inst == Pass0.COMPILATION_UNIT : instruction;

    executeCompilationUnit(jmp);
  }

  private int executeClass(int index) {
    var self = codeIndex;

    var annotations = NOP;
    var modifiers = NOP;
    var name = NOP;
    var typeArgs = NOP;
    var _extends = NOP;
    var _implements = NOP;
    var _permits = NOP;
    var body = NOP;
    var next = EOF;

    add(
      CLASS,
      annotations,
      modifiers,
      name,
      typeArgs,
      _extends,
      _implements,
      _permits,
      body,
      next
    );

    index++;

    var children = source[index++];

    for (int limit = index + children; index < limit; index++) {
      var jmp = source[index];
      var inst = source[jmp];

      switch (inst) {
        case Pass0.MODIFIER -> {
          modifiers = executeModifier(jmp, modifiers);
        }

        case Pass0.IDENTIFIER -> {
          if (name == NOP) {
            name = executeIdentifier(jmp);
          } else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case Pass0.EXTENDS -> {
          if (_extends == NOP) {
            _extends = executeExtends(jmp);
          } else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }

    set(
      self,

      annotations,
      modifiers,
      name,
      typeArgs,
      _extends,
      _implements,
      _permits,
      body,
      next
    );

    return self;
  }

  private void executeCompilationUnit(int index) {
    var self = codeIndex;

    var _package = NOP;
    var _import = NOP;
    var _classIface = NOP;
    var _module = NOP;

    add(
      COMPILATION_UNIT,
      _package,
      _import,
      _classIface,
      _module,
      EOF
    );

    index++;

    var children = source[index++];

    for (int limit = index + children; index < limit; index++) {
      var jmp = source[index];
      var inst = source[jmp];

      switch (inst) {
        case Pass0.PACKAGE -> {
          var value = executePackage(jmp);

          if (_package != NOP) {
            throw new UnsupportedOperationException("Implement me");
          } else {
            _package = value;
          }
        }

        case Pass0.AUTO_IMPORTS -> importSet.enable();

        case Pass0.CLASS -> {
          var value = executeClass(jmp);

          if (_classIface != NOP) {
            throw new UnsupportedOperationException("Implement me");
          } else {
            _classIface = value;
          }
        }

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }

    if (_import != NOP) {
      throw new UnsupportedOperationException("Implement me :: unexpected imports");
    }

    if (importSet.enabled) {
      _import = executeEofImportSet();
    }

    set(
      self,

      _package,
      _import,
      _classIface,
      _module
    );
  }

  private int executeEofImportSet() {
    var self = codeIndex;

    var sorted = importSet.sort();

    for (int i = 0, size = sorted.size(); i < size; i++) {
      add(IMPORT, i);
    }

    add(EOF);

    return self;
  }

  private int executeExtends(int index) {
    index++;

    var result = source[index];

    var o = object[result];

    if (o instanceof ClassName cn) {
      importSet.addClassName(cn);
    }

    return result;
  }

  private int executeIdentifier(int index) {
    index++;

    return source[index];
  }

  private int executeModifier(int index, int list) {
    index++;

    var value = source[index];

    if (list == NOP) {
      list = codeIndex;

      add(MODIFIER, 1, value);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    return list;
  }

  private int executeName(int index) {
    index++;

    return source[index];
  }

  private int executePackage(int index) {
    var self = codeIndex;

    var annotations = NOP;
    var name = NOP;

    add(
      PACKAGE,
      annotations,
      name
    );

    index++;

    var children = source[index++];

    for (int limit = index + children; index < limit; index++) {
      var jmp = source[index];
      var inst = source[jmp];

      switch (inst) {
        case Pass0.NAME -> {
          var value = executeName(jmp);

          if (name != NOP) {
            throw new UnsupportedOperationException("Implement me");
          } else {
            name = value;
          }
        }

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }

    set(
      self,

      annotations,
      name
    );

    return self;
  }

  private void set(
      int zero,
      int v1, int v2) {
    code[zero + 1] = v1;
    code[zero + 2] = v2;
  }

  private void set(
      int zero,
      int v1, int v2, int v3, int v4) {
    code[zero + 1] = v1;
    code[zero + 2] = v2;
    code[zero + 3] = v3;
    code[zero + 4] = v4;
  }

  private void set(
      int zero,
      int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8, int v9) {
    code[zero + 1] = v1;
    code[zero + 2] = v2;
    code[zero + 3] = v3;
    code[zero + 4] = v4;
    code[zero + 5] = v5;
    code[zero + 6] = v6;
    code[zero + 7] = v7;
    code[zero + 8] = v8;
    code[zero + 9] = v9;
  }

}