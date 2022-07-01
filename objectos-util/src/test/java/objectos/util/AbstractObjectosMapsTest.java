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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.testng.annotations.BeforeClass;

public abstract class AbstractObjectosMapsTest {

  Thing t1;

  Thing t2;

  Thing t3;

  Thing[] thingArray;

  int thingSize;

  @BeforeClass
  public final void _beforeClassValues() {
    t1 = Thing.next();

    t2 = Thing.next();

    while (t2.equals(t1)) {
      System.out.println("t2 equals t1");

      t2 = Thing.next();
    }

    t3 = Thing.next();

    while (t3.equals(t1) || t3.equals(t2)) {
      System.out.println("t3 equals t2 || t1");

      t3 = Thing.next();
    }

    thingArray = Thing.nextArray();

    thingSize = thingArray.length;
  }

  final <K, V> void assertMap(Map<K, V> result, Map<K, V> expected) {
    assertEquals(result.size(), expected.size());

    assertEquals(result.isEmpty(), expected.isEmpty());

    assertSet(result.keySet(), new HashSet<K>(expected.keySet()));

    assertSet(result.entrySet(), new HashSet<Map.Entry<K, V>>(expected.entrySet()));

    assertCollection(result.values(), expected.values());
  }

  final <K, V> void assertOrderedMap(Map<K, V> result, Map<K, V> expected) {
    assertEquals(result.size(), expected.size());

    assertEquals(result.isEmpty(), expected.isEmpty());

    assertOrderedSet(result.keySet(), new LinkedHashSet<K>(expected.keySet()));

    assertOrderedSet(result.entrySet(), new LinkedHashSet<Map.Entry<K, V>>(expected.entrySet()));

    assertOrderedCollection(result.values(), expected.values());
  }

  final <E> void assertOrderedSet(Set<E> result, Set<E> expected) {
    assertOrderedCollection(result, expected);
  }

  final <E> void assertSet(Set<E> result, Set<E> expected) {
    assertCollection(result, expected);
  }

  private <E> void assertCollection(Collection<E> result, Collection<E> expected) {
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

  private <E> void assertOrderedCollection(Collection<E> result, Collection<E> expected) {
    assertEquals(result.isEmpty(), expected.isEmpty());

    assertEquals(result.size(), expected.size());

    assertTrue(result.containsAll(expected));

    Iterator<E> resultIter;
    resultIter = result.iterator();

    Iterator<E> expectedIter;
    expectedIter = expected.iterator();

    while (expectedIter.hasNext()) {
      E expectedValue;
      expectedValue = expectedIter.next();

      assertTrue(result.contains(expectedValue));

      assertTrue(resultIter.hasNext());

      E resultValue;
      resultValue = resultIter.next();

      assertEquals(resultValue, expectedValue);

      expectedIter.remove();
    }

    assertFalse(resultIter.hasNext());

    assertTrue(expected.isEmpty());
  }

}
