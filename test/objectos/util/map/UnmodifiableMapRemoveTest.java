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

import org.testng.Assert;

final class UnmodifiableMapRemoveTest {

  private final UnmodifiableMapTestAdapter adapter;

  public UnmodifiableMapRemoveTest(UnmodifiableMapTestAdapter adapter) {
    this.adapter = adapter;
  }

  public void execute() {
    adapter.testAll((map, els) -> {
      var t1 = Thing.next();

      try {
        map.remove(t1);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        adapter.assertContents(map, els);
      }

      try {
        map.remove(t1, "foo");

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        adapter.assertContents(map, els);
      }
    });
  }

}