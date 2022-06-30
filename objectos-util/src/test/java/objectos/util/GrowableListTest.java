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
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
  public void add_withIndex() {
    var t1 = Thing.next();

    try {
      it.add(0, t1);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

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
      assertEquals(expected.getMessage(), "c[50] == null");
    }

    var sub = listWithNull.subList(0, 50);

    assertContents(t1, t2, arrayList, arrayDeque, sub);

    try {
      var notRandomAccessWithNull = new LinkedList<Thing>();

      notRandomAccessWithNull.addAll(listWithNull);

      it.addAll(notRandomAccessWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "c[50] == null");
    }

    assertContents(t1, t2, arrayList, arrayDeque, sub, sub);
  }

  @Test
  public void addAll_withIndex() {
    var arrayList = Thing.randomArrayList(MANY);

    try {
      it.addAll(0, arrayList);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }
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

  @Test
  public void addWithNullMessage() {
    try {
      it.addWithNullMessage(null, "my message");

      Assert.fail("NPE was expected");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "my message");
    }

    try {
      it.addWithNullMessage(null, null);

      Assert.fail("NPE was expected");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "null");
    }

    try {
      it.addWithNullMessage(null, "[", 123, "]");

      Assert.fail("NPE was expected");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "[123]");
    }

    try {
      it.addWithNullMessage(null, null, 123, null);

      Assert.fail("NPE was expected");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "null123null");
    }
  }

  @Test
  public void clear() {
    assertEquals(it.size(), 0);

    it.clear();
    assertContents();

    var arrayList = Thing.randomArrayList(MANY);

    it.addAll(arrayList);
    it.clear();
    assertContents();
  }

  @Test
  public void contains() {
    var t1 = Thing.next();
    var t2 = Thing.next();

    assertEquals(it.size(), 0);

    assertFalse(it.contains(t1));

    assertFalse(it.contains(t1, t2));

    it.add(t1);

    var array = Thing.randomArray(MANY);

    for (var t : array) {
      it.add(t);
    }

    it.add(t2);

    assertTrue(it.contains(t1));

    assertTrue(it.contains(t1, (Object[]) array));

    assertTrue(it.contains(t2));

    it.clear();

    assertFalse(it.contains(t1));

    assertFalse(it.contains(t1, (Object[]) array));

    assertFalse(it.contains(t2));
  }

  @Test
  public void containsAll() {
    var arrayList = Thing.randomArrayList(MANY);

    assertEquals(it.size(), 0);

    assertFalse(it.containsAll(arrayList));

    it.addAll(arrayList);

    assertTrue(it.containsAll(arrayList));

    var list = new ArrayList<Thing>(arrayList.size() + 1);

    var t1 = Thing.next();

    list.add(t1);

    list.addAll(arrayList);

    assertFalse(it.containsAll(list));

    var listWithNull = new ArrayList<Thing>(arrayList.size());

    listWithNull.addAll(arrayList);

    listWithNull.set(HALF, null);

    assertFalse(it.containsAll(listWithNull));
  }

  @Test
  public void equals() {
    var a = new GrowableList<Thing>();

    var b = new GrowableList<Thing>();

    assertTrue(a.equals(b));
    assertTrue(b.equals(a));

    assertFalse(a.equals(null));

    assertTrue(a.equals(Collections.emptyList()));

    var arrayList = Thing.randomArrayList(MANY);

    a.addAll(arrayList);

    var t2 = Thing.next();

    b.addAll(arrayList);
    b.add(t2);

    assertFalse(a.equals(b));

    var c = new GrowableList<Thing>();

    c.addAll(arrayList);

    assertTrue(a.equals(c));
    assertTrue(c.equals(a));
  }

  @Test
  public void get() {
    class Tester {
      public final void add(Thing e) {
        it.add(e);
      }

      public final void get(int index, Thing expected) {
        var value = it.get(index);

        assertEquals(value, expected);
      }

      public final void getOutOfBounds(int index) {
        try {
          it.get(index);

          fail();
        } catch (IndexOutOfBoundsException expected) {

        }
      }
    }

    var tester = new Tester();

    tester.getOutOfBounds(-1);
    tester.getOutOfBounds(0);
    tester.getOutOfBounds(1);

    var t1 = Thing.next();

    tester.add(t1);

    tester.getOutOfBounds(-1);
    tester.get(0, t1);
    tester.getOutOfBounds(1);

    var t2 = Thing.next();

    var t3 = Thing.next();

    tester.add(t2);
    tester.add(t3);

    tester.getOutOfBounds(-1);
    tester.get(0, t1);
    tester.get(1, t2);
    tester.get(2, t3);
    tester.getOutOfBounds(3);
  }

  @Test
  public void getOnly() {
    try {
      it.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: empty.");
    }

    var t1 = Thing.next();

    it.add(t1);

    assertEquals(it.getOnly(), t1);

    var t2 = Thing.next();

    it.add(t2);

    // standard
    try {
      it.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: more than one element.");
    }
  }

  @Test(description = //
  """
  growBy (test case 0)
  ------------------------------

  - start with default capacity
  - simulate add(Collection.size = 1)
  """)
  public void growBy() {
    var length = GrowableList.DEFAULT_CAPACITY;

    length = GrowableList.growBy(length, 1);

    assertEquals(length, 15);

    length = GrowableList.growBy(length, 1);

    assertEquals(length, 22);

    length = GrowableList.growBy(1_197_571_635, 1);

    assertEquals(length, 1_796_357_452);

    length = GrowableList.growBy(length, 1);

    assertEquals(length, MoreArrays.JVM_SOFT_LIMIT);

    try {
      GrowableList.growByOne(MoreArrays.JVM_SOFT_LIMIT);

      Assert.fail();
    } catch (OutOfMemoryError expected) {

    }
  }

  @Test(description = //
  """
  growByOne (test case 0)
  ------------------------------

  - start with default capacity
  - simulate add(E e)
  """)
  public void growByOne() {
    var length = GrowableList.DEFAULT_CAPACITY;

    length = GrowableList.growByOne(length);

    assertEquals(length, 15);

    length = GrowableList.growByOne(length);

    assertEquals(length, 22);

    length = GrowableList.growByOne(1_197_571_635);

    assertEquals(length, 1_796_357_452);

    length = GrowableList.growByOne(length);

    assertEquals(length, MoreArrays.JVM_SOFT_LIMIT);

    try {
      GrowableList.growByOne(MoreArrays.JVM_SOFT_LIMIT);

      Assert.fail();
    } catch (OutOfMemoryError expected) {

    }
  }

  @Test
  public void hashCodeTest() {
    // empty
    assertEquals(it.hashCode(), 1);

    // one
    var t1 = Thing.next();

    it.add(t1);

    var hashCode = (31 + t1.hashCode());

    assertEquals(it.hashCode(), hashCode);

    // two
    var t2 = Thing.next();

    it.add(t2);

    hashCode = 31 * hashCode + t2.hashCode();

    assertEquals(it.hashCode(), hashCode);

    // many
    var arrayList = Thing.randomArrayList(MANY);

    it.addAll(arrayList);

    for (var e : arrayList) {
      hashCode = 31 * hashCode + e.hashCode();
    }

    assertEquals(it.hashCode(), hashCode);
  }

  @Test
  public void indexOf() {
    var t1 = Thing.next();

    assertEquals(it.indexOf(t1), -1);

    it.add(t1);

    assertEquals(it.indexOf(t1), 0);

    it.clear();

    var arrayList = Thing.randomArrayList(MANY);

    it.addAll(arrayList);

    for (int i = 0, size = arrayList.size(); i < size; i++) {
      var t = arrayList.get(i);

      assertEquals(it.indexOf(t), i);
    }

    var index = it.size();

    it.add(t1);

    assertEquals(it.indexOf(t1), index);

    var t2 = Thing.next();

    it.add(t2);

    assertEquals(it.indexOf(t1), index);
  }

  @Test
  public void isEmpty() {
    assertTrue(it.isEmpty());

    var t1 = Thing.next();

    it.add(t1);

    assertFalse(it.isEmpty());

    it.clear();

    assertTrue(it.isEmpty());
  }

  @Test
  public void iterator() {
    var tester = new UnmodifiableIteratorTester<>(it);

    // empty
    assertTrue(it.isEmpty());

    tester.set();
    tester.testNoMoreElements();

    // one
    var t1 = Thing.next();

    it.add(t1);

    tester.set();
    tester.testNext(t1);
    tester.testNoMoreElements();

    // two
    var t2 = Thing.next();

    it.add(t2);

    tester.set();
    tester.testNext(t1);
    tester.testNext(t2);
    tester.testNoMoreElements();

    // many
    var arrayList = Thing.randomArrayList(MANY);

    it.addAll(arrayList);

    tester.set();
    tester.testNext(t1);
    tester.testNext(t2);
    tester.testMany(arrayList);
    tester.testNoMoreElements();
  }

  @Test
  public void join() {
    assertEquals(
      it.join(),
      ""
    );
    assertEquals(
      it.join("|"),
      ""
    );
    assertEquals(
      it.join("|", "{", "}"),
      "{}"
    );

    var t1 = Thing.next();

    it.add(t1);

    assertEquals(
      it.join(),
      t1.toString()
    );
    assertEquals(
      it.join("|"),
      t1.toString()
    );
    assertEquals(
      it.join("|", "{", "}"),
      "{" + t1.toString() + "}"
    );

    it.add(t1);

    assertEquals(
      it.join(),
      t1.toString() + t1.toString()
    );
    assertEquals(
      it.join("|"),
      t1.toString() + "|" + t1.toString()
    );
    assertEquals(
      it.join("|", "{", "}"),
      "{" + t1.toString() + "|" + t1.toString() + "}"
    );
  }

  @Test
  public void lastIndexOf() {
    var t1 = Thing.next();

    assertEquals(it.lastIndexOf(t1), -1);

    it.add(t1);

    assertEquals(it.lastIndexOf(t1), 0);

    it.clear();

    var arrayList = Thing.randomArrayList(MANY);

    it.addAll(arrayList);

    var index = it.size();

    it.add(t1);

    assertEquals(it.lastIndexOf(t1), index);

    var t2 = Thing.next();

    it.add(t2);

    assertEquals(it.lastIndexOf(t1), index);

    it.add(t1);

    assertEquals(it.lastIndexOf(t1), index + 2);
  }

  @Test
  public void listIterator() {
    var arrayList = Thing.randomArrayList(MANY);

    it.addAll(arrayList);

    try {
      it.listIterator();

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }

    try {
      it.listIterator(0);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }
  }

  @Test
  public void remove() {
    var t1 = Thing.next();

    it.add(t1);

    try {
      it.remove(t1);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }

    try {
      it.remove(0);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }
  }

  @Test
  public void removeIf() {
    var t1 = Thing.next();

    it.add(t1);

    try {
      it.removeIf(e -> e.equals(t1));

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }
  }

  @Test
  public void replaceAll() {
    var t1 = Thing.next();
    var t2 = Thing.next();

    it.add(t1);
    it.add(t2);

    try {
      it.replaceAll(t -> Thing.next());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }
  }

  @Test
  public void retainAll() {
    var t1 = Thing.next();
    var t2 = Thing.next();

    it.add(t1);
    it.add(t2);

    try {
      it.retainAll(List.of(t1));

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }
  }

  @Test
  public void set() {
    var t1 = Thing.next();
    var t2 = Thing.next();

    it.add(t1);

    try {
      it.set(0, t2);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {

    }
  }

  @Test
  public void size() {
    assertEquals(it.size(), 0);

    var t1 = Thing.next();

    it.add(t1);

    assertEquals(it.size(), 1);

    it.clear();

    assertEquals(it.size(), 0);
  }

  @Test
  public void toArray() {
    var emptyArray = Thing.EMPTY_ARRAY;

    assertEquals(it.toArray(), new Object[] {});
    assertSame(it.toArray(emptyArray), emptyArray);

    var arrayList = Thing.randomArrayList(MANY);

    var lastNull = new Thing[arrayList.size() + 2];

    var result = it.toArray(lastNull);

    assertSame(result, lastNull);
    assertNull(result[0]);
    assertNull(result[1]);

    var t1 = Thing.next();

    it.add(t1);

    assertEquals(it.toArray(), new Object[] {t1});
    assertEquals(it.toArray(emptyArray), new Thing[] {t1});

    result = it.toArray(lastNull);

    assertSame(result, lastNull);
    assertEquals(result[0], t1);
    assertNull(result[1]);

    it.clear();

    it.addAll(arrayList);

    assertEquals(it.toArray(), arrayList.toArray());
    assertEquals(it.toArray(emptyArray), arrayList.toArray(emptyArray));

    result = it.toArray(lastNull);

    assertSame(result, lastNull);

    int i = 0;

    for (; i < arrayList.size(); i++) {
      assertEquals(result[i], arrayList.get(i));
    }

    assertNull(result[i]);
  }

  @Test
  public void toStringTest() {
    assertEquals(
      it.toString(),

      "GrowableList []"
    );

    var t1 = Thing.parse("7d58452fb817ae98b6d587fe747b87ae");
    var t2 = Thing.parse("402175c4de2f4f4da528112f2121861c");

    it.add(t1);
    it.add(t2);

    assertEquals(
      it.toString(),

      """
      GrowableList [
        0 = Thing [
          value = 7d58452fb817ae98b6d587fe747b87ae
        ]
        1 = Thing [
          value = 402175c4de2f4f4da528112f2121861c
        ]
      ]"""
    );
  }

  @Test
  public void toUnmodifiableList() {
    var result = it.toUnmodifiableList();

    assertTrue(result.isEmpty());

    var t1 = Thing.next();

    it.add(t1);

    assertTrue(result.isEmpty());

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 1);

    assertEquals(result.get(0), t1);

    var t2 = Thing.next();

    it.add(t2);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 2);
    assertEquals(result.get(0), t1);
    assertEquals(result.get(1), t2);

    var t3 = Thing.next();

    it.add(t3);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 3);
    assertEquals(result.get(0), t1);
    assertEquals(result.get(1), t2);
    assertEquals(result.get(2), t3);

    var t4 = Thing.next();

    it.add(t4);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 4);
    assertEquals(result.get(0), t1);
    assertEquals(result.get(1), t2);
    assertEquals(result.get(2), t3);
    assertEquals(result.get(3), t4);

    var t5 = Thing.next();

    it.add(t5);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 5);
    assertEquals(result.get(0), t1);
    assertEquals(result.get(1), t2);
    assertEquals(result.get(2), t3);
    assertEquals(result.get(3), t4);
    assertEquals(result.get(4), t5);

    var t6 = Thing.next();

    it.add(t6);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 6);
    assertEquals(result.get(0), t1);
    assertEquals(result.get(1), t2);
    assertEquals(result.get(2), t3);
    assertEquals(result.get(3), t4);
    assertEquals(result.get(4), t5);
    assertEquals(result.get(5), t6);

    it.clear();

    var arrayList = Thing.randomArrayList(MANY);

    it.addAll(arrayList);

    result = it.toUnmodifiableList();

    assertEquals(result, arrayList);
  }

  @Test(description = //
  """
  Create GrowableList.toUnmodifiableList(Comparator<? super E> c) that
  returns an immutable copy of the mutable list having all elements sorted by
  the specified comparator.

  As required (sort of...) by objectos-git WriteTree use-case.
  """
  )
  public void toUnmodifiableList_withComparator() {
    var c = Comparator.<Integer> naturalOrder();

    var it = new GrowableList<Integer>();

    var result = it.toUnmodifiableList(c);

    assertTrue(result.isEmpty());

    it.add(3);

    result = it.toUnmodifiableList(c);

    assertEquals(result, UnmodifiableList.of(3));

    it.add(1);

    result = it.toUnmodifiableList(c);

    assertEquals(result, UnmodifiableList.of(1, 3));
    assertEquals(it, UnmodifiableList.of(3, 1));

    it.add(2);

    result = it.toUnmodifiableList(c);

    assertEquals(result, UnmodifiableList.of(1, 2, 3));
    assertEquals(it, UnmodifiableList.of(3, 1, 2));

    it = new GrowableList<Integer>();

    for (int i = 0; i < MANY; i++) {
      var next = Next.intValue();

      it.add(next);
    }

    result = it.toUnmodifiableList(c);

    assertEquals(result.size(), MANY);
    assertNotEquals(result, it);

    var sorted = new ArrayList<Integer>(it);

    Collections.sort(sorted);

    assertEquals(result, sorted);
  }

  @Test
  public void truncate() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    it.add(t1);
    it.add(t2);
    it.add(t3);

    assertEquals(it.size(), 3);

    it.truncate(4);

    assertEquals(it.size(), 3);
    assertEquals(it.get(0), t1);
    assertEquals(it.get(1), t2);
    assertEquals(it.get(2), t3);

    it.truncate(3);

    assertEquals(it.size(), 3);
    assertEquals(it.get(0), t1);
    assertEquals(it.get(1), t2);
    assertEquals(it.get(2), t3);

    it.truncate(2);

    assertEquals(it.size(), 2);
    assertEquals(it.get(0), t1);
    assertEquals(it.get(1), t2);

    it.truncate(1);

    assertEquals(it.size(), 1);
    assertEquals(it.get(0), t1);

    it.truncate(0);

    assertEquals(it.size(), 0);

    try {
      it.truncate(-1);

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
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