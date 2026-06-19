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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.testng.Assert;
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

  @DataProvider
  public Object[][] invalidProvider() {
    final List<Object[]> list;
    list = new ArrayList<>();

    final char[] validChars;
    validChars = Rfc.vchar().toCharArray();

    Arrays.sort(validChars);

    for (char c = 0; c < 256; c++) {
      final int search;
      search = Arrays.binarySearch(validChars, c);

      if (search >= 0 || c == ' ' || c == '\t') {
        continue;
      }

      final String s;
      s = Character.toString(c);

      list.add(new Object[] {
          s,

          "Invalid parameter value: character '%s' at index 0 is not allowed".formatted(s)}
      );

      list.add(new Object[] {
          "foo" + s,

          "Invalid parameter value: character '%s' at index 3 is not allowed".formatted(s)}
      );

      list.add(new Object[] {
          "[]-" + s,

          "Invalid parameter value: character '%s' at index 3 is not allowed".formatted(s)}
      );
    }

    return list.toArray(Object[][]::new);
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(String value, String message) {
    final HeaderParamValueFormatter subject;
    subject = new HeaderParamValueFormatter(value);

    try {
      subject.format();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), message);
    }
  }

}
