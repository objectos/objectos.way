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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableListTest2 extends AbstractObjectosUtilTest {

  private GrowableList<Thing> it;

  @BeforeMethod
  public void _beforeMethod() {
    it = new GrowableList<>();
  }

  @Test
  public void testCase01() {
    // empty
    assertEquals(it.size(), 0);
    assertTrue(it.isEmpty());

    List<Thing> result;
    result = it.toUnmodifiableList();

    List<Thing> expected;
    expected = new ArrayList<>();

    assertContents(result, expected);

    // one
    assertTrue(it.add(t1));
    assertEquals(it.size(), 1);
    assertFalse(it.isEmpty());

    result = it.toUnmodifiableList();

    expected.clear();
    expected.add(t1);

    assertContents(result, expected);

    // two
    assertTrue(it.add(t1));
    assertEquals(it.size(), 2);
    assertFalse(it.isEmpty());

    result = it.toUnmodifiableList();

    expected.clear();
    expected.add(t1);
    expected.add(t1);

    assertContents(result, expected);

    // N
    int size;
    size = it.size();

    for (Thing thing : arrayT) {
      assertTrue(it.add(thing));

      size++;

      assertEquals(it.size(), size);
      assertFalse(it.isEmpty());
    }

    result = it.toUnmodifiableList();

    expected.clear();
    expected.add(t1);
    expected.add(t1);

    for (Thing thing : arrayT) {
      expected.add(thing);
    }

    assertContents(result, expected);
  }

  @Test
  public void testCase02() {
    // empty
    assertFalse(it.addAllIterable(emptyThingIterable));

    assertFalse(it.addAllIterable(emptyThingList));

    List<Thing> result;
    result = it.toUnmodifiableList();

    List<Thing> expected;
    expected = new ArrayList<>();

    assertContents(result, expected);

    // non empty
    assertTrue(it.addAllIterable(thingIterable));

    assertEquals(it.size(), thingSize);

    assertTrue(it.addAllIterable(arrayList));

    assertEquals(it.size(), thingSize + thingSize);

    result = it.toUnmodifiableList();

    expected.clear();

    for (Thing thing : thingIterable) {
      expected.add(thing);
    }

    expected.addAll(arrayList);

    assertContents(result, expected);
  }

  @Test
  public final void testCase05() {
    // empty
    assertFalse(it.addAll(Collections.<Thing> emptyList()));

    assertFalse(it.addAll(Collections.<Thing> emptySet()));

    UnmodifiableList<Thing> result;
    result = it.toUnmodifiableList();

    List<Thing> expected;
    expected = new ArrayList<>();

    assertContents(result, expected);

    // non empty
    assertTrue(it.addAll(arrayList));

    assertEquals(it.size(), thingSize);

    assertTrue(it.addAll(hashSet));

    assertEquals(it.size(), thingSize + thingSize);

    result = it.toUnmodifiableList();

    expected.clear();

    expected.addAll(arrayList);

    expected.addAll(hashSet);

    assertContents(result, expected);
  }

  private <E> void assertContents(List<E> result, List<E> expected) {
    assertEquals(result.isEmpty(), expected.isEmpty());
    assertEquals(result.size(), expected.size());

    for (int i = 0, size = expected.size(); i < size; i++) {
      E element;
      element = expected.get(i);

      assertTrue(result.contains(element));

      assertEquals(result.get(i), element);
    }

    Iterator<E> resultIter;
    resultIter = result.iterator();

    Iterator<E> expectedIter;
    expectedIter = expected.iterator();

    while (expectedIter.hasNext()) {
      assertTrue(resultIter.hasNext());

      assertEquals(resultIter.next(), expectedIter.next());
    }

    assertFalse(resultIter.hasNext());

  }

}