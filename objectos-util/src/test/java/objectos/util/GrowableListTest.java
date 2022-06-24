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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableListTest extends AbstractObjectosUtilTest {

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
    assertTrue(it.add(t1));
    assertContents(t1);

    // two
    assertTrue(it.add(t2));
    assertContents(t1, t2);

    // many
    for (int i = 0; i < arrayT.length; i++) {
      var t = arrayT[i];

      assertTrue(it.add(t));
    }
    assertContents(t1, t2, arrayT);

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

    assertFalse(it.addAll(emptyThingList));

    assertEquals(it.size(), 0);

    // one
    it.addAll(List.of(t1));
    assertContents(t1);

    // two
    it.addAll(arrayDequeOf(t2));
    assertContents(t1, t2);

    // many
    it.addAll(arrayList);
    assertContents(t1, t2, arrayList);

    it.addAll(arrayDeque);
    assertContents(t1, t2, arrayList, arrayDeque);

    // must reject null
    var listWithNull = Thing.randomArrayList(thingSize);

    listWithNull.set(thingSize / 2, null);

    try {
      it.addAll(listWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "collection[2839] == null");
    }

    var sub = listWithNull.subList(0, 2839);

    assertContents(t1, t2, arrayList, arrayDeque, sub);

    try {
      var notRandomAccessWithNull = new LinkedList<Thing>();

      notRandomAccessWithNull.addAll(listWithNull);

      it.addAll(notRandomAccessWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "collection[2839] == null");
    }

    assertContents(t1, t2, arrayList, arrayDeque, sub, sub);
  }

  @Test
  public void addAllIterable() {
    // empty
    assertEquals(it.size(), 0);

    assertFalse(it.addAllIterable(emptyThingIterable));
    assertContents();

    assertFalse(it.addAllIterable(emptyThingList));
    assertContents();

    // one
    it.addAllIterable(ArrayBackedIterable.of(t1));
    assertContents(t1);

    // two
    it.addAllIterable(List.of(t2));
    assertContents(t1, t2);

    // many
    it.addAllIterable(thingIterable);
    assertContents(t1, t2, thingIterable);

    it.addAllIterable(arrayList);
    assertContents(t1, t2, thingIterable, arrayList);

    // must reject null
    var randomArray = Thing.randomArray(thingSize);

    randomArray[thingSize / 2] = null;

    var iterWithNull = new ArrayBackedIterable<>(randomArray);

    try {
      it.addAllIterable(iterWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "iterable[2839] == null");
    }

    var copy = Arrays.copyOf(randomArray, thingSize / 2);

    var sub = List.of(copy);

    assertContents(t1, t2, thingIterable, arrayList, sub);
  }

  @Test
  public void clear() {
    assertEquals(it.size(), 0);

    it.clear();
    assertContents();

    it.addAll(arrayList);
    it.clear();
    assertContents();
  }

  @Test
  public void contains() {
    assertEquals(it.size(), 0);

    assertFalse(it.contains(t1));

    assertFalse(it.contains(t1, t2));

    it.add(t1);

    for (var t : arrayT) {
      it.add(t);
    }

    it.add(t2);

    assertTrue(it.contains(t1));

    assertTrue(it.contains(t1, (Object[]) arrayT));

    assertTrue(it.contains(t2));

    it.clear();

    assertFalse(it.contains(t1));

    assertFalse(it.contains(t1, (Object[]) arrayT));

    assertFalse(it.contains(t2));
  }

  @Test
  public void containsAll() {
    assertEquals(it.size(), 0);

    assertFalse(it.containsAll(arrayList));

    it.addAll(arrayList);

    assertTrue(it.containsAll(arrayList));

    var list = new ArrayList<Thing>(arrayList.size() + 1);

    list.add(t1);

    list.addAll(arrayList);

    assertFalse(it.containsAll(list));

    var listWithNull = new ArrayList<Thing>(arrayList.size());

    listWithNull.addAll(arrayList);

    listWithNull.set(thingSize / 2, null);

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

    a.addAll(arrayList);

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
      private final GrowableList<Integer> it = new GrowableList<>();

      public final void add(Integer e) {
        it.add(e);
      }

      public final void get(int index, int expected) {
        Integer value;
        value = it.get(index);

        assertEquals(value, Integer.valueOf(expected));
      }

      public final void getOutOfBounds(int index) {
        try {
          it.get(index);

          fail();
        } catch (IndexOutOfBoundsException expected) {

        }
      }
    }

    Tester tester;
    tester = new Tester();

    tester.getOutOfBounds(-1);
    tester.getOutOfBounds(0);
    tester.getOutOfBounds(1);

    tester.add(1);

    tester.getOutOfBounds(-1);
    tester.get(0, 1);
    tester.getOutOfBounds(1);

    tester.add(2);
    tester.add(3);

    tester.getOutOfBounds(-1);
    tester.get(0, 1);
    tester.get(1, 2);
    tester.get(2, 3);
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

    it.add(t1);

    assertEquals(it.getOnly(), t1);

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
  public void indexOf() {
    assertEquals(it.indexOf(t1), -1);

    it.add(t1);

    assertEquals(it.indexOf(t1), 0);

    it.clear();

    it.addAll(arrayList);

    for (int i = 0, size = arrayList.size(); i < size; i++) {
      var t = arrayList.get(i);

      assertEquals(it.indexOf(t), i);
    }

    var index = it.size();

    it.add(t1);

    assertEquals(it.indexOf(t1), index);

    it.add(t2);

    assertEquals(it.indexOf(t1), index);
  }

  @Test
  public void isEmpty() {
    assertTrue(it.isEmpty());

    it.add(t1);

    assertFalse(it.isEmpty());

    it.clear();

    assertTrue(it.isEmpty());
  }

  @Test
  public void lastIndexOf() {
    assertEquals(it.lastIndexOf(t1), -1);

    it.add(t1);

    assertEquals(it.lastIndexOf(t1), 0);

    it.clear();

    it.addAll(arrayList);

    var index = it.size();

    it.add(t1);

    assertEquals(it.lastIndexOf(t1), index);

    it.add(t2);

    assertEquals(it.lastIndexOf(t1), index);

    it.add(t1);

    assertEquals(it.lastIndexOf(t1), index + 2);
  }

  @Test
  public void size() {
    assertEquals(it.size(), 0);

    it.add(t1);

    assertEquals(it.size(), 1);

    it.clear();

    assertEquals(it.size(), 0);
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

  @Test
  public final void testCase06() {
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
  public void toArray() {
    GrowableList<Object> it;
    it = new GrowableList<>();

    Integer[] emptyArray;
    emptyArray = new Integer[0];

    List<Integer> intList;
    intList = randomIntArrayList(1234);

    Integer[] lastNull;
    lastNull = new Integer[intList.size() + 2];

    assertEquals(it.toArray(), new Object[] {});
    assertSame(it.toArray(emptyArray), emptyArray);

    Integer[] result;
    result = it.toArray(lastNull);

    assertSame(result, lastNull);
    assertNull(result[0]);
    assertNull(result[1]);

    Integer box;
    box = Next.intValue();

    it.add(box);

    assertEquals(it.toArray(), new Object[] {box});
    assertEquals(it.toArray(emptyArray), new Integer[] {box});

    result = it.toArray(lastNull);

    assertSame(result, lastNull);
    assertEquals(result[0], box);
    assertNull(result[1]);

    it.clear();

    it.addAll(intList);

    assertEquals(it.toArray(), intList.toArray());
    assertEquals(it.toArray(emptyArray), intList.toArray(emptyArray));

    result = it.toArray(lastNull);

    assertSame(result, lastNull);

    int i = 0;

    for (; i < intList.size(); i++) {
      assertEquals(result[i], intList.get(i));
    }

    assertNull(result[i]);
  }

  @Test(description = TestCase07.DESCRIPTION)
  public void toImmutableSortedList() {
    Comparator<Integer> c;
    c = TestCase07.ORDER;

    GrowableList<Integer> it;
    it = new GrowableList<>();

    UnmodifiableList<Integer> result;
    result = it.toImmutableSortedList(c);

    assertTrue(result.isEmpty());

    it.add(3);

    result = it.toImmutableSortedList(c);

    assertEquals(result, UnmodifiableList.of(3));

    it.add(1);

    result = it.toImmutableSortedList(c);

    assertEquals(result, UnmodifiableList.of(1, 3));
    assertEquals(it, UnmodifiableList.of(3, 1));

    it.add(2);

    result = it.toImmutableSortedList(c);

    assertEquals(result, UnmodifiableList.of(1, 2, 3));
    assertEquals(it, UnmodifiableList.of(3, 1, 2));

    Integer[] random;
    random = randomIntegerArray(2345);

    it = new GrowableList<Integer>(random);

    result = it.toImmutableSortedList(c);

    assertEquals(result.size(), random.length);
    assertNotEquals(result, it);

    ArrayList<Integer> sorted;
    sorted = new ArrayList<Integer>(it);

    Collections.sort(sorted);

    assertEquals(result, sorted);
  }

  @Test
  public void toStringTest() {
    GrowableList<String> it;
    it = new GrowableList<>();

    assertEquals(
      it.toString(),

      "GrowableList []"
    );

    it.add("A");
    it.add("B");
    it.add("C");

    assertEquals(
      it.toString(),

      lines(
        "GrowableList [",
        "  0 = A",
        "  1 = B",
        "  2 = C",
        "]"
      )
    );
  }

  @Test
  public void toUnmodifiableList() {
    GrowableList<Integer> it;
    it = new GrowableList<>();

    UnmodifiableList<Integer> result;
    result = it.toUnmodifiableList();

    assertTrue(result.isEmpty());

    it.add(1);

    assertTrue(result.isEmpty());

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 1);

    assertEquals(result.get(0), Integer.valueOf(1));

    it.add(2);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 2);
    assertEquals(result.get(0), Integer.valueOf(1));
    assertEquals(result.get(1), Integer.valueOf(2));

    it.add(3);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 3);
    assertEquals(result.get(0), Integer.valueOf(1));
    assertEquals(result.get(1), Integer.valueOf(2));
    assertEquals(result.get(2), Integer.valueOf(3));

    it.add(4);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 4);
    assertEquals(result.get(0), Integer.valueOf(1));
    assertEquals(result.get(1), Integer.valueOf(2));
    assertEquals(result.get(2), Integer.valueOf(3));
    assertEquals(result.get(3), Integer.valueOf(4));

    it.add(5);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 5);
    assertEquals(result.get(0), Integer.valueOf(1));
    assertEquals(result.get(1), Integer.valueOf(2));
    assertEquals(result.get(2), Integer.valueOf(3));
    assertEquals(result.get(3), Integer.valueOf(4));
    assertEquals(result.get(4), Integer.valueOf(5));

    it.add(6);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 6);
    assertEquals(result.get(0), Integer.valueOf(1));
    assertEquals(result.get(1), Integer.valueOf(2));
    assertEquals(result.get(2), Integer.valueOf(3));
    assertEquals(result.get(3), Integer.valueOf(4));
    assertEquals(result.get(4), Integer.valueOf(5));
    assertEquals(result.get(5), Integer.valueOf(6));

    Integer[] random;
    random = randomIntegerArray(2345);

    it = new GrowableList<Integer>(random);

    result = it.toUnmodifiableList();

    assertEquals(result.size(), random.length);

    for (int i = 0; i < random.length; i++) {
      Integer integer;
      integer = result.get(i);

      Integer expected;
      expected = random[i];

      assertEquals(integer, expected);
    }
  }

  @Test
  public void truncate() {
    GrowableList<String> it;
    it = new GrowableList<>();

    it.add("A");

    it.add("B");

    it.add("C");

    assertEquals(it.size(), 3);

    it.truncate(4);

    assertEquals(it.size(), 3);
    assertEquals(it.get(0), "A");
    assertEquals(it.get(1), "B");
    assertEquals(it.get(2), "C");

    it.truncate(3);

    assertEquals(it.size(), 3);
    assertEquals(it.get(0), "A");
    assertEquals(it.get(1), "B");
    assertEquals(it.get(2), "C");

    it.truncate(2);

    assertEquals(it.size(), 2);
    assertEquals(it.get(0), "A");
    assertEquals(it.get(1), "B");

    it.truncate(1);

    assertEquals(it.size(), 1);
    assertEquals(it.get(0), "A");

    it.truncate(0);

    assertEquals(it.size(), 0);

    try {
      it.truncate(-1);

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
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