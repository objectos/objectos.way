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
import objectos.code2.JavaModel.ExpressionName;
import objectos.code2.JavaModel.QualifiedMethodInvocation;
import objectos.code2.JavaModel.UnqualifiedMethodInvocation;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class InternalApi {

  AutoImportsHey autoImports = new AutoImportsHey();

  int[] codeArray = new int[128];

  int codeIndex;

  int[] itemArray = new int[256];

  int itemIndex;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] rootArray = new int[64];

  int rootIndex;

  public final int count(Object e) {
    if (e == JavaModel.INCLUDE) {
      return codeArray[codeIndex--];
    } else {
      return 1;
    }
  }

  public final JavaModel._Elem elem(int proto, int count) {
    int self = itemIndex;

    itemadd(proto, count);

    rootcopy(count);

    rootadd(self);

    return elemret();
  }

  public final ExpressionName expressionName(String id01) {
    int xd01 = identifier(id01);

    int self = itemIndex;

    itemadd(ByteProto.EXPRESSION_NAME, 1);

    itemadd(xd01);

    rootadd(self);

    return elemret();
  }

  public final ExpressionName expressionName(String id01, String id02) {
    int xd01 = identifier(id01);
    int xd02 = identifier(id02);

    int self = itemIndex;

    itemadd(ByteProto.EXPRESSION_NAME, 2);

    itemadd(xd01, xd02);

    rootadd(self);

    return elemret();
  }

  public final ExpressionName expressionName(String id01, String id02, String id03) {
    int xd01 = identifier(id01);
    int xd02 = identifier(id02);
    int xd03 = identifier(id03);

    int self = itemIndex;

    itemadd(ByteProto.EXPRESSION_NAME, 3);

    $itemadd(xd01, xd02, xd03);

    rootadd(self);

    return elemret();
  }

  public final ExpressionName expressionNameClassType(String id01) {
    int xd01 = identifier(id01);

    int self = itemIndex;

    itemadd(ByteProto.EXPRESSION_NAME, 2);

    rootcopy(1);

    itemadd(xd01);

    rootadd(self);

    return elemret();
  }

  public final ExpressionName expressionNameClassType(String id01, String id02) {
    int xd01 = identifier(id01);
    int xd02 = identifier(id02);

    int self = itemIndex;

    itemadd(ByteProto.EXPRESSION_NAME, 3);

    rootcopy(1);

    itemadd(xd01, xd02);

    rootadd(self);

    return elemret();
  }

  public final int identifier(String value) {
    int self = itemIndex;

    itemadd(ByteProto.IDENTIFIER, object(value));

    return self;
  }

  public final void includeEnd() {
    var startCount = codeArray[codeIndex];

    var diff = rootIndex - startCount;

    codeArray[codeIndex] = diff;
  }

  public final void includeStart() {
    codeIndex++;

    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex);

    codeArray[codeIndex] = rootIndex;
  }

  public final UnqualifiedMethodInvocation invoke(int id, int rootCount) {
    int self = itemIndex;

    itemadd(ByteProto.METHOD_INVOCATION, 1 + rootCount);

    itemadd(id);

    rootcopy(rootCount);

    rootadd(self);

    return elemret();
  }

  public final JavaModel._Item item(int v0) {
    rootadd(itemIndex);

    itemadd(v0);

    return itemret();
  }

  public final JavaModel._Item item(int v0, int v1) {
    rootadd(itemIndex);

    itemadd(v0, v1);

    return itemret();
  }

  public final JavaModel._Item item(int v0, int v1, int v2, int v3) {
    rootadd(itemIndex);

    $itemadd(v0, v1, v2, v3);

    return itemret();
  }

  public final JavaModel._Item modifier(Keyword value) {
    rootadd(itemIndex);

    itemadd(ByteProto.MODIFIER, value.ordinal());

    return itemret();
  }

  public final int object(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  public final QualifiedMethodInvocation qualifiedMethodInvocation(int id, int rootCount) {
    int self = itemIndex;

    itemadd(ByteProto.METHOD_INVOCATION_QUALIFIED, 1 + rootCount);

    itemadd(rootArray[rootIndex - rootCount]);

    itemadd(id);

    rootcopy(rootCount - 1);

    rootIndex--;

    rootadd(self);

    return elemret();
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

    rootadd(itemIndex);

    $itemadd(ByteProto.CLASS_TYPE, object(packageName), names);

    for (var index = first; index >= last; index--) {
      var simpleName = objectArray[index];

      itemadd(object(simpleName));
    }

    return itemret();
  }

  final void accept(JavaTemplate template) {
    autoImports.clear();

    codeIndex = -1;

    itemIndex = objectIndex = rootIndex = 0;

    template.execute(this);

    int self = itemIndex;

    int count = rootIndex;

    itemadd(ByteProto.COMPILATION_UNIT, count);

    rootcopy(count);

    itemIndex = self;
  }

  private JavaModel._Elem elemret() {
    return JavaModel.ELEM;
  }

  private void itemadd(int v0) {
    itemArray = IntArrays.growIfNecessary(itemArray, itemIndex + 0);

    itemArray[itemIndex++] = v0;
  }

  private void itemadd(int v0, int v1) {
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

  private JavaModel._Item itemret() {
    return JavaModel.ITEM;
  }

  private void rootadd(int value) {
    rootArray = IntArrays.growIfNecessary(rootArray, rootIndex + 0);

    rootArray[rootIndex++] = value;
  }

  private void rootcopy(int count) {
    if (count > 0) {
      rootIndex -= count;
      int itemMax = itemIndex + count - 1;
      itemArray = IntArrays.growIfNecessary(itemArray, itemMax);
      System.arraycopy(rootArray, rootIndex, itemArray, itemIndex, count);
      itemIndex += count;
    }
  }

}