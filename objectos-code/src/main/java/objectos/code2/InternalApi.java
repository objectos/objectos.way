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
package objectos.code2;

import objectos.code2.JavaModel.ClassType;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class InternalApi {

  AutoImportsHey autoImports = new AutoImportsHey();

  int[] itemArray = new int[256];

  int itemIndex;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] rootArray = new int[64];

  int rootIndex;

  public final JavaModel._Elem elem(int proto, int count) {
    int self = itemIndex;

    $itemadd(proto, count);

    $rootcopy(count);

    $rootadd(self);

    return $elemret();
  }

  public final JavaModel._Item item(int v0) {
    $rootadd(itemIndex);

    $itemadd(v0);

    return $itemret();
  }

  public final JavaModel._Item item(int v0, int v1) {
    $rootadd(itemIndex);

    $itemadd(v0, v1);

    return $itemret();
  }

  public final JavaModel._Item item(int v0, int v1, int v2, int v3) {
    $rootadd(itemIndex);

    $itemadd(v0, v1, v2, v3);

    return $itemret();
  }

  public final JavaModel._Item modifier(Keyword value) {
    $rootadd(itemIndex);

    $itemadd(ByteProto.MODIFIER, value.ordinal());

    return $itemret();
  }

  public final int object(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  public final ClassType t(Class<?> type) {
    var last = objectIndex;

    while (true) {
      var simpleName = type.getSimpleName(); // implicit null-check

      object(simpleName);

      var outer = type.getEnclosingClass();

      if (outer == null) {
        break;
      } else {
        type = outer;
      }
    }

    var first = objectIndex - 1;

    var names = objectIndex - last;

    var packageName = type.getPackageName();

    $rootadd(itemIndex);

    $itemadd(ByteProto.CLASS_TYPE, object(packageName), names);

    for (var index = first; index >= last; index--) {
      var simpleName = objectArray[index];

      $itemadd(object(simpleName));
    }

    return $itemret();
  }

  final void accept(JavaTemplate template) {
    autoImports.clear();

    itemIndex = objectIndex = rootIndex = 0;

    template.execute(this);

    int self = itemIndex;

    int count = rootIndex;

    $itemadd(ByteProto.COMPILATION_UNIT, count);

    $rootcopy(count);

    itemIndex = self;
  }

  private JavaModel._Elem $elemret() {
    return JavaModel.ELEM;
  }

  private void $itemadd(int v0) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 0);

    itemArray[itemIndex++] = v0;
  }

  private void $itemadd(int v0, int v1) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 1);

    itemArray[itemIndex++] = v0;
    itemArray[itemIndex++] = v1;
  }

  private void $itemadd(int v0, int v1, int v2) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 2);

    itemArray[itemIndex++] = v0;
    itemArray[itemIndex++] = v1;
    itemArray[itemIndex++] = v2;
  }

  private void $itemadd(int v0, int v1, int v2, int v3) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 3);

    itemArray[itemIndex++] = v0;
    itemArray[itemIndex++] = v1;
    itemArray[itemIndex++] = v2;
    itemArray[itemIndex++] = v3;
  }

  private JavaModel._Item $itemret() {
    return JavaModel.ITEM;
  }

  private void $rootadd(int value) {
    rootArray = IntArrays.growIfNecessary(rootArray, rootIndex + 0);

    rootArray[rootIndex++] = value;
  }

  private void $rootcopy(int count) {
    if (count > 0) {
      rootIndex -= count;
      int itemMax = itemIndex + count - 1;
      itemArray = IntArrays.growIfNecessary(itemArray, itemMax);
      System.arraycopy(rootArray, rootIndex, itemArray, itemIndex, count);
      itemIndex += count;
    }
  }

}