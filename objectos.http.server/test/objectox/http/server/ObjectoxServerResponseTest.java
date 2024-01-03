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
import static org.testng.Assert.assertSame;

import java.nio.charset.StandardCharsets;
import objectos.http.server.ServerResponseResult;
import org.testng.annotations.Test;

public class ObjectoxServerResponseTest {

  @Test
  public void testCase001() {
    ObjectoxServerResponse resp;
    resp = create(128);

    byte[] msg;
    msg = "Hello World!\n".getBytes(StandardCharsets.UTF_8);

    resp.ok();

    resp.contentType("text/plain; charset=utf-8");

    resp.contentLength(msg.length);

    resp.dateNow();

    ServerResponseResult result;
    result = resp.send(msg);

    assertSame(result, resp);

    assertEquals(
        result.toString(),

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 13\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        """
    );
  }

  private ObjectoxServerResponse create(int bufferSize) {
    byte[] buffer;
    buffer = new byte[bufferSize];

    ObjectoxServerResponse resp;
    resp = new ObjectoxServerResponse(buffer);

    resp.clock(TestingClock.FIXED);

    return resp;
  }

}