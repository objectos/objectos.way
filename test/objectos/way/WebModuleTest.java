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
package objectos.way;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import objectox.way.TestingH2;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WebModuleTest extends Web.Module {

  @BeforeClass
  public void beforeClass() {
    TestingHttpServer.bindWebModuleTest(this);
  }

  @Override
  protected final void configure() {
    source(TestingH2.SOURCE);

    route("/testCase01/trx", transactional(this::$testCase01));
    route("/testCase01/xrt", this::$testCase01);
  }

  private void $testCase01(Http.Exchange http) {
    Sql.Transaction trx;
    trx = http.get(Sql.Transaction.class);

    if (trx == null) {
      http.okText("trx is null\n", StandardCharsets.UTF_8);
    } else {
      http.okText("trx is here\n", StandardCharsets.UTF_8);
    }
  }

  @Test
  public void testCase01() throws IOException {
    try (Socket socket = newSocket()) {
      test(
          socket,

          """
          GET /testCase01/trx HTTP/1.1\r
          Host: web.module.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 12\r
          \r
          trx is here
          """
      );

      test(
          socket,

          """
          GET /testCase01/xrt HTTP/1.1\r
          Host: web.module.test\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 12\r
          \r
          trx is null
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
