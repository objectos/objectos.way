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
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UnmodifiableSetTest {

  private UnmodifiableSet<Thing> us0;

  private UnmodifiableSet<Thing> us1;

  private UnmodifiableSet<Thing> us2;

  private UnmodifiableSet<Thing> us3;

  private UnmodifiableSet<Thing> usX;

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
    us0 = UnmodifiableSet.of();
    jdk0 = Set.of();

    t1 = Thing.next();

    us1 = UnmodifiableSet.of(t1);
    jdk1 = Set.of(t1);

    t2 = Thing.next();

    us2 = UnmodifiableSet.of(t1, t2);
    jdk2 = Set.of(t1, t2);

    t3 = Thing.next();

    us3 = UnmodifiableSet.of(t1, t2, t3);
    jdk3 = Set.of(t1, t2, t3);

    many = Thing.nextArray();

    usX = UnmodifiableSet.copyOf(many);
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

    Consumer<UnmodifiableSet<Thing>> tester = l -> {
      Object a = array;

      SetAssert.iterator(l, a);
    };

    // must reject null
    try {
      Iterable<?> nullIterable = null;

      UnmodifiableSet.copyOf(nullIterable);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "elements == null");
    }

    // iterable
    var iterable = new ArrayBackedIterable<>(array);

    var ulIterable = UnmodifiableSet.copyOf(iterable);

    tester.accept(ulIterable);

    // UnmodifiableSet
    assertSame(UnmodifiableSet.copyOf(ulIterable), ulIterable);

    // GrowableList
    var growableList = new GrowableList<Thing>(array);

    tester.accept(UnmodifiableSet.copyOf(growableList));

    // List & RandomAccess
    var arrayList = new ArrayList<Thing>(Thing.MANY);

    for (var t : array) {
      arrayList.add(t);
    }

    tester.accept(UnmodifiableSet.copyOf(arrayList));

    // List & RandomAccess with null
    var arrayListWithNull = new ArrayList<>(arrayList);

    arrayListWithNull.set(Thing.HALF, null);

    try {
      UnmodifiableSet.copyOf(arrayListWithNull);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "elements[50] == null");
    }

    // Collection
    var collection = new ArrayDeque<Thing>(Thing.MANY);

    for (var t : array) {
      collection.add(t);
    }

    tester.accept(UnmodifiableSet.copyOf(collection));

    // Collection with null
    var collectionWithNull = new LinkedHashSet<>(arrayListWithNull);

    try {
      UnmodifiableSet.copyOf(collectionWithNull);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "elements[50] == null");
    }
  }

  @Test
  public void copyOf_iterator() {
    // empty
    var it = UnmodifiableSet.<Thing> copyOf(Collections.emptyIterator());

    assertSame(it, UnmodifiableSet.of());

    // one
    var t1 = Thing.next();

    it = UnmodifiableSet.copyOf(new SingletonIterator<>(t1));

    SetAssert.iterator(it, t1);

    // many
    var many = Thing.nextArray();

    it = UnmodifiableSet.copyOf(new ArrayIterator<>(many, many.length));

    SetAssert.iterator(it, (Object) many);

    // must reject null
    try {
      Iterator<?> nullIterator = null;

      UnmodifiableSet.copyOf(nullIterator);

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

  private void testAll(Tester tester) {
    // empty
    tester.execute(UnmodifiableSet.of());

    // one
    var t1 = Thing.next();
    tester.execute(UnmodifiableSet.of(t1), t1);

    // two
    var t2 = Thing.next();
    tester.execute(UnmodifiableSet.of(t1, t2), t1, t2);

    //many
    var many = Thing.nextArray();
    tester.execute(UnmodifiableSet.copyOf(many), (Object) many);
  }

  @FunctionalInterface
  interface Tester {
    void execute(UnmodifiableSet<Thing> it, Object... els);
  }

}