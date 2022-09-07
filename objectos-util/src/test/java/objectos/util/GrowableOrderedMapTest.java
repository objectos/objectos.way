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
import static org.testng.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableOrderedMapTest {

  private GrowableOrderedMap<Thing, String> it;

  @BeforeClass
  public void _beforeClass() {
    it = new GrowableOrderedMap<>();
  }

  @BeforeMethod
  public void _beforeMethod() {
    it.clear();
  }

  @Test
  public void clear() {
    var test = new GrowableMapClearTest(it, this::assertContents);

    test.execute();
  }

  @Test
  public void containsKey() {
    var test = new GrowableMapContainsKeyTest(it);

    test.execute();
  }

  @Test
  public void containsValue() {
    var test = new GrowableMapContainsValueTest(it);

    test.execute();
  }

  @Test
  public void put() {
    var test = new GrowableMapPutTest(it, this::assertContents);

    test.execute();
  }

  @Test
  public void putAll() {
    var test = new GrowableMapPutAllTest(it, this::assertContents);

    test.execute();
  }

  @Test
  public void toUnmodifiableMap() {
    var test = new GrowableMapToUnmodifiableMapTest(it, this::assertContents);

    test.execute();
  }

  private void assertContents(Map<?, ?> map, Object... expected) {
    var jdk = new LinkedHashMap<Thing, String>();

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

    var jdkEntries = jdk.entrySet().iterator();

    for (var entry : map.entrySet()) {
      var jdkEntry = jdkEntries.next();

      assertEquals(entry.getKey(), jdkEntry.getKey());

      assertEquals(entry.getValue(), jdkEntry.getValue());

      jdkEntries.remove();
    }

    assertTrue(jdk.isEmpty());
  }

}