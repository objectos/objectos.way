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

  static final int LNULL = -21;

  int[] code = new int[32];

  int codeIndex;

  ImportSet importSet;

  Object[] object;

  int[] source;

  int sourceIndex;

  int proto;

  int[] stack = new int[16];

  int stackIndex;

  protected final int setOrReplace(int pointer, int value) {
    if (pointer == NOP) {
      return value;
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  protected final int setOrThrow(int index, int value) {
    if (index == NOP) {
      return value;
    }

    throw new UnsupportedOperationException("Implement me");
  }

  final int add(int v0) {
    var self = codeIndex;

    code = IntArrays.growIfNecessary(code, codeIndex + 0);

    code[codeIndex++] = v0;

    return self;
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

  final int add(int v0, int v1, int v2, int v3, int v4) {
    var self = codeIndex;

    code = IntArrays.growIfNecessary(code, codeIndex + 4);

    code[codeIndex++] = v0;
    code[codeIndex++] = v1;
    code[codeIndex++] = v2;
    code[codeIndex++] = v3;
    code[codeIndex++] = v4;

    return self;
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
      return add(LHEAD, value, LNULL, LNULL);
    }

    var head = code[list];

    assert head == LHEAD : head;

    var next = add(LNEXT, value, LNULL);

    var last = code[list + 3];

    var target = last != LNULL ? last : list;

    code[target + 2] = next;

    code[list + 3] = next;

    return list;
  }

  final int protoadv() {
    return proto = source[sourceIndex++];
  }

  final void protoass(int value) {
    assert proto == value : proto;
  }

  final void protojmp() {
    protoass(ByteProto.JMP);

    protoadv();

    protopsh();
  }

  final boolean protolop() {
    return proto != ByteProto.BREAK;
  }

  final void protonxt() {
    protopop();

    protoadv();
  }

  final void protopop() {
    sourceIndex = stack[--stackIndex];
  }

  final void protopsh() {
    stack = IntArrays.growIfNecessary(stack, stackIndex);

    stack[stackIndex++] = sourceIndex;

    sourceIndex = proto;

    protoadv();
  }

  final UnsupportedOperationException protouoe() {
    return new UnsupportedOperationException("Implement me :: proto=" + proto);
  }

}