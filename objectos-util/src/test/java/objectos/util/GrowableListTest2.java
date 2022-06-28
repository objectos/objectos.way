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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableListTest2 extends AbstractObjectosUtilTest {

  private GrowableList<Thing> it;

  @BeforeMethod
  public void _beforeMethod() {
    it = new GrowableList<>();
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

    tester.add(t1);

    tester.getOutOfBounds(-1);
    tester.get(0, t1);
    tester.getOutOfBounds(1);

    var t3 = arrayList.get(0);

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
  public void toArray() {
    var emptyArray = new Thing[0];

    assertEquals(it.toArray(), new Object[] {});
    assertSame(it.toArray(emptyArray), emptyArray);

    var lastNull = new Thing[arrayList.size() + 2];

    var result = it.toArray(lastNull);

    assertSame(result, lastNull);
    assertNull(result[0]);
    assertNull(result[1]);

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

    it.add(t1);

    assertTrue(result.isEmpty());

    result = it.toUnmodifiableList();

    assertEquals(result.size(), 1);

    assertEquals(result.get(0), t1);

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

    it.addAll(arrayList);

    result = it.toUnmodifiableList();

    assertEquals(result, arrayList);
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

}