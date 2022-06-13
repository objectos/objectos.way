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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

public abstract class AbstractObjectosSetsTest {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  Iterable<Thing> emptyThingIterable;

  List<Thing> emptyThingList;

  Thing t1;

  Thing t2;

  Thing[] thingArray;

  Iterable<Thing> thingIterable;

  List<Thing> thingList;

  Set<Thing> thingSet;

  int thingSize;

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

    thingArray = Thing.randomArray(thingSize);

    thingIterable = Thing.randomIterable(thingSize);

    thingList = Thing.randomArrayList(thingSize);

    thingSet = Thing.randomHashSet(thingSize);
  }

  final <E> void assertIterator(UnmodifiableIterator<E> it, Set<E> expected) {
    Set<E> set;
    set = Sets.newHashSet();

    while (it.hasNext()) {
      set.add(it.next());
    }

    try {
      it.next();

      Assert.fail("expected exception was not thrown");
    } catch (NoSuchElementException e) {

    }

    assertSet(set, expected);
  }

  final <E> void assertSet(Set<E> result, Set<E> expected) {
    assertEquals(result.isEmpty(), expected.isEmpty());

    assertEquals(result.size(), expected.size());

    assertTrue(result.containsAll(expected));

    for (E e : expected) {
      assertTrue(result.contains(e));
    }

    Iterator<E> resultIter;
    resultIter = result.iterator();

    while (resultIter.hasNext()) {
      E resultValue;
      resultValue = resultIter.next();

      assertTrue(expected.contains(resultValue));
    }
  }

}