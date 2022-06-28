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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableListTest {

  private static final int MANY = 100;

  private static final int HALF = MANY / 2;

  private GrowableList<Thing> it;

  @BeforeMethod
  public void _beforeMethod() {
    it = new GrowableList<>();
  }

  @Test
  public void add() {
    // empty
    assertEquals(it.size(), 0);

    // one
    var t1 = Thing.next();

    assertTrue(it.add(t1));
    assertContents(t1);

    // two
    var t2 = Thing.next();

    assertTrue(it.add(t2));
    assertContents(t1, t2);

    // many
    var array = Thing.randomArray(MANY);

    for (int i = 0; i < array.length; i++) {
      var t = array[i];

      assertTrue(it.add(t));
    }

    assertContents(t1, t2, array);

    // must reject null
    try {
      Thing t = null;

      it.add(t);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "e == null");
    }
  }

  @Test
  public void addAll() {
    // empty
    assertEquals(it.size(), 0);

    assertFalse(it.addAll(Thing.EMPTY_LIST));

    assertEquals(it.size(), 0);

    // one
    var t1 = Thing.next();

    it.addAll(List.of(t1));
    assertContents(t1);

    // two
    var t2 = Thing.next();

    var t2Deque = new ArrayDeque<Thing>();
    t2Deque.add(t2);

    it.addAll(t2Deque);
    assertContents(t1, t2);

    // many
    var arrayList = Thing.randomArrayList(MANY);

    it.addAll(arrayList);
    assertContents(t1, t2, arrayList);

    var arrayDeque = Thing.randomArrayDeque(MANY);

    it.addAll(arrayDeque);
    assertContents(t1, t2, arrayList, arrayDeque);

    // must reject null
    var listWithNull = Thing.randomArrayList(MANY);

    listWithNull.set(HALF, null);

    try {
      it.addAll(listWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "collection[50] == null");
    }

    var sub = listWithNull.subList(0, 50);

    assertContents(t1, t2, arrayList, arrayDeque, sub);

    try {
      var notRandomAccessWithNull = new LinkedList<Thing>();

      notRandomAccessWithNull.addAll(listWithNull);

      it.addAll(notRandomAccessWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "collection[50] == null");
    }

    assertContents(t1, t2, arrayList, arrayDeque, sub, sub);
  }

  @Test
  public void addAllIterable() {
    // empty
    assertEquals(it.size(), 0);

    assertFalse(it.addAllIterable(Thing.EMPTY_ITERABLE));
    assertContents();

    assertFalse(it.addAllIterable(Thing.EMPTY_LIST));
    assertContents();

    // one
    var t1 = Thing.next();

    it.addAllIterable(ArrayBackedIterable.of(t1));
    assertContents(t1);

    // two
    var t2 = Thing.next();

    it.addAllIterable(List.of(t2));
    assertContents(t1, t2);

    // many
    var iterable = Thing.randomIterable(MANY);

    it.addAllIterable(iterable);
    assertContents(t1, t2, iterable);

    var arrayList = Thing.randomArrayList(MANY);

    it.addAllIterable(arrayList);
    assertContents(t1, t2, iterable, arrayList);

    // must reject null
    var arrayWithNull = Thing.randomArray(MANY);

    arrayWithNull[HALF] = null;

    var iterWithNull = new ArrayBackedIterable<>(arrayWithNull);

    try {
      it.addAllIterable(iterWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "iterable[50] == null");
    }

    var copy = Arrays.copyOf(arrayWithNull, HALF);

    var sub = List.of(copy);

    assertContents(t1, t2, iterable, arrayList, sub);
  }

  private void assertContents(Object... expected) {
    int i = 0;

    for (Object o : expected) {
      if (o instanceof Thing t) {
        assertEquals(it.get(i++), t);
      } else if (o instanceof Thing[] arr) {
        for (var t : arr) {
          assertEquals(it.get(i++), t);
        }
      } else if (o instanceof Iterable<?> iter) {
        for (var t : iter) {
          assertEquals(it.get(i++), t);
        }
      } else {
        throw new UnsupportedOperationException("Implement me: " + o.getClass());
      }
    }

    assertEquals(it.size(), i);
  }

}