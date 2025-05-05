/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WebResourcesTest implements Consumer<Http.Routing> {

  private Web.Resources resources;

  private int testCase06Count;

  private int testCase08Count;

  @BeforeClass
  public void beforeClass() throws IOException {
    resources = Web.Resources.create(config -> {
      config.noteSink(TestingNoteSink.INSTANCE);

      config.contentTypes("""
      .txt: text/plain; charset=utf-8
      """);

      config.addTextFile("/reconfigure.txt", "reconfigure", StandardCharsets.UTF_8);
    });

    final WebResources impl;
    impl = (WebResources) resources;

    final Path original;
    original = impl.rootDirectory();

    assertTrue(Files.exists(original));

    resources.reconfigure(config -> {
      config.noteSink(TestingNoteSink.INSTANCE);

      config.contentTypes("""
      .txt: text/plain; charset=utf-8
      """);

      testCase01Option(config);
      testCase03Option(config);
      testCase05Option(config);
      testCase10Option(config);
    });

    testCase09Option();

    assertFalse(Files.exists(original));

    TestingShutdownHook.register(resources);

    TestingHttpServer.bindWebResourcesTest(this);
  }

  @Override
  public final void accept(Http.Routing routing) {
    routing.path("/tc01.txt", resources::handlePath);
    routing.path("/tc02.txt", path -> {
      path.handler(resources);

      path.handler(this::testCase02);
    });
    routing.path("/tc03.txt", resources::handlePath);
    routing.path("/tc04.txt", resources::handlePath);
    routing.path("/tc05.txt", resources::handlePath);
    routing.path("/tc06.txt", path -> {
      path.handler(resources);

      path.handler(this::testCase06);
    });
    routing.path("/tc07.txt", path -> {
      path.handler(this::testCase07);
    });
    routing.path("/tc08.txt", path -> {
      path.handler(resources);

      path.handler(this::testCase08);
    });
    routing.path("/tc09.txt", resources::handlePath);
    routing.path("/tc10.txt", resources::handlePath);
    routing.path("/tc11.html", path -> {
      path.handler(resources);

      path.handler(this::testCase11);
    });

    routing.handler(Http.Handler.notFound());
  }

  private void testCase01Option(Web.Resources.Config config) {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc01.txt");

    write(src, a, "AAAA\n");

    config.addDirectory(src);
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
    final String text;
    text = "BBBB\n";

    final Media.Bytes object;
    object = Media.Bytes.textPlain(text, StandardCharsets.UTF_8);

    http.ok(object);
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

  private void testCase03Option(Web.Resources.Config config) {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc03.txt");

    write(src, a, "AAAA\n");

    config.addDirectory(src);
  }

  @Test(description = """
  It should 405 if the method is not GET or HEAD
  """)
  public void testCase03() throws IOException {
    Y.test(
        Y.httpClient(
            "/tc03.txt",

            builder -> builder.POST(BodyPublishers.noBody()).headers(
                "Host", "web.resources.test"
            )
        ),

        """
        HTTP/1.1 405
        allow: GET, HEAD
        content-length: 0
        date: Wed, 28 Jun 2023 12:08:43 GMT

        """
    );
  }

  @Test(description = """
  Web.Resources::reconfigure
  - resources from before reconfigure should 404
  """)
  public void testCase04() throws IOException, InterruptedException {
    final HttpResponse<String> resp;
    resp = Y.httpClient(
        "/reconfigure.txt",

        builder -> builder.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp.statusCode(), 404);
  }

  private void testCase05Option(Web.Resources.Config config) {
    Path src;
    src = TestingDir.next();

    Path a;
    a = Path.of("tc05.txt");

    write(src, a, "AAAA\n");

    config.addDirectory(src);
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
          HTTP/1.1 304 Not Modified\r
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

      Media.Bytes contents;
      contents = Media.Bytes.textPlain("test-case-06");

      resources.writeMedia(path, contents);

      resources.handle(http);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test(description = """
  Web.Resources::writeCharWritable
  """)
  public void testCase06() throws IOException, InterruptedException {
    final HttpResponse<String> resp01;
    resp01 = Y.httpClient(
        "/tc06.txt",

        builder -> builder.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp01.statusCode(), 200);
    assertEquals(resp01.body(), "test-case-06");

    Optional<String> maybeTag;
    maybeTag = resp01.headers().firstValue("ETag");

    assertTrue(maybeTag.isPresent());

    final HttpResponse<String> resp02;
    resp02 = Y.httpClient(
        "/tc06.txt",

        builder -> builder.headers(
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

      Media.Bytes contents;
      contents = Media.Bytes.textPlain("test-case-07");

      resources.writeMedia(path, contents);

      assertTrue(resources.deleteIfExists(path));

      resources.handle(http);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test(description = """
  Web.Resources::delete
  """)
  public void testCase07() throws IOException, InterruptedException {
    final HttpResponse<String> resp;
    resp = Y.httpClient(
        "/tc07.txt",

        builder -> builder.headers(
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
      throw new UncheckedIOException(e);
    }
  }

  @Test(description = """
  Web.Resources::writeString
  """)
  public void testCase08() throws IOException, InterruptedException {
    final HttpResponse<String> resp01;
    resp01 = Y.httpClient(
        "/tc08.txt",

        builder -> builder.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp01.statusCode(), 200);
    assertEquals(resp01.body(), "test-case-08");

    Optional<String> maybeTag;
    maybeTag = resp01.headers().firstValue("ETag");

    assertTrue(maybeTag.isPresent());

    final HttpResponse<String> resp02;
    resp02 = Y.httpClient(
        "/tc08.txt",

        builder -> builder.headers(
            "Host", "web.resources.test",
            "If-None-Match", maybeTag.get(),
            "Connection", "close"
        )
    );

    assertEquals(resp02.statusCode(), 304);

    assertEquals(testCase08Count, 1);
  }

  private void testCase09Option() throws IOException {
    byte[] bytes;
    bytes = "TC 09!".getBytes(StandardCharsets.UTF_8);

    resources.write("/tc09.txt", bytes);
  }

  @Test
  public void testCase09() throws IOException, InterruptedException {
    final HttpResponse<String> resp01;
    resp01 = Y.httpClient(
        "/tc09.txt",

        builder -> builder.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp01.statusCode(), 200);
    assertEquals(resp01.body(), "TC 09!");
  }

  private void testCase10Option(Web.Resources.Config config) {
    byte[] bytes;
    bytes = "TC 10!".getBytes(StandardCharsets.UTF_8);

    config.addBinaryFile("/tc10.txt", bytes);
  }

  @Test
  public void testCase10() throws IOException, InterruptedException {
    final HttpResponse<String> resp01;
    resp01 = Y.httpClient(
        "/tc10.txt",

        builder -> builder.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp01.statusCode(), 200);
    assertEquals(resp01.body(), "TC 10!");
  }

  private void testCase11(Http.Exchange http) {
    try {
      String path;
      path = http.path();

      resources.writeMedia(path, new TestingSingleParagraph("TC11"));

      resources.handle(http);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  public void testCase11() throws IOException, InterruptedException {
    final HttpResponse<String> resp01;
    resp01 = Y.httpClient(
        "/tc11.html",

        builder -> builder.headers(
            "Host", "web.resources.test"
        )
    );

    assertEquals(resp01.statusCode(), 200);
    assertEquals(resp01.body(), """
    <html>
    <p>TC11</p>
    </html>
    """);
  }

  private void write(Path directory, Path file, String text) {
    try {
      Path target;
      target = directory.resolve(file);

      Path parent;
      parent = target.getParent();

      Files.createDirectories(parent);

      Files.writeString(target, text);

      // set last modified time for etag purposes
      Clock clock;
      clock = Y.clockFixed();

      Instant instant;
      instant = clock.instant();

      FileTime fileTime;
      fileTime = FileTime.from(instant);

      Files.setLastModifiedTime(target, fileTime);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void test(Socket socket, String request, String expectedResponse) throws IOException {
    TestingHttpServer.test(socket, request, expectedResponse);
  }

}