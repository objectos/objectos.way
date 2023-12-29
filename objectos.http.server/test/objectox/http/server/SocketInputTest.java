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
import org.testng.annotations.Test;

public class SocketInputTest {

  @Test(description = """
  Support for:

  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase01() throws IOException {
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
    assertEquals(input.matches(ObjectoxMethod.GET.nameAndSpace), true);
    assertEquals(input.peek(), '/');
    assertEquals(input.index(), 4);
    assertEquals(input.indexOf(Bytes.QUESTION_MARK, Bytes.SP), 5);
    assertEquals(input.setAndNext(5), Bytes.SP);
    assertEquals(input.matches(ObjectoxRequestLine.HTTP_VERSION_PREFIX), true);
    assertEquals(input.hasNext(3), true);
    assertEquals(input.next(), '1');
    assertEquals(input.next(), '.');
    assertEquals(input.next(), '1');
    assertEquals(input.endOfLine(), true);

    // second line
    input.parseLine();

    assertEquals(input.hasNext(), true);
    assertEquals(input.peek(), 'H');
  }

  private SocketInput regularInput(Object... data) {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(data);

    return new SocketInput(64, inputStream);
  }

}