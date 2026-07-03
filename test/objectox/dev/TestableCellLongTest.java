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

public class TestableCellLongTest {

  @Test(description = "reject value larger than width")
  public void format01() {
    final TestableCellLong subject;
    subject = new TestableCellLong(12345L, 4);

    try {
      subject.toString();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Formatted value length will exceed the column width of 4");
    }
  }

  @Test(description = "reject negative value larger than width")
  public void format02() {
    final TestableCellLong subject;
    subject = new TestableCellLong(-1234L, 4);

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
  public void format03() {
    try {
      new TestableCellLong(12345, 0);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Column width must be greater than zero");
    }
  }

  @DataProvider
  public Object[][] format04Provider() {
    return new Object[][] {
        {12345L, 5, "12345"},
        {1234L, 5, "01234"},
        {123L, 5, "00123"},
        {12L, 5, "00012"},
        {1L, 5, "00001"},
        {0L, 5, "00000"},
        {-1234L, 5, "-1234"}
    };
  }

  @Test(dataProvider = "format04Provider", description = "format")
  public void format04(long value, int width, String expected) {
    final TestableCellLong subject;
    subject = new TestableCellLong(value, width);

    assertEquals(subject.toString(), expected);
  }

}
