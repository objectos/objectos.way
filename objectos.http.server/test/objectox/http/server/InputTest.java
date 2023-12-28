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

public class InputTest {

  @Test(description = """
  hasNext() / next()

  - happy path: input stream fits in buffer
  """)
  public void testCase01() throws IOException {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of("FOO\r\n");

    Input input;
    input = new Input(64, inputStream);

    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), 'F');
    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), 'O');
    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), 'O');
    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), '\r');
    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), '\n');
    assertEquals(input.hasNext(), false);
  }

  @Test(description = """
  test()

  - happy path: input stream fits in buffer
  """)
  public void testCase02() throws IOException {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of("1ABC23");

    Input input;
    input = new Input(64, inputStream);

    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), '1');
    assertEquals(input.test(new byte[] {'A', 'B', 'C'}), true);
    input.skip(3);
    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), '2');
    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), '3');
    assertEquals(input.hasNext(), false);
  }

}