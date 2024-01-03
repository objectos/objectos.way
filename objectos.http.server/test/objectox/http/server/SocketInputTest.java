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
import objectox.http.StandardHeaderName;
import objectox.http.StandardMethod;
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

    assertEquals(input.hasNext(), true);
    assertEquals(input.peek(), 'G');
    assertEquals(input.matches(ObjectoxRequestLine.STD_METHOD_BYTES.get(StandardMethod.GET)), true);
    assertEquals(input.peek(), '/');
    assertEquals(input.index(), 4);
    assertEquals(input.indexOf(Bytes.QUESTION_MARK, Bytes.SP), 5);
    assertEquals(input.setAndNext(5), Bytes.SP);
    assertEquals(input.matches(ObjectoxRequestLine.HTTP_VERSION_PREFIX), true);
    assertEquals(input.hasNext(3), true);
    assertEquals(input.next(), '1');
    assertEquals(input.next(), '.');
    assertEquals(input.next(), '1');
    assertEquals(input.consumeIfEndOfLine(), true);

    // second line
    input.parseLine();

    assertEquals(input.index(), 16);
    assertEquals(input.consumeIfEmptyLine(), false);
    byte[] host;
    host = ObjectoxServerRequestHeaders.STD_HEADER_NAME_BYTES.get(StandardHeaderName.HOST);
    assertEquals(input.matches(host), true);
    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), ':');
  }

  @Test(description = "Line is larger than initial buffer size")
  public void edge01() throws IOException {
    String line;
    line = "GET /abcdefghijklmnopqrstuvwxyz HTTP/1.1\r\n";

    assertEquals(line.length(), 42);

    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(line);

    SocketInput input;
    input = new SocketInput(32, inputStream);

    input.parseLine();

    StringBuilder res;
    res = new StringBuilder();

    while (input.hasNext()) {
      res.append((char) input.next());
    }

    assertEquals(res.toString(), "GET /abcdefghijklmnopqrstuvwxyz HTTP/1.1\r");
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

  private SocketInput regularInput(Object... data) throws IOException {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(data);

    return new SocketInput(64, inputStream);
  }

}