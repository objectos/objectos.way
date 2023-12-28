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
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import org.testng.annotations.Test;

public class ObjectoxRequestLineTest {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    ObjectoxRequestLine line;
    line = regularInput("""
    GET / HTTP/1.1
    Host: www.example.com
    Connection: close

    """.replace("\n", "\r\n"));

    line.parse();

    assertEquals(line.method, ObjectoxMethod.GET);

    assertNotNull(line.path);
  }

  private ObjectoxRequestLine regularInput(Object... data) {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(data);

    Input input;
    input = new Input(64, inputStream);

    return new ObjectoxRequestLine(input);
  }

}