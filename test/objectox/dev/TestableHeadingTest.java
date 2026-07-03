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

public class TestableHeadingTest {

  @Test(description = "reject null value")
  public void format01() {
    try {
      new TestableHeading(1, null);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      final String msg = expected.getMessage();

      assertEquals(msg, "value == null");
    }
  }

  @DataProvider
  public Object[][] format02Provider() {
    return new Object[][] {
        {1, "# Title"},
        {2, "## Title"},
        {3, "### Title"},
        {4, "#### Title"}
    };
  }

  @Test(dataProvider = "format02Provider", description = "accept")
  public void format02(int level, String expected) {
    final TestableHeading subject;
    subject = new TestableHeading(level, "Title");

    assertEquals(subject.toString(), expected);
  }

}
