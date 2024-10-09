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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import objectos.util.Hex;
import objectos.util.Thing;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UtilMapTest {

  private UtilMap<Thing, String> it;

  @BeforeClass
  public void _beforeClass() {
    it = new UtilMap<>();
  }

  @BeforeMethod
  public void _beforeMethod() {
    it.clear();
  }

  @Test
  public void clear() {
    // empty
    assertEquals(it.size(), 0);

    it.clear();
    assertContents(it);

    // many
    var array = Thing.nextArray();

    for (int i = 0; i < array.length; i++) {
      var t = array[i];

      assertNull(t.putDec(it));
    }

    it.clear();
    assertContents(it);
  }

  @Test
  public void compute() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.compute(t2, (k, v) -> t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertEquals(it.size(), 3);
    }
  }

  @Test
  public void computeIfAbsent() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.computeIfAbsent(t2, k -> k.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertEquals(it.size(), 3);
    }
  }

  @Test
  public void computeIfPresent() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.computeIfPresent(t2, (k, v) -> t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertEquals(it.size(), 3);
    }
  }

  @Test
  public void containsKey() {
    var t1 = Thing.next();

    var t2 = Thing.next();

    var t3 = Thing.next();

    assertFalse(it.containsKey(t1));
    assertFalse(it.containsKey(t2));
    assertFalse(it.containsKey(t3));
    assertFalse(it.containsKey(null));

    t1.putDec(it);

    assertTrue(it.containsKey(t1));
    assertFalse(it.containsKey(t2));
    assertFalse(it.containsKey(t3));
    assertFalse(it.containsKey(null));

    t2.putDec(it);

    assertTrue(it.containsKey(t1));
    assertTrue(it.containsKey(t2));
    assertFalse(it.containsKey(t3));
    assertFalse(it.containsKey(null));

    t3.putDec(it);

    assertTrue(it.containsKey(t1));
    assertTrue(it.containsKey(t2));
    assertTrue(it.containsKey(t3));
    assertFalse(it.containsKey(null));

    it.clear();

    assertFalse(it.containsKey(t1));
    assertFalse(it.containsKey(t2));
    assertFalse(it.containsKey(t3));
    assertFalse(it.containsKey(null));
  }

  @Test
  public void containsValue() {
    var t1 = Thing.next();
    var v1 = t1.toDecimalString();

    var t2 = Thing.next();
    var v2 = t2.toDecimalString();

    var t3 = Thing.next();
    var v3 = t3.toDecimalString();

    assertFalse(it.containsValue(v1));
    assertFalse(it.containsValue(v2));
    assertFalse(it.containsValue(v3));
    assertFalse(it.containsKey(null));

    t1.putDec(it);

    assertTrue(it.containsValue(v1));
    assertFalse(it.containsValue(v2));
    assertFalse(it.containsValue(v3));
    assertFalse(it.containsKey(null));

    t2.putDec(it);

    assertTrue(it.containsValue(v1));
    assertTrue(it.containsValue(v2));
    assertFalse(it.containsValue(v3));
    assertFalse(it.containsKey(null));

    t3.putDec(it);

    assertTrue(it.containsValue(v1));
    assertTrue(it.containsValue(v2));
    assertTrue(it.containsValue(v3));
    assertFalse(it.containsKey(null));

    it.clear();

    assertFalse(it.containsValue(v1));
    assertFalse(it.containsValue(v2));
    assertFalse(it.containsValue(v3));
    assertFalse(it.containsKey(null));
  }

  @Test
  public void entrySet() {
    var set = it.entrySet();

    assertEntrySet(set);

    var t1 = Thing.next();
    t1.putDec(it);

    assertEntrySet(set, t1);

    var t2 = Thing.next();
    t2.putDec(it);

    assertEntrySet(set, t1, t2);

    var t3 = Thing.next();
    t3.putDec(it);

    assertEntrySet(set, t1, t2, t3);

    var t4 = Thing.next();
    t4.putDec(it);

    assertEntrySet(set, t1, t2, t3, t4);

    it.clear();

    assertEntrySet(set);

    var array = Thing.nextArray();

    for (int i = 0; i < array.length; i++) {
      var t = array[i];
      assertNull(t.putDec(it));
    }

    assertEntrySet(set, (Object) array);
  }

  private void assertEntrySet(Util.UnmodifiableView<Entry<Thing, String>> set, Object... expected) {
    var map = new LinkedHashMap<Thing, String>();

    for (var entry : set) {
      map.put(entry.getKey(), entry.getValue());
    }

    assertContents(map, expected);
  }

  @Test
  public void forEach() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    assertNull(t1.putDec(it));
    assertNull(t2.putDec(it));
    assertNull(t3.putDec(it));

    var jdk = new LinkedHashMap<Thing, String>();

    it.forEach(jdk::put);

    assertContents(jdk, t1, t2, t3);
  }

  @Test
  public void get() {
    var t1 = Thing.next();

    assertNull(it.get(t1));

    assertNull(t1.putDec(it));

    assertEquals(it.get(t1), t1.toDecimalString());

    var t2 = Thing.next();

    assertNull(it.get(t2));

    assertNull(t2.putDec(it));

    assertEquals(it.get(t2), t2.toDecimalString());

    var array = Thing.nextArray();

    for (var thing : array) {
      assertNull(thing.putDec(it));

      assertEquals(it.get(thing), thing.toDecimalString());
    }
  }

  @Test
  public void getOrDefault() {
    var t1 = Thing.next();

    assertEquals(it.getOrDefault(t1, "def"), "def");

    assertNull(t1.putDec(it));

    assertEquals(it.getOrDefault(t1, "def"), t1.toDecimalString());
  }

  @Test
  public void hashCodeTest() {
    var jdk = new HashMap<Thing, String>();

    assertEquals(it.hashCode(), jdk.hashCode());

    var t1 = Thing.next();

    t1.putDec(it);
    t1.putDec(jdk);

    assertEquals(it.hashCode(), jdk.hashCode());

    var t2 = Thing.next();

    t2.putDec(it);
    t2.putDec(jdk);

    assertEquals(it.hashCode(), jdk.hashCode());

    var t3 = Thing.next();

    t3.putDec(it);
    t3.putDec(jdk);

    assertEquals(it.hashCode(), jdk.hashCode());
  }

  @Test
  public void isEmpty() {
    assertTrue(it.isEmpty());

    var thing = Thing.next();

    thing.putDec(it);

    assertFalse(it.isEmpty());

    it.clear();

    assertTrue(it.isEmpty());
  }

  @Test
  public void keySet() {
    var set = it.keySet();

    assertKeySet(set);

    var t1 = Thing.next();
    t1.putDec(it);

    assertKeySet(set, t1);

    var t2 = Thing.next();
    t2.putDec(it);

    assertKeySet(set, t1, t2);

    var t3 = Thing.next();
    t3.putDec(it);

    assertKeySet(set, t1, t2, t3);

    var t4 = Thing.next();
    t4.putDec(it);

    assertKeySet(set, t1, t2, t3, t4);

    it.clear();

    assertKeySet(set);

    var array = Thing.nextArray();

    for (int i = 0; i < array.length; i++) {
      var t = array[i];
      assertNull(t.putDec(it));
    }

    assertKeySet(set, (Object) array);
  }

  private void assertKeySet(Util.UnmodifiableView<Thing> set, Object... expected) {
    var map = new LinkedHashMap<Thing, String>();

    for (var key : set) {
      map.put(key, key.toDecimalString());
    }

    assertContents(map, expected);
  }

  @Test
  public void merge() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.merge(t2, "foo", (k, v) -> t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertEquals(it.size(), 3);
    }
  }

  @Test
  public void put() {
    // empty
    assertEquals(it.size(), 0);

    // one
    var t1 = Thing.next();

    assertNull(t1.putHex(it));
    assertContents(it, new Hex(t1));

    // two (must allow duplicate elements)
    assertEquals(t1.putDec(it), t1.toHexString());
    assertContents(it, t1);

    // many
    var array = Thing.nextArray();

    for (int i = 0; i < array.length; i++) {
      var t = array[i];

      assertNull(t.putDec(it));
    }

    assertContents(it, t1, array);

    // must reject null keys
    try {
      Thing t = null;

      it.put(t, "x");

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "key == null");
    }

    // must reject null values
    try {
      Thing t = Thing.next();

      it.put(t, null);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "value == null");
    }
  }

  @Test
  public void putAll() {
    assertEquals(it.size(), 0);
    assertContents(it);

    Thing k1;
    k1 = Thing.next();

    Thing k2;
    k2 = Thing.next();

    var map = Map.of(
        k1, "1",
        k2, "2"
    );

    it.putAll(map);

    assertEquals(it.size(), 2);
    assertEquals(it.get(k1), "1");
    assertEquals(it.get(k2), "2");
  }

  @Test
  public void putIfAbsent() {
    assertEquals(it.size(), 0);

    var t1 = Thing.next();

    assertEquals(it.putIfAbsent(t1, "foo"), null);
    assertEquals(it.size(), 1);

    assertEquals(it.putIfAbsent(t1, "bar"), "foo");
    assertEquals(it.size(), 1);
  }

  @Test
  public void remove() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.remove(t2);

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertContents(it, t1, t2, t3);
    }

    try {
      it.remove(t2, t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertContents(it, t1, t2, t3);
    }

    try {
      it.remove(t2, t2.toDecimalString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertContents(it, t1, t2, t3);
    }
  }

  @Test
  public void replace() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.replace(t2, t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertContents(it, t1, t2, t3);
    }

    try {
      it.replace(t2, t2.toDecimalString(), t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertContents(it, t1, t2, t3);
    }
  }

  @Test
  public void replaceAll() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.replaceAll((k, v) -> k.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertContents(it, t1, t2, t3);
    }
  }

  @Test
  public void size() {
    assertEquals(it.size(), 0);

    var t1 = Thing.next();

    t1.putDec(it);

    assertEquals(it.size(), 1);

    var t2 = Thing.next();

    t2.putDec(it);

    assertEquals(it.size(), 2);

    var t3 = Thing.next();

    t3.putDec(it);

    assertEquals(it.size(), 3);
  }

  @Test
  public void toStringTest() {
    assertEquals(it.toString(), "UtilMap []");

    var t1 = Thing.next();

    t1.putHex(it);

    assertEquals(
        it.toString(),

        """
        UtilMap [
          %s = %s
        ]""".formatted(t1, t1.toHexString())
    );

    var t2 = Thing.next();

    t2.putHex(it);

    var iterator = it.keySet().iterator();

    var e1 = iterator.next();

    var e2 = iterator.next();

    assertEquals(
        it.toString(),

        """
        UtilMap [
          %s = %s
          %s = %s
        ]""".formatted(e1, e1.toHexString(), e2, e2.toHexString())
    );
  }

  @Test
  public void toUnmodifiableMap() {
    var um0 = it.toUnmodifiableMap();

    var t1 = Thing.next();
    assertNull(t1.putDec(it));

    var um1 = it.toUnmodifiableMap();

    var t2 = Thing.next();
    assertNull(t2.putDec(it));

    var um2 = it.toUnmodifiableMap();

    var t3 = Thing.next();
    assertNull(t3.putDec(it));

    var um3 = it.toUnmodifiableMap();

    assertEquals(um0.size(), 0);
    assertContents(um0);

    assertEquals(um1.size(), 1);
    assertContents(um1, t1);

    assertEquals(um2.size(), 2);
    assertContents(um2, t1, t2);

    assertEquals(um3.size(), 3);
    assertContents(um3, t1, t2, t3);
    it.clear();

    var array = Thing.nextArray();

    for (var thing : array) {
      thing.putDec(it);
    }

    var umX = it.toUnmodifiableMap();

    assertEquals(umX.size(), array.length);
    assertContents(umX, (Object) array);
  }

  @Test
  public void values() {
    var set = it.values();

    assertValues(set);

    var t1 = Thing.next();
    t1.putDec(it);

    assertValues(set, t1);

    var t2 = Thing.next();
    t2.putDec(it);

    assertValues(set, t1, t2);

    var t3 = Thing.next();
    t3.putDec(it);

    assertValues(set, t1, t2, t3);

    var t4 = Thing.next();
    t4.putDec(it);

    assertValues(set, t1, t2, t3, t4);

    it.clear();

    assertValues(set);

    var array = Thing.nextArray();

    for (int i = 0; i < array.length; i++) {
      var t = array[i];
      assertNull(t.putDec(it));
    }

    assertValues(set, array);
  }

  private void assertContents(Map<?, ?> map, Object... expected) {
    var jdk = new HashMap<Thing, String>();

    for (var o : expected) {
      if (o instanceof Thing t) {
        t.putDec(jdk);
      } else if (o instanceof Thing[] a) {
        for (var t : a) {
          t.putDec(jdk);
        }
      } else if (o instanceof Hex hex) {
        var t = hex.value();

        t.putHex(jdk);
      } else {
        throw new UnsupportedOperationException("Implement me: " + o.getClass());
      }
    }

    for (var entry : map.entrySet()) {
      var key = entry.getKey();

      var value = entry.getValue();

      assertEquals(jdk.remove(key), value);
    }

    assertTrue(jdk.isEmpty());
  }

  private void assertValues(Collection<String> set, Thing... expected) {
    var jdk = new HashSet<String>();

    for (var o : expected) {
      jdk.add(o.toDecimalString());
    }

    for (var value : set) {
      assertTrue(jdk.remove(value));
    }

    assertTrue(jdk.isEmpty());
  }

}