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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.Test;

public class HttpServerTaskTestBStaticFiles {

  @Test(description = """
  Options::addDirectory
  """)
  public void testCase01() throws IOException {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.staticFiles = files -> {
            final Path src;
            src = Y.nextTempDir();

            final Path a;
            a = Path.of("tc01.txt");

            write(src, a, "AAAA\n");

            files.addDirectory(src);

            files.contentTypes(".txt: text/plain; charset=utf-8");
          };

          opts.socket = Y.socket(
              """
              GET /tc01.txt HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              \r
              """
          );

          opts.handler = HttpHandler.noop();
        }),

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

  @Test(description = """
  It should not handle if the file does not exist
  """)
  public void testCase02() throws IOException {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(
              """
              GET /tc02.txt HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              \r
              """
          );

          opts.handler = HttpHandler.notFound();
        }),

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Connection: close\r
        \r
        """
    );
  }

  @Test(description = """
  It should 405 if the method is not GET or HEAD
  """)
  public void testCase03() throws IOException {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.staticFiles = files -> {
            final Path src;
            src = Y.nextTempDir();

            final Path a;
            a = Path.of("tc03.txt");

            write(src, a, "AAAA\n");

            files.addDirectory(src);

            files.contentTypes(".txt: text/plain; charset=utf-8");
          };

          opts.socket = Y.socket(
              """
              POST /tc03.txt HTTP/1.1\r
              Host: www.example.com\r
              \r
              """
          );

          opts.handler = HttpHandler.noop();
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

  /*

  @Test(description = """
  Web.Resources::reconfigure
  - resources from before reconfigure should 404
  """)
  public void testCase04() throws IOException {
  final WebResources0 resources;
  resources = create(opts -> {
    final Media.Bytes reconfigure;
    reconfigure = Media.Bytes.textPlain("reconfigure");
  
    opts.addMedia("/reconfigure.txt", reconfigure);
  });
  
  resources.reconfigure(_ -> {});
  
  assertEquals(
      HttpServerTaskY.resp(test -> {
        test.socket = Y.socket("""
        GET /reconfigure.txt HTTP/1.1\r
        Host: www.example.com\r
        \r
        """);
  
        test.handler = HttpHandler.of(routing -> {
          routing.handler(resources);
          routing.handler(HttpHandler.notFound());
        });
      }),
  
      """
      HTTP/1.1 404 Not Found\r
      Date: Wed, 28 Jun 2023 12:08:43 GMT\r
      Content-Length: 0\r
      Connection: close\r
      \r
      """
  );
  }

  */

  @Test(description = """
  It should return 304 when if-none-match
  """)
  public void testCase05() throws IOException {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.staticFiles = files -> {
            final Path src;
            src = Y.nextTempDir();

            final Path a;
            a = Path.of("tc05.txt");

            write(src, a, "AAAA\n");

            files.addDirectory(src);

            files.contentTypes(".txt: text/plain; charset=utf-8");
          };

          opts.socket = Y.socket(
              """
              GET /tc05.txt HTTP/1.1\r
              Host: www.example.com\r
              If-None-Match: 18901e7e8f8-5\r
              Connection: close\r
              \r
              """
          );

          opts.handler = HttpHandler.noop();
        }),

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: 18901e7e8f8-5\r
        \r
        """
    );
  }

  @Test(description = """
  Resources::writeMedia(Media.Bytes)
  """)
  public void testCase06() throws IOException {
    final Path root;
    root = Y.nextTempDir();

    final String resp;
    resp = HttpServerTaskY.resp(test -> {
      test.staticFilesDirectory = root;

      test.socket = Y.socket("""
          GET /tc06.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

      test.handler = http -> {
        final Media.Bytes contents;
        contents = Media.Bytes.textPlain("CCCC\n");

        http.serveStatic(contents);
      };
    });

    final String etag;
    etag = etag(resp);

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.staticFilesDirectory = root;

          test.socket = Y.socket("""
          GET /tc06.txt HTTP/1.1\r
          Host: www.example.com\r
          If-None-Match: %s\r
          \r
          """.formatted(etag));

          test.handler = HttpHandler.noop();
        }),

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: %s\r
        \r
        """.formatted(etag)
    );
  }

  /*

  @Test(description = """
  Resources::delete
  """)
  public void testCase07() throws IOException {
  final WebResources0 resources;
  resources = create(_ -> {});
  
  assertEquals(
      HttpServerTaskY.resp(test -> {
        test.socket = Y.socket("""
        GET /tc07.txt HTTP/1.1\r
        Host: www.example.com\r
        \r
        """);
  
        test.handler = HttpHandler.of(routing -> {
          routing.handler(http -> {
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
          });
          routing.handler(HttpHandler.notFound());
        });
      }),
  
      """
      HTTP/1.1 404 Not Found\r
      Date: Wed, 28 Jun 2023 12:08:43 GMT\r
      Content-Length: 0\r
      Connection: close\r
      \r
      """
  );
  }

  */

  @Test(description = """
  Resources::writeMedia(Media.Text)
  """)
  public void testCase08() throws IOException {
    final Path root;
    root = Y.nextTempDir();

    final String resp;
    resp = HttpServerTaskY.resp(test -> {
      test.staticFilesDirectory = root;

      test.socket = Y.socket("""
          GET /tc08.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

      test.handler = http -> {
        final Media.Text contents;
        contents = Y.mediaTextOf("8888\n");

        http.serveStatic(contents);
      };
    });

    final String etag;
    etag = etag(resp);

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.staticFilesDirectory = root;

          test.socket = Y.socket("""
          GET /tc08.txt HTTP/1.1\r
          Host: www.example.com\r
          If-None-Match: %s\r
          \r
          """.formatted(etag));

          test.handler = HttpHandler.noop();
        }),

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: %s\r
        \r
        """.formatted(etag)
    );
  }

  @Test(description = """
  Resources::writeMedia(Media.Stream)
  """)
  public void testCase09() throws IOException, InterruptedException {
    final Path root;
    root = Y.nextTempDir();

    final String resp;
    resp = HttpServerTaskY.resp(test -> {
      test.staticFilesDirectory = root;

      test.socket = Y.socket("""
          GET /tc09.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

      test.handler = http -> {
        final Media.Stream contents;
        contents = Y.mediaStreamOf("9999\n");

        http.serveStatic(contents);
      };
    });

    final String etag;
    etag = etag(resp);

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.staticFilesDirectory = root;

          test.socket = Y.socket("""
          GET /tc09.txt HTTP/1.1\r
          Host: www.example.com\r
          If-None-Match: %s\r
          \r
          """.formatted(etag));

          test.handler = HttpHandler.noop();
        }),

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: %s\r
        \r
        """.formatted(etag)
    );
  }

  private String etag(String resp) {
    final String[] lines;
    lines = resp.split("\\R");

    final String l1;
    l1 = lines[1];

    assertTrue(l1.startsWith("ETag: "));

    return l1.substring("ETag: ".length());
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