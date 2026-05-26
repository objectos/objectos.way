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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.Test;

public class HostHandlerTest {

  @Test
  public void testCase01() {
    final Path root;
    root = Y.nextTempDir();

    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.path("/tc01.txt");

      opts.staticFilesDirectory(root);
    });

    final HttpHandler main;
    main = x -> x.staticFile(Media.Bytes.textPlain("TC01\n"));

    final HttpHandler staticFiles;
    staticFiles = x -> {
      try {
        final Path file;
        file = root.resolve("tc01.txt");

        final String s;
        s = Files.readString(file);

        x.ok(Media.Bytes.textPlain(s));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    };

    final HttpHandler handler;
    handler = new HostHandler(main, staticFiles);

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), true);

    assertEquals(http.toString(), """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 5\r
    \r
    TC01
    """);
  }

  @Test
  public void testCase02() {
    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.path("/tc02.txt");
    });

    final HttpHandler main;
    main = x -> x.ok(Media.Bytes.textPlain("MAIN\n"));

    final HttpHandler staticFiles;
    staticFiles = x -> x.ok(Media.Bytes.textPlain("FILES\n"));

    final HttpHandler handler;
    handler = new HostHandler(main, staticFiles);

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), true);

    assertEquals(http.toString(), """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 5\r
    \r
    MAIN
    """);
  }

  @Test
  public void testCase03() {
    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.path("/tc03.txt");
    });

    final HttpHandler main;
    main = _ -> {};

    final HttpHandler staticFiles;
    staticFiles = x -> x.ok(Media.Bytes.textPlain("FILES\n"));

    final HttpHandler handler;
    handler = new HostHandler(main, staticFiles);

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), true);

    assertEquals(http.toString(), """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 6\r
    \r
    FILES
    """);
  }

  @Test
  public void testCase04() {
    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.path("/tc04.txt");
    });

    final HttpHandler main;
    main = _ -> {};

    final HttpHandler staticFiles;
    staticFiles = _ -> {};

    final HttpHandler handler;
    handler = new HostHandler(main, staticFiles);

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), true);

    assertEquals(http.toString(), """
    HTTP/1.1 404 Not Found\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Connection: close\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 14\r
    \r
    404 Not Found
    """);
  }

}
