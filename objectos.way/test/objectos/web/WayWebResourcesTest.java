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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import objectos.http.server.ServerLoop;
import objectos.lang.TestingNoteSink;
import objectos.way.Rmdir;
import objectox.http.server.TestableSocket;
import objectox.http.server.TestingClock;
import org.testng.annotations.Test;

public class WayWebResourcesTest {

  @Test(description = """
  It should serve files from the root directory
  """)
  public void testCase01() throws IOException {
    Path root;
    root = Files.createTempDirectory("way-test-");

    try (WayWebResources resources = new WayWebResources(root)) {
      resources.contentType(".txt", "text/plain; charset=utf-8");
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path a;
      a = Path.of("a.txt");

      write(root, a, "AAAA\n");

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
    } finally {
      Rmdir.rmdir(root);
    }
  }

  @Test(description = """
  It should 404 if the file does not exist
  """)
  public void testCase02() throws IOException {
    Path root;
    root = Files.createTempDirectory("way-test-");

    try (WayWebResources resources = new WayWebResources(root)) {
      resources.contentType(".txt", "text/plain; charset=utf-8");
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path a;
      a = Path.of("a.txt");

      write(root, a, "AAAA\n");

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
    } finally {
      Rmdir.rmdir(root);
    }
  }

  @Test(description = """
  It should 405 if the method is not GET or HEAD
  """)
  public void testCase03() throws IOException {
    Path root;
    root = Files.createTempDirectory("way-test-");

    try (WayWebResources resources = new WayWebResources(root)) {
      resources.contentType(".txt", "text/plain; charset=utf-8");
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path a;
      a = Path.of("a.txt");

      write(root, a, "AAAA\n");

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
    } finally {
      Rmdir.rmdir(root);
    }
  }

  private void test(WayWebResources resources, String request, String response) {
    TestableSocket socket;
    socket = TestableSocket.of(request);

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(512, 1024);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      http.parse();

      assertEquals(http.badRequest(), false);

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