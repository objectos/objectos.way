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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

final class GrowableMapContainsKeyTest {

  private final GrowableMap<Thing, String> it;

  public GrowableMapContainsKeyTest(GrowableMap<Thing, String> it) {
    this.it = it;
  }

  public final void execute() {
    var t1 = Thing.next();

    var t2 = Thing.next();

    var t3 = Thing.next();

    assertFalse(it.containsKey(t1));
    assertFalse(it.containsKey(t2));
    assertFalse(it.containsKey(t3));
    assertFalse(it.containsKey(null));

    t1.putDec(it);

    assertTrue(it.containsKey(t1));
    assertFalse(it.containsKey(t2));
    assertFalse(it.containsKey(t3));
    assertFalse(it.containsKey(null));

    t2.putDec(it);

    assertTrue(it.containsKey(t1));
    assertTrue(it.containsKey(t2));
    assertFalse(it.containsKey(t3));
    assertFalse(it.containsKey(null));

    t3.putDec(it);

    assertTrue(it.containsKey(t1));
    assertTrue(it.containsKey(t2));
    assertTrue(it.containsKey(t3));
    assertFalse(it.containsKey(null));

    it.clear();

    assertFalse(it.containsKey(t1));
    assertFalse(it.containsKey(t2));
    assertFalse(it.containsKey(t3));
    assertFalse(it.containsKey(null));
  }

}