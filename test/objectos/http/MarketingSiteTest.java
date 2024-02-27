/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
          HTTP/1.1 301 MOVED PERMANENTLY\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Location: /index.html\r
          \r
          """);
    }
  }

  @Test(description = """
  GET /index.html should return 200 OK
  """)
  public void testCase02() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, """
          GET /index.html HTTP/1.1\r
          Host: marketing\r
          Connection: close\r
          \r
          """);

      resp(socket, """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Content-Length: 30\r
          \r
          <!DOCTYPE html>
          <h1>home</h1>
          """);
    }
  }

  @Test(description = """
  HEAD /index.html should return 200 OK
  """)
  public void testCase03() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, """
          HEAD /index.html HTTP/1.1\r
          Host: marketing\r
          Connection: close\r
          \r
          """);

      resp(socket, """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Content-Length: 30\r
          \r
          """);
    }
  }

  @Test(description = """
  Other methods to /index.html should return 405 METHOD NOT ALLOWED
  """)
  public void testCase04() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, """
          TRACE /index.html HTTP/1.1\r
          Host: marketing\r
          Connection: close\r
          \r
          """);

      resp(socket, """
          HTTP/1.1 405 METHOD NOT ALLOWED\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Connection: close\r
          \r
          """);
    }
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
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
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