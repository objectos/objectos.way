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
package objectos.util.list;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import objectos.util.collection.GrowableCollection;

final class GrowableCollectionIsEmptyTest {

  private final GrowableCollection<Thing> it;

  public GrowableCollectionIsEmptyTest(GrowableCollection<Thing> it) {
    this.it = it;
  }

  public void execute() {
    assertTrue(it.isEmpty());

    var t1 = Thing.next();

    it.add(t1);

    assertFalse(it.isEmpty());

    it.clear();

    assertTrue(it.isEmpty());
  }

}