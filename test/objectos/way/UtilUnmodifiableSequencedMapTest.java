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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import objectos.util.Thing;
import org.testng.annotations.Test;

public class UtilUnmodifiableSequencedMapTest extends UtilUnmodifiableMapTestAdapter {

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
  public void toStringTest() {
    var map0 = map0();

    assertEquals(map0.toString(), "{}");

    var t1 = Thing.next();

    var map1 = map1(t1);

    assertEquals(
        map1.toString(),

        "{%s=%s}".formatted(t1, t1.toDecimalString())
    );

    var t2 = Thing.next();

    var map2 = map2(t1, t2);

    assertEquals(
        map2.toString(),

        "{%s=%s, %s=%s}".formatted(
            t1, t1.toDecimalString(),
            t2, t2.toDecimalString())
    );

    var t3 = Thing.next();

    var map3 = map3(t1, t2, t3);

    assertEquals(
        map3.toString(),

        "{%s=%s, %s=%s, %s=%s}".formatted(
            t1, t1.toDecimalString(),
            t2, t2.toDecimalString(),
            t3, t3.toDecimalString())
    );
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
  final <E> void assertSet(Collection<E> set, Thing[] els, Function<Thing, E> function) {
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
  final UtilUnmodifiableMap<Thing, String> map0() {
    return UtilUnmodifiableSequencedMap.orderedEmpty();
  }

  @Override
  final UtilUnmodifiableMap<Thing, String> map1(Thing t1) {
    return mapX(new Thing[] {t1});
  }

  @Override
  final UtilUnmodifiableMap<Thing, String> map2(Thing t1, Thing t2) {
    return mapX(new Thing[] {t1, t2});
  }

  @Override
  final UtilUnmodifiableMap<Thing, String> map3(Thing t1, Thing t2, Thing t3) {
    return mapX(new Thing[] {t1, t2, t3});
  }

  @Override
  final UtilUnmodifiableMap<Thing, String> mapX(Thing[] many) {
    var manyMap = new UtilSequencedMap<Thing, String>();

    for (var thing : many) {
      thing.putDec(manyMap);
    }

    return manyMap.toUnmodifiableMap();
  }

}