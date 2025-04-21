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
import org.testng.annotations.Test;

public class HttpTest {

  @Test(description = """
  It should parse a single name-value pair
  """)
  public void parseCookies01() {
    Http.Cookies c;
    c = Http.Cookies.parse("foo=bar");

    assertEquals(c.get("foo"), "bar");
  }

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

  @Test(description = "no encoding required")
  public void raw01() {
    assertEquals(Http.raw("/path"), "/path");
  }

  @Test(description = "encoding required: utf-8 1-byte form")
  public void raw02() {
    assertEquals(Http.raw("/utf8/á"), "/utf8/%C3%A1");
  }

  @Test(description = "encoding required: utf-8 3-byte form")
  public void raw03() {
    assertEquals(Http.raw("/utf8/世界"), "/utf8/%E4%B8%96%E7%95%8C");
  }

  @Test(description = "encoding required: utf-8 4-byte form")
  public void raw04() {
    assertEquals(Http.raw("/utf8/😊"), "/utf8/%F0%9F%98%8A");
  }

  @Test(description = "mixed ASCII and non-ASCII characters")
  public void raw05() {
    assertEquals(Http.raw("/path/Café 世界😊"), "/path/Caf%C3%A9%20%E4%B8%96%E7%95%8C%F0%9F%98%8A");
  }

  @Test(description = "empty string")
  public void raw06() {
    assertEquals(Http.raw(""), "");
  }

  @Test(description = "initial buffer size will need increasing")
  public void raw07() {
    assertEquals(Http.raw("😊".repeat(10)), "%F0%9F%98%8A".repeat(10));
  }

  @Test(description = "invalid UTF-16: lone low surrogate")
  public void raw08() {
    try {
      Http.raw("xx\uDC00");

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Low surrogate \\udc00 must be preceeded by a high surrogate.");
    }
  }

  @Test(description = "invalid UTF-16: lone high surrogate")
  public void raw09() {
    try {
      Http.raw("xx\uD800");

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Unmatched high surrogate at end of string");
    }
  }

  @Test(description = "invalid UTF-16: high surrogate followed by non-surrogate")
  public void raw10() {
    try {
      Http.raw("xx\uD800xx");

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "High surrogate \\ud800 must be followed by a low surrogate.");
    }
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