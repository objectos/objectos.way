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

  private int[] elementArray = new int[10];

  private int elementCursor;

  private final ImportSet importSet = new ImportSet();

  private Object[] objectArray = new Object[10];

  private int objectCursor;

  private int[] protoArray = new int[10];

  private int protoCursor;

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

  final State reset() {
    elementCursor = 0;

    importSet.clear();

    objectCursor = 0;

    protoCursor = 0;

    protoadd(ByteProto.JMP, ByteProto.NULL);

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