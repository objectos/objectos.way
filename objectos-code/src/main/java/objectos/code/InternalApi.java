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

  int code;

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

  private JavaTemplate template;

  public final _Item chain(int proto) {
    int second = levelremove();
    int first = levelremove();
    return itemadd(proto, first, second);
  }

  public final _Item classType(Class<?> type) {
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

    leveladd(LOCAL, protoIndex);

    protoadd(ByteProto.CLASS_TYPE, object(packageName), names);

    for (var index = first; index >= last; index--) {
      var simpleName = objectArray[index];

      protoadd(object(simpleName));
    }

    return template.item;
  }

  public final _Item elem(int proto) {
    elempre();
    elemcnt(proto);
    return elemret();
  }

  public final _Item elem(int proto, Object e1) {
    elempre();
    elempre(e1);
    elemcnt(proto);
    elemitem(e1);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2) {
    elempre();
    elempre(e1);
    elempre(e2);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2, Object e3) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elempre(e7);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
    elemitem(e7);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elempre(e7);
    elempre(e8);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
    elemitem(e7);
    elemitem(e8);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8, Object e9) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elempre(e7);
    elempre(e8);
    elempre(e9);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
    elemitem(e7);
    elemitem(e8);
    elemitem(e9);
    return elemret();
  }

  public final _Item elem(int proto, Object e1, Object e2, Object e3, Object e4,
      Object e5, Object e6, Object e7, Object e8, Object e9, Object e10) {
    elempre();
    elempre(e1);
    elempre(e2);
    elempre(e3);
    elempre(e4);
    elempre(e5);
    elempre(e6);
    elempre(e7);
    elempre(e8);
    elempre(e9);
    elempre(e10);
    elemcnt(proto);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
    elemitem(e7);
    elemitem(e8);
    elemitem(e9);
    elemitem(e10);
    return elemret();
  }

  public final void elemargs() {
    protoadd(0);
  }

  public final void elemargs(Object e1) {
    protoadd(1);
    elemitem(e1);
  }

  public final void elemargs(Object e1, Object e2) {
    protoadd(2);
    elemitem(e1);
    elemitem(e2);
  }

  public final void elemargs(Object e1, Object e2, Object e3) {
    protoadd(3);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
  }

  public final void elemargs(Object e1, Object e2, Object e3, Object e4) {
    protoadd(4);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
  }

  public final void elemargs(Object e1, Object e2, Object e3, Object e4, Object e5) {
    protoadd(5);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
  }

  public final void elemargs(Object e1, Object e2, Object e3, Object e4, Object e5,
      Object e6) {
    protoadd(6);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
  }

  public final void elemargs(Object e1, Object e2, Object e3, Object e4, Object e5,
      Object e6, Object e7) {
    protoadd(7);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
    elemitem(e7);
  }

  public final void elemargs(Object e1, Object e2, Object e3, Object e4, Object e5,
      Object e6, Object e7, Object e8) {
    protoadd(8);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
    elemitem(e7);
    elemitem(e8);
  }

  public final void elemargs(Object e1, Object e2, Object e3, Object e4, Object e5,
      Object e6, Object e7, Object e8, Object e9) {
    protoadd(9);
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
    elemitem(e4);
    elemitem(e5);
    elemitem(e6);
    elemitem(e7);
    elemitem(e8);
    elemitem(e9);
  }

  public final _Item elemend() {
    /*localIndex = */stackpop();
    /*extIndex = */stackpop();
    /*includeIndex = */stackpop();

    int levelStart = stackpop(),
        self = stackpop();

    levelIndex[level] = levelStart;

    leveladd(LOCAL, self);

    return template.item;
  }

  public final void elemitem(Object obj) {
    if (obj == template.item) {
      int localIndex = stackpeek(0);

      localIndex = levelsearch(localIndex, LOCAL);

      localIndex++;

      int value = levelget(localIndex);

      protoadd(value);

      stackset(0, localIndex);
    } else if (obj == JavaTemplate.EXT) {
      int extIndex = stackpeek(1);

      extIndex = levelsearch(extIndex, EXT);

      extIndex++;

      int value = levelget(extIndex);

      protoadd(value);

      stackset(1, extIndex);
    } else if (obj == JavaTemplate.INCLUDE) {
      int codeIndex = stackpeek(2);

      codeIndex = levelsearch(codeIndex, LAMBDA);

      codeIndex++;

      int level = levelget(codeIndex);

      elemcntx0lambda(level);

      stackset(2, codeIndex);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: obj=" + obj);
    }
  }

  public final void elemitem(Object e1, Object e2) {
    elemitem(e1);
    elemitem(e2);
  }

  public final void elemitem(Object e1, Object e2, Object e3) {
    elemitem(e1);
    elemitem(e2);
    elemitem(e3);
  }

  public final _Item elemmany(int proto, Object first, Object[] elements) {
    elempre();

    elempre(first);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      elempre(element);
    }

    elemcnt(proto);

    elemitem(first);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemitem(element);
    }

    return elemret();
  }

  public final _Item elemmany(int proto, Object[] elements) {
    elempre();

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      Check.notNull(element, "elements[", i, "] == null");
      elempre(element);
    }

    elemcnt(proto);

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];
      elemitem(element);
    }

    return elemret();
  }

  public final void elemproto(int value) {
    protoadd(value);
  }

  public final void elemstart(int proto) {
    elemstart(proto, 0);
  }

  public final void elemstart(int proto, Object e1) {
    elemstart(proto, 1);
  }

  public final void elemstart(int proto, Object e1, Object e2) {
    elemstart(proto, 2);
  }

  public final void elemstart(int proto, Object e1, Object e2, Object e3) {
    elemstart(proto, 3);
  }

  public final void elemstart(int proto, Object e1, Object e2, Object e3, Object e4) {
    elemstart(proto, 4);
  }

  public final void elemstart(int proto, Object e1, Object e2, Object e3, Object e4, Object e5) {
    elemstart(proto, 5);
  }

  public final void elemstart(int proto, Object e1, Object e2, Object e3, Object e4, Object e5,
      Object e6) {
    elemstart(proto, 6);
  }

  public final void elemstart(int proto, Object e1, Object e2, Object e3, Object e4, Object e5,
      Object e6, Object e7) {
    elemstart(proto, 7);
  }

  public final void elemstart(int proto, Object e1, Object e2, Object e3, Object e4, Object e5,
      Object e6, Object e7, Object e8) {
    elemstart(proto, 8);
  }

  public final void elemstart(int proto, Object e1, Object e2, Object e3, Object e4, Object e5,
      Object e6, Object e7, Object e8, Object e9) {
    elemstart(proto, 9);
  }

  public final void identifierext(String value) {
    leveladd(EXT, protoIndex);

    protoadd(ByteProto.IDENTIFIER, object(value));
  }

  public final _Item itemadd(int v0) {
    leveladd(LOCAL, protoIndex);
    protoadd(v0);
    return template.item;
  }

  public final _Item itemadd(int v0, int v1) {
    leveladd(LOCAL, protoIndex);
    protoadd(v0, v1);
    return template.item;
  }

  public final _Item itemadd(int v0, int v1, int v2) {
    leveladd(LOCAL, protoIndex);
    protoadd(v0, v1, v2);
    return template.item;
  }

  public final _Item itemadd(int v0, int v1, int v2, int v3) {
    leveladd(LOCAL, protoIndex);
    protoadd(v0, v1, v2, v3);
    return template.item;
  }

  public final _Item itemadd(int v0, int v1, int v2, int v3, int v4) {
    leveladd(LOCAL, protoIndex);
    protoadd(v0, v1, v2, v3, v4);
    return template.item;
  }

  public final void lambdaend() {
    levelpop();
  }

  public final void lambdastart() {
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

    levelIndex = IntArrays.growIfNecessary(levelIndex, nextLevel);

    leveladd(LAMBDA, nextLevel);

    level = nextLevel;

    levelIndex[level] = 0;
  }

  public final int levelremove() {
    int[] array = levelArray[level];
    int value = array[--levelIndex[level]];
    --levelIndex[level];
    return value;
  }

  public final int object(Object value) {
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    var result = objectIndex;

    objectArray[objectIndex++] = value;

    return result;
  }

  public final int operator(Symbol symbol) {
    int self = protoIndex;
    protoadd(ByteProto.OPERATOR, symbol.ordinal());
    return self;
  }

  public final int protoAt(int index) { return protoArray[index]; }

  final void accept(JavaTemplate template) {
    this.template = template;

    try {
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

      int count = length / 2;

      protoadd(count);

      for (int i = 0; i < length;) {
        int code = array[i++];

        if (code == LOCAL) {
          protoadd(array[i++]);
        } else {
          throw new UnsupportedOperationException(
            "Implement me :: code=" + code
          );
        }
      }

      protoIndex = self;
    } finally {
      this.template = null;
    }
  }

  private void elemcnt(int value) {
    int itemCount = stackpop(),
        levelStart = levelIndex[level] - (itemCount * 2),
        localIndex = levelStart,
        extIndex = levelStart,
        includeIndex = levelStart;

    stackpush(
      /*4*/protoIndex,
      /*3*/levelStart,
      /*2*/includeIndex,
      /*1*/extIndex,
      /*0*/localIndex
    );

    protoadd(value, 0);
  }

  private void elemcntx0lambda(int level) {
    int[] array = levelArray[level];

    int length = levelIndex[level];

    for (int i = 0; i < length;) {
      int code = array[i++];
      int value = array[i++];

      if (code == LOCAL) {
        protoadd(value);
      } else if (code == LAMBDA) {
        elemcntx0lambda(value);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: code=" + code);
      }
    }

    levelIndex[level] = -1;
  }

  private void elempre() {
    stackpush(0);
  }

  private void elempre(Object obj) {
    stackinc(0);
  }

  private _Item elemret() {
    /*localIndex = */stackpop();
    /*extIndex = */stackpop();
    /*includeIndex = */stackpop();

    int levelStart = stackpop(),
        self = stackpop();

    levelIndex[level] = levelStart;

    int itemStart = self + 2;

    int count = protoIndex - itemStart;

    protoArray[self + 1] = count;

    leveladd(LOCAL, self);

    return template.item;
  }

  private void elemstart(int proto, int itemCount) {
    int levelStart = levelIndex[level] - (itemCount * 2),
        localIndex = levelStart,
        extIndex = levelStart,
        includeIndex = levelStart;

    stackpush(
      /*4*/protoIndex,
      /*3*/levelStart,
      /*2*/includeIndex,
      /*1*/extIndex,
      /*0*/localIndex
    );

    protoadd(proto);
  }

  private void leveladd(int v0, int v1) {
    levelArray = ObjectArrays.growIfNecessary(levelArray, level);

    if (levelArray[level] == null) {
      levelArray[level] = new int[64];
    }

    levelArray[level] = IntArrays.growIfNecessary(levelArray[level], levelIndex[level] + 1);
    levelArray[level][levelIndex[level]++] = v0;
    levelArray[level][levelIndex[level]++] = v1;
  }

  private int levelget(int index) { return levelArray[level][index]; }

  private void levelpop() { level = codeArray[codeIndex--]; }

  private void levelpush() {
    codeIndex++;
    codeArray = IntArrays.growIfNecessary(codeArray, codeIndex);
    codeArray[codeIndex] = level;
  }

  private int levelsearch(int index, int condition) {
    int[] array = levelArray[level];
    int length = levelIndex[level];

    for (int i = index; i < length; i++) {
      int value = array[i];

      if (value == condition) {
        // assuming codeArray was properly assembled
        // there will always be a i+1 index
        return i;
      }
    }

    throw new UnsupportedOperationException(
      "Implement me :: could not find code (index=%d; condition=%d)"
          .formatted(index, condition)
    );
  }

  private void protoadd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 0);
    protoArray[protoIndex++] = v0;
  }

  private void protoadd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  private void protoadd(int v0, int v1, int v2) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 2);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
  }

  private void protoadd(int v0, int v1, int v2, int v3) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 3);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
  }

  private void protoadd(int v0, int v1, int v2, int v3, int v4) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 4);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
    protoArray[protoIndex++] = v4;
  }

  private void stackinc(int offset) { stackArray[stackIndex - offset]++; }

  private int stackpeek(int offset) { return stackArray[stackIndex - offset]; }

  private int stackpop() { return stackArray[stackIndex--]; }

  private void stackpush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);

    stackArray[++stackIndex] = v0;
  }

  private void stackpush(int v0, int v1, int v2, int v3, int v4) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 5);

    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
    stackArray[++stackIndex] = v2;
    stackArray[++stackIndex] = v3;
    stackArray[++stackIndex] = v4;
  }

  private void stackset(int offset, int value) { stackArray[stackIndex - offset] = value; }

}