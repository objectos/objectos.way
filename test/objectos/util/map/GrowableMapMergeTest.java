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

import static org.testng.Assert.assertEquals;

import org.testng.Assert;

final class GrowableMapMergeTest {

  private final GrowableMap<Thing, String> it;

  public GrowableMapMergeTest(GrowableMap<Thing, String> it) {
    this.it = it;
  }

  public void execute() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.merge(t2, "foo", (k, v) -> t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertEquals(it.size(), 3);
    }
  }

}