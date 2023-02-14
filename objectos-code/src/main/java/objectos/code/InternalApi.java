/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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

import java.util.Arrays;
import objectos.code.JavaTemplate._Item;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class InternalApi {

  private static final int LOCAL = -1;
  private static final int EXT = -2;
  private static final int LAMBDA = -3;

  final AutoImports autoImports = new AutoImports();

  int[] codeArray = new int[128];

  int codeIndex;

  int[][] levelArray = new int[2][];

  int[] levelIndex = new int[2];

  int level;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  int[] stackArray = new int[10];

  int stackIndex;

  private final _Item item;

  public InternalApi() {
    item = new JavaTemplate._Item(this);
  }

  final void accept(JavaTemplate template) {
    autoImports.clear();

    codeIndex = stackIndex = -1;

    level = objectIndex = protoIndex = 0;

    Arrays.fill(levelIndex, -1);

    levelIndex[0] = 0;

    template.execute(this);

    assert level == 0;

    int self = protoIndex;

    int[] array = levelArray[level];

    int length = levelIndex[level];

    for (int i = 0; i < length;) {
      int kind = array[i++];

      if (kind == LOCAL) {
        int protoIndex = array[i++];

        int proto = protoGet(protoIndex++);

        protoAdd(proto, protoIndex);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: code=" + kind
        );
      }
    }

    protoAdd(ByteProto.END_ELEMENT);

    protoIndex = self;
  }

  final _Item classType(Class<?> type) {
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

    levelAdd(LOCAL, protoIndex);

    protoAdd(ByteProto.CLASS_TYPE, object(packageName), names);

    for (var index = first; index >= last; index--) {
      var simpleName = objectArray[index];

      protoAdd(object(simpleName));
    }

    return item;
  }

  final _Item dotAdd() {
    int[] array = levelArray[level];

    int second = array[--levelIndex[level]];
    --levelIndex[level];

    int first = array[--levelIndex[level]];
    --levelIndex[level];

    return itemAdd(ByteProto.DOT, protoGet(first++), first, protoGet(second++), second);
  }

  final _Item elem(int proto) {
    elemPre();
    elemCnt(proto);
    return elemRet();
  }

  final _Item elem(int proto, Object e1) {
    elemPre();
    elemPre(e1);
    elemCnt(proto);
    elemItem(e1);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemPre(e3);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemPre(e3);
    elemPre(e4);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemPre(e3);
    elemPre(e4);
    elemPre(e5);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    elemItem(e5);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemPre(e3);
    elemPre(e4);
    elemPre(e5);
    elemPre(e6);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    elemItem(e5);
    elemItem(e6);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemPre(e3);
    elemPre(e4);
    elemPre(e5);
    elemPre(e6);
    elemPre(e7);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    elemItem(e5);
    elemItem(e6);
    elemItem(e7);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemPre(e3);
    elemPre(e4);
    elemPre(e5);
    elemPre(e6);
    elemPre(e7);
    elemPre(e8);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    elemItem(e5);
    elemItem(e6);
    elemItem(e7);
    elemItem(e8);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8, Object e9) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemPre(e3);
    elemPre(e4);
    elemPre(e5);
    elemPre(e6);
    elemPre(e7);
    elemPre(e8);
    elemPre(e9);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    elemItem(e5);
    elemItem(e6);
    elemItem(e7);
    elemItem(e8);
    elemItem(e9);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8, Object e9, Object e10) {
    elemPre();
    elemPre(e1);
    elemPre(e2);
    elemPre(e3);
    elemPre(e4);
    elemPre(e5);
    elemPre(e6);
    elemPre(e7);
    elemPre(e8);
    elemPre(e9);
    elemPre(e10);
    elemCnt(proto);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    elemItem(e5);
    elemItem(e6);
    elemItem(e7);
    elemItem(e8);
    elemItem(e9);
    elemItem(e10);
    return elemRet();
  }

  final _Item elemMany(int proto, Object first, Object[] elements) {
    elemPre();

    elemPre(first);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      elemPre(element);
    }

    elemCnt(proto);

    elemItem(first);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemItem(element);
    }

    return elemRet();
  }

  final _Item elemMany(int proto, Object[] elements) {
    elemPre();

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      elemPre(element);
    }

    elemCnt(proto);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemItem(element);
    }

    return elemRet();
  }

  final void identifierext(String value) {
    levelAdd(EXT, protoIndex);
    protoAdd(ByteProto.IDENTIFIER, object(value));
  }

  final _Item itemAdd(int v0) {
    levelAdd(LOCAL, protoIndex);
    protoAdd(v0);
    return item;
  }

  final _Item itemAdd(int v0, int v1) {
    levelAdd(LOCAL, protoIndex);
    protoAdd(v0, v1);
    return item;
  }

  final _Item itemAdd(int v0, int v1, int v2) {
    levelAdd(LOCAL, protoIndex);
    protoAdd(v0, v1, v2);
    return item;
  }

  final _Item itemAdd(int v0, int v1, int v2, int v3) {
    levelAdd(LOCAL, protoIndex);
    protoAdd(v0, v1, v2, v3);
    return item;
  }

  final _Item itemAdd(int v0, int v1, int v2, int v3, int v4) {
    levelAdd(LOCAL, protoIndex);
    protoAdd(v0, v1, v2, v3, v4);
    return item;
  }

  final void lambdaend() {
    levelpop();
  }

  final void lambdastart() {
    levelpush();

    int nextLevel = -1;

    for (int i = 0; i < levelIndex.length; i++) {
      int length = levelIndex[i];

      if (length == -1) {
        nextLevel = i;

        break;
      }
    }

    if (nextLevel < 0) {
      nextLevel = levelIndex.length;
    }

    levelArray = ObjectArrays.growIfNecessary(levelArray, nextLevel);
    levelIndex = IntArrays.growIfNecessary(levelIndex, nextLevel);

    levelAdd(LAMBDA, nextLevel);

    level = nextLevel;

    levelIndex[level] = 0;
  }

  final int levelRemove() {
    int[] array = levelArray[level];

    int value = array[--levelIndex[level]];

    --levelIndex[level];

    return value;
  }

  final int object(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  final void protoAdd(int v0, int v1, int v2) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 2);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
  }

  private void elemCnt(int value) {
    int itemCount = stackPop(),
        levelStart = levelIndex[level] - (itemCount * 2),
        localIndex = levelStart,
        extIndex = levelStart,
        includeIndex = levelStart;

    stackPush(
      /*4*/protoIndex,
      /*3*/levelStart,
      /*2*/includeIndex,
      /*1*/extIndex,
      /*0*/localIndex
    );

    protoAdd(value);
  }

  private void elemCntx0lambda(int level) {
    int[] array = levelArray[level];

    int length = levelIndex[level];

    for (int i = 0; i < length;) {
      int code = array[i++];

      int levelValue = array[i++];

      if (code == LOCAL) {
        int proto = protoGet(levelValue++);

        protoAdd(proto, levelValue);
      } else if (code == LAMBDA) {
        elemCntx0lambda(levelValue);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: code=" + code);
      }
    }

    levelIndex[level] = -1;
  }

  private void elemItem(Object obj) {
    int offset;
    int kind;

    if (obj == item) {
      offset = 0;

      kind = LOCAL;
    } else if (obj == JavaTemplate.EXT) {
      offset = 1;

      kind = EXT;
    } else if (obj == JavaTemplate.INCLUDE) {
      offset = 2;

      kind = LAMBDA;
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: obj=" + obj);
    }

    int index = stackPeek(offset);

    index = levelSearch(index, kind);

    int levelValue = levelGet(index);

    if (kind != LAMBDA) {
      int proto = protoGet(levelValue++);

      protoAdd(proto, levelValue);
    } else {
      elemCntx0lambda(levelValue);
    }

    stackset(offset, index);
  }

  private void elemPre() {
    stackpush(0);
  }

  private void elemPre(Object obj) {
    stackinc(0);
  }

  private _Item elemRet() {
    /*localIndex = */stackPop();
    /*extIndex = */stackPop();
    /*includeIndex = */stackPop();

    int levelStart = stackPop(),
        self = stackPop();

    levelIndex[level] = levelStart;

    protoAdd(ByteProto.END_ELEMENT);

    levelAdd(LOCAL, self);

    return item;
  }

  private void levelAdd(int v0, int v1) {
    levelArray = ObjectArrays.growIfNecessary(levelArray, level);

    if (levelArray[level] == null) {
      levelArray[level] = new int[64];
    }

    levelArray[level] = IntArrays.growIfNecessary(levelArray[level], levelIndex[level] + 1);
    levelArray[level][levelIndex[level]++] = v0;
    levelArray[level][levelIndex[level]++] = v1;
  }

  private int levelGet(int index) { return levelArray[level][index]; }

  private void levelpop() { level = codeArray[codeIndex--]; }

  private void levelpush() {
    codeIndex++;
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex);
    codeArray[codeIndex] = level;
  }

  private int levelSearch(int index, int condition) {
    int[] array = levelArray[level];
    int length = levelIndex[level];

    for (int i = index; i < length; i++) {
      int value = array[i];

      if (value == condition) {
        // assuming array was properly assembled
        // there will always be a i+1 index
        return i + 1;
      }
    }

    throw new UnsupportedOperationException(
      "Implement me :: could not find code (index=%d; condition=%d)"
          .formatted(index, condition)
    );
  }

  private void protoAdd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 0);
    protoArray[protoIndex++] = v0;
  }

  private void protoAdd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  private void protoAdd(int v0, int v1, int v2, int v3) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 3);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
  }

  private void protoAdd(int v0, int v1, int v2, int v3, int v4) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 4);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
    protoArray[protoIndex++] = v4;
  }

  private int protoGet(int index) { return protoArray[index]; }

  private void stackinc(int offset) { stackArray[stackIndex - offset]++; }

  private int stackPeek(int offset) { return stackArray[stackIndex - offset]; }

  private int stackPop() { return stackArray[stackIndex--]; }

  private void stackpush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);

    stackArray[++stackIndex] = v0;
  }

  private void stackPush(int v0, int v1, int v2, int v3, int v4) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 5);

    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
    stackArray[++stackIndex] = v2;
    stackArray[++stackIndex] = v3;
    stackArray[++stackIndex] = v4;
  }

  private void stackset(int offset, int value) { stackArray[stackIndex - offset] = value; }

}