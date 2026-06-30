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
import objectos.http.RequestMethod;
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

  private StaticFiles create(Consumer<? super StaticFilesStageBuilder> more) throws IOException {
    final StaticFilesStageBuilder builder;
    builder = new StaticFilesStageBuilder();

    builder.etag(attrs -> {
      final Clock clock;
      clock = Y.clockFixed();

      final Instant instant;
      instant = clock.instant();

      final BasicFileAttributes modified;
      modified = BasicFileAttributesY.lastModifiedTime(attrs, instant);

      final StaticFilesETag etag;
      etag = new StaticFilesETag(0L);

      return etag.apply(modified);
    });

    more.accept(builder);

    final StaticFilesStage stage;
    stage = builder.build();

    return stage.toStaticFiles();
  }

  @Test(description = "path traversal => bad request")
  public void apply01() throws IOException {
    try (StaticFiles subject = create(opts -> {
      opts.withDefaultContentTypes();
    })) {
      final Request req;
      req = Request.create(opts -> {
        opts.path("/foo/../../tc01.txt");
      });

      final Result res;
      res = subject.apply(req, req);

      assertEquals(res, Status.BAD_REQUEST);
    }
  }

  @Test(description = "skip: non-existing file")
  public void apply02() throws IOException {
    try (StaticFiles subject = create(opts -> {
      opts.withDefaultContentTypes();
    })) {
      final Request req;
      req = Request.create(opts -> {
        opts.path("/tc02.txt");
      });

      final Result res;
      res = subject.apply(req, req);

      assertSame(res, req);
    }
  }

  @DataProvider
  public Iterator<RequestMethod> methodProvider() {
    return Stream.of(RequestMethodEnum.VALUES)
        .filter(m -> !m.equals(RequestMethodEnum.GET) && !m.equals(RequestMethodEnum.HEAD))
        .filter(v -> v.implemented)
        .map(RequestMethod.class::cast)
        .iterator();
  }

  @Test(dataProvider = "methodProvider")
  public void apply03(RequestMethod method) throws IOException {
    try (StaticFiles subject = create(opts -> {
      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "tc03.txt", "TC03");

      opts.addDirectory(root);

      opts.withDefaultContentTypes();
    })) {
      final Request req;
      req = Request.create(opts -> {
        opts.method(method);

        opts.path("/tc03.txt");
      });

      final Result res;
      res = subject.apply(req, req);

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
  }

  @Test(description = "ok: existing file")
  public void apply04() throws IOException {
    try (StaticFiles subject = create(opts -> {
      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "tc04.txt", "TC04");

      opts.addDirectory(root);

      opts.withDefaultContentTypes();
    })) {
      final Request req;
      req = Request.create(opts -> {
        opts.path("/tc04.txt");
      });

      final Result res;
      res = subject.apply(req, req);

      assertEquals(
          ResponseY.toString(res),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          ETag: 18901e7e8f8-4\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 4\r
          \r
          TC04\
          """
      );
    }
  }

  @Test
  public void apply05() throws IOException {
    try (StaticFiles subject = create(opts -> {
      final Path root;
      root = PathY.nextDir();

      PathY.write(root, "tc05.txt", "TC05");

      opts.addDirectory(root);

      opts.etag(_ -> "foo-bar");

      opts.withDefaultContentTypes();
    })) {
      final Request req;
      req = Request.create(opts -> {
        opts.path("/tc05.txt");

        opts.header(HeaderName.IF_NONE_MATCH, "foo-bar");
      });

      final Result res;
      res = subject.apply(req, req);

      assertEquals(
          ResponseY.toString(res),

          """
          HTTP/1.1 304 Not Modified\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          ETag: foo-bar\r
          Content-Length: 0\r
          \r
          """
      );
    }
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
  public void apply06(Result initial) throws IOException {
    try (StaticFiles subject = create(_ -> {})) {
      final Result res;
      res = subject.apply(null, initial);

      assertSame(res, initial);
    }
  }

  @Test(description = "It should create static file")
  public void apply07() throws IOException {
    try (StaticFiles subject = create(opts -> {
      opts.contentTypes(".txt: text/plain");

      opts.etag(_ -> "foo-bar");
    })) {
      final Request request;
      request = Request.create(opts -> {
        opts.path("/tc07.txt");
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
  }

}