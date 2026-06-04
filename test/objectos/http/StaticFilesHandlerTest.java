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
import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.way.Y;
import objectos.y.PathY;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StaticFilesHandlerTest {

  private static final class Options {

    Map<String, String> contentTypes = Map.of();

    String defaultContentType;

    Path rootDirectory;

  }

  private StaticFilesHandler create(Consumer<? super Options> opts) {
    final Options builder;
    builder = new Options();

    opts.accept(builder);

    return new StaticFilesHandler(
        builder.contentTypes,

        builder.defaultContentType,

        builder.rootDirectory
    );
  }

  @Test(description = """
  handle:
  - existing file
  - file @ root
  - mapped content-type
  """)
  public void handle01() {
    final Handler handler;
    handler = create(opts -> {
      opts.contentTypes = Map.of(".txt", "text/plain");

      final Path root;
      root = PathY.nextDir();

      write(root, "tc01.txt", "TC01");

      opts.rootDirectory = root;
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.path("/tc01.txt");
    }));

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-4\r
        Content-Type: text/plain\r
        Content-Length: 4\r
        \r
        TC01\
        """
    );
  }

  @Test(description = """
  handle:
  - existing file
  - file @ subdir
  - mapped content-type
  """)
  public void handle02() {
    final Handler handler;
    handler = create(opts -> {
      opts.contentTypes = Map.of(".json", "application/json");

      final Path root;
      root = PathY.nextDir();

      write(root, "sub/tc02.json", "[\"TC02\"]");

      opts.rootDirectory = root;
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.path("/sub/tc02.json");
    }));

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-8\r
        Content-Type: application/json\r
        Content-Length: 8\r
        \r
        ["TC02"]\
        """
    );
  }

  @Test(description = """
  handle:
  - existing file
  - file @ root
  - default content-type
  """)
  public void handle03() {
    final Handler handler;
    handler = create(opts -> {
      opts.defaultContentType = "application/octet-stream";

      final Path root;
      root = PathY.nextDir();

      write(root, "tc03.foo", "TC03");

      opts.rootDirectory = root;
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.path("/tc03.foo");
    }));

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-4\r
        Content-Type: application/octet-stream\r
        Content-Length: 4\r
        \r
        TC03\
        """
    );
  }

  @Test(description = """
  handle:
  - existing file
  - file @ root
  - HEAD method
  """)
  public void handle04() {
    final Handler handler;
    handler = create(opts -> {
      opts.contentTypes = Map.of(".txt", "text/plain");

      final Path root;
      root = PathY.nextDir();

      write(root, "tc04.txt", "TC04");

      opts.rootDirectory = root;
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.method(HttpMethod.HEAD);

      opts.path("/tc04.txt");
    }));

    assertEquals(
        ResponseY.toString(res, true),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-4\r
        Content-Type: text/plain\r
        Content-Length: 4\r
        \r
        """
    );
  }

  @DataProvider
  public Iterator<HttpMethod> methodProvider() {
    return Stream.of(HttpMethod.VALUES)
        .filter(m -> !m.equals(HttpMethod.GET) && !m.equals(HttpMethod.HEAD))
        .iterator();
  }

  @Test(dataProvider = "methodProvider")
  public void methodNotAllowed01(HttpMethod method) {
    final Handler handler;
    handler = create(opts -> {
      opts.contentTypes = Map.of(".txt", "text/plain");

      final Path root;
      root = PathY.nextDir();

      write(root, "not-allowed.txt", method.name());

      opts.rootDirectory = root;
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.method(method);

      opts.path("/not-allowed.txt");
    }));

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 405 Method Not Allowed\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Allow: GET, HEAD\r
        \r
        """
    );
  }

  @Test
  public void notModified01() {
    final Handler handler;
    handler = create(opts -> {
      opts.contentTypes = Map.of(".txt", "text/plain");

      final Path root;
      root = PathY.nextDir();

      write(root, "tc01.txt", "TC01");

      opts.rootDirectory = root;
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.path("/tc01.txt");

      opts.header(HttpHeaderName.IF_NONE_MATCH, "18901e7e8f8-4");
    }));

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: 18901e7e8f8-4\r
        \r
        """
    );
  }

  @Test(description = """
  skip:
  - non-existing file
  """)
  public void skip01() {
    final Handler handler;
    handler = create(opts -> {
      opts.rootDirectory = PathY.nextDir();
    });

    final Request request;
    request = Request.create(opts -> {
      opts.path("/i-do-not-exist.txt");
    });

    final Result res;
    res = handler.handle(request);

    assertSame(res, request);
  }

  @Test(description = """
  skip:
  - existing path is a directory
  """)
  public void skip02() {
    final Handler handler;
    handler = create(opts -> {
      opts.rootDirectory = PathY.nextDir();
    });

    final Request request;
    request = Request.create(opts -> {
      opts.path("/");
    });

    final Result res;
    res = handler.handle(request);

    assertSame(res, request);
  }

  private void setLastModifiedTime(Path target) throws IOException {
    // set last modified time for etag purposes
    final Clock clock;
    clock = Y.clockFixed();

    final Instant instant;
    instant = clock.instant();

    final FileTime fileTime;
    fileTime = FileTime.from(instant);

    Files.setLastModifiedTime(target, fileTime);
  }

  private Path write(Path directory, String fileName, String text) {
    try {
      final Path target;
      target = directory.resolve(fileName);

      final Path parent;
      parent = target.getParent();

      Files.createDirectories(parent);

      Files.writeString(target, text);

      setLastModifiedTime(target);

      return target;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}