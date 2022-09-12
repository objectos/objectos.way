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

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UnmodifiableMapTest extends UnmodifiableMapTestAdapter {

  private UnmodifiableMap<Thing, String> um0;

  private UnmodifiableMap<Thing, String> um1;

  private UnmodifiableMap<Thing, String> um2;

  private UnmodifiableMap<Thing, String> um3;

  private UnmodifiableMap<Thing, String> umX;

  private Thing t1;

  private Thing t2;

  private Thing t3;

  private Thing[] many;

  @BeforeClass
  public void _beforeClass() {
    um0 = UnmodifiableMap.of();

    t1 = Thing.next();

    um1 = UnmodifiableMap.of(t1, t1.toDecimalString());

    t2 = Thing.next();

    um2 = UnmodifiableMap.of(
      t1, t1.toDecimalString(),
      t2, t2.toDecimalString());

    t3 = Thing.next();

    um3 = UnmodifiableMap.of(
      t1, t1.toDecimalString(),
      t2, t2.toDecimalString(),
      t3, t3.toDecimalString());

    many = Thing.nextArray();

    var manyMap = new GrowableMap<Thing, String>();

    for (var thing : many) {
      thing.putDec(manyMap);
    }

    umX = manyMap.toUnmodifiableMap();
  }

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

  }

  @Test
  public void get() {
    assertEquals(um0.get(null), null);
    assertEquals(um0.get(t1), null);
    assertEquals(um0.get(t2), null);
    assertEquals(um0.get(t3), null);

    assertEquals(um1.get(null), null);
    assertEquals(um1.get(t1), t1.toDecimalString());
    assertEquals(um1.get(t2), null);
    assertEquals(um1.get(t3), null);

    assertEquals(um2.get(null), null);
    assertEquals(um2.get(t1), t1.toDecimalString());
    assertEquals(um2.get(t2), t2.toDecimalString());
    assertEquals(um2.get(t3), null);

    assertEquals(um3.get(null), null);
    assertEquals(um3.get(t1), t1.toDecimalString());
    assertEquals(um3.get(t2), t2.toDecimalString());
    assertEquals(um3.get(t3), t3.toDecimalString());

    for (var thing : many) {
      assertEquals(umX.get(thing), thing.toDecimalString());
    }
  }

  @Override
  final void assertContents(Map<Thing, String> it, Thing[] els) {
    assertEquals(it.size(), els.length);

    for (var thing : els) {
      assertEquals(it.get(thing), thing.toDecimalString());
    }
  }

  @Override
  final Map<Thing, String> jdk(Thing... many) {
    var jdk = new HashMap<Thing, String>();

    for (var thing : many) {
      thing.putDec(jdk);
    }

    return jdk;
  }

  @Override
  final UnmodifiableMap<Thing, String> map0() {
    return UnmodifiableMap.of();
  }

  @Override
  final UnmodifiableMap<Thing, String> map1(Thing t1) {
    return UnmodifiableMap.of(
      t1, t1.toDecimalString());
  }

  @Override
  final UnmodifiableMap<Thing, String> map2(Thing t1, Thing t2) {
    return UnmodifiableMap.of(
      t1, t1.toDecimalString(),
      t2, t2.toDecimalString());
  }

  @Override
  final UnmodifiableMap<Thing, String> map3(Thing t1, Thing t2, Thing t3) {
    return UnmodifiableMap.of(
      t1, t1.toDecimalString(),
      t2, t2.toDecimalString(),
      t3, t3.toDecimalString());
  }

  @Override
  final UnmodifiableMap<Thing, String> mapX(Thing[] many) {
    var manyMap = new GrowableMap<Thing, String>();

    for (var thing : many) {
      thing.putDec(manyMap);
    }

    return manyMap.toUnmodifiableMap();
  }

}