/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import org.testng.annotations.Test;

public class HttpExchangeTest4ParseVersion {

  @Test(description = "version: 1.1")
  public void testCase01() throws IOException {
    test(
        """
        GET / HTTP/1.1\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  private void badRequest(String request) throws IOException {
    final TestableSocket socket;
    socket = TestableSocket.of(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(
          http.toString(),

          """
          HTTP/1.1 400 Bad Request\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 22\r
          Connection: close\r
          \r
          Invalid request line.
          """
      );
    }
  }

  private void test(String request, Http.Version expected) throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), true);

      assertEquals(http.version(), expected);
    }
  }

}