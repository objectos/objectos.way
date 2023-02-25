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

import objectos.code.JavaTemplate._Item;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class InternalApi {

  static final int NULL = Integer.MIN_VALUE;

  private static final int LOCAL = -1;
  private static final int EXT = -2;
  private static final int LAMBDA = -3;
  private static final int LTAIL = -4;

  final AutoImports autoImports = new AutoImports();

  int[] codeArray = new int[128];

  int codeIndex;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  int[] stackArray = new int[10];

  int stackIndex;

  final void accept(JavaTemplate template) {
    autoImports.clear();

    stackIndex = -1;

    codeIndex = objectIndex = protoIndex = 0;

    template.execute(this);

    int self = protoIndex;

    for (int i = 0; i < codeIndex;) {
      int kind = codeArray[i++];

      if (kind == LOCAL) {
        int protoIndex = codeArray[i++];

        int proto = protoGet(protoIndex++);

        protoAdd(proto, protoIndex);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: code=" + kind
        );
      }
    }

    protoAdd(ByteProto.END_ELEMENT);

    stackIndex = protoIndex;

    protoIndex = self;
  }

  final void arrayTypeName(Class<?> type) {
    int dimCount = 1;

    var componentType = type.getComponentType();

    for (;;) {
      var next = componentType.getComponentType();

      if (next == null) {
        break;
      }

      dimCount++;

      componentType = next;
    }

    classType(componentType);

    for (int i = 0; i < dimCount; i++) {
      itemAdd(ByteProto.ARRAY_DIMENSION, ByteProto.NOOP);
    }

    arrayTypeName(dimCount);
  }

  final _Item arrayTypeName(int dimCount) {
    elemCnt(ByteProto.ARRAY_TYPE, 1 + dimCount);

    elemItem(JavaTemplate.ITEM);

    for (int i = 0; i < dimCount; i++) {
      elemItem(JavaTemplate.ITEM);
    }

    return elemRet();
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

    localStart();

    protoAdd(ByteProto.CLASS_TYPE, object(packageName), names);

    for (var index = first; index >= last; index--) {
      var simpleName = objectArray[index];

      protoAdd(object(simpleName));
    }

    return JavaTemplate.ITEM;
  }

  final _Item elem(int proto) {
    int count = 0;
    elemCnt(proto, count);
    return elemRet();
  }

  final _Item elem(int proto, Object e1) {
    int count = 0;
    count += elemPre(e1);
    elemCnt(proto, count);
    elemItem(e1);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2) {
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    elemCnt(proto, count);
    elemItem(e1);
    elemItem(e2);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3) {
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    count += elemPre(e3);
    elemCnt(proto, count);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4) {
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    count += elemPre(e3);
    count += elemPre(e4);
    elemCnt(proto, count);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5) {
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    count += elemPre(e3);
    count += elemPre(e4);
    count += elemPre(e5);
    elemCnt(proto, count);
    elemItem(e1);
    elemItem(e2);
    elemItem(e3);
    elemItem(e4);
    elemItem(e5);
    return elemRet();
  }

  final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6) {
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    count += elemPre(e3);
    count += elemPre(e4);
    count += elemPre(e5);
    count += elemPre(e6);
    elemCnt(proto, count);
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
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    count += elemPre(e3);
    count += elemPre(e4);
    count += elemPre(e5);
    count += elemPre(e6);
    count += elemPre(e7);
    elemCnt(proto, count);
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
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    count += elemPre(e3);
    count += elemPre(e4);
    count += elemPre(e5);
    count += elemPre(e6);
    count += elemPre(e7);
    count += elemPre(e8);
    elemCnt(proto, count);
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
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    count += elemPre(e3);
    count += elemPre(e4);
    count += elemPre(e5);
    count += elemPre(e6);
    count += elemPre(e7);
    count += elemPre(e8);
    count += elemPre(e9);
    elemCnt(proto, count);
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
    int count = 0;
    count += elemPre(e1);
    count += elemPre(e2);
    count += elemPre(e3);
    count += elemPre(e4);
    count += elemPre(e5);
    count += elemPre(e6);
    count += elemPre(e7);
    count += elemPre(e8);
    count += elemPre(e9);
    count += elemPre(e10);
    elemCnt(proto, count);
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

  final _Item elemMany(int proto, Object first, Object second, Object[] elements) {
    int count = 0;

    count += elemPre(first);
    count += elemPre(second);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      count += elemPre(element);
    }

    elemCnt(proto, count);

    elemItem(first);
    elemItem(second);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemItem(element);
    }

    return elemRet();
  }

  final _Item elemMany(int proto, Object first, Object[] elements) {
    int count = 0;

    count += elemPre(first);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      count += elemPre(element);
    }

    elemCnt(proto, count);

    elemItem(first);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemItem(element);
    }

    return elemRet();
  }

  final _Item elemMany(int proto, Object[] elements) {
    int count = 0;

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      count += elemPre(element);
    }

    elemCnt(proto, count);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemItem(element);
    }

    return elemRet();
  }

  final void externalToLocal() {
    codeArray[codeIndex - 2] = LOCAL;
  }

  final void extStart() {
    levelAdd(EXT, protoIndex);
  }

  final void identifierext(String value) {
    levelAdd(EXT, protoIndex);
    protoAdd(ByteProto.IDENTIFIER, object(value));
  }

  final _Item itemAdd(int v0) {
    localStart();
    protoAdd(v0);
    return JavaTemplate.ITEM;
  }

  final _Item itemAdd(int v0, int v1) {
    localStart();
    protoAdd(v0, v1);
    return JavaTemplate.ITEM;
  }

  final _Item itemAdd(int v0, int v1, int v2) {
    localStart();
    protoAdd(v0, v1, v2);
    return JavaTemplate.ITEM;
  }

  final _Item itemAdd(int v0, int v1, int v2, int v3) {
    localStart();
    protoAdd(v0, v1, v2, v3);
    return JavaTemplate.ITEM;
  }

  final _Item itemAdd(int v0, int v1, int v2, int v3, int v4) {
    localStart();
    protoAdd(v0, v1, v2, v3, v4);
    return JavaTemplate.ITEM;
  }

  final _Item itemEnd() {
    return JavaTemplate.ITEM;
  }

  final void lambdaend() {
    int headIndex = stackPop();

    levelAdd(LTAIL, headIndex);

    codeArray[headIndex + 1] = codeIndex;
  }

  final void lambdastart() {
    // push lambda head
    stackPush(codeIndex);

    levelAdd(LAMBDA, NULL);
  }

  final void localStart() {
    levelAdd(LOCAL, protoIndex);
  }

  final void localToExternal() {
    codeArray[codeIndex - 2] = EXT;
  }

  final int object(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  final void protoAdd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 0);
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

  private void elemCnt(int value, int itemCount) {
    int seenCount = 0;

    int index = codeIndex;

    while (seenCount < itemCount) {
      index -= 2;

      int item = codeArray[index];

      if (item == LOCAL || item == EXT) {
        seenCount++;
      } else if (item == LTAIL) {
        int headIndex = codeArray[index + 1];

        index = headIndex;

        seenCount++;
      } else {
        throw new UnsupportedOperationException("Implement me :: item=" + item);
      }
    }

    int levelStart = index,
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

  private void elemCntx0lambda(int index) {
    // index is at tail index
    // start is first instruction after LAMBDA
    int start = index + 1;

    // index is at tail index
    // max is at lambda tail
    int max = codeArray[index] - 2;

    for (int i = start; i < max;) {
      int code = codeArray[i];

      if (code == LOCAL) {
        i++;

        int levelValue = codeArray[i++];

        int proto = protoGet(levelValue++);

        protoAdd(proto, levelValue);
      } else if (code == LAMBDA) {
        elemCntx0lambda(i);

        i = codeArray[i + 1];
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: code=" + code);
      }
    }
  }

  private void elemItem(Object obj) {
    int offset;
    int kind;

    if (obj instanceof JavaTemplate._Item item) {
      offset = 0;

      kind = LOCAL;
    } else if (
      obj == JavaTemplate.EXT || obj instanceof JavaTemplate.External || obj instanceof String) {
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

    if (kind != LAMBDA) {
      int levelValue = levelGet(index);

      int proto = protoGet(levelValue++);

      protoAdd(proto, levelValue);
    } else {
      elemCntx0lambda(index);
    }

    stackset(offset, index);
  }

  private int elemPre(Object obj) {
    int count = 1;

    if (obj instanceof JavaTemplate.External ext) {
      ext.execute(this);
    } else if (obj instanceof String s) {
      identifierext(s);
    }

    return count;
  }

  private _Item elemRet() {
    /*localIndex = */stackPop();
    /*extIndex = */stackPop();
    /*includeIndex = */stackPop();

    int levelStart = stackPop(),
        self = stackPop();

    codeIndex = levelStart;

    protoAdd(ByteProto.END_ELEMENT);

    levelAdd(LOCAL, self);

    return JavaTemplate.ITEM;
  }

  private void levelAdd(int v0, int v1) {
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex + 1);
    codeArray[codeIndex++] = v0;
    codeArray[codeIndex++] = v1;
  }

  private int levelGet(int index) { return codeArray[index]; }

  private int levelSearch(int index, int condition) {
    for (int i = index; i < codeIndex;) {
      int value = codeArray[i++];

      if (value == LAMBDA && condition != LAMBDA) {
        i = codeArray[i];
      } else if (value == condition) {
        // assuming array was properly assembled
        // there will always be a i+1 index
        return i;
      }
    }

    throw new UnsupportedOperationException(
      "Implement me :: could not find code (index=%d; condition=%d)"
          .formatted(index, condition)
    );
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

  private int stackPeek(int offset) { return stackArray[stackIndex - offset]; }

  private int stackPop() { return stackArray[stackIndex--]; }

  private void stackPush(int v0) {
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