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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import objectos.util.MapAssertContents;
import objectos.util.Thing;

final class GrowableMapPutAllTest {

  private final UtilGrowableMap<Thing, String> it;

  private final MapAssertContents assertContents;

  public GrowableMapPutAllTest(UtilGrowableMap<Thing, String> it, MapAssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public final void execute() {
    assertEquals(it.size(), 0);
    assertContents.execute(it);

    Thing k1;
    k1 = Thing.next();

    Thing k2;
    k2 = Thing.next();

    var map = Map.of(
        k1, "1",
        k2, "2"
    );

    it.putAll(map);

    assertEquals(it.size(), 2);
    assertEquals(it.get(k1), "1");
    assertEquals(it.get(k2), "2");
  }

}