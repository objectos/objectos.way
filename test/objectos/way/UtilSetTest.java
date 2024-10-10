/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import objectos.util.SetAssert;
import objectos.util.Thing;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UtilSetTest {

  private UtilSet<Thing> it;

  @BeforeMethod
  public void _beforeMethod() {
    it = new UtilSet<>();
  }

  @Test
  public void add() {
    // empty
    assertEquals(it.size(), 0);
    assertTrue(it.isEmpty());

    // one
    var t1 = Thing.next();

    assertTrue(it.add(t1));
    assertFalse(it.add(t1));

    assertContents(t1);

    // two
    var t2 = Thing.next();

    assertTrue(it.add(t2));
    assertFalse(it.add(t2));

    assertContents(t1, t2);

    // many
    var many = Thing.nextArray();

    for (var t : many) {
      assertTrue(it.add(t));
      assertFalse(it.add(t));
    }

    assertContents(t1, t2, many);

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
    Consumer<Collection<? extends Thing>> tester = c -> {
      assertEquals(it.addAll(c), !c.isEmpty());
      assertFalse(it.addAll(c));

      assertContents(c);

      _beforeMethod();
    };

    // empty
    tester.accept(Thing.EMPTY_LIST);
    tester.accept(Thing.EMPTY_SET);

    // one
    var t1 = Thing.next();

    tester.accept(List.of(t1));
    tester.accept(Set.of(t1));

    // two
    var t2 = Thing.next();

    tester.accept(List.of(t1, t2));
    tester.accept(Set.of(t1, t2));

    // many
    var many = Thing.nextArray();

    tester.accept(List.of(many));
    tester.accept(Set.of(many));

    // must reject null
    Consumer<Collection<? extends Thing>> nullTester = c -> {
      try {
        it.addAll(c);

        Assert.fail("Must throw NullPointerException");
      } catch (NullPointerException expected) {
        String msg = expected.getMessage();

        assertTrue(msg.matches("c\\[[0-9]{1,2}\\] == null"));
      }

      _beforeMethod();
    };

    many[Thing.HALF] = null;

    var listWithNull = new ArrayList<Thing>(Thing.MANY);
    var setWithNull = new HashSet<Thing>(Thing.MANY);

    for (var t : many) {
      listWithNull.add(t);
      setWithNull.add(t);
    }

    nullTester.accept(listWithNull);
    nullTester.accept(setWithNull);
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

    it.addAllIterable(TestingArrayBackedIterable.of(t1));
    assertContents(t1);

    // two
    var t2 = Thing.next();

    it.addAllIterable(List.of(t2));
    assertContents(t1, t2);

    // many
    var iterable = Thing.nextIterable();

    it.addAllIterable(iterable);
    assertContents(t1, t2, iterable);

    var arrayList = Thing.nextArrayList();

    it.addAllIterable(arrayList);
    assertContents(t1, t2, iterable, arrayList);

    // must reject null
    var arrayWithNull = Thing.nextArray();

    arrayWithNull[Thing.HALF] = null;

    var iterWithNull = new TestingArrayBackedIterable<>(arrayWithNull);

    try {
      it.addAllIterable(iterWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "iterable[50] == null");
    }

    var copy = Arrays.copyOf(arrayWithNull, Thing.HALF);

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

    var arrayList = Thing.nextArrayList();

    it.addAll(arrayList);
    it.clear();
    assertContents();
  }

  @Test
  public void contains() {
    // null must return false
    assertFalse(it.contains(null));

    // mutate
    var t1 = Thing.next();
    var t2 = Thing.next();
    var array = Thing.nextArray();

    assertFalse(it.contains(t1));

    assertFalse(it.contains(t2));

    it.add(t1);

    assertTrue(it.contains(t1));

    for (var t : array) {
      it.add(t);

      assertTrue(it.contains(t));
    }

    it.add(t2);

    assertTrue(it.contains(t2));

    // verify still true
    assertTrue(it.contains(t1));

    for (var t : array) {
      assertTrue(it.contains(t));
    }

    assertTrue(it.contains(t2));
  }

  @Test
  public void containsAll() {
    assertTrue(it.containsAll(Thing.EMPTY_LIST));

    assertTrue(it.containsAll(Thing.EMPTY_SET));

    // all
    var arrayList = Thing.nextArrayList();

    it.addAll(arrayList);

    assertTrue(it.containsAll(arrayList));

    // with more
    var withMore = new ArrayList<>(arrayList);

    var t1 = Thing.next();

    withMore.add(t1);

    assertFalse(it.containsAll(withMore));

    // with less
    var withLess = new ArrayList<>(arrayList);

    withLess.remove(withLess.size() - 1);

    assertTrue(it.containsAll(withLess));

    // with null
    var listWithNull = new ArrayList<>(arrayList);

    listWithNull.set(Thing.HALF, null);

    assertFalse(it.containsAll(listWithNull));

    // must reject null argument
    try {
      it.containsAll(null);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "c == null");
    }
  }

  @Test
  public void equals() {
    var a = new UtilSet<Thing>();

    var b = new UtilSet<Thing>();

    assertTrue(a.equals(b));
    assertTrue(b.equals(a));

    assertFalse(a.equals(null));

    assertTrue(a.equals(Collections.emptySet()));

    var arrayList = Thing.nextArrayList();

    a.addAll(arrayList);

    var t2 = Thing.next();

    b.addAll(arrayList);
    b.add(t2);

    assertFalse(a.equals(b));

    var c = new UtilSet<Thing>();

    c.addAll(arrayList);

    assertTrue(a.equals(c));
    assertTrue(c.equals(a));
  }

  @Test
  public void hashCodeTest() {
    // empty
    assertEquals(it.hashCode(), 0);

    // one
    var t1 = Thing.next();

    it.add(t1);

    var hashCode = t1.hashCode();

    assertEquals(it.hashCode(), hashCode);

    // two
    var t2 = Thing.next();

    it.add(t2);

    hashCode = hashCode + t2.hashCode();

    assertEquals(it.hashCode(), hashCode);

    // many
    var arrayList = Thing.nextArrayList();

    it.addAll(arrayList);

    for (var e : arrayList) {
      hashCode = hashCode + e.hashCode();
    }

    assertEquals(it.hashCode(), hashCode);
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
    // empty
    assertIterator();

    // one
    var t1 = Thing.next();

    it.add(t1);

    assertIterator(t1);

    // two
    var t2 = Thing.next();

    it.add(t2);

    assertIterator(t1, t2);

    // many
    var arrayList = Thing.nextArrayList();

    it.addAll(arrayList);

    assertIterator(t1, t2, arrayList);
  }

  @Test
  public void join() {
    // empty
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

    // one
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

    // two
    var t2 = Thing.next();

    it.add(t2);

    var iterator = it.iterator();

    var o1 = iterator.next();

    var o2 = iterator.next();

    assertEquals(
        it.join(),
        o1.toString() + o2.toString()
    );
    assertEquals(
        it.join("|"),
        o1.toString() + "|" + o2.toString()
    );
    assertEquals(
        it.join("|", "{", "}"),
        "{" + o1.toString() + "|" + o2.toString() + "}"
    );
  }

  @Test
  public void remove() {
    var t1 = Thing.next();

    it.add(t1);

    try {
      it.remove(t1);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertTrue(it.contains(t1));
    }
  }

  @Test
  public void removeAll() {
    var arrayList = Thing.nextArrayList();

    it.addAll(arrayList);

    try {
      it.removeAll(arrayList);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertTrue(it.containsAll(arrayList));
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
      assertTrue(it.contains(t1));
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
      assertTrue(it.contains(t1));
      assertTrue(it.contains(t2));
    }
  }

  @Test
  public void toStringTest() {
    assertEquals(it.toString(), "[]");

    var t1 = Thing.next();

    it.add(t1);

    assertEquals(
        it.toString(),

        "[Thing[%s]]".formatted(t1.toHexString())
    );

    var t2 = Thing.next();

    it.add(t2);

    var iterator = it.iterator();

    var o1 = iterator.next();

    var o2 = iterator.next();

    assertEquals(
        it.toString(),

        "[Thing[%s], Thing[%s]]".formatted(o1.toHexString(), o2.toHexString())
    );
  }

  @Test
  public void toUnmodifiableSet() {
    var us0 = it.toUnmodifiableSet();

    var t1 = Thing.next();
    assertTrue(it.add(t1));
    var us1 = it.toUnmodifiableSet();

    var t2 = Thing.next();
    assertTrue(it.add(t2));
    var us2 = it.toUnmodifiableSet();

    var t3 = Thing.next();
    assertTrue(it.add(t3));
    var us3 = it.toUnmodifiableSet();

    var t4 = Thing.next();
    assertTrue(it.add(t4));
    var us4 = it.toUnmodifiableSet();

    assertEquals(us0.size(), 0);
    assertContents(us0);

    assertEquals(us1.size(), 1);
    assertContents(us1, t1);

    assertEquals(us2.size(), 2);
    assertContents(us2, t1, t2);

    assertEquals(us3.size(), 3);
    assertContents(us3, t1, t2, t3);

    assertEquals(us4.size(), 4);
    assertContents(us4, t1, t2, t3, t4);

    it.clear();

    var array = Thing.nextArray();

    for (var thing : array) {
      assertTrue(it.add(thing));
    }

    var usX = it.toUnmodifiableSet();

    assertEquals(usX.size(), array.length);
    assertContents(usX, (Object) array);
  }

  private void assertContents(Object... expected) {
    assertContents(it, expected);
  }

  private void assertContents(Set<Thing> set, Object... expected) {
    var jdk = new HashSet<>();

    for (var o : expected) {
      if (o instanceof Thing t) {
        jdk.add(t);
      } else if (o instanceof Thing[] a) {
        for (var t : a) {
          jdk.add(t);
        }
      } else if (o instanceof Iterable<?> iter) {
        for (var t : iter) {
          jdk.add(t);
        }
      } else {
        throw new UnsupportedOperationException("Implement me: " + o.getClass());
      }
    }

    var elements = set.toArray();

    for (var e : elements) {
      assertTrue(jdk.remove(e));
    }

    assertTrue(jdk.isEmpty());
  }

  private void assertIterator(Object... expected) {
    SetAssert.iterator(it, expected);
  }

}