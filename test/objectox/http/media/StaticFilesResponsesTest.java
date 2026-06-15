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

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import objectos.http.Response;
import objectos.y.PathY;
import objectox.http.resp.ResponseY;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StaticFilesResponsesTest {

  private StaticFilesResponses subject;

  @BeforeClass
  public void beforeClass() {
    final StaticFilesExtension staticFilesExtension;
    staticFilesExtension = new StaticFilesExtension("*");

    final StaticFilesTypes staticFilesTypes;
    staticFilesTypes = new StaticFilesTypes("application/octet-stream", Map.of(".txt", "text/plain; charset=utf-8"));

    subject = new StaticFilesResponses(staticFilesExtension, staticFilesTypes);
  }

  @Test
  public void methodNotAllowed() {
    final Response res;
    res = subject.methodNotAllowed();

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
  public void notModified() {
    final Response res;
    res = subject.notModified("foo-bar");

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

  @Test
  public void ok1() {
    final Path file;
    file = PathY.nextFile(null, ".txt", "ok\n", StandardCharsets.UTF_8);

    final String etag;
    etag = "foo-bar";

    final Response res;
    res = subject.ok(file, etag);

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: foo-bar\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        ok
        """
    );
  }

  @Test
  public void ok2() {
    final Path file;
    file = PathY.nextFile(null, ".text", "ok\n", StandardCharsets.UTF_8);

    final String etag;
    etag = "foo-bar";

    final Response res;
    res = subject.ok(file, etag);

    assertEquals(
        ResponseY.toString(res),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: foo-bar\r
        Content-Type: application/octet-stream\r
        Content-Length: 3\r
        \r
        ok
        """
    );
  }

}