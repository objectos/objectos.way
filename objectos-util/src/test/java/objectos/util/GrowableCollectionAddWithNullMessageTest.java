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

import static org.testng.Assert.assertEquals;

import org.testng.Assert;

final class GrowableCollectionAddWithNullMessageTest {

  private final GrowableCollection<Thing> it;

  public GrowableCollectionAddWithNullMessageTest(GrowableCollection<Thing> it) {
    this.it = it;
  }

  public void execute() {
    try {
      it.addWithNullMessage(null, "my message");

      Assert.fail("NPE was expected");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "my message");
    }

    try {
      it.addWithNullMessage(null, null);

      Assert.fail("NPE was expected");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "null");
    }

    try {
      it.addWithNullMessage(null, "[", 123, "]");

      Assert.fail("NPE was expected");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "[123]");
    }

    try {
      it.addWithNullMessage(null, null, 123, null);

      Assert.fail("NPE was expected");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "null123null");
    }
  }

}