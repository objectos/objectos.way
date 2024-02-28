/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import objectos.http.UriPath.Segment;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpModuleTest extends HttpModule {

  @BeforeClass
  public void beforeClass() {
    TestingHttpServer.bindHttpModuleTest(this);
  }

  @Override
  protected final void configure() {
    // matches: /testCase01/foo
    // but not: /testCase01, /testCase01/, /testCase01/foo/bar
    route(segments(eq("testCase01"), nonEmpty()), this::testCase01);
  }

  private void testCase01(ServerExchange http) {
    UriPath path;
    path = http.path();

    List<Segment> segments;
    segments = path.segments();

    Segment second;
    second = segments.get(1);

    String text;
    text = second.value();

    SingleParagraph html;
    html = new SingleParagraph(text);

    http.ok(html);
  }

  @Test
  public void testCase01() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase01/foo HTTP/1.1\r
          Host: http.module.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Content-Length: 26\r
          \r
          <html>
          <p>foo</p>
          </html>
          """
      );
    }
  }

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void test(Socket socket, String request, String expectedResponse) throws IOException {
    TestingHttpServer.test(socket, request, expectedResponse);
  }

}