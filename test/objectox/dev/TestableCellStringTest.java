/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.dev;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestableCellStringTest {

  @Test(description = "reject value larger than width")
  public void format01() {
    final TestableCellString subject;
    subject = new TestableCellString("12345", 4);

    try {
      subject.toString();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Formatted value length will exceed the column width of 4");
    }
  }

  @Test(description = "reject width <= 0")
  public void format02() {
    try {
      new TestableCellString("12345", 0);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Column width must be greater than zero");
    }
  }

  @DataProvider
  public Object[][] format03Provider() {
    return new Object[][] {
        {"abcde", 5, "abcde"},
        {"abcd", 5, "abcd "},
        {"abc", 5, "abc  "},
        {"ab", 5, "ab   "},
        {"a", 5, "a    "},
        {"", 5, "     "},
        {null, 5, "null "}
    };
  }

  @Test(dataProvider = "format03Provider", description = "format")
  public void format03(String value, int width, String expected) {
    final TestableCellString subject;
    subject = new TestableCellString(value, width);

    assertEquals(subject.toString(), expected);
  }

}
