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
package objectos.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.Assert;

final class GrowableMapPutTest {

  private final GrowableMap<Thing, String> it;

  private final MapAssertContents assertContents;

  public GrowableMapPutTest(GrowableMap<Thing, String> it, MapAssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public final void execute() {
    // empty
    assertEquals(it.size(), 0);

    // one
    var t1 = Thing.next();

    assertNull(t1.putHex(it));
    assertContents.execute(it, new Hex(t1));

    // two (must allow duplicate elements)
    assertEquals(t1.putDec(it), t1.toHexString());
    assertContents.execute(it, t1);

    // many
    var array = Thing.nextArray();

    for (int i = 0; i < array.length; i++) {
      var t = array[i];

      assertNull(t.putDec(it));
    }

    assertContents.execute(it, t1, array);

    // must reject null keys
    try {
      Thing t = null;

      it.put(t, "x");

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "key == null");
    }

    // must reject null values
    try {
      Thing t = Thing.next();

      it.put(t, null);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "value == null");
    }
  }

}