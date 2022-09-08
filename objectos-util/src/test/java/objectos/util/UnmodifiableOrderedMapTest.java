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
import org.testng.annotations.Test;

public class UnmodifiableOrderedMapTest extends UnmodifiableMapFactory {

  @Test
  public void clear() {
    var test = new UnmodifiableMapClearTest(this, this::assertContents);

    test.execute();
  }

  @Override
  final UnmodifiableMap<Thing, String> mapOf() {
    return UnmodifiableOrderedMap.orderedEmpty();
  }

  @Override
  final UnmodifiableMap<Thing, String> mapOf(Thing t1) {
    return mapOf(new Thing[] {t1});
  }

  @Override
  final UnmodifiableMap<Thing, String> mapOf(Thing... many) {
    var manyMap = new GrowableOrderedMap<Thing, String>();

    for (var thing : many) {
      thing.putDec(manyMap);
    }

    return manyMap.toUnmodifiableMap();
  }

  @Override
  final UnmodifiableMap<Thing, String> mapOf(Thing t1, Thing t2) {
    return mapOf(new Thing[] {t1, t2});
  }

  @Override
  final UnmodifiableMap<Thing, String> mapOf(Thing t1, Thing t2, Thing t3) {
    return mapOf(new Thing[] {t1, t2, t3});
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