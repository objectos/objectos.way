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
import java.net.Socket;
import org.testng.annotations.Test;

public class HttpExchangeTest0Read {

  @Test(description = "Read into initial buffer")
  public void read01() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final int length;
    length = req1.length();

    try (HttpExchange http = http(length, length, req1)) {
      byte nextState;
      nextState = HttpExchange.$PARSE_METHOD;

      byte read;
      read = http.toRead(nextState);

      assertEquals(http.execute(read), nextState);

      assertEquals(http.bufferToAscii(), req1);
    }
  }

  @Test(description = "Read into initial buffer, subsequent requires resize")
  public void read02() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final int len1;
    len1 = req1.length();

    final int len2;
    len2 = req2.length();

    try (HttpExchange http = http(len1, len1 + len2, req1 + req2)) {
      byte nextState;
      nextState = HttpExchange.$PARSE_METHOD;

      byte read;
      read = http.toRead(nextState);

      assertEquals(http.execute(read), nextState);

      assertEquals(http.bufferToAscii(), req1);

      read = http.toRead(nextState);

      assertEquals(http.execute(read), nextState);

      assertEquals(http.bufferToAscii(), req1 + req2);
    }
  }

  @Test(description = "Error: data exceeds max buffer length")
  public void read03() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final int len1;
    len1 = req1.length();

    try (HttpExchange http = http(len1, len1, req1 + req2)) {
      byte nextState;
      nextState = HttpExchange.$PARSE_METHOD;

      byte read;
      read = http.toRead(nextState);

      assertEquals(http.execute(read), nextState);

      assertEquals(http.bufferToAscii(), req1);

      read = http.toRead(nextState);

      assertEquals(http.execute(read), HttpExchange.$READ_MAX_BUFFER);

      assertEquals(http.bufferToAscii(), req1);
    }
  }

  @Test(description = "EOF")
  public void read04() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final int length;
    length = req1.length();

    try (HttpExchange http = http(length, length * 2, req1)) {
      byte nextState;
      nextState = HttpExchange.$PARSE_METHOD;

      byte read;
      read = http.toRead(nextState);

      assertEquals(http.execute(read), nextState);

      assertEquals(http.bufferToAscii(), req1);

      read = http.toRead(nextState);

      assertEquals(http.execute(read), HttpExchange.$READ_EOF);

      assertEquals(http.bufferToAscii(), req1 + "\0".repeat(64));
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

    try (HttpExchange http = new HttpExchange(socket, 64, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      byte nextState;
      nextState = HttpExchange.$PARSE_METHOD;

      byte read;
      read = http.toRead(nextState);

      assertEquals(http.execute(read), nextState);

      assertEquals(http.bufferToAscii(), req1);

      read = http.toRead(nextState);

      assertEquals(http.execute(read), HttpExchange.$ERROR);
    }
  }

  private HttpExchange http(int initial, int max, Object... data) throws IOException {
    Socket socket;
    socket = Y.socket(data);

    return new HttpExchange(socket, initial, max, TestingClock.FIXED, TestingNoteSink.INSTANCE);
  }

  @Test
  public void powerOfTwo() {
    assertEquals(HttpExchange.powerOfTwo(127), 128);
    assertEquals(HttpExchange.powerOfTwo(128), 128);
    assertEquals(HttpExchange.powerOfTwo(129), 256);
    assertEquals(HttpExchange.powerOfTwo(1023), 1024);
    assertEquals(HttpExchange.powerOfTwo(1024), 1024);
    assertEquals(HttpExchange.powerOfTwo(1025), 2048);
    assertEquals(HttpExchange.powerOfTwo(16383), 16384);
    assertEquals(HttpExchange.powerOfTwo(16384), 16384);
    assertEquals(HttpExchange.powerOfTwo(16385), 16384);
  }

}