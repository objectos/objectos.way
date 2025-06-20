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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.testng.annotations.Test;

public class MarketingSiteTest {

  @Test(description = """
  it should redirect '/' to '/index.html'
  """)
  public void testCase01() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, """
          GET / HTTP/1.1\r
          Host: marketing\r
          Connection: close\r
          \r
          """);

      resp(socket, """
          HTTP/1.1 301 Moved Permanently\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Location: /index.html\r
          \r
          """);
    }
  }

  @Test(description = """
  GET /index.html should return 200 OK
  """)
  public void testCase02() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Y.httpClient(
        "/index.html",

        builder -> builder.headers(
            "Host", "marketing",
            "Connection", "close"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.headers().allValues("Content-Type"), List.of("text/html; charset=utf-8"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));
    assertEquals(response.headers().allValues("Transfer-Encoding"), List.of("chunked"));
    assertEquals(response.body(), """
    <!DOCTYPE html>
    <h1>home</h1>
    """);
  }

  @Test(description = """
  HEAD /index.html should return 200 OK
  """)
  public void testCase03() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Y.httpClient(
        "/index.html",

        builder -> builder.HEAD().headers(
            "Host", "marketing",
            "Connection", "close"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.headers().allValues("Content-Type"), List.of("text/html; charset=utf-8"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));
    assertEquals(response.headers().allValues("Transfer-Encoding"), List.of("chunked"));
    assertEquals(response.body(), "");
  }

  @Test(description = """
  Other methods to /index.html should return 405 METHOD NOT ALLOWED
  """)
  public void testCase04() throws IOException {
    Y.test(
        Y.httpClient(
            "/index.html",

            builder -> builder.method("POST", BodyPublishers.noBody()).headers(
                "Host", "marketing"
            )
        ),

        """
        HTTP/1.1 405
        allow: GET, HEAD
        content-length: 0
        date: Wed, 28 Jun 2023 12:08:43 GMT

        """
    );
  }

  @Test(description = """
  GET /i-do-not-exist should return 404
  """)
  public void testCase05() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, """
          GET /i-do-not-exist HTTP/1.1\r
          Host: marketing\r
          Connection: close\r
          \r
          """);

      resp(socket, """
          HTTP/1.1 404 Not Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """);
    }
  }

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void req(Socket socket, String string) throws IOException {
    OutputStream out;
    out = socket.getOutputStream();

    byte[] bytes;
    bytes = string.getBytes(StandardCharsets.UTF_8);

    out.write(bytes);
  }

  private void resp(Socket socket, String expected) throws IOException {
    byte[] expectedBytes;
    expectedBytes = expected.getBytes(StandardCharsets.UTF_8);

    InputStream in;
    in = socket.getInputStream();

    byte[] bytes;
    bytes = in.readNBytes(expectedBytes.length);

    String res;
    res = new String(bytes, StandardCharsets.UTF_8);

    assertEquals(res, expected);
  }

}