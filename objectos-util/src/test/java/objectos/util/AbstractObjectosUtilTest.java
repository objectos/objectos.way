/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.testng.annotations.BeforeClass;

public abstract class AbstractObjectosUtilTest {

  static final String LINE_SEPARATOR = System.getProperty("line.separator");

  Iterable<Thing> emptyThingIterable;

  List<Thing> emptyThingList;

  Thing t1;

  Thing t2;

  Thing[] arrayT;

  Iterable<Thing> thingIterable;

  List<Thing> arrayList;

  Set<Thing> hashSet;

  int thingSize;

  ArrayDeque<Thing> arrayDeque;

  static String lines(String... lines) {
    switch (lines.length) {
      case 0:
        return "";
      case 1:
        return lines[0];
      default:
        StringBuilder sb;
        sb = new StringBuilder();

        sb.append(lines[0]);

        for (int i = 1; i < lines.length; i++) {
          sb.append(LINE_SEPARATOR);

          sb.append(lines[i]);
        }

        return sb.toString();
    }
  }

  static ArrayBackedIterable<Integer> randomIntArrayBackedIterable(int size) {
    Integer[] array;
    array = randomIntegerArray(size);

    return new ArrayBackedIterable<Integer>(array);
  }

  static ArrayBackedIterator<Integer> randomIntArrayBackedIterator(int size) {
    Integer[] it;
    it = new Integer[size];

    for (int i = 0; i < size; i++) {
      int randomInt;
      randomInt = Next.intValue();

      it[i] = Integer.valueOf(randomInt);
    }

    return new ArrayBackedIterator<Integer>(it);
  }

  static ArrayList<Integer> randomIntArrayList(int size) {
    Integer box;

    ArrayList<Integer> it;
    it = new ArrayList<>(size);

    for (int i = 0; i < size; i++) {
      int randomInt;
      randomInt = Next.intValue();

      box = Integer.valueOf(randomInt);

      it.add(box);
    }

    return it;
  }

  static Integer[] randomIntegerArray(int size) {
    Integer[] it;
    it = new Integer[size];

    for (int i = 0; i < size; i++) {
      int randomInt;
      randomInt = Next.intValue();

      it[i] = Integer.valueOf(randomInt);
    }

    return it;
  }

  static GrowableList<Integer> randomIntGrowableList(int size) {
    Integer box;

    GrowableList<Integer> it;
    it = new GrowableList<>();

    for (int i = 0; i < size; i++) {
      int randomInt;
      randomInt = Next.intValue();

      box = Integer.valueOf(randomInt);

      it.add(box);
    }

    return it;
  }

  @BeforeClass
  public final void _beforeClassValues() {
    emptyThingIterable = Collections.emptySet();

    emptyThingList = Collections.emptyList();

    t1 = Thing.randomThing();

    t2 = Thing.randomThing();

    while (t2.equals(t1)) {
      System.out.println("t2 equals t1");

      t2 = Thing.randomThing();
    }

    thingSize = 5678;

    arrayT = Thing.randomArray(thingSize);

    thingIterable = Thing.randomIterable(thingSize);

    arrayList = Thing.randomArrayList(thingSize);

    arrayDeque = Thing.randomArrayDeque(thingSize);

    hashSet = Thing.randomHashSet(thingSize);
  }

  @SafeVarargs
  // ordered collection that is not List
  final <E> ArrayDeque<E> arrayDequeOf(E... elements) {
    var deque = new ArrayDeque<E>(elements.length);

    for (E e : elements) {
      deque.add(e);
    }

    return deque;
  }

}