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

public class ServerTaskTest9ResultContentProvider {

  @Test
  public void provider01() {
    final ContentProvider entity;
    entity = () -> Content.of(MediaType.TEXT_PLAIN, "foo");

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> entity);

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        foo\
        """
    );
  }

  @Test
  public void provider02() {
    final ContentProvider entity;
    entity = () -> Content.of(MediaType.TEXT_PLAIN, out -> out.write("foo".getBytes()));

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> entity);

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        03\r
        foo\r
        0\r
        \r
        """
    );
  }

}