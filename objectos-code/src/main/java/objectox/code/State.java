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
import objectos.code.TypeName;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

public final class State {

  private int[] codeArray = new int[32];

  private int codeCursor;

  private int[] elementArray = new int[10];

  private int elementCursor;

  private final ImportSet importSet = new ImportSet();

  private Object[] objectArray = new Object[10];

  private int objectCursor;

  private int proto;

  private int[] protoArray = new int[10];

  private int protoCursor;

  private int[] stack = new int[16];

  private int stackCursor;

  public final int[] codeArray() {
    return Arrays.copyOf(codeArray, codeCursor);
  }

  public final ImportSet importSet() { return importSet; }

  public final Object[] objects() {
    return Arrays.copyOf(objectArray, objectCursor);
  }

  public final int[] protos() {
    return Arrays.copyOf(protoArray, protoCursor);
  }

  final void autoImports() {
    importSet.enable();
  }

  final int codeadd() {
    return codeCursor;
  }

  final int codeadd(int v0) {
    var result = codeCursor;

    codeArray = IntArrays.growIfNecessary(codeArray, codeCursor + 0);

    codeArray[codeCursor++] = v0;

    return result;
  }

  final int codeadd(int v0, int v1) {
    var result = codeCursor;

    codeArray = IntArrays.growIfNecessary(codeArray, codeCursor + 1);

    codeArray[codeCursor++] = v0;
    codeArray[codeCursor++] = v1;

    return result;
  }

  final int codeadd(int v0, int v1, int v2) {
    var result = codeCursor;

    codeArray = IntArrays.growIfNecessary(codeArray, codeCursor + 2);

    codeArray[codeCursor++] = v0;
    codeArray[codeCursor++] = v1;
    codeArray[codeCursor++] = v2;

    return result;
  }

  final int codeadd(int v0, int v1, int v2, int v3) {
    var result = codeCursor;

    codeArray = IntArrays.growIfNecessary(codeArray, codeCursor + 3);

    codeArray[codeCursor++] = v0;
    codeArray[codeCursor++] = v1;
    codeArray[codeCursor++] = v2;
    codeArray[codeCursor++] = v3;

    return result;
  }

  final int codeadd(int v0, int v1, int v2, int v3, int v4) {
    var result = codeCursor;

    codeArray = IntArrays.growIfNecessary(codeArray, codeCursor + 4);

    codeArray[codeCursor++] = v0;
    codeArray[codeCursor++] = v1;
    codeArray[codeCursor++] = v2;
    codeArray[codeCursor++] = v3;
    codeArray[codeCursor++] = v4;

    return result;
  }

  final int codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    var result = codeCursor;

    codeArray = IntArrays.growIfNecessary(codeArray, codeCursor + 7);

    codeArray[codeCursor++] = v0;
    codeArray[codeCursor++] = v1;
    codeArray[codeCursor++] = v2;
    codeArray[codeCursor++] = v3;
    codeArray[codeCursor++] = v4;
    codeArray[codeCursor++] = v5;
    codeArray[codeCursor++] = v6;
    codeArray[codeCursor++] = v7;

    return result;
  }

  final int codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    var result = codeCursor;

    codeArray = IntArrays.growIfNecessary(codeArray, codeCursor + 8);

    codeArray[codeCursor++] = v0;
    codeArray[codeCursor++] = v1;
    codeArray[codeCursor++] = v2;
    codeArray[codeCursor++] = v3;
    codeArray[codeCursor++] = v4;
    codeArray[codeCursor++] = v5;
    codeArray[codeCursor++] = v6;
    codeArray[codeCursor++] = v7;
    codeArray[codeCursor++] = v8;

    return result;
  }

  final int codelst(int list, int value) {
    if (list == ByteCode.NOP) {
      list = codeCursor;

      codeadd(ByteCode.LIST, ByteCode.NOP, value, ByteCode.EOF, ByteCode.NOP);

      return list;
    }

    var newcell = codeCursor;

    codeadd(ByteCode.LIST_CELL, value, ByteCode.EOF, ByteCode.NOP);

    var lastcell = codeArray[list + 1];

    if (lastcell == ByteCode.NOP) {
      codeArray[list + 1] = newcell; // list last cell
      codeArray[list + 3] = ByteCode.JMP;
      codeArray[list + 4] = newcell;

      return list;
    }

    codeArray[list + 1] = newcell; // list last cell
    codeArray[lastcell + 2] = ByteCode.JMP;
    codeArray[lastcell + 3] = newcell;

    return list;
  }

  final int codeset(
      int zero,
      int v1, int v2) {
    codeArray[zero + 1] = v1;
    codeArray[zero + 2] = v2;

    return zero;
  }

  final int codeset(
      int zero,
      int v1, int v2, int v3) {
    codeArray[zero + 1] = v1;
    codeArray[zero + 2] = v2;
    codeArray[zero + 3] = v3;

    return zero;
  }

  final int codeset(
      int zero,
      int v1, int v2, int v3, int v4) {
    codeArray[zero + 1] = v1;
    codeArray[zero + 2] = v2;
    codeArray[zero + 3] = v3;
    codeArray[zero + 4] = v4;

    return zero;
  }

  final int codeset(
      int zero,
      int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    codeArray[zero + 1] = v1;
    codeArray[zero + 2] = v2;
    codeArray[zero + 3] = v3;
    codeArray[zero + 4] = v4;
    codeArray[zero + 5] = v5;
    codeArray[zero + 6] = v6;
    codeArray[zero + 7] = v7;

    return zero;
  }

  final int codeset(
      int zero,
      int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    codeArray[zero + 1] = v1;
    codeArray[zero + 2] = v2;
    codeArray[zero + 3] = v3;
    codeArray[zero + 4] = v4;
    codeArray[zero + 5] = v5;
    codeArray[zero + 6] = v6;
    codeArray[zero + 7] = v7;
    codeArray[zero + 8] = v8;

    return zero;
  }

  final void elementadd(int type, int length) {
    var start = elementCursor - length;

    var mark = protoCursor;

    protoadd(type, length);

    for (int i = start; i < elementCursor; i++) {
      protoadd(elementArray[i]);
    }

    elementCursor = start;

    elementmark(mark);
  }

  final void objectadd(int type, Object value) {
    elementmark(protoCursor);

    protoadd(type, objectadd(value));
  }

  final void packagename(String packageName) {
    importSet.packageName(packageName);
  }

  final void pass0end() {
    elementadd(ByteProto.COMPILATION_UNIT, elementCursor);

    if (elementCursor != 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    protoArray[1] = elementArray[0];
  }

  final void pass0start() {
    protoadd(ByteProto.JMP, ByteProto.NULL);
  }

  final void protoass(int value) {
    assert proto == value : value;
  }

  final int protolst() {
    protorea();

    return proto;
  }

  final void protopop() {
    protoCursor = stack[--stackCursor];
  }

  final int protopsh() {
    stack = IntArrays.growIfNecessary(stack, stackCursor);

    stack[stackCursor++] = protoCursor;

    protoCursor = proto;

    return protorea();
  }

  final int protorea() {
    return proto = protoArray[protoCursor++];
  }

  final void protorea(int value) {
    proto = protoArray[protoCursor++];

    protoass(value);
  }

  final UnsupportedOperationException protouoe() {
    return new UnsupportedOperationException("implement me :: proto=" + proto);
  }

  final State reset() {
    codeCursor = 0;

    elementCursor = 0;

    importSet.clear();

    objectCursor = 0;

    protoCursor = 0;

    protoadd(ByteProto.JMP, ByteProto.NULL);

    return this;
  }

  final State startPass1() {
    protoCursor = 0;

    return this;
  }

  final void typenameadd(TypeName typeName) {
    typeName.acceptClassNameSet(importSet);
  }

  private void elementmark(int value) {
    elementArray = IntArrays.growIfNecessary(elementArray, elementCursor);

    elementArray[elementCursor++] = value;
  }

  private int objectadd(Object value) {
    int result = objectCursor;

    objectArray = ObjectArrays.growIfNecessary(objectArray, objectCursor);

    objectArray[objectCursor++] = value;

    return result;
  }

  private void protoadd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoCursor);

    protoArray[protoCursor++] = v0;
  }

  private void protoadd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoCursor + 1);

    protoArray[protoCursor++] = v0;
    protoArray[protoCursor++] = v1;
  }

}