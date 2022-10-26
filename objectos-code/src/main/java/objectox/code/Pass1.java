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

  static final int LIST = -7;

  static final int EOF = -8;

  static final int ANNOTATION = -9;

  static final int METHOD = -10;

  static final int LOCAL_VARIABLE = -11;

  static final int STRING_LITERAL = -12;

  static final int MODIFIER = -13;

  static final int METHOD_INVOCATION = -14;

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

  private void add(int v0, int v1, int v2, int v3) {
    code = IntArrays.growIfNecessary(code, codeIndex + 3);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
  }

  private void add(int v0, int v1, int v2, int v3, int v4) {
    code = IntArrays.growIfNecessary(code, codeIndex + 4);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
    code[codeIndex++] = v4;
  }

  private void add(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    code = IntArrays.growIfNecessary(code, codeIndex + 7);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
    code[codeIndex++] = v4;
    code[codeIndex++] = v5;
    code[codeIndex++] = v6;
    code[codeIndex++] = v7;
  }

  private void add(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    code = IntArrays.growIfNecessary(code, codeIndex + 8);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
    code[codeIndex++] = v4;
    code[codeIndex++] = v5;
    code[codeIndex++] = v6;
    code[codeIndex++] = v7;
    code[codeIndex++] = v8;
  }

  private void execute() {
    var start = source[0];

    assert start == Pass0.JMP : start;

    var jmp = source[1];

    var inst = source[jmp];

    assert inst == Pass0.COMPILATION_UNIT : instruction;

    executeCompilationUnit(jmp);
  }

  private int executeAnnotation(int index) {
    var self = codeIndex;

    int name = NOP;
    int pairs = NOP;

    add(ANNOTATION, name, pairs);

    index++;

    int children = source[index++];

    for (int limit = index + children; index < limit; index++) {
      int jmp = source[index];
      int inst = source[jmp];

      switch (inst) {
        case Pass0.NAME -> {
          if (name == NOP) {
            name = executeName(jmp);
          } else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }

    set(self, name, pairs);

    return self;
  }

  private int executeClass(int index) {
    int self = codeIndex;

    int modifiers = NOP;
    int name = NOP;
    int typeArgs = NOP;
    int _extends = NOP;
    int _implements = NOP;
    int _permits = NOP;
    int body = NOP;

    add(
      CLASS,
      modifiers, name, typeArgs, _extends, _implements, _permits,
      body
    );

    index++;

    int children = source[index++];

    for (int limit = index + children; index < limit; index++) {
      int jmp = source[index];
      int inst = source[jmp];

      switch (inst) {
        case Pass0.ANNOTATION -> modifiers = listAdd(modifiers, executeAnnotation(jmp));

        case Pass0.MODIFIER -> modifiers = listAdd(modifiers, executeModifier(jmp));

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

        case Pass0.METHOD -> {
          var value = executeMethod(jmp);

          body = listAdd(body, value);
        }

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }

    set(
      self,
      modifiers, name, typeArgs, _extends, _implements, _permits,
      body
    );

    return self;
  }

  private void executeCompilationUnit(int index) {
    var self = codeIndex;

    var _package = NOP;
    var _import = NOP;
    var body = NOP;

    add(
      COMPILATION_UNIT,
      _package,
      _import,
      body
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

        case Pass0.CLASS -> body = listAdd(body, executeClass(jmp));

        case Pass0.LOCAL_VARIABLE -> body = listAdd(body, executeLocalVariable(jmp));

        case Pass0.METHOD_INVOCATION -> body = listAdd(body, executeMethodInvocation(jmp));

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
      body
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

  private int executeLocalVariable(int index) {
    var self = codeIndex;

    var modifiers = NOP;
    var type = NOP;
    var name = NOP;
    var init = NOP;

    add(
      LOCAL_VARIABLE,
      modifiers, type, name, init
    );

    index++;

    var children = source[index++];

    for (var limit = index + children; index < limit; index++) {
      var jmp = source[index];
      var inst = source[jmp];

      switch (inst) {
        case Pass0.IDENTIFIER -> name = setOrThrow(name, executeIdentifier(jmp));

        case Pass0.STRING_LITERAL -> init = setOrThrow(init, executeStringLiteral(jmp));

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }

    set(
      self,
      modifiers, type, name, init
    );

    return self;
  }

  private int executeMethod(int index) {
    int self = codeIndex;

    int modifiers = NOP;
    int typeParams = NOP;
    int returnType = NOP;
    int name = NOP;
    int receiver = NOP;
    int params = NOP;
    int _throws = NOP;
    int body = NOP;

    add(
      METHOD,
      modifiers, typeParams, returnType, name, receiver, params, _throws,
      body
    );

    index++;

    int children = source[index++];

    for (int limit = index + children; index < limit; index++) {
      int jmp = source[index];
      int inst = source[jmp];

      switch (inst) {
        case Pass0.IDENTIFIER -> {
          if (name == NOP) {
            name = executeIdentifier(jmp);
          } else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }

    set(
      self,
      modifiers, typeParams, returnType, name, receiver, params, _throws,
      body
    );

    return self;
  }

  private int executeMethodInvocation(int index) {
    var self = codeIndex;

    int callee = NOP;
    int typeArgs = NOP;
    int name = NOP;
    int args = NOP;

    add(METHOD_INVOCATION, callee, typeArgs, name, args);

    index++;

    int children = source[index++];

    for (int limit = index + children; index < limit; index++) {
      int jmp = source[index];
      int inst = source[jmp];

      switch (inst) {
        case Pass0.NAME -> name = setOrThrow(name, executeName(jmp));

        case Pass0.STRING_LITERAL -> args = listAdd(args, executeStringLiteral(jmp));

        default -> throw new UnsupportedOperationException("Implement me :: inst=" + inst);
      }
    }

    set(self, callee, typeArgs, name, args);

    return self;
  }

  private int executeModifier(int index) {
    var self = codeIndex;

    index++;

    add(MODIFIER, source[index]);

    return self;
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

  private int executeStringLiteral(int index) {
    var self = codeIndex;

    index++;

    add(STRING_LITERAL, source[index]);

    return self;
  }

  private int listAdd(int list, int value) {
    if (list == NOP) {
      list = codeIndex;

      add(LIST, 1, value);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    return list;
  }

  private void set(
      int zero,
      int v1, int v2) {
    code[zero + 1] = v1;
    code[zero + 2] = v2;
  }

  private void set(
      int zero,
      int v1, int v2, int v3) {
    code[zero + 1] = v1;
    code[zero + 2] = v2;
    code[zero + 3] = v3;
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
      int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    code[zero + 1] = v1;
    code[zero + 2] = v2;
    code[zero + 3] = v3;
    code[zero + 4] = v4;
    code[zero + 5] = v5;
    code[zero + 6] = v6;
    code[zero + 7] = v7;
  }

  private void set(
      int zero,
      int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    code[zero + 1] = v1;
    code[zero + 2] = v2;
    code[zero + 3] = v3;
    code[zero + 4] = v4;
    code[zero + 5] = v5;
    code[zero + 6] = v6;
    code[zero + 7] = v7;
    code[zero + 8] = v8;
  }

  private int setOrThrow(int index, int value) {
    if (index == NOP) {
      return value;
    }

    throw new UnsupportedOperationException("Implement me");
  }

}