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

  private int[] elements = new int[10];

  private int elementsIndex;

  private final ImportSet importSet = new ImportSet();

  private Object[] objects = new Object[10];

  private int objectsIndex;

  private int[] protos = new int[10];

  private int protosCursor;

  public final ImportSet importSet() { return importSet; }

  public final Object[] objects() {
    return Arrays.copyOf(objects, objectsIndex);
  }

  public final int[] protos() {
    return Arrays.copyOf(protos, protosCursor);
  }

  final void autoImports() {
    importSet.enable();
  }

  final void elementadd(int type, int length) {
    var start = elementsIndex - length;

    var mark = protosCursor;

    protoadd(type, length);

    for (int i = start; i < elementsIndex; i++) {
      protoadd(elements[i]);
    }

    elementsIndex = start;

    elementmark(mark);
  }

  final void objectadd(int type, Object value) {
    elementmark(protosCursor);

    protoadd(type, objectadd(value));
  }

  final void packagename(String packageName) {
    importSet.packageName(packageName);
  }

  final void pass0end() {
    elementadd(ByteProto.COMPILATION_UNIT, elementsIndex);

    if (elementsIndex != 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    protos[1] = elements[0];
  }

  final void pass0start() {
    protoadd(ByteProto.JMP, ByteProto.NULL);
  }

  final State reset() {
    elementsIndex = 0;

    importSet.clear();

    objectsIndex = 0;

    protosCursor = 0;

    protoadd(ByteProto.JMP, ByteProto.NULL);

    return this;
  }

  final void typenameadd(TypeName typeName) {
    typeName.acceptClassNameSet(importSet);
  }

  private void elementmark(int value) {
    elements = IntArrays.growIfNecessary(elements, elementsIndex);

    elements[elementsIndex++] = value;
  }

  private int objectadd(Object value) {
    int result = objectsIndex;

    objects = ObjectArrays.growIfNecessary(objects, objectsIndex);

    objects[objectsIndex++] = value;

    return result;
  }

  private void protoadd(int v0) {
    protos = IntArrays.growIfNecessary(protos, protosCursor);

    protos[protosCursor++] = v0;
  }

  private void protoadd(int v0, int v1) {
    protos = IntArrays.growIfNecessary(protos, protosCursor + 1);

    protos[protosCursor++] = v0;
    protos[protosCursor++] = v1;
  }

}