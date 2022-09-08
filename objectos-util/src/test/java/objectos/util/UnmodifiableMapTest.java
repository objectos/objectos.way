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

import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UnmodifiableMapTest {

  @FunctionalInterface
  interface Tester {
    void execute(UnmodifiableMap<Thing, String> it, Thing... els);
  }

  private UnmodifiableMap<Thing, String> um0;

  private UnmodifiableMap<Thing, String> um1;

  private UnmodifiableMap<Thing, String> um2;

  private UnmodifiableMap<Thing, String> um3;

  private UnmodifiableMap<Thing, String> umX;

  private Map<Thing, String> jdk0;

  private Map<Thing, String> jdk1;

  private Map<Thing, String> jdk2;

  private Map<Thing, String> jdk3;

  private Map<Thing, String> jdkX;

  private Thing t1;

  private Thing t2;

  private Thing t3;

  private Thing[] many;

  @BeforeClass
  public void _beforeClass() {
    um0 = UnmodifiableMap.of();
    jdk0 = Map.of();

    t1 = Thing.next();

    um1 = UnmodifiableMap.of(t1, t1.toDecimalString());
    jdk1 = Map.of(t1, t1.toDecimalString());

    t2 = Thing.next();

    um2 = UnmodifiableMap.of(
      t1, t1.toDecimalString(),
      t2, t2.toDecimalString());
    jdk2 = Map.of(
      t1, t1.toDecimalString(),
      t2, t2.toDecimalString());

    t3 = Thing.next();

    um3 = UnmodifiableMap.of(
      t1, t1.toDecimalString(),
      t2, t2.toDecimalString(),
      t3, t3.toDecimalString());
    jdk3 = Map.of(
      t1, t1.toDecimalString(),
      t2, t2.toDecimalString(),
      t3, t3.toDecimalString());

    many = Thing.nextArray();

    var manyMap = new GrowableMap<Thing, String>();

    for (var thing : many) {
      thing.putDec(manyMap);
    }

    umX = manyMap.toUnmodifiableMap();
    jdkX = Map.copyOf(manyMap);
  }

  @Test
  public void clear() {
    testAll((it, els) -> {
      try {
        it.clear();

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        assertContents(it, els);
      }
    });
  }

  @Test
  public void compute() {
    testAll((it, els) -> {
      try {
        var t = Thing.next();

        it.compute(t, (k, v) -> t.toHexString());

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        assertContents(it, els);
      }
    });
  }

  @Test
  public void computeIfAbsent() {
    testAll((it, els) -> {
      try {
        var t = Thing.next();

        it.computeIfAbsent(t, k -> k.toHexString());

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        assertContents(it, els);
      }
    });
  }

  @Test
  public void computeIfPresent() {
    testAll((it, els) -> {
      try {
        var t = Thing.next();

        it.computeIfPresent(t, (k, v) -> k.toHexString());

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        assertContents(it, els);
      }
    });
  }

  @Test
  public void equals() {
    // empty
    assertTrue(um0.equals(jdk0));
    assertTrue(jdk0.equals(um0));
    assertFalse(um0.equals(null));
    assertTrue(um0.equals(um0));
    assertFalse(um0.equals(um1));
    assertFalse(um0.equals(um2));
    assertFalse(um0.equals(um3));
    assertFalse(um0.equals(umX));

    // one
    assertTrue(um1.equals(jdk1));
    assertTrue(jdk1.equals(um1));
    assertFalse(um1.equals(null));
    assertFalse(um1.equals(um0));
    assertTrue(um1.equals(um1));
    assertFalse(um1.equals(um2));
    assertFalse(um1.equals(um3));
    assertFalse(um1.equals(umX));

    // two
    assertTrue(um2.equals(jdk2));
    assertTrue(jdk2.equals(um2));
    assertFalse(um2.equals(null));
    assertFalse(um2.equals(um0));
    assertFalse(um2.equals(um1));
    assertTrue(um2.equals(um2));
    assertFalse(um2.equals(um3));
    assertFalse(um2.equals(umX));

    // three
    assertTrue(um3.equals(jdk3));
    assertTrue(jdk3.equals(um3));
    assertFalse(um3.equals(null));
    assertFalse(um3.equals(um0));
    assertFalse(um3.equals(um1));
    assertFalse(um3.equals(um2));
    assertTrue(um3.equals(um3));
    assertFalse(um3.equals(umX));

    // many
    assertTrue(umX.equals(jdkX));
    assertTrue(jdkX.equals(umX));
    assertFalse(umX.equals(null));
    assertFalse(umX.equals(um0));
    assertFalse(umX.equals(um1));
    assertFalse(umX.equals(um2));
    assertFalse(umX.equals(um3));
    assertTrue(umX.equals(umX));
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

  private void assertContents(UnmodifiableMap<Thing, String> it, Thing[] els) {
    assertEquals(it.size, els.length);

    for (var thing : els) {
      assertEquals(it.get(thing), thing.toDecimalString());
    }
  }

  private void testAll(Tester tester) {
    tester.execute(um0);

    tester.execute(um1, t1);

    tester.execute(um2, t1, t2);

    tester.execute(um3, t1, t2, t3);

    tester.execute(umX, many);
  }

}