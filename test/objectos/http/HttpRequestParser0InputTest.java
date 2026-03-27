/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * input://www.apache.org/licenses/LICENSE-2.0
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

public class HttpRequestParser0InputTest {

  @Test(description = """
  - string matches
  - buffer initially empty
  - single read
  """)
  public void matches0() throws IOException {
    final String req1;
    req1 = "DELETE ";

    final HttpRequestParser0Input input;
    input = input(64, 64, req1);

    assertEquals(input.readByte(), 'D');
    assertEquals(input.matches(ascii("DELETE "), 1), true);
  }

  @Test(description = """
  - string matches
  - buffer initially empty
  - slow client
  """)
  public void matches1() throws IOException {
    final String req1;
    req1 = "DELETE ";

    final HttpRequestParser0Input input;
    input = input(64, 64, Y.slowStream(1, req1));

    assertEquals(input.readByte(), 'D');
    assertEquals(input.matches(ascii("DELETE "), 1), true);
  }

  @Test(description = "Read into initial buffer")
  public void readByte01() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final HttpRequestParser0Input input;
    input = input(64, 64, req1);

    assertEquals(readByte0(input, 64), req1);

    assertEquals(input.bufferToAscii(), req1);
  }

  @Test(description = "Read into initial buffer, subsequent requires resize")
  public void readByte02() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final HttpRequestParser0Input input;
    input = input(64, 128, req1 + req2);

    assertEquals(readByte0(input, 64), req1);

    assertEquals(input.bufferToAscii(), req1);

    assertEquals(readByte0(input, 64), req2);

    assertEquals(input.bufferToAscii(), req1 + req2);
  }

  @Test(description = "Error: data exceeds max buffer length")
  public void readByte03() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final HttpRequestParser0Input input;
    input = input(64, 64, req1 + req2);

    assertEquals(readByte0(input, 64), req1);

    assertEquals(input.bufferToAscii(), req1);

    try {
      input.readByte();

      Assert.fail();
    } catch (HttpRequestParser0Input.Overflow expected) {
      // noop
    }
  }

  @Test(description = "EOF")
  public void readByte04() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final HttpRequestParser0Input input;
    input = input(64, 128, req1);

    assertEquals(readByte0(input, 64), req1);

    assertEquals(input.bufferToAscii(), req1);

    try {
      input.readByte();

      Assert.fail();
    } catch (HttpRequestParser0Input.Eof expected) {
      // noop
    }
  }

  @Test(description = "IOException")
  public void readByte05() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final IOException exception;
    exception = Y.trimStackTrace(new IOException("Read Error"), 1);

    final HttpRequestParser0Input input;
    input = input(64, 128, req1, exception);

    assertEquals(readByte0(input, 64), req1);

    assertEquals(input.bufferToAscii(), req1);

    try {
      input.readByte();

      Assert.fail();
    } catch (IOException expected) {
      assertSame(expected, exception);
    }
  }

  @Test
  public void powerOfTwo() {
    assertEquals(HttpRequestParser0Input.powerOfTwo(127), 128);
    assertEquals(HttpRequestParser0Input.powerOfTwo(128), 128);
    assertEquals(HttpRequestParser0Input.powerOfTwo(129), 256);
    assertEquals(HttpRequestParser0Input.powerOfTwo(1023), 1024);
    assertEquals(HttpRequestParser0Input.powerOfTwo(1024), 1024);
    assertEquals(HttpRequestParser0Input.powerOfTwo(1025), 2048);
    assertEquals(HttpRequestParser0Input.powerOfTwo(32767), 32768);
    assertEquals(HttpRequestParser0Input.powerOfTwo(32768), 32768);
    assertEquals(HttpRequestParser0Input.powerOfTwo(32769), 32768);
  }

  private byte[] ascii(String s) {
    return s.getBytes(StandardCharsets.US_ASCII);
  }

  private String readByte0(HttpRequestParser0Input socket, int len) throws IOException {
    final byte[] bytes;
    bytes = new byte[len];

    for (int i = 0; i < len; i++) {
      bytes[i] = socket.readByte();
    }

    return new String(bytes, StandardCharsets.UTF_8);
  }

  private HttpRequestParser0Input input(int initial, int max, Object... data) throws IOException {
    return HttpRequestParser0Input.of(
        initial,

        max,

        Y.socket(data)
    );
  }

}