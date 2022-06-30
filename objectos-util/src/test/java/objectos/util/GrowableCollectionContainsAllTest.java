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
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import org.testng.Assert;

final class GrowableCollectionContainsAllTest {

  private final GrowableCollection<Thing> it;

  public GrowableCollectionContainsAllTest(GrowableCollection<Thing> it) {
    this.it = it;
  }

  public void execute() {
    assertTrue(it.containsAll(Thing.EMPTY_LIST));

    assertTrue(it.containsAll(Thing.EMPTY_SET));

    // all
    var arrayList = Thing.nextArrayList();

    it.addAll(arrayList);

    assertTrue(it.containsAll(arrayList));

    // with more
    var withMore = new ArrayList<>(arrayList);

    var t1 = Thing.next();

    withMore.add(t1);

    assertFalse(it.containsAll(withMore));

    // with less
    var withLess = new ArrayList<>(arrayList);

    withLess.remove(withLess.size() - 1);

    assertTrue(it.containsAll(withLess));

    // with null
    var listWithNull = new ArrayList<>(arrayList);

    listWithNull.set(Thing.HALF, null);

    assertFalse(it.containsAll(listWithNull));

    // must reject null argument
    try {
      it.containsAll(null);

      Assert.fail("Expected a NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "c == null");
    }
  }

}