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

  private interface Class {
    int _COUNT = 9;
    int NAME = 3;
    //int TYPE_ARGS = 4;
    int SUPERCLASS = 5;
    int NEXT = 9;
  }

  private interface CompilationUnit {
    int _COUNT = 5;
    int PACKAGE = 1;
    int IMPORTS = 2;
    int CLASS_INTERFACE = 3;
    int MODULE = 4;
    int EOF = 5;
  }

  private interface Package {
    int _COUNT = 3;
    int NAME = 2;
  }

  private static final int NULL = Integer.MAX_VALUE;

  static final int NOP = -1;

  static final int COMPILATION_UNIT = -2;

  static final int IMPORT = -3;

  static final int IMPORT_ON_DEMAND = -4;

  static final int PACKAGE = -5;

  static final int CLASS = -6;

  static final int EOF = -7;

  private final ImportSet importSet = new ImportSet();

  private int[] code = new int[32];

  private int codeIndex;

  private Object[] object;

  private int parent;

  private int parentCount;

  private int parentIndex;

  private int[] source;

  private int sourceIndex;

  private int[] stack = new int[16];

  private int stackIndex;

  public final void execute(int[] source, Object[] object) {
    this.source = source;
    this.object = object;

    importSet.clear();

    codeIndex = 0;

    sourceIndex = 0;

    stackIndex = -1;

    execute0();
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

  private void execute0() {
    while (true) {
      var code = source[sourceIndex++];

      switch (code) {
        case Pass0.JMP -> sourceIndex = source[sourceIndex];

        case Pass0.AUTO_IMPORTS -> executeAutoImports();

        case Pass0.COMPILATION_UNIT -> executeCompilationUnit();

        case Pass0.PACKAGE -> executePackage();

        case Pass0.CLASS -> executeClass();

        case Pass0.EXTENDS -> executeExtends();

        case Pass0.IDENTIFIER -> executeIdentifier();

        case Pass0.NAME -> executeName();

        case Pass0.EOF -> {
          executeEof();

          return;
        }

        default -> throw new UnsupportedOperationException("Implement me :: code=" + code);
      }
    }
  }

  private void executeAutoImports() {
    parentUpdate();

    importSet.enable();
  }

  private void executeClass() {
    parentUpdate();

    switch (parent) {
      case COMPILATION_UNIT -> {
        var module = parentGet(CompilationUnit.MODULE);

        if (module != NULL) {
          throw new UnsupportedOperationException(
            "Implement me :: invalid class decl in module");
        }

        parentSetCode(
          CompilationUnit.CLASS_INTERFACE,
          "Implement me :: sibling class?"
        );

        executeElementEnd(CompilationUnit._COUNT);
      }

      default -> throw new UnsupportedOperationException("Implement me :: parent=" + parent);
    }

    push();

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

  private void executeCompilationUnit() {
    push();

    add(
      COMPILATION_UNIT,
      NULL, // package
      NULL, // import
      NULL, // class/interface
      NULL, // module
      NULL // EOF
    );
  }

  private void executeElementEnd(int count) {
    if (parentCount > 0) {
      return;
    }

    pop();

    for (int i = 1; i < count; i++) {
      var idx = parentIndex + i;

      if (code[idx] == NULL) {
        code[idx] = NOP;
      }
    }
  }

  private void executeEof() {
    assert stackIndex == -1 : stackIndex;

    var imports = code[CompilationUnit.IMPORTS];

    if (imports != NOP) {
      throw new UnsupportedOperationException("Implement me :: unexpected imports");
    }

    if (importSet.enabled) {
      executeEofImportSet();
    }

    var classIface = code[CompilationUnit.CLASS_INTERFACE];

    if (classIface != NOP) {
      executeEofClassIface(classIface, 5);
    }

    var eof = code[CompilationUnit.EOF];

    assert eof == NULL : eof;

    code[CompilationUnit.EOF] = EOF;
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

  private void executeEofImportSet() {
    var sorted = importSet.sort();

    code[CompilationUnit.IMPORTS] = codeIndex;

    for (int i = 0, size = sorted.size(); i < size; i++) {
      add(IMPORT, i);
    }

    add(EOF);
  }

  private void executeExtends() {
    parentUpdate();

    switch (parent) {
      case CLASS -> {
        parentSetObject(Class.SUPERCLASS, "Implement me :: replace superclass?");

        executeElementEnd(Class._COUNT);
      }

      default -> throw new UnsupportedOperationException("Implement me :: parent=" + parent);
    }
  }

  private void executeIdentifier() {
    parentUpdate();

    switch (parent) {
      case CLASS -> {
        parentSetObject(Class.NAME, "Implement me :: replace name?");

        executeElementEnd(Class._COUNT);
      }

      default -> throw new UnsupportedOperationException("Implement me :: parent=" + parent);
    }
  }

  private void executeName() {
    parentUpdate();

    switch (parent) {
      case PACKAGE -> {
        var pkg = parentSetObject(Package.NAME, "Implement me :: replace name?");

        if (pkg instanceof String s) {
          importSet.packageName(s);
        } else {
          throw new UnsupportedOperationException("Implement me");
        }

        executeElementEnd(Package._COUNT);
      }

      default -> throw new UnsupportedOperationException("Implement me :: parent=" + parent);
    }
  }

  private void executePackage() {
    parentUpdate();

    switch (parent) {
      case COMPILATION_UNIT -> parentSetCode(
        CompilationUnit.PACKAGE,
        "Implement me :: multiple package declarations"
      );

      default -> throw new UnsupportedOperationException("Implement me :: parent=" + parent);
    }

    push();

    add(
      PACKAGE,
      NULL, // annotations
      NULL // name
    );
  }

  private int parentGet(int offset) {
    var index = parentIndex + offset;

    return code[index];
  }

  private void parentSetCode(int offset, String errorMessage) {
    var index = parentIndex + offset;

    var value = code[index];

    if (value != NULL) {
      throw new UnsupportedOperationException(errorMessage);
    }

    code[index] = codeIndex;
  }

  private Object parentSetObject(int offset, String errorMessage) {
    var index = parentIndex + offset;

    var current = code[index];

    if (current != NULL) {
      throw new UnsupportedOperationException(errorMessage);
    }

    var objectIndex = source[sourceIndex++];

    var o = object[objectIndex];

    if (o instanceof ClassName cn) {
      importSet.addClassName(cn);
    }

    code[index] = objectIndex;

    return o;
  }

  private void parentUpdate() {
    assert stackIndex >= 1;

    parentCount = --stack[stackIndex];

    parentIndex = stack[stackIndex - 1];

    parent = code[parentIndex];
  }

  private void pop() {
    stackIndex -= 2;
  }

  private void push() {
    var len = source[sourceIndex++];

    push(codeIndex, len);
  }

  private void push(int v0, int v1) {
    stack = IntArrays.growIfNecessary(stack, stackIndex + 2);

    stack[++stackIndex] = v0;
    stack[++stackIndex] = v1;
  }

}