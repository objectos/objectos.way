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
package objectox.http.media;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Clock;
import java.time.Instant;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.http.Content;
import objectos.http.ContentProvider;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.http.MediaType;
import objectos.http.Request;
import objectos.http.Response;
import objectos.http.Result;
import objectos.http.StaticFile;
import objectos.way.Y;
import objectos.y.BasicFileAttributesY;
import objectos.y.PathY;
import objectox.http.RequestMethodEnum;
import objectox.http.resp.ResponseY;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
public class StaticFilesTest {

  private StaticFiles create(Consumer<? super StaticFilesBuilder> more) throws IOException {
    return StaticFiles.create(opts -> {
      opts.etag = attrs -> {
        final Clock clock;
        clock = Y.clockFixed();

        final Instant instant;
        instant = clock.instant();

        final BasicFileAttributes modified;
        modified = BasicFileAttributesY.lastModifiedTime(attrs, instant);

        final StaticFilesETag etag;
        etag = new StaticFilesETag(0L);

        return etag.apply(modified);
      };

      more.accept(opts);
    });
  }

  @Test(description = "Result < Request should behave as handle")
  public void apply01() throws IOException {
    final StaticFiles subject;
    subject = create(opts -> {
      opts.contentTypes(".txt: text/plain");

      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "tc01.txt", "TC01");

      opts.addDirectory(root);
    });

    final Result res;
    res = subject.apply(null, Request.create(opts -> { opts.path("/tc01.txt"); }));

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

  @DataProvider
  public Object[][] passThroughProvider() {
    return new Object[][] {
        {Content.of(MediaType.TEXT_PLAIN, "ok\n")},
        {(ContentProvider) () -> Content.of(MediaType.TEXT_PLAIN, "ok\n")},
        {Response.create(_ -> {})},
        {Status.NOT_FOUND}
    };
  }

  @Test(dataProvider = "passThroughProvider", description = "Result not Request nor StaticFile -> pass through")
  public void apply02(Result initial) throws IOException {
    final StaticFiles subject;
    subject = create(_ -> {});

    final Result res;
    res = subject.apply(null, initial);

    assertSame(res, initial);
  }

  @Test
  public void apply03() throws IOException {
    final StaticFiles subject;
    subject = create(opts -> {
      opts.contentTypes(".txt: text/plain");

      opts.etag = _ -> "foo-bar";
    });

    final Request request;
    request = Request.create(opts -> {
      opts.path("/tc01.txt");
    });

    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "CONTENT\n");

    final StaticFile file;
    file = StaticFile.of(content);

    final Result res;
    res = subject.apply(request, file);

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: foo-bar\r
        Content-Type: text/plain\r
        Content-Length: 8\r
        \r
        CONTENT
        """
    );
  }

  @Test(description = """
  handle:
  - existing file
  - file @ root
  - mapped content-type
  """)
  public void handle01() throws IOException {
    final StaticFiles handler;
    handler = create(opts -> {
      opts.contentTypes(".txt: text/plain");

      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "tc01.txt", "TC01");

      opts.addDirectory(root);
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
  public void handle02() throws IOException {
    final StaticFiles handler;
    handler = create(opts -> {
      opts.contentTypes(".json: application/json");

      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "sub/tc02.json", "[\"TC02\"]");

      opts.addDirectory(root);
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
  public void handle03() throws IOException {
    final StaticFiles handler;
    handler = create(opts -> {
      opts.contentTypes("*: application/octet-stream");

      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "tc03.foo", "TC03");

      opts.addDirectory(root);
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
  public void handle04() throws IOException {
    final StaticFiles handler;
    handler = create(opts -> {
      opts.contentTypes(".txt: text/plain");

      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "tc04.txt", "TC04");

      opts.addDirectory(root);
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.method(RequestMethodEnum.HEAD);

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
  public Iterator<RequestMethodEnum> methodProvider() {
    return Stream.of(RequestMethodEnum.VALUES)
        .filter(m -> !m.equals(RequestMethodEnum.GET) && !m.equals(RequestMethodEnum.HEAD))
        .iterator();
  }

  @Test(dataProvider = "methodProvider")
  public void methodNotAllowed01(RequestMethodEnum method) throws IOException {
    final StaticFiles handler;
    handler = create(opts -> {
      opts.contentTypes(".txt: text/plain");

      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "not-allowed.txt", method.name());

      opts.addDirectory(root);
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
  public void notModified01() throws IOException {
    final StaticFiles handler;
    handler = create(opts -> {
      opts.contentTypes(".txt: text/plain");

      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "tc01.txt", "TC01");

      opts.addDirectory(root);
    });

    final Result res;
    res = handler.handle(Request.create(opts -> {
      opts.path("/tc01.txt");

      opts.header(HeaderName.IF_NONE_MATCH, "18901e7e8f8-4");
    }));

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-4\r
        Content-Length: 0\r
        \r
        """
    );
  }

  @Test(description = """
  skip:
  - non-existing file
  """)
  public void skip01() throws IOException {
    final StaticFiles handler;
    handler = create(_ -> {
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
  public void skip02() throws IOException {
    final StaticFiles handler;
    handler = create(_ -> {
    });

    final Request request;
    request = Request.create(opts -> {
      opts.path("/");
    });

    final Result res;
    res = handler.handle(request);

    assertSame(res, request);
  }

}