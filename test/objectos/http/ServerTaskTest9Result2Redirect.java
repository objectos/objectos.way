/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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
package objectos.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ServerTaskTest9Result2Redirect {

  @Test
  public void movedPermanently01() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.movedPermanently("/login"));
        }),

        """
        HTTP/1.1 301 Moved Permanently\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /login\r
        \r
        """
    );
  }

  @Test
  public void movedPermanently02() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.movedPermanently("/product/café/😀"));
        }),

        """
        HTTP/1.1 301 Moved Permanently\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /product/caf%C3%A9/%F0%9F%98%80\r
        \r
        """
    );
  }

  @Test
  public void movedPermanently03() {
    final String veryLargeHex;
    veryLargeHex = "f756cd80".repeat(256);

    final String location;
    location = "/foo/" + veryLargeHex;

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.movedPermanently(location));
        }),

        """
        HTTP/1.1 301 Moved Permanently\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: %s\r
        \r
        """.formatted(location)
    );
  }

  @Test
  public void found01() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.found("/login"));
        }),

        """
        HTTP/1.1 302 Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /login\r
        \r
        """
    );
  }

  @Test
  public void found02() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.found("/product/café/😀"));
        }),

        """
        HTTP/1.1 302 Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /product/caf%C3%A9/%F0%9F%98%80\r
        \r
        """
    );
  }

  @Test
  public void found03() {
    final String veryLargeHex;
    veryLargeHex = "f756cd80".repeat(256);

    final String location;
    location = "/foo/" + veryLargeHex;

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.found(location));
        }),

        """
        HTTP/1.1 302 Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: %s\r
        \r
        """.formatted(location)
    );
  }

  @Test
  public void seeOther01() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.seeOther("/page"));
        }),

        """
        HTTP/1.1 303 See Other\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /page\r
        \r
        """
    );
  }

  @Test
  public void seeOther02() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.seeOther("/product/café/😀"));
        }),

        """
        HTTP/1.1 303 See Other\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /product/caf%C3%A9/%F0%9F%98%80\r
        \r
        """
    );
  }

  @Test
  public void seeOther03() {
    final String veryLargeHex;
    veryLargeHex = "f756cd80".repeat(256);

    final String location;
    location = "/foo/" + veryLargeHex;

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Redirect.seeOther(location));
        }),

        """
        HTTP/1.1 303 See Other\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: %s\r
        \r
        """.formatted(location)
    );
  }

}