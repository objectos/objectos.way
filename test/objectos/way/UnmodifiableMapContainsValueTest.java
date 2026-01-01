/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import objectos.util.Thing;

public final class UnmodifiableMapContainsValueTest {

  private final UtilUnmodifiableMapTestAdapter adapter;

  public UnmodifiableMapContainsValueTest(UtilUnmodifiableMapTestAdapter adapter) {
    this.adapter = adapter;
  }

  public final void execute() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();
    var many = Thing.nextArray();

    var map0 = adapter.map0();
    var map1 = adapter.map1(t1);
    var map2 = adapter.map2(t1, t2);
    var map3 = adapter.map3(t1, t2, t3);
    var mapX = adapter.mapX(many);

    assertFalse(map0.containsValue(null));
    assertFalse(map1.containsValue(null));
    assertFalse(map2.containsValue(null));
    assertFalse(map3.containsValue(null));
    assertFalse(mapX.containsValue(null));

    var v1 = t1.toDecimalString();

    assertFalse(map0.containsValue(v1));
    assertTrue(map1.containsValue(v1));
    assertTrue(map2.containsValue(v1));
    assertTrue(map3.containsValue(v1));
    assertFalse(mapX.containsValue(v1));

    var v2 = t2.toDecimalString();

    assertFalse(map0.containsValue(v2));
    assertFalse(map1.containsValue(v2));
    assertTrue(map2.containsValue(v2));
    assertTrue(map3.containsValue(v2));
    assertFalse(mapX.containsValue(v2));

    var v3 = t3.toDecimalString();

    assertFalse(map0.containsValue(v3));
    assertFalse(map1.containsValue(v3));
    assertFalse(map2.containsValue(v3));
    assertTrue(map3.containsValue(v3));
    assertFalse(mapX.containsValue(v3));

    for (var t : many) {
      var v = t.toDecimalString();

      assertFalse(map0.containsValue(v));
      assertFalse(map1.containsValue(v));
      assertFalse(map2.containsValue(v));
      assertFalse(map3.containsValue(v));
      assertTrue(mapX.containsValue(v));
    }
  }

}