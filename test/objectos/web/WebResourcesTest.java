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
package objectos.web;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.function.Consumer;
import objectos.http.HttpHandler;
import objectos.http.HttpMethod;
import objectos.http.HttpServerTaskY;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.Test;

public class WebResourcesTest {

  @Test(description = """
  Options::addDirectory
  """)
  public void testCase01() throws IOException {
    final WebResources0 resources;
    resources = create(opts -> {
      Path src;
      src = Y.nextTempDir();

      Path a;
      a = Path.of("tc01.txt");

      write(src, a, "AAAA\n");

      opts.addDirectory(src);

      opts.contentTypes(".txt: text/plain; charset=utf-8");
    });

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc01.txt HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          test.handler = HttpHandler.of(routing -> {
            routing.handler(resources);
            routing.handler(HttpHandler.notFound());
          });
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
    final WebResources0 resources;
    resources = create(_ -> {});

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc02.txt HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          test.handler = HttpHandler.of(routing -> {
            routing.path("/tc02.txt", path -> {
              path.allow(HttpMethod.GET, resources, http -> {
                final String text;
                text = "BBBB\n";

                final Media.Bytes object;
                object = Media.Bytes.textPlain(text, StandardCharsets.UTF_8);

                http.ok(object);
              });
            });
          });
        }),

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

  @Test(description = """
  It should 405 if the method is not GET or HEAD
  """)
  public void testCase03() throws IOException {
    final WebResources0 resources;
    resources = create(opts -> {
      Path src;
      src = Y.nextTempDir();

      Path a;
      a = Path.of("tc03.txt");

      write(src, a, "AAAA\n");

      opts.addDirectory(src);

      opts.contentTypes(".txt: text/plain; charset=utf-8");
    });

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          POST /tc03.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = HttpHandler.of(routing -> {
            routing.handler(resources);
            routing.handler(HttpHandler.notFound());
          });
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

  @Test(description = """
  It should return 304 when if-none-match
  """)
  public void testCase05() throws IOException {
    final WebResources0 resources;
    resources = create(opts -> {
      Path src;
      src = Y.nextTempDir();

      Path a;
      a = Path.of("tc05.txt");

      write(src, a, "AAAA\n");

      opts.addDirectory(src);

      opts.contentTypes(".txt: text/plain; charset=utf-8");
    });

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc05.txt HTTP/1.1\r
          Host: www.example.com\r
          If-None-Match: 18901e7e8f8-5\r
          Connection: close\r
          \r
          """);

          test.handler = HttpHandler.of(routing -> {
            routing.handler(resources);
            routing.handler(HttpHandler.notFound());
          });
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
    final WebResources0 resources;
    resources = create(_ -> {});

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc06.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = http -> {
            try {
              String path;
              path = http.path();

              Media.Bytes contents;
              contents = Media.Bytes.textPlain("CCCC\n");

              resources.writeMedia(path, contents);

              final Path rootDirectory;
              rootDirectory = resources.rootDirectory();

              final Path target;
              target = rootDirectory.resolve("tc06.txt");

              setLastModifiedTime(target);

              resources.handle(http);
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        CCCC
        """
    );

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc06.txt HTTP/1.1\r
          Host: www.example.com\r
          If-None-Match: 18901e7e8f8-5\r
          \r
          """);

          test.handler = resources;
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

  @Test(description = """
  Resources::writeMedia(Media.Text)
  """)
  public void testCase08() throws IOException {
    final WebResources0 resources;
    resources = create(_ -> {});

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc08.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = http -> {
            try {
              String path;
              path = http.path();

              Media.Text contents;
              contents = Y.mediaTextOf("8888\n");

              resources.writeMedia(path, contents);

              final Path rootDirectory;
              rootDirectory = resources.rootDirectory();

              final Path target;
              target = rootDirectory.resolve("tc08.txt");

              setLastModifiedTime(target);

              resources.handle(http);
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        8888
        """
    );

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc08.txt HTTP/1.1\r
          Host: www.example.com\r
          If-None-Match: 18901e7e8f8-5\r
          \r
          """);

          test.handler = resources;
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
  Resources::writeMedia(Media.Stream)
  """)
  public void testCase09() throws IOException, InterruptedException {
    final WebResources0 resources;
    resources = create(_ -> {});

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc09.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = http -> {
            try {
              String path;
              path = http.path();

              Media.Stream contents;
              contents = Y.mediaStreamOf("9999\n");

              resources.writeMedia(path, contents);

              final Path rootDirectory;
              rootDirectory = resources.rootDirectory();

              final Path target;
              target = rootDirectory.resolve("tc09.txt");

              setLastModifiedTime(target);

              resources.handle(http);
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        9999
        """
    );

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc09.txt HTTP/1.1\r
          Host: www.example.com\r
          If-None-Match: 18901e7e8f8-5\r
          \r
          """);

          test.handler = resources;
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
  Options::addMedia(Media.Bytes)
  """)
  public void testCase10() throws IOException {
    final WebResources0 resources;
    resources = create(opts -> {
      final Media media;
      media = Media.Bytes.textPlain("XXXX\n");

      opts.addMedia("/tc10.txt", media);
    });

    setLastModifiedTime(resources, "tc10.txt");

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc10.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = resources;
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        XXXX
        """
    );
  }

  @Test(description = """
  Options::addMedia(Media.Stream)
  """)
  public void testCase11() throws IOException {
    final WebResources0 resources;
    resources = create(opts -> {
      final Media media;
      media = Y.mediaStreamOf("XXXX\n");

      opts.addMedia("/tc11.txt", media);
    });

    setLastModifiedTime(resources, "tc11.txt");

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc11.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = resources;
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        XXXX
        """
    );
  }

  @Test(description = """
  Options::addMedia(Media.Text)
  """)
  public void testCase12() throws IOException {
    final WebResources0 resources;
    resources = create(opts -> {
      final Media media;
      media = Y.mediaTextOf("XXXX\n");

      opts.addMedia("/tc12.txt", media);
    });

    setLastModifiedTime(resources, "tc12.txt");

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc12.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = resources;
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        XXXX
        """
    );
  }

  @Test(description = """
  Options::addFile(InputStream);
  """)
  public void testCase13() throws IOException {
    final byte[] bytes;
    bytes = "test-case-13\n".getBytes(StandardCharsets.UTF_8);

    final WebResources0 resources;
    resources = create(opts -> {
      final InputStream in;
      in = new ByteArrayInputStream(bytes);

      opts.addFile("/tc13.txt", in);
    });

    setLastModifiedTime(resources, "tc13.txt");

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /tc13.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = resources;
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Type: application/octet-stream\r
        Content-Length: 13\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-d\r
        \r
        test-case-13
        """
    );
  }

  @Test(description = """
  Options::addFile(InputStream);
  """)
  public void testCase14() throws IOException {
    final byte[] bytes;
    bytes = "test-case-14\n".getBytes(StandardCharsets.UTF_8);

    final WebResources0 resources;
    resources = create(opts -> {
      final InputStream in;
      in = new ByteArrayInputStream(bytes);

      opts.addFile("/a/b/c/tc14.txt", in);
    });

    setLastModifiedTime(resources, "a/b/c/tc14.txt");

    assertEquals(
        HttpServerTaskY.resp(test -> {
          test.socket = Y.socket("""
          GET /a/b/c/tc14.txt HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          test.handler = resources;
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Type: application/octet-stream\r
        Content-Length: 13\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-d\r
        \r
        test-case-14
        """
    );
  }

  private WebResources0 create(Consumer<? super WebResources.Options> options) {
    try {
      final WebResources resources;
      resources = WebResources.create(opt -> {
        opt.noteSink(Y.noteSink());

        options.accept(opt);
      });

      Y.shutdownHook(resources);

      return (WebResources0) resources;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void write(Path directory, Path file, String text) {
    try {
      Path target;
      target = directory.resolve(file);

      Path parent;
      parent = target.getParent();

      Files.createDirectories(parent);

      Files.writeString(target, text);

      setLastModifiedTime(target);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void setLastModifiedTime(WebResources0 resources, String name) throws IOException {
    final Path rootDirectory;
    rootDirectory = resources.rootDirectory();

    final Path target;
    target = rootDirectory.resolve(name);

    setLastModifiedTime(target);
  }

  private void setLastModifiedTime(Path target) throws IOException {
    // set last modified time for etag purposes
    Clock clock;
    clock = Y.clockFixed();

    Instant instant;
    instant = clock.instant();

    FileTime fileTime;
    fileTime = FileTime.from(instant);

    Files.setLastModifiedTime(target, fileTime);
  }

}