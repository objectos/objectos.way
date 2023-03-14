/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidateTest {

  @Test
  public void pathName() {
    invalidPathName("", "Path name must not be empty");
    invalidPathName("index.html", "Path name must be absolute and start with a '/' character");
  }

  private void invalidPathName(String value, String message) {
    try {
      Validate.pathName(value);

      Assert.fail("Expected pathname to be invalid: " + value);
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), message);
    }
  }

}