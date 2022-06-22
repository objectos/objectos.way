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
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableListTest extends AbstractObjectosListsTest {

  private GrowableList<Thing> list;

  @BeforeClass
  public void _beforeClass() {
    list = new GrowableList<>();
  }

  @BeforeMethod
  public void _beforeMethod() {
    list.clear();
  }

  @Test
  public final void add() {
    Integer box;

    GrowableList<Integer> it;
    it = new GrowableList<>();

    assertTrue(it.isEmpty());

    int iters;
    iters = 10 * 1000;

    for (int i = 0; i < iters; i++) {
      int randomInt;
      randomInt = Next.intValue();

      box = Integer.valueOf(randomInt);

      assertEquals(it.add(box), true);

      assertEquals(it.size(), i + 1);

      assertTrue(it.contains(box));
    }

    try {
      box = null;

      it.add(box);

      Assert.fail();
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "e == null");
    }
  }

  @Test
  public void addAll() {
    int size;
    size = 2000;

    GrowableList<Integer> it;
    it = new GrowableList<>();

    assertEquals(it.size(), 0);

    it.addAll(Collections.<Integer> emptySet());

    assertEquals(it.size(), 0);

    ArrayList<Integer> arrayList;
    arrayList = randomIntArrayList(size);

    it.addAll(arrayList);

    assertEquals(it.size(), size);

    assertTrue(it.containsAll(arrayList));

    GrowableList<Integer> growableList;
    growableList = randomIntGrowableList(size);

    it.addAll(growableList);

    assertEquals(it.size(), size + size);
    assertTrue(it.containsAll(arrayList));
    assertTrue(it.containsAll(growableList));
  }

  @Test
  public void addAllIterable() {
    int size;
    size = 2000;

    GrowableList<Integer> it;
    it = new GrowableList<>();

    assertEquals(it.size(), 0);

    it.addAllIterable(Collections.<Integer> emptySet());

    assertEquals(it.size(), 0);

    ArrayList<Integer> arrayList;
    arrayList = randomIntArrayList(size);

    it.addAllIterable(arrayList);

    assertEquals(it.size(), size);

    assertTrue(it.containsAll(arrayList));

    Iterable<Integer> iterable;
    iterable = randomIntArrayBackedIterable(size);

    it.addAllIterable(iterable);

    assertEquals(it.size(), size + size);
    assertTrue(it.containsAll(arrayList));
    assertTrue(it.containsAllIterable0(iterable));
  }

  @Test
  public void clear() {
    GrowableList<Integer> it;
    it = new GrowableList<>();

    assertEquals(it.size(), 0);

    ArrayList<Integer> arrayList;
    arrayList = randomIntArrayList(100);

    it.addAll(arrayList);

    assertEquals(it.size(), 100);

    it.clear();

    assertEquals(it.size(), 0);

    for (int i = 0; i < arrayList.size(); i++) {
      Integer element;
      element = arrayList.get(i);

      assertFalse(it.contains(element));
    }
  }

  @Test
  public void contains() {
    GrowableList<Integer> it;
    it = new GrowableList<>();

    assertFalse(it.contains(1));

    assertFalse(it.contains(1, 2));

    Integer[] random;
    random = randomIntegerArray(1000);

    it = new GrowableList<Integer>(random);

    for (Integer e : random) {
      assertTrue(it.contains(e));
    }

    Object[] objectIntArray;
    objectIntArray = Arrays.copyOf(random, random.length, Object[].class);

    assertTrue(it.contains(objectIntArray[0], objectIntArray));
  }

  @Test
  public void containsAll() {
    addAll();
  }

  @Test
  public void equals() {
    GrowableList<Integer> a;
    a = new GrowableList<>();

    GrowableList<Integer> b;
    b = new GrowableList<>();

    assertTrue(a.equals(b));

    assertFalse(a.equals(null));

    assertTrue(a.equals(Collections.emptyList()));

    Integer[] randomA;
    randomA = randomIntegerArray(1000);

    a = new GrowableList<Integer>(randomA);

    Integer[] randomB;
    randomB = randomIntegerArray(1000);

    b = new GrowableList<Integer>(randomB);

    assertFalse(a.equals(b));

    randomB = Arrays.copyOf(randomA, randomA.length);

    b = new GrowableList<Integer>(randomB);

    assertTrue(a.equals(b));
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
    GrowableList<Integer> it;
    it = new GrowableList<>();

    try {
      it.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: empty.");
    }

    it.add(1);

    assertEquals(it.getOnly(), Integer.valueOf(1));

    it.add(2);

    // standard
    try {
      it.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: more than one element.");
    }
  }

  @Test
  public void indexOf() {
    GrowableList<Integer> it;
    it = new GrowableList<>();

    Integer target;
    target = Next.intValue();

    assertEquals(it.indexOf(target), -1);

    it.add(target);

    assertEquals(it.indexOf(target), 0);

    it.clear();

    Integer[] random;
    random = randomIntegerArray(1000);

    for (Integer r : random) {
      if (r.equals(target)) {
        continue;
      }

      it.add(r);
    }

    int index;
    index = it.size();

    it.add(target);

    assertEquals(it.indexOf(target), index);

    it.add(target);

    assertEquals(it.indexOf(target), index);
  }

  @Test
  public void isEmpty() {
    GrowableList<Integer> it;
    it = new GrowableList<>();

    assertTrue(it.isEmpty());

    it.add(1);

    assertFalse(it.isEmpty());

    it.clear();

    assertTrue(it.isEmpty());
  }

  @Test
  public void lastIndexOf() {
    GrowableList<Integer> it;
    it = new GrowableList<>();

    Integer target;
    target = Next.intValue();

    assertEquals(it.lastIndexOf(target), -1);

    it.add(target);

    assertEquals(it.lastIndexOf(target), 0);

    it.clear();

    Integer[] random;
    random = randomIntegerArray(1000);

    for (Integer r : random) {
      if (r.equals(target)) {
        continue;
      }

      it.add(r);
    }

    int index;
    index = it.size();

    it.add(target);

    assertEquals(it.lastIndexOf(target), index);

    it.add(target);

    assertEquals(it.lastIndexOf(target), index + 1);
  }

  @Test
  public void size() {
    GrowableList<Integer> it;
    it = new GrowableList<>();

    assertEquals(it.size(), 0);

    it.add(1);

    assertEquals(it.size(), 1);

    it.clear();

    assertEquals(it.size(), 0);
  }

  @Test
  public void testCase01() {
    // empty
    assertEquals(list.size(), 0);
    assertTrue(list.isEmpty());

    List<Thing> result;
    result = list.toUnmodifiableList();

    List<Thing> expected;
    expected = new ArrayList<>();

    assertContents(result, expected);

    // one
    assertTrue(list.add(t1));
    assertEquals(list.size(), 1);
    assertFalse(list.isEmpty());

    result = list.toUnmodifiableList();

    expected.clear();
    expected.add(t1);

    assertContents(result, expected);

    // two
    assertTrue(list.add(t2));
    assertEquals(list.size(), 2);
    assertFalse(list.isEmpty());

    result = list.toUnmodifiableList();

    expected.clear();
    expected.add(t1);
    expected.add(t2);

    assertContents(result, expected);

    // N
    int size;
    size = list.size();

    for (Thing thing : thingArray) {
      assertTrue(list.add(thing));

      size++;

      assertEquals(list.size(), size);
      assertFalse(list.isEmpty());
    }

    result = list.toUnmodifiableList();

    expected.clear();
    expected.add(t1);
    expected.add(t2);

    for (Thing thing : thingArray) {
      expected.add(thing);
    }

    assertContents(result, expected);
  }

  @Test
  public void testCase02() {
    // empty
    assertFalse(list.addAllIterable(emptyThingIterable));

    assertFalse(list.addAllIterable(emptyThingList));

    List<Thing> result;
    result = list.toUnmodifiableList();

    List<Thing> expected;
    expected = new ArrayList<>();

    assertContents(result, expected);

    // non empty
    assertTrue(list.addAllIterable(thingIterable));

    assertEquals(list.size(), thingSize);

    assertTrue(list.addAllIterable(thingList));

    assertEquals(list.size(), thingSize + thingSize);

    result = list.toUnmodifiableList();

    expected.clear();

    for (Thing thing : thingIterable) {
      expected.add(thing);
    }

    expected.addAll(thingList);

    assertContents(result, expected);
  }

  @Test
  public final void testCase05() {
    // empty
    assertFalse(list.addAll(Collections.<Thing> emptyList()));

    assertFalse(list.addAll(Collections.<Thing> emptySet()));

    UnmodifiableList<Thing> result;
    result = list.toUnmodifiableList();

    List<Thing> expected;
    expected = new ArrayList<>();

    assertContents(result, expected);

    // non empty
    assertTrue(list.addAll(thingList));

    assertEquals(list.size(), thingSize);

    assertTrue(list.addAll(thingSet));

    assertEquals(list.size(), thingSize + thingSize);

    result = list.toUnmodifiableList();

    expected.clear();

    expected.addAll(thingList);

    expected.addAll(thingSet);

    assertContents(result, expected);
  }

  @Test
  public final void testCase06() {
    assertEquals(
      list.join(),
      ""
    );
    assertEquals(
      list.join("|"),
      ""
    );
    assertEquals(
      list.join("|", "{", "}"),
      "{}"
    );

    list.add(t1);

    assertEquals(
      list.join(),
      t1.toString()
    );
    assertEquals(
      list.join("|"),
      t1.toString()
    );
    assertEquals(
      list.join("|", "{", "}"),
      "{" + t1.toString() + "}"
    );

    list.add(t2);

    assertEquals(
      list.join(),
      t1.toString() + t2.toString()
    );
    assertEquals(
      list.join("|"),
      t1.toString() + "|" + t2.toString()
    );
    assertEquals(
      list.join("|", "{", "}"),
      "{" + t1.toString() + "|" + t2.toString() + "}"
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