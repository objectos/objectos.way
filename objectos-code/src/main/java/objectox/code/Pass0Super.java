/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectox.code;

import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

abstract class Pass0Super {

  int[] element = new int[10];

  int elementIndex;

  final ImportSet importSet = new ImportSet();

  Object[] objectArray = new Object[10];

  int objectIndex;

  int[] protoArray = new int[10];

  int protoIndex;

  final void addObject(int type, Object value) {
    elemMark(protoIndex);

    protoAdd(type, objectAdd(value));
  }

  final void element(int type, int length) {
    var start = elementIndex - length;

    var mark = protoIndex;

    protoAdd(type);

    for (int i = start; i < elementIndex; i++) {
      protoAdd(ByteProto.JMP, element[i]);
    }

    protoAdd(ByteProto.BREAK);

    elementIndex = start;

    elemMark(mark);
  }

  final void elemMark(int value) {
    element = IntArrays.growIfNecessary(element, elementIndex);

    element[elementIndex++] = value;
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