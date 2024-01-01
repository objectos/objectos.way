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
import org.testng.annotations.Test;

public class ObjectoxServerRequestHeadersTest {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    ObjectoxServerRequestHeaders headers;
    headers = regularInput("""
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    headers.parse();

    assertEquals(headers.size(), 2);
    assertEquals(headers.first(StandardHeaderName.HOST), "www.example.com");
    assertEquals(headers.first(StandardHeaderName.CONNECTION), "close");
  }

  private ObjectoxServerRequestHeaders regularInput(Object... data) {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(data);

    SocketInput input;
    input = new SocketInput(64, inputStream);

    return new ObjectoxServerRequestHeaders(input);
  }

}