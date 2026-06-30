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
package objectox.http.srv;

import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.function.BiFunction;
import objectos.http.Content;
import objectos.http.MediaType;
import objectos.http.Request;
import objectos.http.Result;
import objectos.http.StaticFile;
import objectos.http.StaticFilesOptions;
import objectos.way.Y;
import objectos.y.PathY;
import objectos.y.SocketY;
import objectox.http.host.Host;
import objectox.http.host.HostMap;
import objectox.http.media.StaticFiles;
import objectox.http.media.StaticFilesStageBuilder;
import org.testng.annotations.Test;

public class ServerTaskTestBStaticFiles {

  @Test(description = """
  Options::addDirectory
  """)
  public void testCase01() throws IOException {
    final ServerTask subject;
    subject = ServerTaskY.create(opts -> {
      opts.host(host -> {
        host.name = "www.example.com";

        host.staticFiles = files -> {
          etagMask(files);

          final Path src;
          src = PathY.nextDir();

          write(src, Path.of("tc01.txt"), "AAAA\n");

          files.addDirectory(src);

          files.withDefaultContentTypes();
        };
      });

      opts.socket = SocketY.of("""
      GET /tc01.txt HTTP/1.1\r
      Host: www.example.com\r
      Connection: close\r
      \r
      """
      );
    });

    final Path file;
    file = resolve(subject, "/tc01.txt");

    setLastModifiedTime(file);

    assertEquals(
        ServerTaskY.resp(subject),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        \r
        AAAA
        """
    );
  }

  @Test(description = """
  It should not handle if the file does not exist
  """)
  public void testCase02() throws IOException {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host(host -> {
            host.name = "www.example.com";

            host.staticFiles = files -> {
              etagMask(files);

              final Path src;
              src = PathY.nextDir();

              write(src, Path.of("tc02.txt"), "AAAA\n");

              files.addDirectory(src);

              files.withDefaultContentTypes();
            };
          });

          opts.socket = SocketY.of("""
          GET /tc02.pdf HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """
          );
        }),

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 14\r
        \r
        404 Not Found
        """
    );
  }

  @Test(description = """
  It should 405 if the method is not GET or HEAD
  """)
  public void testCase03() throws IOException {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host(host -> {
            host.name = "www.example.com";

            host.staticFiles = files -> {
              final Path src;
              src = PathY.nextDir();

              write(src, Path.of("tc03.txt"), "AAAA\n");

              files.addDirectory(src);

              files.withDefaultContentTypes();
            };
          });

          opts.socket = SocketY.of("""
          POST /tc03.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """
          );
        }),

        """
        HTTP/1.1 405 Method Not Allowed\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Allow: GET, HEAD\r
        \r
        """
    );
  }

  //  @Test(description = """
  //  Web.Resources::reconfigure
  //  - resources from before reconfigure should 404
  //  """)
  //  public void testCase04() throws IOException {
  //    final WebResources0 resources;
  //    resources = create(opts -> {
  //      final Media.Bytes reconfigure;
  //      reconfigure = Media.Bytes.textPlain("reconfigure");
  //
  //      opts.addMedia("/reconfigure.txt", reconfigure);
  //    });
  //
  //    resources.reconfigure(_ -> {});
  //
  //    assertEquals(
  //        ServerTaskY.resp(test -> {
  //          test.socket = SocketY.of("""
  //        GET /reconfigure.txt HTTP/1.1\r
  //        Host: www.example.com\r
  //        \r
  //        """);
  //
  //          test.handler = HttpHandler.of(routing -> {
  //            routing.handler(resources);
  //            routing.handler(HttpHandler.notFound());
  //          });
  //        }),
  //
  //        """
  //      HTTP/1.1 404 Not Found\r
  //      Date: Wed, 28 Jun 2023 12:08:43 GMT\r
  //      Content-Length: 0\r
  //      Connection: close\r
  //      \r
  //      """
  //    );
  //  }

  @Test(description = """
  It should return 304 when if-none-match
  """)
  public void testCase05() throws IOException {
    final ServerTask subject;
    subject = ServerTaskY.create(opts -> {
      opts.host(host -> {
        host.name = "www.example.com";

        host.staticFiles = files -> {
          etagMask(files);

          final Path src;
          src = PathY.nextDir();

          write(src, Path.of("tc05.txt"), "AAAA\n");

          files.addDirectory(src);

          files.withDefaultContentTypes();
        };
      });

      opts.socket = SocketY.of("""
      GET /tc05.txt HTTP/1.1\r
      Host: www.example.com\r
      If-None-Match: 18901e7e8f8-5\r
      Connection: close\r
      \r
      """
      );
    });

    final Path file;
    file = resolve(subject, "/tc05.txt");

    setLastModifiedTime(file);

    assertEquals(
        ServerTaskY.resp(subject),

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        Content-Length: 0\r
        \r
        """
    );
  }

  @Test(description = """
  Resources::writeMedia(Media.Bytes)
  """)
  public void testCase06() throws IOException {
    final ServerTask subject;
    subject = ServerTaskY.create(opts -> {
      opts.host(host -> {
        host.name = "www.example.com";

        final Content content;
        content = Content.of(MediaType.TEXT_PLAIN, "CCCC\n");

        final StaticFile staticFile;
        staticFile = StaticFile.of(content);

        host.handler = _ -> staticFile;

        host.staticFiles = files -> {
          files.withDefaultContentTypes();
        };
      });

      opts.socket = SocketY.of("""
      GET /tc06.txt HTTP/1.1\r
      Host: www.example.com\r
      \r
      """);
    });

    final Path file;
    file = resolve(subject, "/tc06.txt");

    assertEquals(Files.exists(file), false);

    ServerTaskY.resp(subject);

    assertEquals(Files.readString(file), "CCCC\n");
  }

  private void etagMask(StaticFilesOptions options) {
    final StaticFilesStageBuilder builder;
    builder = (StaticFilesStageBuilder) options;

    builder.etagMask(0L);
  }

  @SuppressWarnings("resource")
  private Path resolve(ServerTask subject, String path) {
    final HostMap hostMap;
    hostMap = subject.hostMap;

    final Host host;
    host = hostMap.get("www.example.com");

    final BiFunction<Request, Result, Result> _staticFiles;
    _staticFiles = host.staticFiles();

    final StaticFiles staticFiles;
    staticFiles = (StaticFiles) _staticFiles;

    return staticFiles.resolve(path);
  }

  private void write(Path directory, Path file, String text) {
    try {
      final Path target;
      target = directory.resolve(file);

      final Path parent;
      parent = target.getParent();

      Files.createDirectories(parent);

      Files.writeString(target, text);

      setLastModifiedTime(target);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
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

}