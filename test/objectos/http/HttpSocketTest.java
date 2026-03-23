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

  @Test(description = """
  - string matches
  - buffer initially empty
  - single read
  """)
  public void matches0() throws IOException {
    final String req1;
    req1 = "DELETE ";

    final HttpSocket http;
    http = http(64, 64, req1);

    assertEquals(http.readByte(), 'D');
    assertEquals(http.matches(ascii("DELETE "), 1), true);
  }

  @Test(description = """
  - string matches
  - buffer initially empty
  - slow client
  """)
  public void matches1() throws IOException {
    final String req1;
    req1 = "DELETE ";

    final HttpSocket http;
    http = http(64, 64, Y.slowStream(1, req1));

    assertEquals(http.readByte(), 'D');
    assertEquals(http.matches(ascii("DELETE "), 1), true);
  }

  @Test(description = "Read into initial buffer")
  public void readByte01() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final HttpSocket http;
    http = http(64, 64, req1);

    assertEquals(readByte0(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);
  }

  @Test(description = "Read into initial buffer, subsequent requires resize")
  public void readByte02() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final HttpSocket http;
    http = http(64, 128, req1 + req2);

    assertEquals(readByte0(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);

    assertEquals(readByte0(http, 64), req2);

    assertEquals(http.bufferToAscii(), req1 + req2);
  }

  @Test(description = "Error: data exceeds max buffer length")
  public void readByte03() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final HttpSocket http;
    http = http(64, 64, req1 + req2);

    assertEquals(readByte0(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);

    try {
      http.readByte();

      Assert.fail();
    } catch (HttpSocketOverflow expected) {
      // noop
    }
  }

  @Test(description = "EOF")
  public void readByte04() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final HttpSocket http;
    http = http(64, 128, req1);

    assertEquals(readByte0(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);

    try {
      http.readByte();

      Assert.fail();
    } catch (HttpSocketEof expected) {
      // noop
    }
  }

  @Test(description = "IOException")
  public void readByte05() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final IOException exception;
    exception = Y.trimStackTrace(new IOException("Read Error"), 1);

    final HttpSocket http;
    http = http(64, 128, req1, exception);

    assertEquals(readByte0(http, 64), req1);

    assertEquals(http.bufferToAscii(), req1);

    try {
      http.readByte();

      Assert.fail();
    } catch (IOException expected) {
      assertSame(expected, exception);
    }
  }

  @Test
  public void powerOfTwo() {
    assertEquals(HttpSocket.powerOfTwo(127), 128);
    assertEquals(HttpSocket.powerOfTwo(128), 128);
    assertEquals(HttpSocket.powerOfTwo(129), 256);
    assertEquals(HttpSocket.powerOfTwo(1023), 1024);
    assertEquals(HttpSocket.powerOfTwo(1024), 1024);
    assertEquals(HttpSocket.powerOfTwo(1025), 2048);
    assertEquals(HttpSocket.powerOfTwo(16383), 16384);
    assertEquals(HttpSocket.powerOfTwo(16384), 16384);
    assertEquals(HttpSocket.powerOfTwo(16385), 16384);
  }

  private byte[] ascii(String s) {
    return s.getBytes(StandardCharsets.US_ASCII);
  }

  private String readByte0(HttpSocket socket, int len) throws IOException {
    final byte[] bytes;
    bytes = new byte[len];

    for (int i = 0; i < len; i++) {
      bytes[i] = socket.readByte();
    }

    return new String(bytes, StandardCharsets.UTF_8);
  }

  private HttpSocket http(int initial, int max, Object... data) throws IOException {
    return HttpSocket.of(
        initial,

        max,

        Y.socket(data)
    );
  }

}