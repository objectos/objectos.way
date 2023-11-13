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

import static org.testng.Assert.assertEquals;

final class UnmodifiableMapGetOrDefaultTest {

  private final UnmodifiableMapTestAdapter adapter;

  public UnmodifiableMapGetOrDefaultTest(UnmodifiableMapTestAdapter adapter) {
    this.adapter = adapter;
  }

  public final void execute() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();
    var many = Thing.nextArray();

    var df = Thing.next();

    var map0 = adapter.map0();
    var map1 = adapter.map1(t1);
    var map2 = adapter.map2(t1, t2);
    var map3 = adapter.map3(t1, t2, t3);
    var mapX = adapter.mapX(many);

    assertEquals(map0.getOrDefault(df, "def"), "def");
    assertEquals(map0.getOrDefault(t1, "def"), "def");
    assertEquals(map0.getOrDefault(t2, "def"), "def");
    assertEquals(map0.getOrDefault(t3, "def"), "def");

    assertEquals(map1.getOrDefault(df, "def"), "def");
    assertEquals(map1.getOrDefault(t1, "def"), t1.toDecimalString());
    assertEquals(map1.getOrDefault(t2, "def"), "def");
    assertEquals(map1.getOrDefault(t3, "def"), "def");

    assertEquals(map2.getOrDefault(df, "def"), "def");
    assertEquals(map2.getOrDefault(t1, "def"), t1.toDecimalString());
    assertEquals(map2.getOrDefault(t2, "def"), t2.toDecimalString());
    assertEquals(map2.getOrDefault(t3, "def"), "def");

    assertEquals(map3.getOrDefault(df, "def"), "def");
    assertEquals(map3.getOrDefault(t1, "def"), t1.toDecimalString());
    assertEquals(map3.getOrDefault(t2, "def"), t2.toDecimalString());
    assertEquals(map3.getOrDefault(t3, "def"), t3.toDecimalString());

    assertEquals(mapX.getOrDefault(df, "def"), "def");
    for (var thing : many) {
      assertEquals(mapX.getOrDefault(thing, "def"), thing.toDecimalString());
    }
  }

}