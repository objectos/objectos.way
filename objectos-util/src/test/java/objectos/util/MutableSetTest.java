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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MutableSetTest extends AbstractObjectosSetsTest {

  private MutableSet<Thing> set;

  @BeforeClass
  public void _beforeClass() {
    set = new MutableSet<>();
  }

  @BeforeMethod
  public void _beforeMethod() {
    set.clear();
  }

  @Test(description = "add(E e)")
  public void add() {
    // empty
    assertEquals(set.size(), 0);
    assertTrue(set.isEmpty());

    Set<Thing> result;
    result = set.toUnmodifiableSet();

    Set<Thing> expected;
    expected = new HashSet<>();

    assertSet(result, expected);

    // one
    assertTrue(set.add(t1));
    assertEquals(set.size(), 1);
    assertFalse(set.isEmpty());

    result = set.toUnmodifiableSet();

    expected.clear();
    expected.add(t1);

    assertSet(result, expected);

    // two
    assertTrue(set.add(t2));
    assertEquals(set.size(), 2);
    assertFalse(set.isEmpty());

    result = set.toUnmodifiableSet();

    expected.clear();
    expected.add(t1);
    expected.add(t2);

    assertSet(result, expected);

    // N
    int size;
    size = set.size();

    for (Thing thing : thingArray) {
      assertTrue(set.add(thing));

      size++;

      assertEquals(set.size(), size);
      assertFalse(set.isEmpty());
    }

    result = set.toUnmodifiableSet();

    expected.clear();
    expected.add(t1);
    expected.add(t2);

    for (Thing thing : thingArray) {
      expected.add(thing);
    }

    assertSet(result, expected);
  }

  @Test(description = "addAll(Collection)")
  public void addAll() {
    // empty
    assertFalse(set.addAll(java.util.Collections.<Thing> emptyList()));

    assertFalse(set.addAll(Collections.<Thing> emptySet()));

    UnmodifiableSet<Thing> result;
    result = set.toUnmodifiableSet();

    Set<Thing> expected;
    expected = new HashSet<>();

    assertSet(result, expected);

    // non empty
    assertTrue(set.addAll(thingList));

    assertEquals(set.size(), thingSize);

    assertTrue(set.addAll(thingSet));

    assertEquals(set.size(), thingSize + thingSize);

    result = set.toUnmodifiableSet();

    expected.clear();

    expected.addAll(thingList);

    expected.addAll(thingSet);

    assertSet(result, expected);
  }

  @Test
  public void addAllIterable() {
    // empty
    assertFalse(set.addAllIterable(emptyThingIterable));

    assertFalse(set.addAllIterable(emptyThingList));

    UnmodifiableSet<Thing> result;
    result = set.toUnmodifiableSet();

    Set<Thing> expected;
    expected = new HashSet<>();

    assertSet(result, expected);

    // non empty
    assertTrue(set.addAllIterable(thingIterable));

    assertEquals(set.size(), thingSize);

    assertTrue(set.addAllIterable(thingList));

    assertEquals(set.size(), thingSize + thingSize);

    result = set.toUnmodifiableSet();

    expected.clear();

    for (Thing thing : thingIterable) {
      expected.add(thing);
    }

    expected.addAll(thingList);

    assertSet(result, expected);
  }

  @Test
  public void addWillNullMessage() {
    String message;
    message = Next.string(120);

    assertTrue(set.addWithNullMessage(t1, message));

    assertFalse(set.addWithNullMessage(t1, message));

    try {
      set.addWithNullMessage(null, message);

      Assert.fail();
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), message);
    }

    int index;
    index = Next.intValue();

    String end;
    end = Next.string(28);

    assertTrue(set.addWithNullMessage(t2, message, index, end));

    assertFalse(set.addWithNullMessage(t2, message, index, end));

    try {
      set.addWithNullMessage(null, message, index, end);

      Assert.fail();
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), message + index + end);
    }
  }

  @Test
  public void contains() {
    assertFalse(set.contains(t1));

    set.add(t1);

    assertTrue(set.contains(t1));

    set.clear();

    assertFalse(set.contains(t1));

    set.addAll(thingList);

    assertFalse(set.contains(t1));

    for (Thing thing : thingList) {
      assertTrue(set.contains(thing));
    }

    for (Thing thing : thingSet) {
      assertFalse(set.contains(thing));
    }

    set.clear();

    for (Thing thing : thingArray) {
      set.add(thing);
    }

    Object[] tail;
    tail = Arrays.copyOfRange(thingArray, 1, thingArray.length);

    assertTrue(set.contains(thingArray[0], tail));
  }

  @Test
  public void containsAll() {
    assertTrue(thingSet.containsAll(emptyThingList));

    assertTrue(set.containsAll(emptyThingList));

    set.addAll(thingList);

    assertTrue(set.containsAll(thingList));

    List<Thing> withMore;
    withMore = new ArrayList<Thing>(thingList);

    withMore.add(t1);

    assertFalse(set.containsAll(withMore));

    List<Thing> withLess;
    withLess = new ArrayList<Thing>(thingList);

    withLess.remove(withLess.size() - 1);

    assertTrue(set.containsAll(withLess));
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void equalsTest() {
    Set<Thing> hashSet;
    hashSet = new HashSet<>();

    assertTrue(set.equals(set));

    assertTrue(set.equals(hashSet));

    assertTrue(hashSet.equals(set));

    hashSet.addAll(thingSet);

    set.addAll(thingSet);

    assertTrue(set.equals(set));

    assertTrue(set.equals(hashSet));

    assertTrue(hashSet.equals(set));

    set.clear();

    assertFalse(set.equals(hashSet));

    assertFalse(set.equals(null));

    set.addAll(thingList);

    assertFalse(set.equals(thingList));
  }

  @Test
  public void getOnly() {
    // empty
    try {
      set.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: empty.");
    }

    // one
    set.add(t1);

    assertEquals(set.getOnly(), t1);

    set.add(t2);

    // standard
    try {
      set.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: more than one element.");
    }
  }

  @Test
  public void iterator() {
    assertTrue(set.isEmpty());

    Set<Thing> expected;
    expected = new HashSet<>();

    assertIterator(set.iterator(), expected);

    for (Thing t : thingArray) {
      set.add(t);

      expected.add(t);
    }

    assertIterator(set.iterator(), expected);
  }

  @Test
  public void join() {
    assertEquals(
      set.join(),
      ""
    );
    assertEquals(
      set.join("|"),
      ""
    );
    assertEquals(
      set.join("|", "{", "}"),
      "{}"
    );

    set.add(t1);

    assertEquals(
      set.join(),
      t1.toString()
    );
    assertEquals(
      set.join("|"),
      t1.toString()
    );
    assertEquals(
      set.join("|", "{", "}"),
      "{" + t1.toString() + "}"
    );

    set.add(t2);

    Iterator<Thing> iterator;
    iterator = set.iterator();

    Object o1;
    o1 = iterator.next();

    Object o2;
    o2 = iterator.next();

    assertEquals(
      set.join(),
      o1.toString() + o2.toString()
    );
    assertEquals(
      set.join("|"),
      o1.toString() + "|" + o2.toString()
    );
    assertEquals(
      set.join("|", "{", "}"),
      "{" + o1.toString() + "|" + o2.toString() + "}"
    );
  }

  @Test
  public void remove() {
    set.add(t1);

    try {
      set.remove(t1);

      Assert.fail();
    } catch (UnsupportedOperationException expected) {
      assertTrue(set.contains(t1));
    }
  }

  @Test
  public void removeAll() {
    set.addAll(thingList);

    try {
      set.removeAll(thingList);

      Assert.fail();
    } catch (UnsupportedOperationException expected) {
      assertTrue(set.containsAll(thingList));
    }
  }

  @Test
  public void retainAll() {
    set.addAll(thingList);

    List<Thing> retain;
    retain = new ArrayList<Thing>();

    retain.add(thingList.get(0));

    retain.add(thingList.get(1));

    try {
      set.retainAll(retain);

      Assert.fail();
    } catch (UnsupportedOperationException expected) {
      assertTrue(set.containsAll(thingList));
    }
  }

}