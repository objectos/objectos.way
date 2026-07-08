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
package objectox.http.host;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.IOException;
import objectos.http.Content;
import objectos.http.ContentProvider;
import objectos.http.Status;
import objectos.lang.Throwables;
import objectos.http.MediaType;
import objectos.http.Request;
import objectos.http.Response;
import objectos.http.Result;
import objectos.http.StaticFile;
import objectox.http.resp.ResponseY;
import org.testng.annotations.Test;

public class ResultProcessorTest {

  private final IOException exception = Throwables.trimStackTrace(new IOException(), 1);

  private final ResultProcessor subject = new ResultProcessor();

  @Test
  public void content() {
    final Response res;
    res = subject.process(Content.of(MediaType.TEXT_PLAIN, "OK\n"));

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test
  public void contentProvider() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "OK\n");

    final ContentProvider provider;
    provider = () -> content;

    final Response res;
    res = subject.process(provider);

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test
  public void request() {
    final Request req;
    req = Request.create(opts -> {
      opts.path("/");
    });

    final Response res;
    res = subject.process(req);

    assertEquals(
        ResponseY.toString(res),

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

  @Test
  public void response() {
    final Response orig;
    orig = Response.create(_ -> {});

    final Response res;
    res = subject.process(orig);

    assertSame(res, orig);
  }

  @Test
  public void staticFile() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "OK\n");

    final StaticFile file;
    file = StaticFile.of(content);

    final Response res;
    res = subject.process(file);

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test
  public void status() {
    final Response res;
    res = subject.process(Status.BAD_REQUEST);

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 16\r
        \r
        400 Bad Request
        """
    );
  }

  @Test
  public void statusThrowable() {
    final Result result;
    result = Result.error(Status.INTERNAL_SERVER_ERROR, exception);

    final Response res;
    res = subject.process(result);

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 138\r
        \r
        500 Internal Server Error
        java.io.IOException
        	at objectos.way/objectox.http.host.ResultProcessorTest.<init>(ResultProcessorTest.java:36)
        """
    );
  }

}