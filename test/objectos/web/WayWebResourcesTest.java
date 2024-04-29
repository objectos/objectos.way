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
package objectos.web;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import objectos.http.TestableSocket;
import objectos.http.WayServerLoop;
import objectos.http.WayServerLoop.ParseStatus;
import objectos.way.TestingClock;
import objectos.way.TestingDir;
import objectos.way.TestingNoteSink;
import org.testng.annotations.Test;

public class WayWebResourcesTest {

  @Test(description = """
  It should copy directory recursively
  """)
  public void testCase01() throws IOException {
    Path root;
    root = TestingDir.next();

    try (WayWebResources resources = new WayWebResources(root)) {
      resources.contentType(".txt", "text/plain; charset=utf-8");
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path src;
      src = TestingDir.next();

      Path a;
      a = Path.of("a.txt");

      write(src, a, "AAAA\n");

      resources.copyDirectory(src);

      test(
          resources,

          """
          GET /a.txt HTTP/1.1\r
          Host: www.example.com\r
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

    // it should have been deleted by close()
    assertFalse(Files.exists(root));
  }

  @Test(description = """
  It should 404 if the file does not exist
  """)
  public void testCase02() throws IOException {
    try (WayWebResources resources = new WayWebResources()) {
      resources.contentType(".txt", "text/plain; charset=utf-8");
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path src;
      src = TestingDir.next();

      Path a;
      a = Path.of("a.txt");

      write(src, a, "AAAA\n");

      test(
          resources,

          """
          GET /b.txt HTTP/1.1\r
          Host: www.example.com\r
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

  @Test(description = """
  It should 405 if the method is not GET or HEAD
  """)
  public void testCase03() throws IOException {
    Path root;
    root = TestingDir.next();

    try (WayWebResources resources = new WayWebResources(root)) {
      resources.contentType(".txt", "text/plain; charset=utf-8");
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path src;
      src = TestingDir.next();

      Path a;
      a = Path.of("a.txt");

      write(src, a, "AAAA\n");

      resources.copyDirectory(src);

      test(
          resources,

          """
          POST /a.txt HTTP/1.1\r
          Host: www.example.com\r
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

  @Test(description = """
  It should be able to add custom files
  """)
  public void testCase04() throws IOException {
    Path root;
    root = TestingDir.next();

    try (WayWebResources resources = new WayWebResources(root)) {
      resources.contentType(".txt", "text/plain; charset=utf-8");
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path a;
      a = Path.of("assets", "a.txt");

      resources.createNew(a, "AAAA\n".getBytes(StandardCharsets.UTF_8));

      Clock clock;
      clock = TestingClock.FIXED;

      Instant instant;
      instant = clock.instant();

      FileTime fileTime;
      fileTime = FileTime.from(instant);

      resources.setLastModifiedTime(a, fileTime);

      test(
          resources,

          """
          GET /assets/a.txt HTTP/1.1\r
          Host: www.example.com\r
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
  
  @Test(description = """
  It should return 304 when if-none-match
  """)
  public void testCase05() throws IOException {
    Path root;
    root = TestingDir.next();

    try (WayWebResources resources = new WayWebResources(root)) {
      resources.contentType(".txt", "text/plain; charset=utf-8");
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path src;
      src = TestingDir.next();

      Path a;
      a = Path.of("a.txt");

      write(src, a, "AAAA\n");

      resources.copyDirectory(src);

      test(
          resources,

          """
          GET /a.txt HTTP/1.1\r
          Host: www.example.com\r
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

  private void test(WayWebResources resources, String request, String response) {
    TestableSocket socket;
    socket = TestableSocket.of(request);

    try (WayServerLoop http = new WayServerLoop(socket)) {
      http.bufferSize(512, 1024);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      resources.handle(http);

      http.commit();

      assertEquals(socket.outputAsString(), response);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
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

}