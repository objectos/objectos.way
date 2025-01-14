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

import static org.testng.Assert.assertEquals;

import objectos.util.Thing;

public final class UnmodifiableMapHashCodeTest {

  private final UtilUnmodifiableMapTestAdapter adapter;

  public UnmodifiableMapHashCodeTest(UtilUnmodifiableMapTestAdapter adapter) {
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

    assertEquals(map0.hashCode(), jdk0.hashCode());
    assertEquals(map1.hashCode(), jdk1.hashCode());
    assertEquals(map2.hashCode(), jdk2.hashCode());
    assertEquals(map3.hashCode(), jdk3.hashCode());
    assertEquals(mapX.hashCode(), jdkX.hashCode());
  }

}