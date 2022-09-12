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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.testng.annotations.Test;

public class UnmodifiableOrderedMapTest extends UnmodifiableMapTestAdapter {

  @Test
  public void clear() {
    var test = new UnmodifiableMapClearTest(this);

    test.execute();
  }

  @Test
  public void compute() {
    var test = new UnmodifiableMapComputeTest(this);

    test.execute();
  }

  @Test
  public void computeIfAbsent() {
    var test = new UnmodifiableMapComputeIfAbsentTest(this);

    test.execute();
  }

  @Test
  public void computeIfPresent() {
    var test = new UnmodifiableMapComputeIfPresentTest(this);

    test.execute();
  }

  @Test
  public void containsKey() {
    var test = new UnmodifiableMapContainsKeyTest(this);

    test.execute();
  }

  @Test
  public void containsValue() {
    var test = new UnmodifiableMapContainsValueTest(this);

    test.execute();
  }

  @Test
  public void entrySet() {
    var test = new UnmodifiableMapEntrySetTest(this);

    test.execute();
  }

  @Test
  public void equals() {
    var test = new UnmodifiableMapEqualsTest(this);

    test.execute();
  }

  @Test
  public void forEach() {
    var test = new UnmodifiableMapForEachTest(this);

    test.execute();
  }

  @Test
  public void get() {
    var test = new UnmodifiableMapGetTest(this);

    test.execute();
  }

  @Test
  public void getOrDefault() {
    var test = new UnmodifiableMapGetOrDefaultTest(this);

    test.execute();
  }

  @Test
  public void hashCodeTest() {
    var test = new UnmodifiableMapHashCodeTest(this);

    test.execute();
  }

  @Test
  public void isEmpty() {
    var test = new UnmodifiableMapIsEmptyTest(this);

    test.execute();
  }

  @Test
  public void keySet() {
    var test = new UnmodifiableMapKeySetTest(this);

    test.execute();
  }

  @Test
  public void merge() {
    var test = new UnmodifiableMapMergeTest(this);

    test.execute();
  }

  @Test
  public void put() {
    var test = new UnmodifiableMapPutTest(this);

    test.execute();
  }

  @Test
  public void putAll() {
    var test = new UnmodifiableMapPutAllTest(this);

    test.execute();
  }

  @Test
  public void putIfAbsent() {
    var test = new UnmodifiableMapPutIfAbsentTest(this);

    test.execute();
  }

  @Test
  public void remove() {
    var test = new UnmodifiableMapRemoveTest(this);

    test.execute();
  }

  @Test
  public void replace() {
    var test = new UnmodifiableMapReplaceTest(this);

    test.execute();
  }

  @Test
  public void replaceAll() {
    var test = new UnmodifiableMapReplaceAllTest(this);

    test.execute();
  }

  @Test
  public void size() {
    var test = new UnmodifiableMapSizeTest(this);

    test.execute();
  }

  @Test
  public void values() {
    var test = new UnmodifiableMapValuesTest(this);

    test.execute();
  }

  @Override
  final void assertContents(Map<Thing, String> map, Thing[] els) {
    assertEquals(map.size(), els.length);

    var index = 0;

    for (var entry : map.entrySet()) {
      var thing = els[index++];

      assertEquals(entry.getKey(), thing);

      assertEquals(entry.getValue(), thing.toDecimalString());
    }
  }

  @Override
  final <E> void assertSet(Set<E> set, Thing[] els, Function<Thing, E> function) {
    assertEquals(set.size(), els.length);

    var it = set.iterator();

    for (var thing : els) {
      E value = function.apply(thing);

      assertEquals(it.next(), value);
    }
  }

  @Override
  final Map<Thing, String> jdk(Thing... many) {
    var jdk = new LinkedHashMap<Thing, String>();

    for (var thing : many) {
      thing.putDec(jdk);
    }

    return jdk;
  }

  @Override
  final UnmodifiableMap<Thing, String> map0() {
    return UnmodifiableOrderedMap.orderedEmpty();
  }

  @Override
  final UnmodifiableMap<Thing, String> map1(Thing t1) {
    return mapX(new Thing[] {t1});
  }

  @Override
  final UnmodifiableMap<Thing, String> map2(Thing t1, Thing t2) {
    return mapX(new Thing[] {t1, t2});
  }

  @Override
  final UnmodifiableMap<Thing, String> map3(Thing t1, Thing t2, Thing t3) {
    return mapX(new Thing[] {t1, t2, t3});
  }

  @Override
  final UnmodifiableMap<Thing, String> mapX(Thing[] many) {
    var manyMap = new GrowableOrderedMap<Thing, String>();

    for (var thing : many) {
      thing.putDec(manyMap);
    }

    return manyMap.toUnmodifiableMap();
  }

}