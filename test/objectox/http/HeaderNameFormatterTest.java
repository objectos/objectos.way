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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HeaderNameFormatterTest {

  @DataProvider
  public Object[][] formatProvider() {
    return new Object[][] {
        {"Accept", "accept"},
        {"Accept-Encoding", "accept-encoding"},
        {"ETag", "etag"},
        {"if-none-match", "if-none-match"}
    };
  }

  @Test(dataProvider = "formatProvider")
  public void format(String input, String expected) {
    final HeaderNameFormatter subject;
    subject = new HeaderNameFormatter(input);

    final String res;
    res = subject.format();

    assertEquals(res, expected);
  }

  @DataProvider
  public Object[][] invalidProvider() {
    return new Object[][] {
        {"x y", "Invalid header name character ' ' at index 1"},
        {"x[y]", "Invalid header name character '[' at index 1"}
    };
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(String input, String message) {
    final HeaderNameFormatter subject;
    subject = new HeaderNameFormatter(input);

    try {
      subject.format();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, message);
    }
  }

}
