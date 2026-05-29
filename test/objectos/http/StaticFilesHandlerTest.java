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
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;
import objectos.way.Y;
import objectos.y.PathY;
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
  public void testCase01() {
    final Path root;
    root = PathY.nextDir();

    write(root, "tc01.txt", "TC01");

    final Handler handler;
    handler = create(opts -> {
      opts.contentTypes = Map.of(".txt", "text/plain");

      opts.rootDirectory = root;
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.path("/tc01.txt");
    }));

    assertEquals(
        ResultY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain\r
        Content-Length: 4\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-4\r
        \r
        TC01\
        """
    );
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