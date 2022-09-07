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

final class GrowableMapToUnmodifiableMapTest {

  private final GrowableMap<Thing, String> it;

  private final MapAssertContents assertContents;

  public GrowableMapToUnmodifiableMapTest(GrowableMap<Thing, String> it,
                                          MapAssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public final void execute() {
    var result = it.toUnmodifiableMap();

    assertTrue(result.isEmpty());

    assertContents.execute(result);

    // one
    var t1 = Thing.next();

    assertNull(t1.putDec(it));

    result = it.toUnmodifiableMap();

    assertFalse(result.isEmpty());
    assertEquals(result.size(), 1);

    assertContents.execute(result, t1);

    // two
    var t2 = Thing.next();

    assertNull(t2.putDec(it));

    result = it.toUnmodifiableMap();

    assertFalse(result.isEmpty());
    assertEquals(result.size(), 2);

    assertContents.execute(result, t1, t2);

    // three
    var t3 = Thing.next();

    assertNull(t3.putDec(it));

    result = it.toUnmodifiableMap();

    assertFalse(result.isEmpty());
    assertEquals(result.size(), 3);

    assertContents.execute(result, t1, t2, t3);

    it.clear();

    var array = Thing.nextArray();

    for (var thing : array) {
      thing.putDec(it);
    }

    result = it.toUnmodifiableMap();

    assertContents.execute(result, (Object) array);
  }

}