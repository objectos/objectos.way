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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WebResourcesTest extends Http.Module {

  private Path root;

  private Web.Resources resources;

  private int testCase06Count;

  private int testCase08Count;

  @BeforeClass
  public void beforeClass() throws IOException {
    root = TestingDir.next();

    resources = Web.Resources.create(config -> {
      config.rootDirectory(root);

      config.noteSink(TestingNoteSink.INSTANCE);

      config.contentTypes("""
        .txt: text/plain; charset=utf-8
        """);

      testCase01Option(config);
      testCase03Option(config);
      testCase04Option(config);
      testCase05Option(config);
    });

    TestingShutdownHook.register(resources);

    TestingHttpServer.bindWebResourcesTest(this);
  }

  @Override
  protected final void configure() {
    route("/tc01.txt", handler(resources));
    route("/tc02.txt", handler(resources), handler(this::testCase02));
    route("/tc03.txt", handler(resources));
    route("/tc04.txt", handler(resources));
    route("/tc05.txt", handler(resources));
    route("/tc06.txt", handler(resources), handler(this::testCase06));
    route("/tc07.txt", handler(this::testCase07));
    route("/tc08.txt", handler(resources), handler(this::testCase08));
  }

  private void testCase01Option(Web.Resources.Config config) throws IOException {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc01.txt");

    write(src, a, "AAAA\n");

    config.serveDirectory(src);
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

  private void testCase02(Http.Exchange http) {
    http.okText("BBBB\n", StandardCharsets.UTF_8);
  }

  @Test(description = """
  It should not handle if the file does not exist
  """)
  public void testCase02() throws IOException {
    try (Socket socket = newSocket()) {
      test(
          socket,

          """
          GET /tc02.txt HTTP/1.1\r
          Host: web.resources.test\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 5\r
          \r
          BBBB
          """
      );
    }
  }

  private void testCase03Option(Web.Resources.Config config) throws IOException {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc03.txt");

    write(src, a, "AAAA\n");

    config.serveDirectory(src);
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

  private void testCase04Option(Web.Resources.Config config) throws IOException {
    config.serveFile("/tc04.txt", "AAAA\n".getBytes(StandardCharsets.UTF_8));
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

  private void testCase05Option(Web.Resources.Config config) throws IOException {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc05.txt");

    write(src, a, "AAAA\n");

    config.serveDirectory(src);
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

  private void testCase06(Http.Exchange http) {
    testCase06Count++;

    try {
      String path;
      path = http.path();

      Lang.CharWritable contents;
      contents = out -> out.append("test-case-06");

      resources.writeCharWritable(path, contents, StandardCharsets.UTF_8);

      resources.handle(http);
    } catch (IOException e) {
      http.internalServerError(e);
    }
  }

  @Test(description = """
  Web.Resources::writeCharWritable
  """)
  public void testCase06() throws IOException, InterruptedException {
    final HttpResponse<String> resp01;
    resp01 = Testing.httpClient(
        "/tc06.txt",

        Testing.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp01.statusCode(), 200);
    assertEquals(resp01.body(), "test-case-06");

    Optional<String> maybeTag;
    maybeTag = resp01.headers().firstValue("ETag");

    assertTrue(maybeTag.isPresent());

    final HttpResponse<String> resp02;
    resp02 = Testing.httpClient(
        "/tc06.txt",

        Testing.headers(
            "Host", "web.resources.test",
            "If-None-Match", maybeTag.get(),
            "Connection", "close"
        )
    );

    assertEquals(resp02.statusCode(), 304);

    assertEquals(testCase06Count, 1);
  }

  private void testCase07(Http.Exchange http) {
    try {
      String path;
      path = http.path();

      Lang.CharWritable contents;
      contents = out -> out.append("test-case-07");

      resources.writeCharWritable(path, contents, StandardCharsets.UTF_8);

      assertTrue(resources.deleteIfExists(path));

      resources.handle(http);
    } catch (IOException e) {
      http.internalServerError(e);
    }
  }

  @Test(description = """
  Web.Resources::delete
  """)
  public void testCase07() throws IOException, InterruptedException {
    final HttpResponse<String> resp;
    resp = Testing.httpClient(
        "/tc07.txt",

        Testing.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp.statusCode(), 404);
  }

  private void testCase08(Http.Exchange http) {
    testCase08Count++;

    try {
      String path;
      path = http.path();

      resources.writeString(path, "test-case-08", StandardCharsets.UTF_8);

      resources.handle(http);
    } catch (IOException e) {
      http.internalServerError(e);
    }
  }

  @Test(description = """
  Web.Resources::writeString
  """)
  public void testCase08() throws IOException, InterruptedException {
    final HttpResponse<String> resp01;
    resp01 = Testing.httpClient(
        "/tc08.txt",

        Testing.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp01.statusCode(), 200);
    assertEquals(resp01.body(), "test-case-08");

    Optional<String> maybeTag;
    maybeTag = resp01.headers().firstValue("ETag");

    assertTrue(maybeTag.isPresent());

    final HttpResponse<String> resp02;
    resp02 = Testing.httpClient(
        "/tc08.txt",

        Testing.headers(
            "Host", "web.resources.test",
            "If-None-Match", maybeTag.get(),
            "Connection", "close"
        )
    );

    assertEquals(resp02.statusCode(), 304);

    assertEquals(testCase08Count, 1);
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