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
import objectos.util.ObjectArrays;

abstract class Pass0Super {

  final Pass0AutoImports autoImports = new AutoImports();

  int[] elementArray = new int[10];

  int elementIndex;

  int[] lambdaArray = new int[10];

  int lambdaIndex;

  int[] markArray = new int[10];

  int markIndex;

  Object[] objectArray = new Object[10];

  int objectIndex;

  int[] protoArray = new int[10];

  int protoIndex;

  final void element(int type) {
    var length = markArray[markIndex--];

    var start = elementIndex - length;

    var mark = protoIndex;

    protoAdd(type);

    for (int i = start; i < elementIndex; i++) {
      protoAdd(ByteProto.JMP, elementArray[i]);
    }

    protoAdd(ByteProto.BREAK);

    elementIndex = start;

    elementAdd(mark);
  }

  final void elementAdd(int value) {
    elementArray = IntArrays.growIfNecessary(elementArray, elementIndex);

    elementArray[elementIndex++] = value;
  }

  final void lambdaCount() {
    markArray[markIndex] += lambdaArray[lambdaIndex--];
  }

  final void lambdaPop() {
    var startCount = lambdaArray[lambdaIndex];

    var diff = elementIndex - startCount;

    lambdaArray[lambdaIndex] = diff;
  }

  final void lambdaPush() {
    lambdaIndex++;

    lambdaArray = IntArrays.growIfNecessary(lambdaArray, lambdaIndex);

    lambdaArray[lambdaIndex] = elementIndex;
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

  final void protoAdd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex);

    protoArray[protoIndex++] = v0;
  }

  final void protoAdd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);

    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  private int objectAdd(Object value) {
    int result = objectIndex;

    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    objectArray[objectIndex++] = value;

    return result;
  }

}