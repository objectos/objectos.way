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
import static org.testng.Assert.assertNull;

import objectos.util.MapAssertContents;
import objectos.util.Thing;

final class GrowableMapToUnmodifiableMapTest {

  private final UtilGrowableMap<Thing, String> it;

  private final MapAssertContents assertContents;

  public GrowableMapToUnmodifiableMapTest(
      UtilGrowableMap<Thing, String> it,
      MapAssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public final void execute() {
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
    assertContents.execute(um0);

    assertEquals(um1.size(), 1);
    assertContents.execute(um1, t1);

    assertEquals(um2.size(), 2);
    assertContents.execute(um2, t1, t2);

    assertEquals(um3.size(), 3);
    assertContents.execute(um3, t1, t2, t3);
    it.clear();

    var array = Thing.nextArray();

    for (var thing : array) {
      thing.putDec(it);
    }

    var umX = it.toUnmodifiableMap();

    assertEquals(umX.size(), array.length);
    assertContents.execute(umX, (Object) array);
  }

}