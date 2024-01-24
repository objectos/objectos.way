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
package objectos.web;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.notes.Note;
import objectos.way.TestingNoteSink;
import objectox.http.server.TestingClock;
import org.testng.annotations.Test;

public class WayWebServerTest {

  @Test(description = """
  It should default to loopback
  """)
  public void testCase01() throws Exception {
    Handler handler = http -> {
      http.status(Status.OK);
      http.header(HeaderName.CONTENT_TYPE, "text/plain");
      http.header(HeaderName.CONTENT_LENGTH, 5);
      http.dateNow();
      http.send("TC01\n".getBytes(StandardCharsets.UTF_8));
    };

    HandlerFactory factory = () -> handler;

    WayWebServer server;
    server = new WayWebServer(factory);

    try {
      server.bufferSize(128, 256);
      server.clock(TestingClock.FIXED);
      server.noteSink(new ThisNoteSink(server));

      server.start();

      test(
          server,

          """
          GET / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Content-Type: text/plain\r
          Content-Length: 5\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          \r
          TC01
          """
      );
    } finally {
      server.close();
    }
  }

  private void test(WayWebServer server, String request, String expectedResponse) throws Exception {
    synchronized (server) {
      server.wait();
    }

    InetAddress address;
    address = server.address();

    int port;
    port = server.port();

    try (Socket socket = new Socket(address, port)) {
      OutputStream out;
      out = socket.getOutputStream();

      byte[] requestBytes;
      requestBytes = request.getBytes(StandardCharsets.UTF_8);

      out.write(requestBytes);

      byte[] expectedBytes;
      expectedBytes = expectedResponse.getBytes(StandardCharsets.UTF_8);

      InputStream in;
      in = socket.getInputStream();

      byte[] responseBytes;
      responseBytes = in.readNBytes(expectedBytes.length);

      String res;
      res = new String(responseBytes, StandardCharsets.UTF_8);

      assertEquals(res, expectedResponse);
    }
  }

  private static class ThisNoteSink extends TestingNoteSink {

    private final Object lock;

    public ThisNoteSink(Object lock) { this.lock = lock; }

    @Override
    protected void visitNote(Note note) {
      if (note == WebServer.LISTENING) {
        synchronized (lock) {
          lock.notify();
        }
      }
    }

  }

}