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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import objectos.way.UtilGrowableCollection;

public final class GrowableCollectionContainsTest {

  private final UtilGrowableCollection<Thing> it;

  public GrowableCollectionContainsTest(UtilGrowableCollection<Thing> it) {
    this.it = it;
  }

  public void execute() {
    // null must return false
    assertFalse(it.contains(null));

    // mutate
    var t1 = Thing.next();
    var t2 = Thing.next();
    var array = Thing.nextArray();

    assertFalse(it.contains(t1));

    assertFalse(it.contains(t2));

    it.add(t1);

    assertTrue(it.contains(t1));

    for (var t : array) {
      it.add(t);

      assertTrue(it.contains(t));
    }

    it.add(t2);

    assertTrue(it.contains(t2));

    // verify still true
    assertTrue(it.contains(t1));

    for (var t : array) {
      assertTrue(it.contains(t));
    }

    assertTrue(it.contains(t2));
  }

}