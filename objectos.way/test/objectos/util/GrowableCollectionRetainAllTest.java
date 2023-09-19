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

import static org.testng.Assert.assertTrue;

import java.util.List;
import org.testng.Assert;

final class GrowableCollectionRetainAllTest {

  private final GrowableCollection<Thing> it;

  public GrowableCollectionRetainAllTest(GrowableCollection<Thing> it) {
    this.it = it;
  }

  public void execute() {
    var t1 = Thing.next();
    var t2 = Thing.next();

    it.add(t1);
    it.add(t2);

    try {
      it.retainAll(List.of(t1));

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertTrue(it.contains(t1));
      assertTrue(it.contains(t2));
    }
  }

}