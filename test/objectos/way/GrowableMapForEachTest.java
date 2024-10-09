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

import static org.testng.Assert.assertNull;

import java.util.LinkedHashMap;
import objectos.util.MapAssertContents;
import objectos.util.Thing;

final class GrowableMapForEachTest {

  private final UtilGrowableMap<Thing, String> it;
  private final MapAssertContents assertContents;

  public GrowableMapForEachTest(UtilGrowableMap<Thing, String> it, MapAssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public final void execute() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    assertNull(t1.putDec(it));
    assertNull(t2.putDec(it));
    assertNull(t3.putDec(it));

    var jdk = new LinkedHashMap<Thing, String>();

    it.forEach(jdk::put);

    assertContents.execute(jdk, t1, t2, t3);
  }

}