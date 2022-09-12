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

import org.testng.Assert;

final class GrowableMapReplaceTest {

  private final GrowableMap<Thing, String> it;

  private final MapAssertContents assertContents;

  public GrowableMapReplaceTest(GrowableMap<Thing, String> it, MapAssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public void execute() {
    var t1 = Thing.next();
    var t2 = Thing.next();
    var t3 = Thing.next();

    t1.putDec(it);
    t2.putDec(it);
    t3.putDec(it);

    try {
      it.replace(t2, t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertContents.execute(it, t1, t2, t3);
    }

    try {
      it.replace(t2, t2.toDecimalString(), t2.toHexString());

      Assert.fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertContents.execute(it, t1, t2, t3);
    }
  }

}