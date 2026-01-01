/*
 * Copyright (C) 2026 Objectos Software LTDA.
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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HtmlTest {

  @DataProvider
  public Object[][] formatAttrValueProvider() {
    return new Object[][] {
        {"", "", "empty"},
        {" x", "x", "leading ws"},
        {"x ", "x", "trailing ws"},
        {"\t\f\r\n x", "x", "leading ws"},
        {"x \t\f\r\n", "x", "trailing ws"},
        {"x y", "x y", "no change"},
        {"x  y", "x y", "middle"},
        {"x\n  y", "x y", "middle"},
    };
  }

  @Test(dataProvider = "formatAttrValueProvider")
  public void formatAttrValue(String value, String expected, String description) {
    assertEquals(Html.formatAttrValue(value), expected, description);
  }

}