/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectox.code;

import objectos.util.IntArrays;

abstract class Pass1Super {

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

  static final int LIST_CELL = -15;

  static final int JMP = -16;

  static final int NEW_LINE = -17;

  static final int EXPRESSION_NAME = -18;

  static final int LHEAD = -19;

  static final int LNEXT = -20;

  int[] code = new int[32];

  int codeIndex;

  ImportSet importSet;

  Object[] object;

  int[] source;

  int sourceIndex;

  int proto;

  int[] stack = new int[16];

  int stackIndex;

  final void add(int v0) {
    code = IntArrays.growIfNecessary(code, codeIndex + 0);

    code[codeIndex++] = v0;
  }

  final int add(int v0, int v1) {
    var self = codeIndex;

    code = IntArrays.growIfNecessary(code, codeIndex + 1);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;

    return self;
  }

  final int add(int v0, int v1, int v2) {
    var self = codeIndex;

    code = IntArrays.growIfNecessary(code, codeIndex + 2);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;

    return self;
  }

  final int add(int v0, int v1, int v2, int v3) {
    var self = codeIndex;

    code = IntArrays.growIfNecessary(code, codeIndex + 3);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;

    return self;
  }

  final void add(int v0, int v1, int v2, int v3, int v4) {
    code = IntArrays.growIfNecessary(code, codeIndex + 4);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
    code[codeIndex++] = v4;
  }

  final int add(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    var self = codeIndex;

    code = IntArrays.growIfNecessary(code, codeIndex + 7);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
    code[codeIndex++] = v4;
    code[codeIndex++] = v5;
    code[codeIndex++] = v6;
    code[codeIndex++] = v7;

    return self;
  }

  final int add(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    var self = codeIndex;

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

    return self;
  }

  final int listadd(int list, int value) {
    if (list == NOP) {
      return add(LHEAD, value, NOP);
    }

    throw new UnsupportedOperationException("Implement me");
  }

}