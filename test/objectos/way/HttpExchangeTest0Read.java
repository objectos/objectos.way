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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.way.HttpExchange.ParseStatus;
import org.testng.Assert;
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
    exception = new IOException("Read Error");

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

  @Test(description = """
  Support for:

  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    HttpExchange input;
    input = regularInput("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    // first line
    input.parseLine();

    assertEquals(input.parseStatus, ParseStatus.NORMAL);
    assertEquals(hasNext(input), true);
    assertEquals(peek(input), 'G');
    assertEquals(input.matches("GET ".getBytes(StandardCharsets.UTF_8)), true);
    assertEquals(peek(input), '/');
    assertEquals(input.bufferIndex, 4);
    assertEquals(input.indexOf(Bytes.QUESTION_MARK, Bytes.SP), 5);
    input.bufferIndex = 5;
    assertEquals(next(input), Bytes.SP);
    assertEquals(input.matches(HttpExchange.HTTP_VERSION_PREFIX), true);
    assertEquals(hasNext(input, 3), true);
    assertEquals(next(input), '1');
    assertEquals(next(input), '.');
    assertEquals(next(input), '1');
    assertEquals(input.consumeIfEndOfLine(), true);

    // second line
    input.parseLine();

    assertEquals(input.parseStatus, ParseStatus.NORMAL);
    assertEquals(input.bufferIndex, 16);
    assertEquals(input.consumeIfEmptyLine(), false);
    byte[] host;
    host = HttpHeaderName.HOST.getBytes(Http.Version.HTTP_1_1);
    assertEquals(input.matches(host), true);
    assertEquals(hasNext(input), true);
    assertEquals(next(input), ':');
  }

  @Test(description = "Support for request body parsing")
  public void testCase008() throws IOException {
    String body;
    body = "email=user%40example.com";

    HttpExchange input;
    input = regularInput(body);

    long contentLength;
    contentLength = 24;

    assertEquals(input.canBuffer(contentLength), true);

    int read;
    read = input.read(24);

    assertEquals(read, 24);
    assertEquals(input.bufferToString(0, 24), body);
  }

  @Test(description = """
  Request body is larger than buffer
  """)
  public void testCase019() throws IOException {
    String chunk256 = """
    .................................................
    .................................................
    .................................................
    .................................................
    .................................................
    123456""";

    HttpExchange input;
    input = regularInput(chunk256);

    long contentLength;
    contentLength = 256;

    assertEquals(input.canBuffer(contentLength), false);

    Path file;
    file = Files.createTempFile("objectos-way-socket-input-tc19-", ".tmp");

    try {
      long read;
      read = input.read(file, contentLength);

      assertEquals(read, contentLength);

      String s;
      s = Files.readString(file, StandardCharsets.UTF_8);

      assertEquals(s, chunk256);
    } finally {
      Files.delete(file);
    }
  }

  @Test(description = """
  It should properly handle EOF on subsequent request line
  """)
  public void testCase020() throws IOException {
    HttpExchange input;
    input = regularInput("""
    GET / HTTP/1.1\r
    """);

    input.parseLine();

    assertEquals(input.parseStatus, ParseStatus.NORMAL);
    assertEquals(hasNext(input), true);
    assertEquals(peek(input), 'G');
    assertEquals(input.matches("GET ".getBytes(StandardCharsets.UTF_8)), true);
    assertEquals(peek(input), '/');
    assertEquals(input.bufferIndex, 4);
    assertEquals(input.indexOf(Bytes.QUESTION_MARK, Bytes.SP), 5);
    input.bufferIndex = 5;
    assertEquals(next(input), Bytes.SP);
    assertEquals(input.matches(HttpExchange.HTTP_VERSION_PREFIX), true);
    assertEquals(hasNext(input, 3), true);
    assertEquals(next(input), '1');
    assertEquals(next(input), '.');
    assertEquals(next(input), '1');
    assertEquals(input.consumeIfEndOfLine(), true);

    // second line
    try {
      input.resetSocketInput();

      input.parseLine();

      Assert.fail("Should have thrown");
    } catch (Exception expected) {
      Class<? extends Exception> type;
      type = expected.getClass();

      assertEquals(type.getName(), "objectos.way.HttpExchange$RemoteClosedException");
    }
  }

  @Test(description = "Line is larger than initial buffer size")
  public void edge01() throws IOException {
    String line;
    line = "GET /abcdefghijklmnopqrstuvwxyz HTTP/1.1\r\n";

    assertEquals(line.length(), 42);

    Socket socket;
    socket = Y.socket(line);

    HttpExchange input;
    input = new HttpExchange(socket, 32, 128, null, TestingNoteSink.INSTANCE);

    input.parseLine();

    StringBuilder sb;
    sb = new StringBuilder();

    while (hasNext(input)) {
      sb.append((char) next(input));
    }

    String res = sb.toString();

    assertEquals(res, "GET /abcdefghijklmnopqrstuvwxyz HTTP/1.1\r");
  }

  @Test(description = "It should be possible to serialize contents for debugging purposes")
  public void hexDump() throws IOException {
    HttpExchange input;
    input = regularInput("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    input.parseLine();

    input.hexDump();
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

  private boolean hasNext(HttpExchange input) {
    return input.bufferIndex < input.lineLimit;
  }

  private boolean hasNext(HttpExchange input, int count) {
    return input.bufferIndex + count - 1 < input.lineLimit;
  }

  private byte next(HttpExchange input) {
    return input.buffer[input.bufferIndex++];
  }

  private byte peek(HttpExchange input) {
    return input.buffer[input.bufferIndex];
  }

  private HttpExchange regularInput(Object... data) throws IOException {
    Socket socket;
    socket = Y.socket(data);

    return new HttpExchange(socket, 64, 128, null, TestingNoteSink.INSTANCE);
  }

}