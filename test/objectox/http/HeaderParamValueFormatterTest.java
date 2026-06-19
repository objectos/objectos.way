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
package objectox.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HeaderParamValueFormatterTest {

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {"", "\"\""},
        {"document.pdf", "document.pdf"},
        {"[foo].txt", "\"[foo].txt\""},
        {"category[first].txt", "\"category[first].txt\""},
        {"my report.pdf", "\"my report.pdf\""},
        {"char\".txt", "\"char\\\".txt\""},
        {"\".txt", "\"\\\".txt\""},
    };
  }

  @Test(dataProvider = "validProvider")
  public void valid(String value, String expected) {
    final HeaderParamValueFormatter subject;
    subject = new HeaderParamValueFormatter(value);

    final String res;
    res = subject.format();

    assertEquals(res, expected);
  }

}
