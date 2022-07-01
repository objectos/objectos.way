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
import org.testng.annotations.Test;

public class UnmodifiableSetTest {

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
    var us0 = UnmodifiableSet.<Thing> of();

    var t1 = Thing.next();
    var us1 = UnmodifiableSet.of(t1);

    var t2 = Thing.next();
    var us2 = UnmodifiableSet.of(t1, t2);

    var t3 = Thing.next();
    var us3 = UnmodifiableSet.of(t1, t2, t3);

    var many = Thing.nextArray();
    var usX = UnmodifiableSet.copyOf(many);

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
    var us0 = UnmodifiableSet.<Thing> of();

    var t1 = Thing.next();
    var us1 = UnmodifiableSet.of(t1);

    var t2 = Thing.next();
    var us2 = UnmodifiableSet.of(t1, t2);

    var t3 = Thing.next();
    var us3 = UnmodifiableSet.of(t1, t2, t3);

    var many = Thing.nextArray();
    var usX = UnmodifiableSet.copyOf(many);
    var jdkX = Set.of(many);

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