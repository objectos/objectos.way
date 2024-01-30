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

import static org.testng.Assert.assertNull;

import java.util.LinkedHashMap;

final class GrowableMapKeySetTest {

  private final GrowableMap<Thing, String> it;

  private final MapAssertContents assertContents;

  public GrowableMapKeySetTest(GrowableMap<Thing, String> it, MapAssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public final void execute() {
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

  private void assertKeySet(UnmodifiableView<Thing> set, Object... expected) {
    var map = new LinkedHashMap<Thing, String>();

    for (var key : set) {
      map.put(key, key.toDecimalString());
    }

    assertContents.execute(map, expected);
  }

}