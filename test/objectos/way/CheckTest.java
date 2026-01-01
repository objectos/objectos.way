/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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

import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckTest {

  @Test
  public void argument() {
    try {
      Check.argument(true, "should not happen");
    } catch (IllegalArgumentException e) {
      Assert.fail();
    }

    try {
      Check.argument(false, "failed");

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "failed");
    }

    try {
      Check.argument(false, "number 1 ", "letter A");

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "number 1 letter A");
    }
  }

  @Test
  public void notNull() {
    Object notNull;
    notNull = Boolean.TRUE;

    try {
      Check.notNull(notNull, "Should not happen");
    } catch (NullPointerException e) {
      Assert.fail();
    }

    try {
      Check.notNull(null, "foo is null");

      Assert.fail();
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "foo is null");
    }

    try {
      Check.notNull(null, "foo ", "is null");

      Assert.fail();
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "foo is null");
    }

    try {
      Check.notNull(null, "elements[", 10, "] == null");

      Assert.fail();
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "elements[10] == null");
    }
  }

  @Test
  public void state() {
    try {
      Check.state(true, "Should not happen");
    } catch (IllegalStateException e) {
      Assert.fail();
    }

    try {
      Check.state(false, "state not valid");

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "state not valid");
    }

    try {
      Check.state(false, "number 1 ", "letter A");

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "number 1 letter A");
    }
  }

}