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
package objectox.http.server;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.http.HeaderName;
import objectos.http.Method;
import org.testng.annotations.Test;

public class SocketInputTest {

  @Test(description = """
  Support for:

  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    SocketInput input;
    input = regularInput("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    // first line
    input.parseLine();

    assertEquals(hasNext(input), true);
    assertEquals(peek(input), 'G');
    assertEquals(input.matches(ObjectoxRequestLine.STD_METHOD_BYTES[Method.GET.index()]), true);
    assertEquals(peek(input), '/');
    assertEquals(input.bufferIndex, 4);
    assertEquals(input.indexOf(Bytes.QUESTION_MARK, Bytes.SP), 5);
    input.bufferIndex = 5;
    assertEquals(next(input), Bytes.SP);
    assertEquals(input.matches(ObjectoxRequestLine.HTTP_VERSION_PREFIX), true);
    assertEquals(hasNext(input, 3), true);
    assertEquals(next(input), '1');
    assertEquals(next(input), '.');
    assertEquals(next(input), '1');
    assertEquals(input.consumeIfEndOfLine(), true);

    // second line
    input.parseLine();

    assertEquals(input.bufferIndex, 16);
    assertEquals(input.consumeIfEmptyLine(), false);
    byte[] host;
    host = ObjectoxServerRequestHeaders.STD_HEADER_NAME_BYTES[HeaderName.HOST.index()];
    assertEquals(input.matches(host), true);
    assertEquals(hasNext(input), true);
    assertEquals(next(input), ':');
  }

  @Test(description = "Support for request body parsing")
  public void testCase008() throws IOException {
    String body;
    body = "email=user%40example.com";

    SocketInput input;
    input = regularInput(body);

    long contentLength;
    contentLength = 24;

    assertEquals(input.canBuffer(contentLength), true);

    int read;
    read = input.read(24);

    assertEquals(read, 24);
    assertEquals(input.bufferToString(0, 24), body);
  }

  @Test(description = "Line is larger than initial buffer size")
  public void edge01() throws IOException {
    String line;
    line = "GET /abcdefghijklmnopqrstuvwxyz HTTP/1.1\r\n";

    assertEquals(line.length(), 42);

    SocketInput input;
    input = new SocketInput();

    input.bufferSize(32, 128);

    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(line);

    input.initSocketInput(inputStream);

    input.parseLine();

    StringBuilder sb;
    sb = new StringBuilder();

    while (hasNext(input)) {
      sb.append((char) next(input));
    }

    String res = sb.toString();

    assertEquals(res, "GET /abcdefghijklmnopqrstuvwxyz HTTP/1.1\r");
  }

  @Test
  public void powerOfTwo() {
    assertEquals(SocketInput.powerOfTwo(127), 128);
    assertEquals(SocketInput.powerOfTwo(128), 128);
    assertEquals(SocketInput.powerOfTwo(129), 256);
    assertEquals(SocketInput.powerOfTwo(1023), 1024);
    assertEquals(SocketInput.powerOfTwo(1024), 1024);
    assertEquals(SocketInput.powerOfTwo(1025), 2048);
    assertEquals(SocketInput.powerOfTwo(16383), 16384);
    assertEquals(SocketInput.powerOfTwo(16384), 16384);
    assertEquals(SocketInput.powerOfTwo(16385), 16384);
  }

  private boolean hasNext(SocketInput input) {
    return input.bufferIndex < input.lineLimit;
  }

  private boolean hasNext(SocketInput input, int count) {
    return input.bufferIndex + count - 1 < input.lineLimit;
  }

  private byte next(SocketInput input) {
    return input.buffer[input.bufferIndex++];
  }

  private byte peek(SocketInput input) {
    return input.buffer[input.bufferIndex];
  }

  private SocketInput regularInput(Object... data) {
    SocketInput input;
    input = new SocketInput();

    input.bufferSize(64, 128);

    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(data);

    input.initSocketInput(inputStream);

    return input;
  }

}