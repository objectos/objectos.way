/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WebResourcesTest implements Http.Handler {

  private Path root;

  private Web.Resources resources;

  @BeforeClass
  public void beforeClass() throws IOException {
    root = TestingDir.next();

    resources = Web.createResources(
        Web.rootDirectory(root),

        Web.noteSink(TestingNoteSink.INSTANCE),

        Web.contentTypes("""
        .txt: text/plain; charset=utf-8
        """),

        testCase01Option(),
        testCase02Option(),
        testCase03Option(),
        testCase04Option(),
        testCase05Option()
    );

    TestingShutdownHook.register(resources);

    TestingHttpServer.bindWebResourcesTest(this);
  }

  @Override
  public final void handle(Http.Exchange http) {
    resources.handle(http);
  }

  private Web.Resources.Option testCase01Option() throws IOException {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc01.txt");

    write(src, a, "AAAA\n");

    return Web.serveDirectory(src);
  }

  @Test(description = """
  It should copy directory recursively
  """)
  public void testCase01() throws IOException {
    try (Socket socket = newSocket()) {
      test(
          socket,

          """
          GET /tc01.txt HTTP/1.1\r
          Host: web.resources.test\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 5\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          ETag: 18901e7e8f8-5\r
          \r
          AAAA
          """
      );
    }
  }

  private Web.Resources.Option testCase02Option() throws IOException {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc02.txt");

    write(src, a, "AAAA\n");

    return Web.serveDirectory(src);
  }

  @Test(description = """
  It should 404 if the file does not exist
  """)
  public void testCase02() throws IOException {
    try (Socket socket = newSocket()) {
      Path src;
      src = TestingDir.next();

      Path a;
      a = Path.of("tc02.txt");

      write(src, a, "AAAA\n");

      test(
          socket,

          """
          GET /b.txt HTTP/1.1\r
          Host: web.resources.test\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private Web.Resources.Option testCase03Option() throws IOException {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc03.txt");

    write(src, a, "AAAA\n");

    return Web.serveDirectory(src);
  }

  @Test(description = """
  It should 405 if the method is not GET or HEAD
  """)
  public void testCase03() throws IOException {
    try (Socket socket = newSocket()) {
      test(
          socket,

          """
          POST /tc03.txt HTTP/1.1\r
          Host: web.resources.test\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 405 METHOD NOT ALLOWED\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private Web.Resources.Option testCase04Option() throws IOException {
    return Web.serveFile("/tc04.txt", "AAAA\n".getBytes(StandardCharsets.UTF_8));
  }

  @Test(description = """
  It should be able to add custom files
  """)
  public void testCase04() throws IOException {
    try (Socket socket = newSocket()) {
      Path a;
      a = root.resolve("tc04.txt");

      Clock clock;
      clock = TestingClock.FIXED;

      Instant instant;
      instant = clock.instant();

      FileTime fileTime;
      fileTime = FileTime.from(instant);

      Files.setLastModifiedTime(a, fileTime);

      test(
          socket,

          """
          GET /tc04.txt HTTP/1.1\r
          Host: web.resources.test\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 5\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          ETag: 18901e7e8f8-5\r
          \r
          AAAA
          """
      );
    }
  }

  private Web.Resources.Option testCase05Option() throws IOException {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc05.txt");

    write(src, a, "AAAA\n");

    return Web.serveDirectory(src);
  }

  @Test(description = """
  It should return 304 when if-none-match
  """)
  public void testCase05() throws IOException {
    try (Socket socket = newSocket()) {
      test(
          socket,

          """
          GET /tc05.txt HTTP/1.1\r
          Host: web.resources.test\r
          If-None-Match: 18901e7e8f8-5\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 304 NOT MODIFIED\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          ETag: 18901e7e8f8-5\r
          \r
          """
      );
    }
  }

  private void write(Path directory, Path file, String text) throws IOException {
    Path target;
    target = directory.resolve(file);

    Path parent;
    parent = target.getParent();

    Files.createDirectories(parent);

    Files.writeString(target, text);

    // set last modified time for etag purposes
    Clock clock;
    clock = TestingClock.FIXED;

    Instant instant;
    instant = clock.instant();

    FileTime fileTime;
    fileTime = FileTime.from(instant);

    Files.setLastModifiedTime(target, fileTime);
  }

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void test(Socket socket, String request, String expectedResponse) throws IOException {
    TestingHttpServer.test(socket, request, expectedResponse);
  }

}