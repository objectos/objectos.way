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
import static org.testng.Assert.assertNull;

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

    // method
    ObjectoxMethod method;
    method = line.method;

    assertEquals(method, ObjectoxMethod.GET);

    // target
    HttpRequestPath path;
    path = line.path;

    assertEquals(path.toString(), "/");

    // version
    assertEquals(line.versionMajor, 1);
    assertEquals(line.versionMinor, 1);

    // no status
    assertNull(line.status);
  }

  private ObjectoxRequestLine regularInput(Object... data) {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(data);

    SocketInput input;
    input = new SocketInput(64, inputStream);

    return new ObjectoxRequestLine(input);
  }

}