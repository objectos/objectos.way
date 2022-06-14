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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MutableMapTest extends AbstractObjectosMapsTest {

  private MutableMap<Thing, String> map;

  @BeforeClass
  public void _beforeClass() {
    map = createGrowableMap();
  }

  @BeforeMethod
  public void _beforeMethod() {
    map.clear();
  }

  @Test
  public void testCase08() {
    // empty
    assertEquals(map.size(), 0);
    assertTrue(map.isEmpty());

    ImmutableMap<Thing, String> result;
    result = map.toImmutableMap();

    Map<Thing, String> expected;
    expected = createExpectedMap();

    assertContents(result, expected);

    // one
    assertNull(putHex(map, t1));

    assertEquals(map.size(), 1);
    assertFalse(map.isEmpty());
    assertTrue(map.containsKey(t1));
    assertFalse(map.containsKey(t2));
    assertFalse(map.containsKey(t3));

    result = map.toImmutableMap();

    expected.clear();

    assertNull(putHex(expected, t1));

    assertContents(result, expected);

    // two
    assertNull(putHex(map, t2));

    assertEquals(map.size(), 2);
    assertFalse(map.isEmpty());
    assertTrue(map.containsKey(t1));
    assertTrue(map.containsKey(t2));
    assertFalse(map.containsKey(t3));

    result = map.toImmutableMap();

    expected.clear();

    assertNull(putHex(expected, t1));
    assertNull(putHex(expected, t2));

    assertContents(result, expected);

    // many
    for (Thing thing : thingArray) {
      assertNull(putHex(map, thing));
    }

    assertEquals(map.size(), 2 + thingArray.length);
    assertFalse(map.isEmpty());
    assertTrue(map.containsKey(t1));
    assertTrue(map.containsKey(t2));
    assertFalse(map.containsKey(t3));

    for (Thing thing : thingArray) {
      assertTrue(map.containsKey(thing));
    }

    result = map.toImmutableMap();

    expected.clear();

    assertNull(putHex(expected, t1));
    assertNull(putHex(expected, t2));

    for (Thing thing : thingArray) {
      assertNull(putHex(expected, thing));
    }

    assertContents(result, expected);
  }

  @Test
  public void testCase09() {
    // one
    assertNull(putHex(map, t1));
    assertEquals(putDec(map, t1), t1.toHexString());

    assertEquals(map.size(), 1);
    assertFalse(map.isEmpty());
    assertTrue(map.containsKey(t1));
    assertFalse(map.containsKey(t2));
    assertFalse(map.containsKey(t3));

    ImmutableMap<Thing, String> result;
    result = map.toImmutableMap();

    Map<Thing, String> expected;
    expected = createExpectedMap();

    assertNull(putDec(expected, t1));

    assertContents(result, expected);

    // two
    assertNull(putHex(map, t2));
    assertEquals(putDec(map, t2), t2.toHexString());

    assertEquals(map.size(), 2);
    assertFalse(map.isEmpty());
    assertTrue(map.containsKey(t1));
    assertTrue(map.containsKey(t2));
    assertFalse(map.containsKey(t3));

    result = map.toImmutableMap();

    expected.clear();

    assertNull(putDec(expected, t1));
    assertNull(putDec(expected, t2));

    assertContents(result, expected);

    // many
    for (Thing thing : thingArray) {
      assertNull(putHex(map, thing));
      assertEquals(putDec(map, thing), thing.toHexString());
    }

    assertEquals(map.size(), 2 + thingArray.length);

    result = map.toImmutableMap();

    expected.clear();

    assertNull(putDec(expected, t1));
    assertNull(putDec(expected, t2));

    for (Thing thing : thingArray) {
      assertNull(putDec(expected, thing));
    }

    assertContents(result, expected);
  }

  <K, V> void assertContents(Map<Thing, String> result, Map<Thing, String> expected) {
    assertMap(result, expected);
  }

  <K, V> Map<K, V> createExpectedMap() {
    return new HashMap<>();
  }

  <K, V> MutableMap<K, V> createGrowableMap() {
    return new MutableMap<>();
  }

  private String putDec(Map<Thing, String> map, Thing key) {
    String value;
    value = key.toDecimalString();

    return map.put(key, value);
  }

  private String putHex(Map<Thing, String> map, Thing key) {
    String value;
    value = key.toHexString();

    return map.put(key, value);
  }

}