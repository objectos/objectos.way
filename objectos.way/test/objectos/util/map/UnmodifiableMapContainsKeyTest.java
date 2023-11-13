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
package objectos.util.map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

final class UnmodifiableMapContainsKeyTest {

  private final UnmodifiableMapTestAdapter adapter;

  public UnmodifiableMapContainsKeyTest(UnmodifiableMapTestAdapter adapter) {
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

    assertFalse(map0.containsKey(null));
    assertFalse(map1.containsKey(null));
    assertFalse(map2.containsKey(null));
    assertFalse(map3.containsKey(null));
    assertFalse(mapX.containsKey(null));

    assertFalse(map0.containsKey(t1));
    assertTrue(map1.containsKey(t1));
    assertTrue(map2.containsKey(t1));
    assertTrue(map3.containsKey(t1));
    assertFalse(mapX.containsKey(t1));

    assertFalse(map0.containsKey(t2));
    assertFalse(map1.containsKey(t2));
    assertTrue(map2.containsKey(t2));
    assertTrue(map3.containsKey(t2));
    assertFalse(mapX.containsKey(t2));

    assertFalse(map0.containsKey(t3));
    assertFalse(map1.containsKey(t3));
    assertFalse(map2.containsKey(t3));
    assertTrue(map3.containsKey(t3));
    assertFalse(mapX.containsKey(t3));

    for (var t : many) {
      assertFalse(map0.containsKey(t));
      assertFalse(map1.containsKey(t));
      assertFalse(map2.containsKey(t));
      assertFalse(map3.containsKey(t));
      assertTrue(mapX.containsKey(t));
    }
  }

}