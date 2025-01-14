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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import objectos.util.Thing;

public final class UnmodifiableMapEqualsTest {

  private final UtilUnmodifiableMapTestAdapter adapter;

  public UnmodifiableMapEqualsTest(UtilUnmodifiableMapTestAdapter adapter) {
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

    var jdk0 = adapter.jdk();
    var jdk1 = adapter.jdk(t1);
    var jdk2 = adapter.jdk(t1, t2);
    var jdk3 = adapter.jdk(t1, t2, t3);
    var jdkX = adapter.jdk(many);

    // empty
    assertTrue(map0.equals(jdk0));
    assertTrue(jdk0.equals(map0));
    assertFalse(map0.equals(null));
    assertTrue(map0.equals(map0));
    assertFalse(map0.equals(map1));
    assertFalse(map0.equals(map2));
    assertFalse(map0.equals(map3));
    assertFalse(map0.equals(mapX));

    // one
    assertTrue(map1.equals(jdk1));
    assertTrue(jdk1.equals(map1));
    assertFalse(map1.equals(null));
    assertFalse(map1.equals(map0));
    assertTrue(map1.equals(map1));
    assertFalse(map1.equals(map2));
    assertFalse(map1.equals(map3));
    assertFalse(map1.equals(mapX));

    // two
    assertTrue(map2.equals(jdk2));
    assertTrue(jdk2.equals(map2));
    assertFalse(map2.equals(null));
    assertFalse(map2.equals(map0));
    assertFalse(map2.equals(map1));
    assertTrue(map2.equals(map2));
    assertFalse(map2.equals(map3));
    assertFalse(map2.equals(mapX));

    // three
    assertTrue(map3.equals(jdk3));
    assertTrue(jdk3.equals(map3));
    assertFalse(map3.equals(null));
    assertFalse(map3.equals(map0));
    assertFalse(map3.equals(map1));
    assertFalse(map3.equals(map2));
    assertTrue(map3.equals(map3));
    assertFalse(map3.equals(mapX));

    // many
    assertTrue(mapX.equals(jdkX));
    assertTrue(jdkX.equals(mapX));
    assertFalse(mapX.equals(null));
    assertFalse(mapX.equals(map0));
    assertFalse(mapX.equals(map1));
    assertFalse(mapX.equals(map2));
    assertFalse(mapX.equals(map3));
    assertTrue(mapX.equals(mapX));
  }

}