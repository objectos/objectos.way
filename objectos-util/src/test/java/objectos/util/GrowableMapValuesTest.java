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

import static org.testng.Assert.assertNull;

final class GrowableMapValuesTest {

  private final GrowableMap<Thing, String> it;

  private final MapAssertValues assertValues;

  public GrowableMapValuesTest(GrowableMap<Thing, String> it, MapAssertValues assertValues) {
    this.it = it;
    this.assertValues = assertValues;
  }

  public final void execute() {
    var set = it.values();

    assertValues.execute(set);

    var t1 = Thing.next();
    t1.putDec(it);

    assertValues.execute(set, t1);

    var t2 = Thing.next();
    t2.putDec(it);

    assertValues.execute(set, t1, t2);

    var t3 = Thing.next();
    t3.putDec(it);

    assertValues.execute(set, t1, t2, t3);

    var t4 = Thing.next();
    t4.putDec(it);

    assertValues.execute(set, t1, t2, t3, t4);

    it.clear();

    assertValues.execute(set);

    var array = Thing.nextArray();

    for (int i = 0; i < array.length; i++) {
      var t = array[i];
      assertNull(t.putDec(it));
    }

    assertValues.execute(set, array);
  }

}