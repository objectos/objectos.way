/*
 * Copyright (C) 2022-2025 Objectos Software LTDA.
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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;
import objectos.util.SetAssert;
import objectos.util.SingletonIterator;
import objectos.util.Thing;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UtilUnmodifiableSetTest {

  @FunctionalInterface
  interface Tester {
    void execute(UtilUnmodifiableSet<Thing> it, Object... els);
  }

  private UtilUnmodifiableSet<Thing> us0;

  private UtilUnmodifiableSet<Thing> us1;

  private UtilUnmodifiableSet<Thing> us2;

  private UtilUnmodifiableSet<Thing> us3;

  private UtilUnmodifiableSet<Thing> usX;

  private Set<Thing> jdk0;

  private Set<Thing> jdk1;

  private Set<Thing> jdk2;

  private Set<Thing> jdk3;

  private Set<Thing> jdkX;

  private Thing t1;

  private Thing t2;

  private Thing t3;

  private Thing[] many;

  @BeforeClass
  public void _beforeClass() {
    us0 = UtilUnmodifiableSet.of();
    jdk0 = Set.of();

    t1 = Thing.next();

    us1 = UtilUnmodifiableSet.of(t1);
    jdk1 = Set.of(t1);

    t2 = Thing.next();

    us2 = UtilUnmodifiableSet.of(t1, t2);
    jdk2 = Set.of(t1, t2);

    t3 = Thing.next();

    us3 = UtilUnmodifiableSet.of(t1, t2, t3);
    jdk3 = Set.of(t1, t2, t3);

    many = Thing.nextArray();

    usX = UtilUnmodifiableSet.copyOf(many);
    jdkX = Set.of(many);
  }

  @Test
  public void add() {
    testAll((it, els) -> {
      try {
        var t = Thing.next();

        it.add(t);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        SetAssert.iterator(it, els);
      }
    });
  }

  @Test
  public void addAll() {
    final var arrayList = Thing.nextArrayList();

    testAll((it, els) -> {
      try {
        it.addAll(arrayList);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        SetAssert.iterator(it, els);
      }
    });
  }

  @Test
  public void clear() {
    testAll((it, els) -> {
      try {
        it.clear();

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        SetAssert.iterator(it, els);
      }
    });
  }

  @Test
  public void contains() {
    assertFalse(us0.contains(null));
    assertFalse(us1.contains(null));
    assertFalse(us2.contains(null));
    assertFalse(us3.contains(null));
    assertFalse(usX.contains(null));

    assertFalse(us0.contains(t1));
    assertTrue(us1.contains(t1));
    assertTrue(us2.contains(t1));
    assertTrue(us3.contains(t1));
    assertFalse(usX.contains(t1));

    assertFalse(us0.contains(t2));
    assertFalse(us1.contains(t2));
    assertTrue(us2.contains(t2));
    assertTrue(us3.contains(t2));
    assertFalse(usX.contains(t2));

    assertFalse(us0.contains(t3));
    assertFalse(us1.contains(t3));
    assertFalse(us2.contains(t3));
    assertTrue(us3.contains(t3));
    assertFalse(usX.contains(t3));

    for (var t : many) {
      assertFalse(us0.contains(t));
      assertFalse(us1.contains(t));
      assertFalse(us2.contains(t));
      assertFalse(us3.contains(t));
      assertTrue(usX.contains(t));
    }
  }

  @Test
  public void containsAll() {
    assertFalse(us0.containsAll(jdkX));
    assertFalse(us1.containsAll(jdkX));
    assertFalse(us2.containsAll(jdkX));
    assertFalse(us3.containsAll(jdkX));
    assertTrue(usX.containsAll(jdkX));

    var list = new ArrayList<Thing>(jdkX.size() + 1);

    var tn = Thing.next();

    list.add(tn);

    list.addAll(jdkX);

    assertFalse(usX.containsAll(list));

    var listWithNull = new ArrayList<Thing>(jdkX.size());

    listWithNull.addAll(jdkX);

    listWithNull.set(Thing.HALF, null);

    assertFalse(usX.containsAll(listWithNull));
  }

  @Test
  public void copyOf_iterable() {
    final var array = Thing.nextArray();

    Consumer<UtilUnmodifiableSet<Thing>> tester = l -> {
      Object a = array;

      SetAssert.iterator(l, a);
    };

    // must reject null
    try {
      Iterable<?> nullIterable = null;

      UtilUnmodifiableSet.copyOf(nullIterable);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "elements == null");
    }

    // iterable
    var iterable = new TestingArrayBackedIterable<>(array);

    var ulIterable = UtilUnmodifiableSet.copyOf(iterable);

    tester.accept(ulIterable);

    // UnmodifiableSet
    assertSame(UtilUnmodifiableSet.copyOf(ulIterable), ulIterable);

    // LinkedList
    var linkedList = new LinkedList<Thing>();

    for (var o : array) {
      linkedList.add(o);
    }

    tester.accept(UtilUnmodifiableSet.copyOf(linkedList));

    // List & RandomAccess
    var arrayList = new ArrayList<Thing>(Thing.MANY);

    for (var t : array) {
      arrayList.add(t);
    }

    tester.accept(UtilUnmodifiableSet.copyOf(arrayList));

    // List & RandomAccess with null
    var arrayListWithNull = new ArrayList<>(arrayList);

    arrayListWithNull.set(Thing.HALF, null);

    try {
      UtilUnmodifiableSet.copyOf(arrayListWithNull);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "elements[50] == null");
    }

    // Collection
    var collection = new ArrayDeque<Thing>(Thing.MANY);

    for (var t : array) {
      collection.add(t);
    }

    tester.accept(UtilUnmodifiableSet.copyOf(collection));

    // Collection with null
    var collectionWithNull = new LinkedHashSet<>(arrayListWithNull);

    try {
      UtilUnmodifiableSet.copyOf(collectionWithNull);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "elements[50] == null");
    }
  }

  @Test
  public void copyOf_iterator() {
    // empty
    var it = UtilUnmodifiableSet.<Thing> copyOf(Collections.emptyIterator());

    assertSame(it, UtilUnmodifiableSet.of());

    // one
    var t1 = Thing.next();

    it = UtilUnmodifiableSet.copyOf(new SingletonIterator<>(t1));

    SetAssert.iterator(it, t1);

    // many
    var many = Thing.nextArray();

    it = UtilUnmodifiableSet.copyOf(new ArrayIterator<>(many, many.length));

    SetAssert.iterator(it, (Object) many);

    // must reject null
    try {
      Iterator<?> nullIterator = null;

      UtilUnmodifiableSet.copyOf(nullIterator);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "iterator == null");
    }
  }

  @Test
  public void equals() {
    // empty
    assertTrue(us0.equals(jdk0));
    assertTrue(jdk0.equals(us0));
    assertFalse(us0.equals(null));
    assertTrue(us0.equals(us0));
    assertFalse(us0.equals(us1));
    assertFalse(us0.equals(us2));
    assertFalse(us0.equals(us3));
    assertFalse(us0.equals(usX));

    // one
    assertTrue(us1.equals(jdk1));
    assertTrue(jdk1.equals(us1));
    assertFalse(us1.equals(null));
    assertFalse(us1.equals(us0));
    assertTrue(us1.equals(us1));
    assertFalse(us1.equals(us2));
    assertFalse(us1.equals(us3));
    assertFalse(us1.equals(usX));

    // two
    assertTrue(us2.equals(jdk2));
    assertTrue(jdk2.equals(us2));
    assertFalse(us2.equals(null));
    assertFalse(us2.equals(us0));
    assertFalse(us2.equals(us1));
    assertTrue(us2.equals(us2));
    assertFalse(us2.equals(us3));
    assertFalse(us2.equals(usX));

    // many
    assertTrue(usX.equals(jdkX));
    assertTrue(jdkX.equals(usX));
    assertFalse(usX.equals(null));
    assertFalse(usX.equals(us0));
    assertFalse(usX.equals(us1));
    assertFalse(usX.equals(us2));
    assertFalse(usX.equals(us3));
    assertTrue(usX.equals(usX));
  }

  /*
  
  // getOnly is not public anymore
  
  @Test
  public void getOnly() {
    try {
      us0.getOnly();
  
      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: empty.");
    }
  
    assertEquals(us1.getOnly(), jdk1.iterator().next());
  
    try {
      us2.getOnly();
  
      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: more than one element.");
    }
  }
  */

  @Test
  public void hashCodeTest() {
    assertEquals(us0.hashCode(), jdk0.hashCode());
    assertEquals(us1.hashCode(), jdk1.hashCode());
    assertEquals(us2.hashCode(), jdk2.hashCode());
    assertEquals(us3.hashCode(), jdk3.hashCode());
    assertEquals(usX.hashCode(), jdkX.hashCode());
  }

  @Test
  public void isEmpty() {
    assertTrue(us0.isEmpty());
    assertFalse(us1.isEmpty());
    assertFalse(us2.isEmpty());
    assertFalse(us3.isEmpty());
    assertFalse(usX.isEmpty());
  }

  @Test
  public void iterator() {
    SetAssert.iterator(us0, jdk0);
    SetAssert.iterator(us1, jdk1);
    SetAssert.iterator(us2, jdk2);
    SetAssert.iterator(us3, jdk3);
    SetAssert.iterator(usX, jdkX);
  }

  @Test
  public void join() {
    assertEquals(us0.join(), "");
    assertEquals(us0.join("|"), "");
    assertEquals(us0.join("|", "{", "}"), "{}");

    assertEquals(us1.join(), t1.toString());
    assertEquals(us1.join("|"), t1.toString());
    assertEquals(us1.join("|", "{", "}"), "{" + t1 + "}");

    var iterator = us2.iterator();

    var o1 = iterator.next();

    var o2 = iterator.next();

    assertEquals(
        us2.join(),
        o1.toString() + o2.toString()
    );
    assertEquals(
        us2.join("|"),
        o1.toString() + "|" + o2.toString()
    );
    assertEquals(
        us2.join("|", "{", "}"),
        "{" + o1.toString() + "|" + o2.toString() + "}"
    );
  }

  @Test
  public void remove() {
    testAll((it, els) -> {
      try {
        it.remove(t1);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        SetAssert.iterator(it, els);
      }
    });
  }

  @Test
  public void removeAll() {
    testAll((it, els) -> {
      try {
        var all = SetAssert.all(els);

        it.removeAll(all);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        SetAssert.iterator(it, els);
      }
    });
  }

  @Test
  public void removeIf() {
    testAll((it, els) -> {
      try {
        it.removeIf(e -> e.equals(t1));

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        SetAssert.iterator(it, els);
      }
    });
  }

  @Test
  public void retainAll() {
    testAll((it, els) -> {
      try {
        var all = SetAssert.all(els);

        it.retainAll(all);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        SetAssert.iterator(it, els);
      }
    });
  }

  @Test
  public void size() {
    assertEquals(us0.size(), 0);
    assertEquals(us1.size(), 1);
    assertEquals(us2.size(), 2);
    assertEquals(us3.size(), 3);
    assertEquals(usX.size(), Thing.MANY);
  }

  @Test
  public void toArray() {
    var emptyArray = Thing.EMPTY_ARRAY;

    assertEquals(us0.toArray(), jdk0.toArray());
    assertSame(us0.toArray(emptyArray), jdk0.toArray(emptyArray));

    var lastNull = new Thing[jdkX.size() + 2];

    var result = us0.toArray(lastNull);

    assertSame(result, lastNull);
    assertNull(result[0]);
    assertNull(result[1]);

    assertEquals(us1.toArray(), jdk1.toArray());
    assertEquals(us1.toArray(emptyArray), jdk1.toArray(emptyArray));

    result = us1.toArray(lastNull);

    assertSame(result, lastNull);
    testToArray(result, t1);

    testToArray(us2.toArray(), jdk2.toArray());
    testToArray(us2.toArray(emptyArray), (Object[]) jdk2.toArray(emptyArray));

    result = us2.toArray(lastNull);

    assertSame(result, lastNull);
    testToArray(result, t1, t2);

    testToArray(usX.toArray(), jdkX.toArray());
    testToArray(usX.toArray(emptyArray), (Object[]) jdkX.toArray(emptyArray));

    result = usX.toArray(lastNull);

    assertSame(result, lastNull);
    testToArray(result, (Object[]) many);
  }

  @Test
  public void toStringTest() {
    assertEquals(us0.toString(), "[]");

    assertEquals(
        us1.toString(),

        "[Thing[%s]]".formatted(t1.toHexString())
    );

    var iterator = us2.iterator();

    var o1 = iterator.next();

    var o2 = iterator.next();

    assertEquals(
        us2.toString(),

        "[Thing[%s], Thing[%s]]".formatted(o1.toHexString(), o2.toHexString())
    );
  }

  private void testAll(Tester tester) {
    // empty
    tester.execute(UtilUnmodifiableSet.of());

    // one
    var t1 = Thing.next();
    tester.execute(UtilUnmodifiableSet.of(t1), t1);

    // two
    var t2 = Thing.next();
    tester.execute(UtilUnmodifiableSet.of(t1, t2), t1, t2);

    //many
    var many = Thing.nextArray();
    tester.execute(UtilUnmodifiableSet.copyOf(many), (Object) many);
  }

  private void testToArray(Object[] result, Object... expected) {
    var jdk = new HashSet<>(expected.length);

    for (var t : expected) {
      jdk.add(t);
    }

    for (int i = 0; i < expected.length; i++) {
      assertTrue(jdk.remove(result[i]));
    }

    if (result.length > expected.length) {
      assertNull(result[expected.length]);
    }
  }

  private static final class ArrayIterator<E> extends Util.UnmodifiableIterator<E> {

    private final Object[] array;

    private int index;

    private final int size;

    public ArrayIterator(Object[] array, int size) {
      this.array = array;
      this.size = size;
    }

    @Override
    public final boolean hasNext() {
      return index < size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      else {
        return (E) array[index++];
      }
    }

  }

}