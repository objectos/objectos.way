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
import org.testng.annotations.Test;

public class WebResourcesTest {

  @Test(description = """
  Options::addDirectory
  """)
  public void testCase01() throws IOException {
    final WebResources resources;
    resources = create(opts -> {
      Path src;
      src = TestingDir.next();

      Path a;
      a = Path.of("tc01.txt");

      write(src, a, "AAAA\n");

      opts.addDirectory(src);

      opts.contentTypes(".txt: text/plain; charset=utf-8");
    });

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc01.txt HTTP/1.1\r
        Host: web.resources.test\r
        Connection: close\r
        \r
        """);

        xch.handler(Http.Handler.of(routing -> {
          routing.handler(resources);
          routing.handler(Http.Handler.notFound());
        }));

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        AAAA
        """);
      });
    });
  }

  @Test(description = """
  It should not handle if the file does not exist
  """)
  public void testCase02() throws IOException {
    final WebResources resources;
    resources = create(_ -> {});

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc02.txt HTTP/1.1\r
        Host: web.resources.test\r
        Connection: close\r
        \r
        """);

        xch.handler(Http.Handler.of(routing -> {
          routing.path("/tc02.txt", path -> {
            path.allow(Http.Method.GET, resources, http -> {
              final String text;
              text = "BBBB\n";

              final Media.Bytes object;
              object = Media.Bytes.textPlain(text, StandardCharsets.UTF_8);

              http.ok(object);
            });
          });
        }));

        xch.resp("""
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        \r
        BBBB
        """);
      });
    });
  }

  @Test(description = """
  It should 405 if the method is not GET or HEAD
  """)
  public void testCase03() throws IOException {
    final WebResources resources;
    resources = create(opts -> {
      Path src;
      src = TestingDir.next();

      Path a;
      a = Path.of("tc03.txt");

      write(src, a, "AAAA\n");

      opts.addDirectory(src);

      opts.contentTypes(".txt: text/plain; charset=utf-8");
    });

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        POST /tc03.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(Http.Handler.of(routing -> {
          routing.handler(resources);
          routing.handler(Http.Handler.notFound());
        }));

        xch.resp("""
        HTTP/1.1 405 Method Not Allowed\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Allow: GET, HEAD\r
        \r
        """);
      });
    });
  }

  @Test(description = """
  Web.Resources::reconfigure
  - resources from before reconfigure should 404
  """)
  public void testCase04() throws IOException {
    final WebResources resources;
    resources = create(opts -> {
      final Media.Bytes reconfigure;
      reconfigure = Media.Bytes.textPlain("reconfigure");

      opts.addMedia("/reconfigure.txt", reconfigure);
    });

    resources.reconfigure(_ -> {});

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /reconfigure.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(Http.Handler.of(routing -> {
          routing.handler(resources);
          routing.handler(Http.Handler.notFound());
        }));

        xch.resp("""
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Connection: close\r
        \r
        """);
      });
    });
  }

  @Test(description = """
  It should return 304 when if-none-match
  """)
  public void testCase05() throws IOException {
    final WebResources resources;
    resources = create(opts -> {
      Path src;
      src = TestingDir.next();

      Path a;
      a = Path.of("tc05.txt");

      write(src, a, "AAAA\n");

      opts.addDirectory(src);

      opts.contentTypes(".txt: text/plain; charset=utf-8");
    });

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc05.txt HTTP/1.1\r
        Host: web.resources.test\r
        If-None-Match: 18901e7e8f8-5\r
        Connection: close\r
        \r
        """);

        xch.handler(Http.Handler.of(routing -> {
          routing.handler(resources);
          routing.handler(Http.Handler.notFound());
        }));

        xch.resp("""
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: 18901e7e8f8-5\r
        \r
        """);
      });
    });
  }

  @Test(description = """
  Resources::writeMedia(Media.Bytes)
  """)
  public void testCase06() throws IOException {
    final WebResources resources;
    resources = create(_ -> {});

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc06.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(http -> {
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
        });

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        CCCC
        """);
      });

      test.xch(xch -> {
        xch.req("""
        GET /tc06.txt HTTP/1.1\r
        Host: web.resources.test\r
        If-None-Match: 18901e7e8f8-5\r
        \r
        """);

        xch.handler(resources);

        xch.resp("""
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: 18901e7e8f8-5\r
        \r
        """);
      });
    });
  }

  @Test(description = """
  Resources::delete
  """)
  public void testCase07() throws IOException {
    final WebResources resources;
    resources = create(_ -> {});

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc07.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(Http.Handler.of(routing -> {
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
          routing.handler(Http.Handler.notFound());
        }));

        xch.resp("""
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Connection: close\r
        \r
        """);
      });
    });
  }

  @Test(description = """
  Resources::writeMedia(Media.Text)
  """)
  public void testCase08() throws IOException {
    final WebResources resources;
    resources = create(_ -> {});

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc08.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(http -> {
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
        });

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        8888
        """);
      });

      test.xch(xch -> {
        xch.req("""
        GET /tc08.txt HTTP/1.1\r
        Host: web.resources.test\r
        If-None-Match: 18901e7e8f8-5\r
        \r
        """);

        xch.handler(resources);

        xch.resp("""
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: 18901e7e8f8-5\r
        \r
        """);
      });
    });
  }

  @Test(description = """
  Resources::writeMedia(Media.Stream)
  """)
  public void testCase09() throws IOException, InterruptedException {
    final WebResources resources;
    resources = create(_ -> {});

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc09.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(http -> {
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
        });

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        9999
        """);
      });

      test.xch(xch -> {
        xch.req("""
        GET /tc09.txt HTTP/1.1\r
        Host: web.resources.test\r
        If-None-Match: 18901e7e8f8-5\r
        \r
        """);

        xch.handler(resources);

        xch.resp("""
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        ETag: 18901e7e8f8-5\r
        \r
        """);
      });
    });
  }

  @Test(description = """
  Options::addMedia(Media.Bytes)
  """)
  public void testCase10() throws IOException {
    final WebResources resources;
    resources = create(opts -> {
      final Media media;
      media = Media.Bytes.textPlain("XXXX\n");

      opts.addMedia("/tc10.txt", media);
    });

    setLastModifiedTime(resources, "tc10.txt");

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc10.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(resources);

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        XXXX
        """);
      });
    });
  }

  @Test(description = """
  Options::addMedia(Media.Stream)
  """)
  public void testCase11() throws IOException {
    final WebResources resources;
    resources = create(opts -> {
      final Media media;
      media = Y.mediaStreamOf("XXXX\n");

      opts.addMedia("/tc11.txt", media);
    });

    setLastModifiedTime(resources, "tc11.txt");

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc11.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(resources);

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        XXXX
        """);
      });
    });
  }

  @Test(description = """
  Options::addMedia(Media.Text)
  """)
  public void testCase12() throws IOException {
    final WebResources resources;
    resources = create(opts -> {
      final Media media;
      media = Y.mediaTextOf("XXXX\n");

      opts.addMedia("/tc12.txt", media);
    });

    setLastModifiedTime(resources, "tc12.txt");

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc12.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(resources);

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        XXXX
        """);
      });
    });
  }

  @Test(description = """
  Options::addFile(InputStream);
  """)
  public void testCase13() throws IOException {
    final byte[] bytes;
    bytes = "test-case-13\n".getBytes(StandardCharsets.UTF_8);

    final WebResources resources;
    resources = create(opts -> {
      final InputStream in;
      in = new ByteArrayInputStream(bytes);

      opts.addFile("/tc13.txt", in);
    });

    setLastModifiedTime(resources, "tc13.txt");

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /tc13.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(resources);

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: application/octet-stream\r
        Content-Length: 13\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-d\r
        \r
        test-case-13
        """);
      });
    });
  }

  @Test(description = """
  Options::addFile(InputStream);
  """)
  public void testCase14() throws IOException {
    final byte[] bytes;
    bytes = "test-case-14\n".getBytes(StandardCharsets.UTF_8);

    final WebResources resources;
    resources = create(opts -> {
      final InputStream in;
      in = new ByteArrayInputStream(bytes);

      opts.addFile("/a/b/c/tc14.txt", in);
    });

    setLastModifiedTime(resources, "a/b/c/tc14.txt");

    Y.httpExchange(test -> {
      test.xch(xch -> {
        xch.req("""
        GET /a/b/c/tc14.txt HTTP/1.1\r
        Host: web.resources.test\r
        \r
        """);

        xch.handler(resources);

        xch.resp("""
        HTTP/1.1 200 OK\r
        Content-Type: application/octet-stream\r
        Content-Length: 13\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-d\r
        \r
        test-case-14
        """);
      });
    });
  }

  private WebResources create(Consumer<? super Web.Resources.Options> options) {
    try {
      final Web.Resources resources;
      resources = Web.Resources.create(opt -> {
        opt.noteSink(Y.noteSink());

        options.accept(opt);
      });

      Y.shutdownHook(resources);

      return (WebResources) resources;
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

  private void setLastModifiedTime(WebResources resources, String name) throws IOException {
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