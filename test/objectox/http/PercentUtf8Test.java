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

import objectos.internal.Util;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PercentUtf8Test {

  private String encode0(String dictionary, String input) {
    final PercentDictionary percentDictionary;
    percentDictionary = new PercentDictionary(dictionary);

    final PercentEncoder parent;
    parent = new PercentEncoder(percentDictionary, input);

    final byte[] start;
    start = Util.EMPTY_BYTE_ARRAY;

    final PercentString string;
    string = new PercentString(start);

    final PercentUtf8 subject;
    subject = new PercentUtf8(parent, string);

    return subject.encode();
  }

  @DataProvider
  public Object[][] encodeProvider() {
    return new Object[][] {
        {Rfc.vchar(), "", ""},
        {Rfc.vchar(), "/path", "/path"},
        {Rfc.vchar(), "/utf8/\u0000", "/utf8/%00"},
        {Rfc.vchar(), "/utf8/á", "/utf8/%C3%A1"},
        {Rfc.vchar(), "/utf8/世界", "/utf8/%E4%B8%96%E7%95%8C"},
        {Rfc.vchar(), "/utf8/😊", "/utf8/%F0%9F%98%8A"},
        {Rfc.vchar(), "/path/Café 世界😊", "/path/Caf%C3%A9%20%E4%B8%96%E7%95%8C%F0%9F%98%8A"},
        {Rfc.vchar(), "😊".repeat(10), "%F0%9F%98%8A".repeat(10)}
    };
  }

  @Test(dataProvider = "encodeProvider")
  public void encode(String dictionary, String input, String expected) {
    assertEquals(
        encode0(dictionary, input),

        expected
    );
  }

  @DataProvider
  public Object[][] invalidProvider() {
    return new Object[][] {
        {Rfc.vchar(), "xx\uDC00", "Low surrogate \\udc00 must be preceeded by a high surrogate."},
        {Rfc.vchar(), "xx\uD800", "Unmatched high surrogate at end of string"},
        {Rfc.vchar(), "xx\uD800xx", "High surrogate \\ud800 must be followed by a low surrogate."}
    };
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(String dictionary, String input, String expectedMessage) {
    try {
      encode0(dictionary, input);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, expectedMessage);
    }
  }

}
