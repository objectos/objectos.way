/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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
import static org.testng.Assert.assertSame;

import module java.base;

import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpSocketTest {

  @Test(description = "Read into initial buffer")
  public void read01() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final HttpSocket http;
    http = http(64, 64, req1);

    assertEquals(consume(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);
  }

  @Test(description = "Read into initial buffer, subsequent requires resize")
  public void read02() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final HttpSocket http;
    http = http(64, 128, req1 + req2);

    assertEquals(consume(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);

    assertEquals(consume(http, 64), req2);

    assertEquals(http.bufferToAscii(), req1 + req2);
  }

  @Test(description = "Error: data exceeds max buffer length")
  public void read03() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final HttpSocket http;
    http = http(64, 64, req1 + req2);

    assertEquals(consume(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);

    try {
      http.readByte();

      Assert.fail();
    } catch (HttpReadException expected) {
      assertEquals(expected.kind, HttpReadException.Kind.BUFFER_OVERFLOW);
    }
  }

  @Test(description = "EOF")
  public void read04() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final HttpSocket http;
    http = http(64, 128, req1);

    assertEquals(consume(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);

    try {
      http.readByte();

      Assert.fail();
    } catch (HttpReadException expected) {
      assertEquals(expected.kind, HttpReadException.Kind.EOF);
    }
  }

  @Test(description = "IOException")
  public void read05() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final IOException exception;
    exception = Y.trimStackTrace(new IOException("Read Error"), 1);

    final Socket socket;
    socket = Y.socket(req1, exception);

    final HttpSocket http = HttpSocket.of(64, 128, socket);

    assertEquals(consume(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);

    try {
      http.readByte();

      Assert.fail();
    } catch (IOException expected) {
      assertSame(expected, exception);
    }
  }

  private String consume(HttpSocket socket, int len) throws IOException {
    final byte[] bytes;
    bytes = new byte[len];

    for (int i = 0; i < len; i++) {
      bytes[i] = socket.readByte();
    }

    return new String(bytes, StandardCharsets.UTF_8);
  }

  private HttpSocket http(int initial, int max, Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    return HttpSocket.of(initial, max, socket);
  }

  @Test
  public void powerOfTwo() {
    assertEquals(HttpExchangeImpl.powerOfTwo(127), 128);
    assertEquals(HttpExchangeImpl.powerOfTwo(128), 128);
    assertEquals(HttpExchangeImpl.powerOfTwo(129), 256);
    assertEquals(HttpExchangeImpl.powerOfTwo(1023), 1024);
    assertEquals(HttpExchangeImpl.powerOfTwo(1024), 1024);
    assertEquals(HttpExchangeImpl.powerOfTwo(1025), 2048);
    assertEquals(HttpExchangeImpl.powerOfTwo(16383), 16384);
    assertEquals(HttpExchangeImpl.powerOfTwo(16384), 16384);
    assertEquals(HttpExchangeImpl.powerOfTwo(16385), 16384);
  }

}