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

import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

abstract class State {

  final AutoImports autoImports = new AutoImports();

  int code;

  int[] codeArray = new int[10];

  int codeIndex;

  int[] markArray = new int[10];

  int markIndex;

  Object[] objectArray = new Object[10];

  int objectIndex;

  int proto;

  int[] protoArray = new int[10];

  int protoIndex;

  int[] stackArray = new int[10];

  int stackIndex;

  final int codeadd(int v0) {
    var self = codeIndex;

    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 0);

    codeArray[codeIndex++] = v0;

    return self;
  }

  final int codeadd(int v0, int v1) {
    var self = codeIndex;

    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;

    return self;
  }

  final int codeadd(int v0, int v1, int v2) {
    var self = codeIndex;

    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 2);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;

    return self;
  }

  final int codeadd(int v0, int v1, int v2, int v3) {
    var self = codeIndex;

    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 3);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;

    return self;
  }

  final int codeadd(int v0, int v1, int v2, int v3, int v4) {
    var self = codeIndex;

    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 4);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;
    codeArray[codeIndex++] = v4;

    return self;
  }

  final int codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
    var self = codeIndex;

    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 7);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;
    codeArray[codeIndex++] = v4;
    codeArray[codeIndex++] = v5;
    codeArray[codeIndex++] = v6;
    codeArray[codeIndex++] = v7;

    return self;
  }

  final int codeadd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    var self = codeIndex;

    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 8);

    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
    codeArray[codeIndex++] = v2;
    codeArray[codeIndex++] = v3;
    codeArray[codeIndex++] = v4;
    codeArray[codeIndex++] = v5;
    codeArray[codeIndex++] = v6;
    codeArray[codeIndex++] = v7;
    codeArray[codeIndex++] = v8;

    return self;
  }

  final void codeadv() {
    code = codeArray[codeIndex++];
  }

  final void codeass(int value) {
    assert code == value : value;
  }

  final void codejmp() {
    codeadv();

    codeass(ByteCode.JMP);

    codeadv();

    codepsh();
  }

  final boolean codenop() {
    return code == ByteCode.NOP;
  }

  final boolean codenxt() {
    codeadv();

    return code != ByteCode.NOP;
  }

  final Object codeobj() {
    return objectArray[code];
  }

  final int codepek() {
    return codeArray[code];
  }

  final void codepop() {
    codeIndex = stackArray[--stackIndex];

    code = codeArray[codeIndex - 1];
  }

  final void codepsh() {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex++] = codeIndex;

    codeIndex = code;

    codeadv();
  }

  final UnsupportedOperationException codeuoe() {
    return new UnsupportedOperationException("Implement me :: code=" + code);
  }

  final void element(int type) {
    var length = markArray[markIndex--];

    var start = codeIndex - length;

    var mark = protoIndex;

    protoAdd(type);

    for (int i = start; i < codeIndex; i++) {
      protoAdd(ByteProto.JMP, codeArray[i]);
    }

    protoAdd(ByteProto.BREAK);

    codeIndex = start;

    elementAdd(mark);
  }

  final void elementAdd(int value) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex);

    codeArray[codeIndex++] = value;
  }

  final void lambdaCount() {
    markArray[markIndex] += stackArray[stackIndex--];
  }

  final void lambdaPop() {
    var startCount = stackArray[stackIndex];

    var diff = codeIndex - startCount;

    stackArray[stackIndex] = diff;
  }

  final void lambdaPush() {
    stackIndex++;

    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex] = codeIndex;
  }

  final int listadd(int list, int value) {
    if (list == ByteCode.NOP) {
      return codeadd(ByteCode.LHEAD, value, ByteCode.LNULL, ByteCode.LNULL);
    }

    var head = codeArray[list];

    assert head == ByteCode.LHEAD : head;

    var next = codeadd(ByteCode.LNEXT, value, ByteCode.LNULL);

    var last = codeArray[list + 3];

    var target = last != ByteCode.LNULL ? last : list;

    codeArray[target + 2] = next;

    codeArray[list + 3] = next;

    return list;
  }

  final boolean lnext() {
    if (code == ByteCode.LHEAD) {
      codeadv();

      codepsh();

      return true;
    }

    if (code == ByteCode.LNEXT) {
      throw new UnsupportedOperationException("Implement me");
    }

    if (code == ByteCode.LNULL) {
      throw new UnsupportedOperationException("Implement me");
    }

    codepop();

    codeadv();

    if (code == ByteCode.LNULL) {
      return false;
    }

    codeIndex = code;

    codeadv();

    codeass(ByteCode.LNEXT);

    codeadv();

    codepsh();

    return true;
  }

  final void markIncrement() {
    markArray[markIndex]++;
  }

  final void markPush() {
    markIndex++;

    markArray = IntArrays.growIfNecessary(markArray, markIndex);

    markArray[markIndex] = 0;
  }

  final void object(int type, Object value) {
    elementAdd(protoIndex);

    protoAdd(type, objectAdd(value));
  }

  final Object objget(int index) {
    return objectArray[index];
  }

  final void protoAdd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex);

    protoArray[protoIndex++] = v0;
  }

  final void protoAdd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  final int protoadv() {
    return proto = protoArray[protoIndex++];
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
    protoIndex = stackArray[--stackIndex];
  }

  final void protopsh() {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex++] = protoIndex;

    protoIndex = proto;

    protoadv();
  }

  final UnsupportedOperationException protouoe() {
    return new UnsupportedOperationException("Implement me :: proto=" + proto);
  }

  final int setOrReplace(int pointer, int value) {
    if (pointer == ByteCode.NOP) {
      return value;
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  final int setOrThrow(int index, int value) {
    if (index == ByteCode.NOP) {
      return value;
    }

    throw new UnsupportedOperationException("Implement me");
  }

  private int objectAdd(Object value) {
    int result = objectIndex;

    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    objectArray[objectIndex++] = value;

    return result;
  }

}