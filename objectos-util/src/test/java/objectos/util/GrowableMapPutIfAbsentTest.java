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

final class GrowableMapPutIfAbsentTest {

  private final GrowableMap<Thing, String> it;

  public GrowableMapPutIfAbsentTest(GrowableMap<Thing, String> it) {
    this.it = it;
  }

  public final void execute() {
    assertEquals(it.size(), 0);

    var t1 = Thing.next();

    assertEquals(it.putIfAbsent(t1, "foo"), null);
    assertEquals(it.size(), 1);

    assertEquals(it.putIfAbsent(t1, "bar"), "foo");
    assertEquals(it.size(), 1);
  }

}