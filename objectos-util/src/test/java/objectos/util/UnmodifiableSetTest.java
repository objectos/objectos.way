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
import static org.testng.Assert.assertSame;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UnmodifiableSetTest {

  @Test
  public void copyOf() {
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

}