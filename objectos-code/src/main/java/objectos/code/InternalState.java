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

abstract class InternalState {

  final AutoImports autoImports = new AutoImports();

  int code;

  int[] codeArray = new int[10];

  int codeIndex;

  int[] markArray = new int[10];

  int markIndex;

  Object[] objectArray = new Object[10];

  int objectIndex;

  int[] protoArray = new int[10];

  int protoIndex;

  int[] stackArray = new int[10];

  int stackIndex;

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

  final void lambdaEnd() {
    var startCount = stackArray[stackIndex];

    var diff = codeIndex - startCount;

    stackArray[stackIndex] = diff;
  }

  final void lambdaStart() {
    stackIndex++;

    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex);

    stackArray[stackIndex] = codeIndex;
  }

  final void markIncrement() {
    markArray[markIndex]++;
  }

  final void markIncrement(int count) {
    markArray[markIndex] += count;
  }

  final void markStart() {
    markIndex++;

    markArray = IntArrays.growIfNecessary(markArray, markIndex);

    markArray[markIndex] = 0;
  }

  final void object(int type, Object value) {
    elementAdd(protoIndex);

    protoAdd(type, objectAdd(value), ByteProto.OBJECT_END);
  }

  final int objectAdd(Object value) {
    int result = objectIndex;

    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    objectArray[objectIndex++] = value;

    return result;
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

  final void protoAdd(int v0, int v1, int v2) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 2);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
  }

  final void protopop() {
    protoIndex = stackArray[--stackIndex];
  }

}