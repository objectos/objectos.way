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
import org.testng.annotations.Test;

public class ObjectoxHeaderTest {

  @Test
  public void testCase01() throws IOException {
    ObjectoxHeader header;
    header = regularInput(": foo\r\n\r\n");

    header.parseValue();

    assertEquals(header.valueStart, 2);
    assertEquals(header.valueEnd, 5);
  }

  @Test(description = """
  No leading OWS
  """)
  public void edge01() throws IOException {
    ObjectoxHeader header;
    header = regularInput(":foo\r\n");

    header.parseValue();

    assertEquals(header.valueStart, 1);
    assertEquals(header.valueEnd, 4);
  }

  @Test(description = """
  Empty value
  """)
  public void edge02() throws IOException {
    ObjectoxHeader header;
    header = regularInput(":\r\n");

    header.parseValue();

    assertEquals(header.valueStart, 1);
    assertEquals(header.valueEnd, 1);
  }

  @Test(description = """
  Trailing OWS
  """)
  public void edge04() throws IOException {
    ObjectoxHeader header;
    header = regularInput(": foo \r\n");

    header.parseValue();

    assertEquals(header.valueStart, 2);
    assertEquals(header.valueEnd, 5);
  }

  @Test(description = """
  Trailing OWS with non-standard line break
  """)
  public void edge05() throws IOException {
    ObjectoxHeader header;
    header = regularInput(": foo \n");

    header.parseValue();

    assertEquals(header.valueStart, 2);
    assertEquals(header.valueEnd, 5);
  }

  private ObjectoxHeader regularInput(Object... data) throws IOException {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(data);

    SocketInput input;
    input = new SocketInput(64, inputStream);

    input.parseLine();

    assertEquals(input.hasNext(), true);
    assertEquals(input.next(), ':');

    return new ObjectoxHeader(input, HeaderName.HOST);
  }

}