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

import objectos.internal.Ascii;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UrlEncoderTest {

  @Test(description = "all non visibile us-ascii characters must be encoded")
  public void encode01() {
    final StringBuilder sb;
    sb = new StringBuilder();

    for (char c = 0; c <= ' '; c++) {
      sb.append(c);
    }

    final String input;
    input = sb.toString();

    final String res;
    res = new UrlEncoder(input).encode();

    assertEquals(res, "%00%01%02%03%04%05%06%07%08%09%0A%0B%0C%0D%0E%0F%10%11%12%13%14%15%16%17%18%19%1A%1B%1C%1D%1E%1F%20");
  }

  @Test(description = "all visible us-ascii do not need encoding")
  public void encode02() {
    final String input;
    input = Ascii.visible();

    final String res;
    res = new UrlEncoder(input).encode();

    assertEquals(res, input);
  }

  @DataProvider
  public Object[][] encode03Provider() {
    return new Object[][] {
        {"", ""},
        {"/utf8/\u0000", "/utf8/%00"},
        {"/utf8/á", "/utf8/%C3%A1"},
        {"/utf8/世界", "/utf8/%E4%B8%96%E7%95%8C"},
        {"/utf8/😊", "/utf8/%F0%9F%98%8A"},
        {"/path/Café 世界😊", "/path/Caf%C3%A9%20%E4%B8%96%E7%95%8C%F0%9F%98%8A"},
        {"😊".repeat(10), "%F0%9F%98%8A".repeat(10)}
    };
  }

  @Test(dataProvider = "encode03Provider")
  public void encode03(String input, String expected) {
    assertEquals(
        new UrlEncoder(input).encode(),

        expected
    );
  }

  @DataProvider
  public Object[][] invalidProvider() {
    return new Object[][] {
        {"xx\uDC00", "Low surrogate \\udc00 must be preceeded by a high surrogate."},
        {"xx\uD800", "Unmatched high surrogate at end of string"},
        {"xx\uD800xx", "High surrogate \\ud800 must be followed by a low surrogate."}
    };
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(String input, String expectedMessage) {
    try {
      new UrlEncoder(input).encode();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, expectedMessage);
    }
  }

}
