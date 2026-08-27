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
package objectox.html.format;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class QuotationMarkOptionTest {

  @DataProvider
  public Object[][] escapeProvider() {
    return new Object[][] {
      {QuotationMarkOption.DOUBLE, "", "", "empty string"},
      {QuotationMarkOption.DOUBLE, "abc", "abc", "no escaping required"},
      {QuotationMarkOption.DOUBLE, "A&B", "A&amp;B", "ampersand"},
      {QuotationMarkOption.DOUBLE, "A<B", "A&lt;B", "less-than"},
      {QuotationMarkOption.DOUBLE, "A>B", "A&gt;B", "greater-than"},
      {QuotationMarkOption.DOUBLE, "A\"B", "A&quot;B", "double quote escaped"},
      {QuotationMarkOption.DOUBLE, "A'B", "A'B", "single quote not escaped"},
      {QuotationMarkOption.DOUBLE, "A&B<C>D\"E", "A&amp;B&lt;C&gt;D&quot;E", "all double-escapable chars"},
      {QuotationMarkOption.DOUBLE, "&amp;", "&amp;amp;", "html entity double-escaped"},

      {QuotationMarkOption.SINGLE, "", "", "empty string"},
      {QuotationMarkOption.SINGLE, "abc", "abc", "no escaping required"},
      {QuotationMarkOption.SINGLE, "A&B", "A&amp;B", "ampersand"},
      {QuotationMarkOption.SINGLE, "A<B", "A&lt;B", "less-than"},
      {QuotationMarkOption.SINGLE, "A>B", "A&gt;B", "greater-than"},
      {QuotationMarkOption.SINGLE, "A'B", "A&#39;B", "single quote escaped"},
      {QuotationMarkOption.SINGLE, "A\"B", "A\"B", "double quote not escaped"},
      {QuotationMarkOption.SINGLE, "A&B<C>D'E", "A&amp;B&lt;C&gt;D&#39;E", "all single-escapable chars"},
      {QuotationMarkOption.SINGLE, "&amp;", "&amp;amp;", "html entity double-escaped"},
    };
  }

  @Test(dataProvider = "escapeProvider")
  public void escape(QuotationMarkOption option, String input, String expected, String description) {
    assertEquals(option.escape(input), expected, description);
  }

}
