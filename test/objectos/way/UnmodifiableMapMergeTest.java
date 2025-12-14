/*
 * Copyright (C) 2022-2025 Objectos Software LTDA.
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

import objectos.util.Thing;
import org.testng.Assert;

public final class UnmodifiableMapMergeTest {

  private final UtilUnmodifiableMapTestAdapter adapter;

  public UnmodifiableMapMergeTest(UtilUnmodifiableMapTestAdapter adapter) {
    this.adapter = adapter;
  }

  public void execute() {
    adapter.testAll((map, els) -> {
      try {
        var t = Thing.next();

        map.merge(t, "foo", (_, _) -> t.toHexString());

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        adapter.assertContents(map, els);
      }
    });
  }

}