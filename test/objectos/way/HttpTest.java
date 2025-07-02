/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpTest {

  @Test
  public void parseRequestTarget01() {
    Http.RequestTarget target;
    target = Http.Exchange.create(cfg -> cfg.path("/"));

    assertEquals(target.path(), "/");
  }

  @Test
  public void parseRequestTarget02() {
    Http.RequestTarget target;
    target = Http.Exchange.create(cfg -> cfg.path("/foo/bar?page=1&sort=asc"));

    assertEquals(target.path(), "/foo/bar");
    assertEquals(target.queryParam("page"), "1");
    assertEquals(target.queryParamAsInt("page", 0), 1);
    assertEquals(target.queryParam("sort"), "asc");
  }

  @DataProvider
  public Object[][] rawValidProvider() {
    return new Object[][] {
        {"/path", "/path", "no encoding required"},
        {"/utf8/á", "/utf8/%C3%A1", "encoding required: utf-8 1-byte form"},
        {"/utf8/世界", "/utf8/%E4%B8%96%E7%95%8C", "encoding required: utf-8 3-byte form"},
        {"/utf8/😊", "/utf8/%F0%9F%98%8A", "encoding required: utf-8 4-byte form"},
        {"/path/Café 世界😊", "/path/Caf%C3%A9%20%E4%B8%96%E7%95%8C%F0%9F%98%8A", "mixed ASCII and non-ASCII characters"},
        {"", "", "empty string"},
        {"😊".repeat(10), "%F0%9F%98%8A".repeat(10), "initial buffer size will need increasing"}
    };
  }

  @Test(dataProvider = "rawValidProvider")
  public void rawValid(String source, String expected, String description) {
    assertEquals(Http.raw(source), expected);
  }

  @DataProvider
  public Object[][] rawInvalidProvider() {
    return new Object[][] {
        {"xx\uDC00", "Low surrogate \\udc00 must be preceeded by a high surrogate."},
        {"xx\uD800", "Unmatched high surrogate at end of string"},
        {"xx\uD800xx", "High surrogate \\ud800 must be followed by a low surrogate."}
    };
  }

  @Test(dataProvider = "rawInvalidProvider")
  public void rawInvalid(String source, String expectedMessage) {
    try {
      Http.raw(source);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    }
  }

  @DataProvider
  public Object[][] rfc8187ValidProvider() {
    return new Object[][] {
        // Original test cases
        {"", "UTF-8''", "empty string should return itself"},
        {"!#$&+-.^_`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
            "UTF-8''!#$&+-.^_`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
            "All attr-char"},

        // 1-byte percent-encoded characters (ASCII control chars and special chars)
        {" ", "UTF-8''%20", "space character (1-byte percent-encoded)"},
        {"\"", "UTF-8''%22", "double quote (1-byte percent-encoded)"},
        {"%", "UTF-8''%25", "percent sign (1-byte percent-encoded)"},
        {"'", "UTF-8''%27", "single quote (1-byte percent-encoded)"},
        {"(", "UTF-8''%28", "left parenthesis (1-byte percent-encoded)"},
        {")", "UTF-8''%29", "right parenthesis (1-byte percent-encoded)"},
        {"*", "UTF-8''%2A", "asterisk (1-byte percent-encoded)"},
        {",", "UTF-8''%2C", "comma (1-byte percent-encoded)"},
        {"/", "UTF-8''%2F", "forward slash (1-byte percent-encoded)"},
        {":", "UTF-8''%3A", "colon (1-byte percent-encoded)"},
        {";", "UTF-8''%3B", "semicolon (1-byte percent-encoded)"},
        {"<", "UTF-8''%3C", "less than (1-byte percent-encoded)"},
        {"=", "UTF-8''%3D", "equals (1-byte percent-encoded)"},
        {">", "UTF-8''%3E", "greater than (1-byte percent-encoded)"},
        {"?", "UTF-8''%3F", "question mark (1-byte percent-encoded)"},
        {"@", "UTF-8''%40", "at sign (1-byte percent-encoded)"},
        {"[", "UTF-8''%5B", "left bracket (1-byte percent-encoded)"},
        {"\\", "UTF-8''%5C", "backslash (1-byte percent-encoded)"},
        {"]", "UTF-8''%5D", "right bracket (1-byte percent-encoded)"},
        {"{", "UTF-8''%7B", "left brace (1-byte percent-encoded)"},
        {"}", "UTF-8''%7D", "right brace (1-byte percent-encoded)"},
        {"\u007F", "UTF-8''%7F", "DEL character (1-byte percent-encoded)"},

        // 2-byte percent-encoded characters (Latin-1 supplement)
        {"¢", "UTF-8''%C2%A2", "cent sign (2-byte percent-encoded)"},
        {"£", "UTF-8''%C2%A3", "pound sign (2-byte percent-encoded)"},
        {"©", "UTF-8''%C2%A9", "copyright sign (2-byte percent-encoded)"},
        {"®", "UTF-8''%C2%AE", "registered sign (2-byte percent-encoded)"},
        {"°", "UTF-8''%C2%B0", "degree sign (2-byte percent-encoded)"},
        {"µ", "UTF-8''%C2%B5", "micro sign (2-byte percent-encoded)"},
        {"×", "UTF-8''%C3%97", "multiplication sign (2-byte percent-encoded)"},
        {"÷", "UTF-8''%C3%B7", "division sign (2-byte percent-encoded)"},
        {"ñ", "UTF-8''%C3%B1", "Latin small letter n with tilde (2-byte percent-encoded)"},
        {"ü", "UTF-8''%C3%BC", "Latin small letter u with diaeresis (2-byte percent-encoded)"},

        // 3-byte percent-encoded characters (various Unicode blocks)
        {"€", "UTF-8''%E2%82%AC", "euro sign (3-byte percent-encoded)"},
        {"™", "UTF-8''%E2%84%A2", "trade mark sign (3-byte percent-encoded)"},
        {"←", "UTF-8''%E2%86%90", "leftwards arrow (3-byte percent-encoded)"},
        {"→", "UTF-8''%E2%86%92", "rightwards arrow (3-byte percent-encoded)"},
        {"中", "UTF-8''%E4%B8%AD", "CJK ideograph 'middle' (3-byte percent-encoded)"},
        {"文", "UTF-8''%E6%96%87", "CJK ideograph 'text' (3-byte percent-encoded)"},
        {"♠", "UTF-8''%E2%99%A0", "black spade suit (3-byte percent-encoded)"},
        {"♥", "UTF-8''%E2%99%A5", "black heart suit (3-byte percent-encoded)"},
        {"★", "UTF-8''%E2%98%85", "black star (3-byte percent-encoded)"},
        {"☆", "UTF-8''%E2%98%86", "white star (3-byte percent-encoded)"},

        // 4-byte percent-encoded characters (supplementary planes)
        {"😀", "UTF-8''%F0%9F%98%80", "grinning face emoji (4-byte percent-encoded)"},
        {"😊", "UTF-8''%F0%9F%98%8A", "smiling face with smiling eyes emoji (4-byte percent-encoded)"},
        {"🎉", "UTF-8''%F0%9F%8E%89", "party popper emoji (4-byte percent-encoded)"},
        {"🚀", "UTF-8''%F0%9F%9A%80", "rocket emoji (4-byte percent-encoded)"},
        {"💯", "UTF-8''%F0%9F%92%AF", "hundred points symbol emoji (4-byte percent-encoded)"},
        {"🌟", "UTF-8''%F0%9F%8C%9F", "glowing star emoji (4-byte percent-encoded)"},
        {"🎯", "UTF-8''%F0%9F%8E%AF", "direct hit emoji (4-byte percent-encoded)"},
        {"🔥", "UTF-8''%F0%9F%94%A5", "fire emoji (4-byte percent-encoded)"},

        // Mathematical symbols (4-byte)
        {"𝕏", "UTF-8''%F0%9D%95%8F", "mathematical double-struck capital X (4-byte percent-encoded)"},
        {"𝐀", "UTF-8''%F0%9D%90%80", "mathematical bold capital A (4-byte percent-encoded)"},

        // Mixed content test cases
        {"file name.txt", "UTF-8''file%20name.txt", "filename with space"},
        {"résumé.pdf", "UTF-8''r%C3%A9sum%C3%A9.pdf", "filename with accented characters"},
        {"测试文件.doc", "UTF-8''%E6%B5%8B%E8%AF%95%E6%96%87%E4%BB%B6.doc", "filename with Chinese characters"},
        {"file(1).txt", "UTF-8''file%281%29.txt", "filename with parentheses"},
        {"100% complete", "UTF-8''100%25%20complete", "text with percent and space"},
        {"user@domain.com", "UTF-8''user%40domain.com", "email address"},
        {"path/to/file", "UTF-8''path%2Fto%2Ffile", "path with slashes"},
        {"Hello, 世界! 🌍", "UTF-8''Hello%2C%20%E4%B8%96%E7%95%8C!%20%F0%9F%8C%8D", "mixed ASCII, Chinese, and emoji"},

        // Edge cases
        {"'single'", "UTF-8''%27single%27", "single quotes (attr-char delimiter)"},
        {"a'b'c", "UTF-8''a%27b%27c", "multiple single quotes"},
        {"100%", "UTF-8''100%25", "percent at end"},
        {"%start", "UTF-8''%25start", "percent at start"},
        {"mid%dle", "UTF-8''mid%25dle", "percent in middle"}
    };
  }

  @Test(dataProvider = "rfc8187ValidProvider")
  public void rfc8187Valid(String source, String expected, String description) {
    final String result;
    result = Http.rfc8187(source);

    assertEquals(result, expected);
  }

  @Test
  public void requiredHexDigits() {
    assertEquals(Http.requiredHexDigits(0b0000), 1);
    assertEquals(Http.requiredHexDigits(0b0001), 1);
    assertEquals(Http.requiredHexDigits(0b1000), 1);
    assertEquals(Http.requiredHexDigits(0b1111), 1);
    assertEquals(Http.requiredHexDigits(0b1_0000), 2);
  }

}